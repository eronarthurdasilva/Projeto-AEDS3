/*
 * Projeto AEDs 1 - Implementação do crud de transações
 * 
 */

import java.io.*;
import java.text.DecimalFormat;//importa a classe DecimalFormat 

//começo da classe Transaction
class Transaction{
    //declaração de variáveis
    protected int transactionID;//protected para que as classes filhas possam acessar
    protected String userID;//UserID é o id do usuário
    protected float transactionAmount;//TransactionAmout é o valor da transação
    protected String transactionType;//TransactionType é o tipo da transação
    protected String timestamp;//Timestamp é a data e hora da transação
    protected float accountBalance;//AccountBalance é o saldo da conta
    protected String deviceType;//DeviceType é o tipo de dispositivo
    protected String location;//Location é a localização da transação
    protected String merchantCategory;//MerchantCategory é a categoria do comerciante
    protected boolean ipAddressFlag;//IpAddressFlag é a flag do endereço de IP, usando o boolean para indicar se é verdadeiro ou falso
    protected int previousFraudulentActivity;//PreviousFraudulentActivity é a atividade fraudulenta anterior
    protected int dailyTransactionCount;//DailyTransactionCount é a contagem de transações diárias
    protected float avgTransactionAmount7d;//AvgTransactionAmount7d é a média do valor da transação em 7 dias
    protected int failedTransactionCount7d;//FailedTransactionCount7d é a contagem de transações falhadas em 7 dias
    protected String cardType;//CardType é o tipo de cartão
    protected int cardAge;//CardAge é a idade do cartão
    protected float transactionDistance;//TransactionDistance é a distância da transação
    protected String authenticationMethod;//AuthenticationMethod é o método de autenticação
    protected float riskScore;//RiskScore é a pontuação de risco
    protected boolean isWeekend;//IsWeekend é o fim de semana
    protected boolean fraudLabel;//FraudLabel é a etiqueta de fraude

    //construtor da classe Transaction
    public Transaction (int transactionID, String userID, float transactionAmount, String transactionType, String timestamp, float accountBalance, String deviceType, String location, String merchantCategory, boolean ipAddressFlag, int previousFraudulentActivity, int dailyTransactionCount, float avgTransactionAmount7d, int failedTransactionCount7d, String cardType, int cardAge, float transactionDistance, String authenticationMethod, float riskScore, boolean isWeekend, boolean fraudLabel){
        this.transactionID = transactionID;
        this.userID = userID;
        this.transactionAmount = transactionAmount;
        this.transactionType = transactionType;
        this.timestamp = timestamp;
        this.accountBalance = accountBalance;
        this.deviceType = deviceType;
        this.location = location;
        this.merchantCategory = merchantCategory;
        this.ipAddressFlag = ipAddressFlag;
        this.previousFraudulentActivity = previousFraudulentActivity;
        this.dailyTransactionCount = dailyTransactionCount;
        this.avgTransactionAmount7d = avgTransactionAmount7d;
        this.failedTransactionCount7d = failedTransactionCount7d;
        this.cardType = cardType;
        this.cardAge = cardAge;
        this.transactionDistance = transactionDistance;
        this.authenticationMethod = authenticationMethod;
        this.riskScore = riskScore;
        this.isWeekend = isWeekend;
        this.fraudLabel = fraudLabel;
    }

    //construtor vazio
    public Transaction(){
        this.transactionID = -1;
        this.userID = "";
        this.transactionAmount = 0F;
        this.transactionType = "";
        this.timestamp = "";
        this.accountBalance = 0F;
        this.deviceType = "";
        this.location = "";
        this.merchantCategory = "";
        this.ipAddressFlag = false;
        this.previousFraudulentActivity = 0;
        this.dailyTransactionCount = 0;
        this.avgTransactionAmount7d = 0F;
        this.failedTransactionCount7d = 0;
        this.cardType = "";
        this.cardAge = 0;
        this.transactionDistance = 0F;
        this.authenticationMethod = "";
        this.riskScore = 0F;
        this.isWeekend = false;
        this.fraudLabel = false;
    }
    
    //método para imprimir os dados da transação
    public String toString(){
        DecimalFormat df = new DecimalFormat("#,##0.00");//cria um objeto da classe DecimalFormat
        return "Transaction ID: " + this.transactionID + "\n" +
        "User ID: " + this.userID + "\n" +
        "Transaction Amount: $" + df.format(this.transactionAmount) + "\n" +
        "Transaction Type: " + this.transactionType + "\n" +
        "Timestamp: " + this.timestamp + "\n" +
        "Account Balance: $" + df.format(this.accountBalance) + "\n" +
        "Device Type: " + this.deviceType + "\n" +
        "Location: " + this.location + "\n" +
        "Merchant Category: " + this.merchantCategory + "\n" +
        "IP Address Flag: " + this.ipAddressFlag + "\n" +
        "Previous Fraudulent Activity: " + this.previousFraudulentActivity + "\n" +
        "Daily Transaction Count: " + this.dailyTransactionCount + "\n" +
        "Average Transaction Amount 7d: $" + df.format(this.avgTransactionAmount7d) + "\n" +
        "Failed Transaction Count 7d: " + this.failedTransactionCount7d + "\n" +
        "Card Type: " + this.cardType + "\n" +
        "Card Age: " + this.cardAge + "\n" +
        "Transaction Distance: " + this.transactionDistance + "\n" +
        "Authentication Method: " + this.authenticationMethod + "\n" +
        "Risk Score: " + this.riskScore + "\n" +
        "Is Weekend: " + this.isWeekend + "\n" +
        "Fraud Label: " + this.fraudLabel + "\n";
    }

    //Método parta converter objeto em array de bytes
    public byte[] toByteArray() throws IOException{
        //cria um  objeto da classe ByteArrayOutputStream
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        //Cria uma dataoutputstream para escrever os dados
        DataOutputStream dos = new DataOutputStream(baos);

        //escreve os dados no arquivo
        dos.writeInt(transactionID);
        dos.writeUTF(userID);
        dos.writeFloat(transactionAmount);
        dos.writeUTF(transactionType);
        dos.writeUTF(timestamp);
        dos.writeFloat(accountBalance);
        dos.writeUTF(deviceType);
        dos.writeUTF(location);
        dos.writeUTF(merchantCategory);
        dos.writeBoolean(ipAddressFlag);
        dos.writeInt(previousFraudulentActivity);
        dos.writeInt(dailyTransactionCount);
        dos.writeFloat(avgTransactionAmount7d);
        dos.writeInt(failedTransactionCount7d);
        dos.writeUTF(cardType);
        dos.writeInt(cardAge);
        dos.writeFloat(transactionDistance);
        dos.writeUTF(authenticationMethod);
        dos.writeFloat(riskScore);
        dos.writeBoolean(isWeekend);
        dos.writeBoolean(fraudLabel);

        return baos.toByteArray();
    }

    //Método para reconstruir objeto a partir de um array de bytes
    public static Transaction fromByteArray (byte[] data) throws IOException {
        //cria um objeto da classe ByteArrayInputStream de saida
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        //cria um objeto da classe DataInputStream para ler os dados
        DataInputStream dis = new DataInputStream(bais);

        int transactionID = dis.readInt();
        String userID = dis.readUTF();
        float transactionAmount = dis.readFloat();
        String transactionType = dis.readUTF();
        String timestamp = dis.readUTF();
        float accountBalance = dis.readFloat();
        String deviceType = dis.readUTF();
        String location = dis.readUTF();
        String merchantCategory = dis.readUTF();
        boolean ipAddressFlag = dis.readBoolean();
        int previousFraudulentActivity = dis.readInt();
        int dailyTransactionCount = dis.readInt();
        float avgTransactionAmount7d = dis.readFloat();
        int failedTransactionCount7d = dis.readInt();
        String cardType = dis.readUTF();
        int cardAge = dis.readInt();
        float transactionDistance = dis.readFloat();
        String authenticationMethod = dis.readUTF();
        float riskScore = dis.readFloat();
        boolean isWeekend = dis.readBoolean();
        boolean fraudLabel = dis.readBoolean();
        
        //retorna um novo objeto da classe Transaction
        return new Transaction(transactionID, userID, transactionAmount, transactionType, timestamp, 
                accountBalance, deviceType, location, merchantCategory, ipAddressFlag, previousFraudulentActivity, 
                dailyTransactionCount, avgTransactionAmount7d, failedTransactionCount7d, cardType, cardAge, 
                transactionDistance, authenticationMethod, riskScore, isWeekend, fraudLabel);


    }
}