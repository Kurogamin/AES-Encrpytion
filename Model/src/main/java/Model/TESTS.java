package Model;

import java.nio.charset.StandardCharsets;

public class TESTS {
    /*public static void main(String args[]) {
        byte left = (byte) 0b11110000;
        System.out.println(SBox.substitute(left) == (byte) 0x8c);
        System.out.println(SBox.substitute(SBox.substitute(left), true) == left);
        byte [][] matrix = new byte[4][4];
        int i = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                matrix[j][k] = (byte) i++;
            }
        }
        printMatrix(matrix);
        System.out.println();
        matrix = AES.shiftRows(matrix);
        printMatrix(matrix);
        System.out.println();
        matrix = AES.shiftRows(matrix, true);
        printMatrix(matrix);
    }
    public static void printMatrix(byte [][] matrix) {
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                System.out.print((int) matrix[j][k] + " ");
            }
            System.out.println();
        }
    }*/

    String key = "1234567890123456";
    byte [] key_bytes = key.getBytes(StandardCharsets.UTF_8);
    byte [] expanded_keys = new byte[176];
    AES cipher = new AES();
}
