import java.security.Key;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.io.UnsupportedEncodingException;
import java.security.InvalidKeyException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Arrays;
import java.util.Base64;
import javax.crypto.Cipher;
import javax.crypto.NoSuchPaddingException;
import javax.crypto.spec.SecretKeySpec;


public class AES {
    private static String inputK;
    private static SecretKeySpec secretKey;
    private static String outputMessage;

    /*
    Constructor accepts input message, key and type
    type 0 is encryption
    type 1 is decryption
    it then prepares the key and encrypts or decrypts the message
     */
    AES(String inM, String inK, int typeIn) {
        inputK = inK;
        prepareKey();
        outputMessage = calculate(inM, typeIn);
    }


    /*
    Function that uses the libraries to prepare the key for use
     */
    private static void prepareKey() {
        MessageDigest sha = null;
        try {
            byte[] key = inputK.getBytes("UTF-8");
            sha = MessageDigest.getInstance("SHA-1");
            key = sha.digest(key);
            key = Arrays.copyOf(key, 16);
            secretKey = new SecretKeySpec(key, "AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
        }
    }

        /*
    Using the library and the pre calculated key the funcction encrypts the message.
     */

    private static String calculate(String inputMessage, int type) {
        try {
            Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
            if(type==0){
                cipher.init(Cipher.ENCRYPT_MODE, secretKey);
                return Base64.getEncoder().encodeToString(cipher.doFinal(inputMessage.getBytes("UTF-8")));
            }else{
                cipher.init(Cipher.DECRYPT_MODE, secretKey);
                return new String(cipher.doFinal(Base64.getDecoder().decode(inputMessage)));
            }
        } catch (Exception e) {
            System.out.println("ERROR CAlCULATING AES");
        }
        return null;
    }

    /*
    Function to get the results
     */
    public String getResult() {
        return outputMessage;
    }
}