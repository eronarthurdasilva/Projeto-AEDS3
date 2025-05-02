import java.io.*;
import java.util.*;

public class Main {
    private static final String FILENAME = "transactions.db";
    private static IndexManager indexManager = new IndexManager(4); // ordem 4, por exemplo
    private static final String SEPARATOR = "==============================";
    private static Scanner teclado = new Scanner(System.in);
    private static ExtendibleHash hash = new ExtendibleHash(2,4); // Profundidade inicial 2, tamanho do bucket 4
    private static ExtendibleHash hashIndex = new ExtendibleHash(2,4); // Inicializa hashIndex
    private static String indiceAtual = "BPLUS"; // Indica o tipo de índice atual


    // Menu para os comandos do CRUD
    public static void main(String[] args) {
        int opcao = 0;
        do {
            // Exibe o menu de opções
            System.out.println(SEPARATOR);
            System.out.println("       MENU PRINCIPAL        ");
            System.out.println(SEPARATOR);
            System.out.println("1 - Carregar base de dados");
            System.out.println("2 - Create");
            System.out.println("3 - Read");
            System.out.println("4 - Update");
            System.out.println("5 - Delete");
            System.out.println("6 - Imprimir árvore B+");
            System.out.println("7 - Trocar indice (Atual: " + indiceAtual + ")");
            System.out.println("8 - Imprimir Hash Extendível");
            System.out.println("0 - Sair");
            System.out.print("Digite a opção desejada: ");
            opcao = teclado.nextInt();
            teclado.nextLine();
            switch (opcao) {
                case 1:
                    if (!baseDeDadosExiste()) {
                        carregarCSV("Data//synthetic_fraud_dataset.csv");
                    } else {
                        System.out.println("A base de dados já existe!");
                    }
                    break;
                case 2:
                    // Cria um novo registro
                    create();
                    break;
                case 3:
                    // Lê um registro
                    read();
                    break;
                case 4:
                    // Atualiza um registro
                    try {
                        update();
                    } catch (IOException e) {
                        System.out.println("Erro ao atualizar transação: " + e.getMessage());
                    }
                    break;
                case 5:
                    // Deleta um registro
                    delete();
                    break;
                case 6:
                    reconstruirIndice();
                    System.out.println("Árvore B+:");
                    indexManager.printIndexTree();
                    break;

                case 7:
                    if (indiceAtual.equals("BPLUS")) indiceAtual = "HASH";
                    else indiceAtual = "BPLUS";
                    System.out.println("Índice agora: " + indiceAtual);
                    break;
                
                case 8:
                    hashIndex.printHash();
                    break;
                case 0:
                    // Sai do programa
                    System.out.println("Saindo...");
                    break;
                default:
                    // Opção inválida
                    System.out.println("Opção inválida!");
            }
        } while (opcao != 0);
    }

    // Método para verificar se o arquivo de dados já existe
    private static boolean baseDeDadosExiste() {
        File f = new File(FILENAME);
        return f.exists() && f.length() > 0;
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
                long posicaoRegistro = raf.getFilePointer();
                raf.writeBoolean(true);
                raf.writeInt(data.length);
                raf.write(data);
                indexManager.insert(txn.transactionID, posicaoRegistro);
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

            // Solicita os dados do usuário
            System.out.println("\n==============================");
            System.out.println("       CRIAR TRANSAÇÃO");
            System.out.println("==============================");
            System.out.println("Total de transações existentes: " + count);
            String userID = "User_" + (count + 1);
            System.out.print("Transaction Amount: ");
            float transactionAmount = getValidFloat();
            System.out.print("Transaction Type: ");
            String transactionType = teclado.nextLine();

            Transaction txn = new Transaction(count + 1, // ID gerado automaticamente
                    userID, transactionAmount, transactionType, new java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new java.util.Date()), // Data gerada automaticamente
                    0, "", "", "", false, 0, 0, 0, 0, "", 0, 0, "", 0, false, false);

            byte[] data = txn.toByteArray();
            long posicaoRegistro = raf.getFilePointer();
            raf.writeBoolean(true);
            raf.writeInt(data.length);
            raf.write(data);
            // INSERÇÃO NO ÍNDICE ESCOLHIDO
            if (indiceAtual.equals("BPLUS"))
                indexManager.insert(txn.transactionID, posicaoRegistro);
            else
                hashIndex.insert(txn.transactionID, posicaoRegistro);
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
    
        // Busca a posição no índice selecionado
        Long posicao;
        if (indiceAtual.equals("BPLUS"))
            posicao = indexManager.search(id);
        else
            posicao = hashIndex.search(id);
    
        if (posicao == null) {
            System.out.println("Transação não encontrada!");
            return;
        }
    
        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "r")) {
            raf.seek(posicao);
            boolean lapide = raf.readBoolean();
            int tamanho = raf.readInt();
            byte[] data = new byte[tamanho];
            raf.read(data);
            Transaction txn = Transaction.fromByteArray(data);
            if (lapide)
                System.out.println(txn);
            else
                System.out.println("Transação deletada!");
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
    
        // Busca a posição no índice selecionado
        Long posicao;
        if (indiceAtual.equals("BPLUS"))
            posicao = indexManager.search(id);
        else
            posicao = hashIndex.search(id);
    
        if (posicao == null) {
            System.out.println("Transação não encontrada!");
            return;
        }
    
        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw")) {
            raf.seek(posicao);
            boolean lapide = raf.readBoolean();
            int tamanho = raf.readInt();
            byte[] data = new byte[tamanho];
            raf.read(data);
    
            if (!lapide) {
                System.out.println("Transação deletada!");
                return;
            }
    
            System.out.print("Nova Transaction Amount: ");
            float newAmount = getValidFloat();
            System.out.print("Novo Transaction Type: ");
            String newType = teclado.nextLine();
    
            Transaction txTransaction = new Transaction(id, "", newAmount, newType, "", 0, "", "", "", false, 0, 0, 0, 0, "", 0, 0, "", 0, false, false);
            byte[] dataAtualizado = txTransaction.toByteArray();
            raf.seek(posicao);
            raf.writeBoolean(true);
            raf.writeInt(dataAtualizado.length);
            raf.write(dataAtualizado);
    
            // ATUALIZA O ÍNDICE ESCOLHIDO
            if (indiceAtual.equals("BPLUS"))
                indexManager.insert(id, posicao);
            else
                hashIndex.insert(id, posicao);
    
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
    
        // Remove do índice selecionado
        if (indiceAtual.equals("BPLUS"))
            indexManager.remove(id);
        else
            hashIndex.delete(id);
    
        // Busca a posição no índice selecionado
        Long posicao;
        if (indiceAtual.equals("BPLUS"))
            posicao = indexManager.search(id);
        else
            posicao = hashIndex.search(id);
    
        if (posicao == null) {
            System.out.println("Transação não encontrada!");
            return;
        }
    
        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw")) {
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


    /**
     * Reconstrói o índice B+ a partir do arquivo de dados.
     */
    private static void reconstruirIndice() {
        indexManager = new IndexManager(4); // Limpa o índice atual
        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                long posicao = raf.getFilePointer();
                boolean lapide = raf.readBoolean();
                int tamanho = raf.readInt();
                byte[] data = new byte[tamanho];
                raf.read(data);
                if (lapide) {
                    Transaction txn = Transaction.fromByteArray(data);
                    indexManager.insert(txn.transactionID, posicao);
                }
            }
        } catch (IOException e) {
            System.out.println("Erro ao reconstruir índice: " + e.getMessage());
        }
    }
}