import java.io.*;
import java.util.*;

public class Main {
    private static final String FILENAME = "transactions.db";
    private static IndexManager indexManager = new IndexManager(4); // ordem 4, por exemplo
    private static final String SEPARATOR = "==============================";
    private static Scanner teclado = new Scanner(System.in);
    private static ExtendibleHash hashIndex = new ExtendibleHash(2,4); // Inicializa hashIndex
    private static String indiceAtual = "BPLUS"; // Indica o tipo de índice atual

    // Menu para os comandos do CRUD
    public static void main(String[] args) {
        if (baseDeDadosExiste()) {
            System.out.println("Reconstruindo índices a partir do arquivo existente...");
            reconstruirIndice();
            
        } else {
            System.out.println("Arquivo de base de dados não encontrado. Carregue o CSV para criar o arquivo.");
        }
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
            System.out.println("9 - Verificar consistência dos índices");
            System.out.println("10 - Debug Hash Extendível");
            System.out.println("11 - Comprimir base de dados");
            System.out.println("12 - Descomprimir base de dados");
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
                    System.out.println("Estrutura da Árvore B+:");
                    indexManager.printIndexTree();
                    break;

                case 7:
                    if (indiceAtual.equals("BPLUS")) {
                        indiceAtual = "HASH";
                        System.out.println("Índice alterado para Hashing Estendido.");
                    } else {
                        indiceAtual = "BPLUS";
                        System.out.println("Índice alterado para Árvore B+.");
                    }
                    reconstruirIndice();
                    System.out.println("Estrutura do índice " + indiceAtual + ":");
                    break;
                
                case 8:
                    hashIndex.printHash();
                    break;

                case 9:
                    if (indiceAtual.equals("BPLUS") && indexManager == null) {
                        System.out.println("Reconstruindo índice B+...");
                        reconstruirIndice();
                    } else if (indiceAtual.equals("HASH") && hashIndex == null) {
                        System.out.println("Reconstruindo índice Hash...");
                        reconstruirIndice();
                    }
                    verificarConsistencia();
                    break;

                case 10:

                    hashIndex.debugPrint();
                    break;

                case 11:
                    comprimirBaseDeDados();
                    break;
                case 12:
                    descomprimirBaseDeDados();
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

    // Método para carregar os dados do CSV e criar o arquivo binário
    public static void carregarCSV(String filePath) {
        File dbFile = new File(FILENAME);

        // Verifica se o arquivo de base de dados já existe
        if (dbFile.exists() && dbFile.length() > 0) {
            System.out.println("O arquivo de base de dados já existe. Não será recriado.");
            return;
        }

        try (BufferedReader br = new BufferedReader(new FileReader(filePath));
                RandomAccessFile raf = new RandomAccessFile(FILENAME, "rw")) {
            raf.setLength(0); // Apaga os dados existentes
            br.readLine(); // Ignora o cabeçalho
            String linha;

            // Processa cada linha do CSV
            while ((linha = br.readLine()) != null) {
                try {
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
                            campos[20].equals("1") // Fraud_Label
                    );

                    // Serializa a transação e grava no arquivo
                    byte[] data = txn.toByteArray();
                    long posicaoRegistro = raf.getFilePointer();
                    raf.writeBoolean(true); // Marca lápide como ativa
                    raf.writeInt(data.length);
                    raf.write(data);

                    // Insere no índice escolhido
                    if (indiceAtual.equals("BPLUS")) {
                        indexManager.insert(txn.transactionID, posicaoRegistro);
                    } else {
                        hashIndex.insert(txn.transactionID, posicaoRegistro);
                    }
                } catch (Exception e) {
                    System.out.println("Erro ao processar linha do CSV: " + linha);
                    System.out.println("Detalhes do erro: " + e.getMessage());
                }
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

            reconstruirIndice();
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
        // Solicite apenas os campos a serem alterados e mantenha os outros
    
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

    // Método para verificar os valores interiros
    private static int getValidInt(String prompt) {
        while (true) {
            System.out.print(prompt);
            try {
                int val = teclado.nextInt();
                teclado.nextLine();
                return val;
            } catch (InputMismatchException e) {
                System.out.println("Valor inválido. Digite um número inteiro válido.");
                teclado.nextLine(); // Limpa entrada inválida
            }
        }
    }

    /*
     * Método para reconstruir o índice B+ ou Hash Extendível
     */
    private static void reconstruirIndice() {
        if (indiceAtual.equals("BPLUS")) {
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
                        System.out.println("Inserindo no índice B+: ID = " + txn.transactionID + ", Posição = " + posicao);
                        indexManager.insert(txn.transactionID, posicao);
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro ao reconstruir índice B+: " + e.getMessage());
            }
        } else if (indiceAtual.equals("HASH")) {
            hashIndex = new ExtendibleHash(2, 4); // Limpa o índice atual
            try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "r")) {
                while (raf.getFilePointer() < raf.length()) {
                    long posicao = raf.getFilePointer();
                    boolean lapide = raf.readBoolean();
                    int tamanho = raf.readInt();
                    byte[] data = new byte[tamanho];
                    raf.read(data);
                    if (lapide) {
                        Transaction txn = Transaction.fromByteArray(data);
                        System.out.println("Inserindo no índice Hash: " + txn.transactionID + " na posição " + posicao);
                        hashIndex.insert(txn.transactionID, posicao);
                    }
                }
            } catch (IOException e) {
                System.out.println("Erro ao reconstruir índice Hash: " + e.getMessage());
            }
        }
    }

    private static void verificarConsistencia() {
        reconstruirIndice();
        if (indiceAtual.equals("BPLUS")) {
            if (indexManager == null) {
                System.out.println("Reconstruindo índice B+...");
                reconstruirIndice();
            }
        } else if (indiceAtual.equals("HASH")) {
            if (hashIndex == null) {
                System.out.println("Reconstruindo índice Hash...");
                reconstruirIndice();
            }
        }
        try (RandomAccessFile raf = new RandomAccessFile(FILENAME, "r")) {
            while (raf.getFilePointer() < raf.length()) {
                long posicao = raf.getFilePointer();
                boolean lapide = raf.readBoolean();
                int tamanho = raf.readInt();
                byte[] data = new byte[tamanho];
                raf.read(data);
                if (lapide) {
                    Transaction txn = Transaction.fromByteArray(data);
                    Long posicaoBPlus = indexManager.search(txn.transactionID);
                    Long posicaoHash = hashIndex.search(txn.transactionID);
    
                    if (posicaoBPlus == null || !posicaoBPlus.equals(posicao)) {
                        System.out.println("Inconsistência no índice B+ para ID: " + txn.transactionID);
                    }
                    if (posicaoHash == null || !posicaoHash.equals(posicao)) {
                        System.out.println("Inconsistência no índice Hash para ID: " + txn.transactionID);
                    }
                }
            }
            System.out.println("Verificação de consistência concluída.");
        } catch (IOException e) {
            System.out.println("Erro ao verificar consistência: " + e.getMessage());
        }
    }

    /*
     * Metodo para comprimir a base de dados
     * Utiliza os algoritmos Huffman e LZW
     */
    private static void comprimirBaseDeDados(){
        File pasta = new File("Compressed");
        if (!pasta.exists()) {
            pasta.mkdir();
        }

        File original = new File(FILENAME);
        if(!original.exists()) {
            System.out.println("Arquivo de base de dados não encontrado.");
            return;
        }
        try{
            // lê todo o arquivo em bytes
            byte[] inputData = new byte[(int) original.length()];
            try (FileInputStream fis = new FileInputStream(original)) {
                fis.read(inputData);
            }

            //huffman
            long startHuff = System.currentTimeMillis();
            byte[] huffCompressed = Huffman.compress(inputData);
            long endHuff = System.currentTimeMillis();
            String huffFile ="Compressed/" + FILENAME + "Huffman1";
            try (FileOutputStream fos = new FileOutputStream(huffFile)) {
                fos.write(huffCompressed);
            }
            double huffRatio = 100.0 * (1.0 - ((double) huffCompressed.length / (double) inputData.length));
            System.out.printf("Huffman: %.2f%% de compressão, tempo: %d ms\n", huffRatio, (endHuff - startHuff));
            

            //lZw 
            long startLZW = System.currentTimeMillis();
            byte[] lzwCompressed = LZW.compress(inputData);
            long endLZW = System.currentTimeMillis();
            String lzwFile = "Compressed/" + FILENAME + "LZW1";
            try (FileOutputStream fos = new FileOutputStream(lzwFile)) {
                fos.write(lzwCompressed);
            }
            double lzwRatio = 100.0 * (1.0 - ((double) lzwCompressed.length / (double) inputData.length));
            System.out.printf("LZW: %.2f%% de compressão, tempo: %d ms\n", lzwRatio, (endLZW - startLZW));

            //Comparação entre os dois metodos 
            if (huffCompressed.length < lzwCompressed.length) {
                System.out.println("Huffman é mais eficiente.");
            } else if (huffCompressed.length > lzwCompressed.length) {
                System.out.println("LZW é mais eficiente.");
            } else {
                System.out.println("Ambos os métodos têm a mesma eficiência.");
            }
        } catch (IOException e) {
            System.out.println("Erro ao comprimir a base de dados: " + e.getMessage());

        }
        reconstruirIndice();
    }

    private static void descomprimirBaseDeDados() {
        System.out.println("Digite o algoritmo que queria descomprimir (Huffman ou LZW): ");
        String algoritmo = teclado.nextLine().trim();
        System.out.println("Digite a versão (1 ou 2): ");
        String versao = teclado.nextLine().trim();
        String arquivo = "Compressed/" + FILENAME + algoritmo + versao;
        // Verifica se o arquivo comprimido existe
        
        File compressedFile = new File(arquivo);
        if(!compressedFile.exists()) {
            System.out.println("Arquivo comprimido não encontrado.");
            return;
        }
        
        try {
            byte[] compressedData = new byte[(int) compressedFile.length()];
            try (FileInputStream fis = new FileInputStream(compressedFile)) {
                fis.read(compressedData);
            }
            long startDecompress = System.currentTimeMillis();
            byte[] decompressedData;
            if (algoritmo.equalsIgnoreCase("Huffman")) {
                decompressedData = Huffman.decompress(compressedData);
            } else if (algoritmo.equalsIgnoreCase("LZW")) {
                decompressedData = LZW.decompress(compressedData);
            } else {
                System.out.println("Algoritmo inválido.");
                return;
            }
            long endDecompress = System.currentTimeMillis();

            // Grava os dados descomprimidos de volta no arquivo original
            try (FileOutputStream fos = new FileOutputStream(FILENAME)) {
                fos.write(decompressedData);
            }
            System.out.println("Descompressão (%s) concluida em %d ms".formatted(algoritmo, (endDecompress - startDecompress)));
        } catch (IOException e) {
            System.out.println("Erro ao descomprimir a base de dados: " + e.getMessage());
        }
        reconstruirIndice();

    }
}