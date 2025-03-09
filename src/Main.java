import java.io.*;
import java.util.*;

public class Main {
    private static final String FILENAME = "transactions.db";
    private static Scanner teclado = new Scanner(System.in);

    // Menu para os comandos do CRUD
    public static void main(String[] args) {
        int opcao = 0;
        do {
            System.out.println("\n==============================");
            System.out.println("       MENU DE OPÇÕES");
            System.out.println("==============================");
            System.out.println("1 - Carregar base de dados");
            System.out.println("2 - Create");
            System.out.println("3 - Read");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("0 - Sair");
            System.out.print("Digite a opção desejada: ");
            opcao = teclado.nextInt();
            teclado.nextLine();
            switch (opcao) {
                case 1:
                    carregarCSV("Data//synthetic_fraud_dataset.csv");
                    break;
                case 2:
                    create();
                    break;
                case 3:
                    read();
                    break;
                case 4:
                    try {
                        update();
                    } catch (IOException e) {
                        System.out.println("Erro ao atualizar transação: " + e.getMessage());
                    }
                    break;
                case 5:
                    delete();
                    break;
                case 0:
                    System.out.println("Saindo...");
                    break;
                default:
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    // Método para carregar o arquivo CSV
    public static void carregarCSV(String filePath) {
        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
             RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw")) {
            raf.setLength(0); // Apaga os dados existentes
            br.readLine(); // Ignora o cabeçalho
            String linha;
            while ((linha = br.readLine()) != null) {
                String[] campos = linha.split(",");
                Transaction txn = new Transaction(
                        Integer.parseInt(campos[0].substring(4)), // Transaction_ID sem o "TXN_"
                        campos[1], // User_ID
                        Float.parseFloat(campos[2]), // Transaction_Amount
                        campos[3], // Transaction_Type
                        campos[4], // Timestamp
                        Float.parseFloat(campos[5]), // Account_Balance
                        campos[6], // Device_Type
                        campos[7], // Location
                        campos[8], // Merchant_Category
                        campos[9].equals("1"), // IP_Address_Flag
                        Integer.parseInt(campos[10]), // Previous_Fraudulent_Activity
                        Integer.parseInt(campos[11]), // Daily_Transaction_Count
                        Float.parseFloat(campos[12]), // Avg_Transaction_Amount_7d
                        Integer.parseInt(campos[13]), // Failed_Transaction_Count_7d
                        campos[14], // Card_Type
                        Integer.parseInt(campos[15]), // Card_Age
                        Float.parseFloat(campos[16]), // Transaction_Distance
                        campos[17], // Authentication_Method
                        Float.parseFloat(campos[18]), // Risk_Score
                        campos[19].equals("1"), // Is_Weekend
                        campos[20].equals("1")  // Fraud_Label
                );
                byte[] data = txn.toByteArray();
                raf.writeBoolean(true); // Lápide (true = ativo)
                raf.writeInt(data.length); // Tamanho do registro
                raf.write(data); // Dados serializados
            }
            System.out.println("Dados carregados com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao carregar CSV: " + e.getMessage());
        }
    }

    // Método para criar um novo registro no arquivo binário
    public static void create() {
        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw")) {
            // Contar quantos registros já existem
            int count = 0;
            while (raf.getFilePointer() < raf.length()) {
                boolean lapide = raf.readBoolean();
                int tamanho = raf.readInt();
                raf.skipBytes(tamanho);
                if (lapide) {
                    count++;
                }
            }

            System.out.println("\n==============================");
            System.out.println("       CRIAR TRANSAÇÃO");
            System.out.println("==============================");
            System.out.println("Total de transações existentes: " + count);
            System.out.print("User ID: ");
            String userID = teclado.nextLine();
            System.out.print("Transaction Amount: ");
            float transactionAmount = getValidFloat();
            System.out.print("Transaction Type: ");
            String transactionType = teclado.nextLine();

            Transaction txn = new Transaction(count + 1, // ID gerado automaticamente
                    userID, transactionAmount, transactionType, "2025-03-02 12:00:00", // Data fixa (ajustar depois)
                    0, "", "", "", false, 0, 0, 0, 0, "", 0, 0, "", 0, false, false);

            byte[] data = txn.toByteArray();
            raf.writeBoolean(true); // Lápide (true = ativo)
            raf.writeInt(data.length); // Tamanho do registro
            raf.write(data); // Dados serializados
            System.out.println("Transação criada com sucesso!");
            System.out.println(txn);
        } catch (IOException e) {
            System.out.println("Erro ao criar transação: " + e.getMessage());
        }
    }

    // Método para ler um registro do arquivo binário
    public static void read() {
        System.out.println("\n==============================");
        System.out.println("       LER TRANSAÇÃO");
        System.out.println("==============================");
        System.out.print("Digite o ID da transação: ");
        int id = teclado.nextInt();
        teclado.nextLine();

        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                boolean lapide = raf.readBoolean(); // Verifica se tem alguma lápide
                int tamanho = raf.readInt(); // Verifica o tamanho do registro
                byte[] data = new byte[tamanho]; // Cria um array de bytes com o tamanho do registro
                raf.read(data); // Lê o registro

                Transaction txn = Transaction.fromByteArray(data);
                if (lapide && txn.transactionID == id) {
                    System.out.println(txn);
                    return;
                }
            }
            System.out.println("Transação não encontrada!");
        } catch (IOException e) {
            System.out.println("Erro ao ler transação: " + e.getMessage());
        }
    }

    // Método para atualizar um registro do arquivo binário
    public static void update() throws IOException {
        System.out.println("\n==============================");
        System.out.println("       ATUALIZAR TRANSAÇÃO");
        System.out.println("==============================");
        System.out.print("Digite o ID a ser atualizado: ");
        int id = teclado.nextInt();
        teclado.nextLine();

        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw")) {
            long posicao = -1; // Posição inicial
            boolean encontrada = false; // Variável para verificar se a transação foi encontrada

            while (raf.getFilePointer() < raf.length()) {
                long posInicio = raf.getFilePointer(); // Posição inicial
                boolean lapide = raf.readBoolean(); // Verifica se tem alguma lápide
                int tamanho = raf.readInt(); // Verifica o tamanho do registro
                byte[] data = new byte[tamanho]; // Cria um array de bytes com o tamanho do registro
                raf.read(data); // Lê o registro

                Transaction txn = Transaction.fromByteArray(data);
                if (lapide && txn.transactionID == id) {
                    posicao = posInicio; // Posição atual
                    encontrada = true; // Transação encontrada
                    break;
                }
            }

            if (!encontrada) {
                System.out.println("Transação não encontrada!");
                return;
            }

            System.out.print("Nova Transaction Amount: ");
            float newAmount = getValidFloat();
            System.out.print("Novo Transaction Type: ");
            String newType = teclado.nextLine();

            Transaction txTransaction = new Transaction(id, "", newAmount, newType, "", 0, "", "", "", false, 0, 0, 0, 0, "", 0, 0, "", 0, false, false);
            byte[] dataAtualizado = txTransaction.toByteArray();
            raf.seek(posicao); // Posiciona o ponteiro na posição da transação
            raf.writeBoolean(true); // Lápide
            raf.writeInt(dataAtualizado.length); // Tamanho do registro
            raf.write(dataAtualizado); // Dados serializados

            System.out.println("Transação atualizada com sucesso!");
            System.out.println(txTransaction);
        } catch (IOException e) {
            System.out.println("Erro ao atualizar transação: " + e.getMessage());
        }
    }

    // Método para deletar um registro do arquivo binário
    public static void delete() {
        System.out.println("\n==============================");
        System.out.println("       DELETAR TRANSAÇÃO");
        System.out.println("==============================");
        System.out.print("Digite o Transaction ID a ser deletado: ");
        int id = teclado.nextInt();
        teclado.nextLine();

        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw")) {
            long posicao = -1; // Posição inicial
            boolean encontrada = false; // Variável para verificar se a transação foi encontrada

            while (raf.getFilePointer() < raf.length()) {
                long posInicio = raf.getFilePointer(); // Posição inicial
                boolean lapide = raf.readBoolean(); // Verifica se tem alguma lápide
                int tamanho = raf.readInt(); // Verifica o tamanho do registro
                byte[] data = new byte[tamanho]; // Cria um array de bytes com o tamanho do registro
                raf.read(data); // Lê o registro

                Transaction txn = Transaction.fromByteArray(data);
                if (lapide && txn.transactionID == id) {
                    posicao = posInicio; // Posição atual
                    encontrada = true; // Transação encontrada
                    break;
                }
            }

            if (!encontrada) {
                System.out.println("Transação não encontrada!");
                return;
            }

            raf.seek(posicao);
            raf.writeBoolean(false); // Marca lápide como false

            System.out.println("Transação deletada com sucesso!");
        } catch (IOException e) {
            System.out.println("Erro ao deletar transação: " + e.getMessage());
        }
    }

    // Método para obter um float válido do usuário
    private static float getValidFloat() {
        while (true) {
            try {
                return teclado.nextFloat();
            } catch (InputMismatchException e) {
                System.out.print("Valor inválido. Digite um número válido: ");
                teclado.next(); // Limpa a entrada inválida
            }
        }
    }
}