/*
 * Projeto AEDs 1 - Implementação do crud de transações
 */

import java.io.*;
import java.text.DecimalFormat;
import java.util.Arrays;

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
    protected String[] tags;

    // Construtor completo
    public Transaction(int transactionID, String userID, float transactionAmount, String transactionType, String timestamp, float accountBalance, String deviceType, String location, String merchantCategory, boolean ipAddressFlag, int previousFraudulentActivity, int dailyTransactionCount, float avgTransactionAmount7d, int failedTransactionCount7d, String cardType, int cardAge, float transactionDistance, String authenticationMethod, float riskScore, boolean isWeekend, boolean fraudLabel, String[] tags) {
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
        this.tags = tags != null ? tags : new String[0];
    }

    // Construtor vazio
    public Transaction() {
        this(-1, "", 0F, "", "", 0F, "", "", "", false, 0, 0, 0F, 0, "", 0, 0F, "", 0F, false, false, new String[0]);
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
                "Fraud Label: " + this.fraudLabel + "\n"
                + "Tags: " + Arrays.toString(this.tags) + "\n";
    }

    // Método para converter objeto em array de bytes
    public byte[] toByteArray() throws IOException {
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        DataOutputStream dos = new DataOutputStream(baos);

        // Escreve todos os campos
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

        // Escreve as tags
        dos.writeInt(tags.length);
        for (String tag : tags) {
            dos.writeUTF(tag);
        }

        return baos.toByteArray();
    }

    // Método para reconstruir objeto a partir de um array de bytes
    public static Transaction fromByteArray(byte[] data) throws IOException {
        ByteArrayInputStream bais = new ByteArrayInputStream(data);
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

        int numTags = dis.readInt();
        String[] tags = new String[numTags];
        for (int i = 0; i < numTags; i++) {
            tags[i] = dis.readUTF();
        }

        return new Transaction(transactionID, userID, transactionAmount, transactionType, timestamp,
            accountBalance, deviceType, location, merchantCategory, ipAddressFlag, previousFraudulentActivity,
            dailyTransactionCount, avgTransactionAmount7d, failedTransactionCount7d, cardType, cardAge,
            transactionDistance, authenticationMethod, riskScore, isWeekend, fraudLabel, tags);
    }
}