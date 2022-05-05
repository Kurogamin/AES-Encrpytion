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
        AES cipher = new AES();
        expanded_keys = cipher.keyExpansion(key_bytes, 10);

        byte [] newText = cipher.encrypt(message.getBytes(StandardCharsets.UTF_8), expanded_keys, 10);
        byte [] startMessage = message.getBytes(StandardCharsets.UTF_8);
        byte [] decrypted = cipher.decrypt(newText, expanded_keys, 10);
        System.out.println();
    }


}
