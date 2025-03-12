import java.io.*;
import java.text.DecimalFormat;

class Transaction {
    // Declaração de variáveis
    protected int transactionID;
    protected String userID;
    protected float transactionAmount;
    protected String transactionType;
    protected String timestamp;
    protected float accountBalance;
    protected String deviceType;
    protected String location;
    protected String merchantCategory;
    protected boolean ipAddressFlag;
    protected int previousFraudulentActivity;
    protected int dailyTransactionCount;
    protected float avgTransactionAmount7d;
    protected int failedTransactionCount7d;
    protected String cardType;
    protected int cardAge;
    protected float transactionDistance;
    protected String authenticationMethod;
    protected float riskScore;
    protected boolean isWeekend;
    protected boolean fraudLabel;

    // Construtor da classe Transaction
    public Transaction(int transactionID, String userID, float transactionAmount, String transactionType, String timestamp, float accountBalance, String deviceType, String location, String merchantCategory, boolean ipAddressFlag, int previousFraudulentActivity, int dailyTransactionCount, float avgTransactionAmount7d, int failedTransactionCount7d, String cardType, int cardAge, float transactionDistance, String authenticationMethod, float riskScore, boolean isWeekend, boolean fraudLabel) {
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

    // Construtor vazio
    public Transaction() {
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

    // Método para imprimir os dados da transação
    public String toString() {
        DecimalFormat df = new DecimalFormat("#,##0.00");
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

    // Método para converter objeto em array de bytes
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escrevemos primeiro os dados normalmente
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

        // Pegamos os bytes da transação
        byte[] transactionData = baos.toByteArray();

        // Criamos um novo array que inclui o tamanho do registro no início
        ByteArrayOutputStream finalBaos = new ByteArrayOutputStream();
        DataOutputStream finalDos = new DataOutputStream(finalBaos);

        // Escrevemos o tamanho do registro
        finalDos.writeInt(transactionData.length);
        // Escrevemos os dados da transação
        finalDos.write(transactionData);

        return finalBaos.toByteArray();
    }

    // Método para reconstruir objeto a partir de um array de bytes
    public static Transaction fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
        DataInputStream dis = new DataInputStream(bais);

        // Lê o tamanho do registro
        int tamanho = dis.readInt();
        byte[] transactionData = new byte[tamanho];
        dis.readFully(transactionData);

        ByteArrayInputStream transactionBais = new ByteArrayInputStream(transactionData);
        DataInputStream transactionDis = new DataInputStream(transactionBais);

        // Lê os dados do DataInputStream
        int transactionID = transactionDis.readInt();
        String userID = transactionDis.readUTF();
        float transactionAmount = transactionDis.readFloat();
        String transactionType = transactionDis.readUTF();
        String timestamp = transactionDis.readUTF();
        float accountBalance = transactionDis.readFloat();
        String deviceType = transactionDis.readUTF();
        String location = transactionDis.readUTF();
        String merchantCategory = transactionDis.readUTF();
        boolean ipAddressFlag = transactionDis.readBoolean();
        int previousFraudulentActivity = transactionDis.readInt();
        int dailyTransactionCount = transactionDis.readInt();
        float avgTransactionAmount7d = transactionDis.readFloat();
        int failedTransactionCount7d = transactionDis.readInt();
        String cardType = transactionDis.readUTF();
        int cardAge = transactionDis.readInt();
        float transactionDistance = transactionDis.readFloat();
        String authenticationMethod = transactionDis.readUTF();
        float riskScore = transactionDis.readFloat();
        boolean isWeekend = transactionDis.readBoolean();
        boolean fraudLabel = transactionDis.readBoolean();

        // Retorna um novo objeto Transaction
        return new Transaction(transactionID, userID, transactionAmount, transactionType, timestamp,
                accountBalance, deviceType, location, merchantCategory, ipAddressFlag, previousFraudulentActivity,
                dailyTransactionCount, avgTransactionAmount7d, failedTransactionCount7d, cardType, cardAge,
                transactionDistance, authenticationMethod, riskScore, isWeekend, fraudLabel);
    }
}