package Model;

public class AES {

    public byte [][] shiftRows(byte [][] matrix, boolean inverse) {
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

    public byte [][] shiftRows(byte [][] matrix) {
        return shiftRows(matrix, false);
    }

    public void rotate(byte [][] matrix, boolean left) {
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

    public void rotate(byte [][] matrix) {
        rotate(matrix, false);
    }

    public byte [][] substituteBytes(byte [][] matrix, boolean isDecrypt) {
        SBox sBox = new SBox();

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                    matrix[i][j] = sBox.substitute(matrix[i][j], isDecrypt);
            }
        }

        return matrix;
    }

    public byte [][] mixColumns(byte [][] matrix) {
        byte [][] result = new byte[4][4];

        for (int i = 0; i < 4; i++) {
            result[0][i] = (byte)(GMul((byte)0x02, matrix[0][i]) ^ GMul((byte)0x03, matrix[1][i]) ^ matrix[2][i] ^ matrix[3][i]);
            result[1][i] = (byte)(matrix[0][i] ^ GMul((byte)0x02, matrix[1][i]) ^ GMul((byte)0x03, matrix[2][i]) ^ matrix[3][i]);
            result[2][i] = (byte)(matrix[0][i] ^ matrix[1][i] ^ GMul((byte)0x02, matrix[2][i]) ^ GMul((byte)0x03, matrix[3][i]));
            result[3][i] = (byte)(GMul((byte)0x03, matrix[0][i]) ^ matrix[1][i] ^ matrix[2][i] ^ GMul((byte)0x02, matrix[3][i]));
        }
        return result;
    }

    private byte GMul(byte a, byte b) { // Galois Field (256) Multiplication of two Bytes
        byte p = 0;

        for (int counter = 0; counter < 8; counter++) {
            if ((b & 1) != 0) {
                p ^= a;
            }

            boolean hi_bit_set = (a & 0x80) != 0;
            a <<= 1;
            if (hi_bit_set) {
                a ^= 0x1B; /* x^8 + x^4 + x^3 + x + 1 */
            }
            b >>= 1;
        }

        return p;
    }

    byte [][] convertIntToByteArray(int[][] matrix) {
        byte [][] byteArray = new byte[4][4];
        for (int i = 0; i < 4; i ++) {
            for (int j = 0; j < 4; j++) {
                byteArray[i][j] = (byte)matrix[i][j];
            }
        }
        return byteArray;
    }
}
