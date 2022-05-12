package Model;

import java.nio.charset.StandardCharsets;
import java.util.Arrays;

public class TESTS {
    public static void main(String[] args) {
        String key = "Thats my Kung Fu";
        String message = "Two One Nine Two";
        byte [] key_bytes = key.getBytes(StandardCharsets.UTF_8);
        for (byte b : key_bytes) {
            System.out.print(Integer.toHexString(b));
            System.out.print(" ");
        }
        System.out.println();
        byte [] expanded_keys;
        AES aes = new AES();
        expanded_keys = aes.keyExpansion(key_bytes, 10);

        byte [] file = IO.readFileToByteArray("specjalnosci.pdf");
        byte [] encryptedFile = aes.cipher(file, key, true);
        byte [] decryptedFile = aes.cipher(encryptedFile, key, false);
        IO.writeFileFromByteArray(decryptedFile, "xd.pdf");
        System.out.println();
    }


}
