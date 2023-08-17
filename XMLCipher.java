package ProjectIntern;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.io.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;

public class XMLCipher {

    private boolean isEncryptionSuccessful;
    private boolean isDecryptionSuccessful;

    public void encryptXML(File inputFile, File outputFile, String encryptionKey) {
        try {
            byte[] keyData = encryptionKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            keyData = md.digest(keyData);
            SecretKeySpec secretKey = new SecretKeySpec(keyData, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.ENCRYPT_MODE, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);
            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] encryptedBytes = cipher.update(buffer, 0, bytesRead);
                if (encryptedBytes != null) {
                    outputStream.write(encryptedBytes);
                }
            }

            byte[] finalEncryptedBytes = cipher.doFinal();
            if (finalEncryptedBytes != null) {
                outputStream.write(finalEncryptedBytes);
            }

            inputStream.close();
            outputStream.close();

            isEncryptionSuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
            isEncryptionSuccessful = false;
        }
    }


    public void decryptXML(File inputFile, File outputFile, String encryptionKey) {
        try {
            byte[] keyData = encryptionKey.getBytes(StandardCharsets.UTF_8);
            MessageDigest md = MessageDigest.getInstance("SHA-256");
            keyData = md.digest(keyData);
            SecretKeySpec secretKey = new SecretKeySpec(keyData, "AES");

            Cipher cipher = Cipher.getInstance("AES");
            cipher.init(Cipher.DECRYPT_MODE, secretKey);

            FileInputStream inputStream = new FileInputStream(inputFile);
            FileOutputStream outputStream = new FileOutputStream(outputFile);

            byte[] buffer = new byte[8192];
            int bytesRead;

            while ((bytesRead = inputStream.read(buffer)) != -1) {
                byte[] decryptedBytes = cipher.update(buffer, 0, bytesRead);
                if (decryptedBytes != null) {
                    outputStream.write(decryptedBytes);
                }
            }

            byte[] finalDecryptedBytes = cipher.doFinal();
            if (finalDecryptedBytes != null) {
                outputStream.write(finalDecryptedBytes);
            }

            inputStream.close();
            outputStream.close();

            isDecryptionSuccessful = true;
        } catch (Exception e) {
            e.printStackTrace();
            isDecryptionSuccessful = false;
        }
    }

    public boolean isEncryptionSuccessful() {
        return isEncryptionSuccessful;
    }

    public boolean isDecryptionSuccessful() {
        return isDecryptionSuccessful;
    }
}
