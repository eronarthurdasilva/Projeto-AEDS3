import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

public class CryptoUtils {
    private static final String AES_KEY = "1234567890abcdef"; // 16 chars

    // AES
    public static String encryptAES(String data) throws Exception {
        SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.ENCRYPT_MODE, key);
        byte[] enc = c.doFinal(data.getBytes());
        return Base64.getEncoder().encodeToString(enc);
    }
    public static String decryptAES(String enc) throws Exception {
        SecretKeySpec key = new SecretKeySpec(AES_KEY.getBytes(), "AES");
        Cipher c = Cipher.getInstance("AES");
        c.init(Cipher.DECRYPT_MODE, key);
        byte[] dec = c.doFinal(Base64.getDecoder().decode(enc));
        return new String(dec);
    }

    // César
    public static String encryptCaesar(String data, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : data.toCharArray()) {
            sb.append((char)(c + shift));
        }
        return sb.toString();
    }
    public static String decryptCaesar(String data, int shift) {
        StringBuilder sb = new StringBuilder();
        for (char c : data.toCharArray()) {
            sb.append((char)(c - shift));
        }
        return sb.toString();
    }
} 
    

