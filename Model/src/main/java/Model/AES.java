package Model;

public class AES {
    
    public static byte [][] shiftRows(byte [][] matrix, boolean inverse) {
        byte[][] shifted = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrix[i], 0, shifted[i], 0, 4);
        }
        if (!inverse) {
            rotate(shifted);
        } else {
            rotate(shifted, true);
        }
        return shifted;
    }

    public static void rotate(byte [][] matrix, boolean left) {
        for (int i = 1; i < 4; i++) {
            byte [] rotatedRow = new byte[4];
            for (int j = 0; j < 4; j++) {
                if (left) {
                    rotatedRow[j] = matrix[i][((j + 4) - i) % 4];
                } else {
                    rotatedRow[j] = matrix[i][((j + 4) + i) % 4];
                }
            }
            matrix[i] = rotatedRow;
        }
    }

    public static void rotate(byte [][] matrix) {
        rotate(matrix, false);
    }
}
