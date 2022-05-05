package Model;

public class AES {
    public byte [] encrypt(byte [] data, byte[] expandedKey, int rounds) {
        byte [][] state = IO.makeByteMatrix(data);


        byte [] nextKey = new byte[16];
        addRoundKey(state, expandedKey);

        for (int i = 0; i < rounds - 1; i++) {
            substituteBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            System.arraycopy(expandedKey, 16 * (i + 1), nextKey, 0, 16);
            addRoundKey(state, nextKey);
        }

        // Final round
        substituteBytes(state);
        state = shiftRows(state);
        System.arraycopy(expandedKey, rounds * 16, nextKey, 0, 16);
        addRoundKey(state, nextKey);

        return IO.byteArrayFromMatrix(state);
    }

    byte [] decrypt(byte [] data, byte[] expandedKey, int rounds) {
        byte [][] state = IO.makeByteMatrix(data);
        byte [] nextKey = new byte[16];

        System.arraycopy(expandedKey, rounds * 16, nextKey, 0, 16);
        addRoundKey(state, nextKey);

        for (int i = rounds - 1; i > 0; i--) {
            state = shiftRows(state, true);
            substituteBytes(state, true);
            System.arraycopy(expandedKey, i * 16, nextKey, 0, 16);
            addRoundKey(state, nextKey);
            state = mixColumns(state, true);
        }

        state = shiftRows(state, true);
        substituteBytes(state, true);
        addRoundKey(state, expandedKey);

        return IO.byteArrayFromMatrix(state);
    }
    public byte [][] shiftRows(byte [][] matrix, boolean inverse) {
        byte[][] shifted = new byte[4][4];
        for (int i = 0; i < 4; i++) {
            System.arraycopy(matrix[i], 0, shifted[i], 0, 4);
        }
        if (!inverse) {
            shifted = rotate(shifted);
        } else {
            shifted = rotate(shifted, true);
        }
        return shifted;
    }

    void addRoundKey(byte [][] state, byte [] round_key) {
        int k = 0;
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                state[j][i] ^= round_key[k++];
            }
        }

    }

    public byte [][] shiftRows(byte [][] matrix) {
        return shiftRows(matrix, false);
    }

    public byte [][] rotate(byte [][] matrix, boolean left) {
        byte [][] newMatrix = new byte [4][4];
        newMatrix[0] = matrix[0];
        for (int i = 1; i < 4; i++) {
            byte [] rotatedRow = new byte[4];
            for (int j = 0; j < 4; j++) {
                if (left) {
                    rotatedRow[j] = matrix[i][((j + 4) - i) % 4];
                } else {
                    rotatedRow[j] = matrix[i][((j + 4) + i) % 4];
                }
            }
            newMatrix[i] = rotatedRow;
        }
        return newMatrix;
    }

    public byte [][] rotate(byte [][] matrix) {
        return rotate(matrix, false);
    }

    public void substituteBytes(byte [][] matrix, boolean isDecrypt) {

        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                matrix[i][j] = Utilities.substitute(matrix[i][j], isDecrypt);
            }
        }

        //return matrix;
    }

    public void substituteBytes(byte [][] matrix) {
        substituteBytes(matrix, false);
    }

    public byte [][] mixColumns(byte [][] matrix, boolean inverse) {
        byte [][] result = new byte[4][4];
        if (!inverse) {
            for (int i = 0; i < 4; i++) {
                result[0][i] = (byte)(GMul((byte)0x02, matrix[0][i]) ^ GMul((byte)0x03, matrix[1][i]) ^ matrix[2][i] ^ matrix[3][i]);
                result[1][i] = (byte)(matrix[0][i] ^ GMul((byte)0x02, matrix[1][i]) ^ GMul((byte)0x03, matrix[2][i]) ^ matrix[3][i]);
                result[2][i] = (byte)(matrix[0][i] ^ matrix[1][i] ^ GMul((byte)0x02, matrix[2][i]) ^ GMul((byte)0x03, matrix[3][i]));
                result[3][i] = (byte)(GMul((byte)0x03, matrix[0][i]) ^ matrix[1][i] ^ matrix[2][i] ^ GMul((byte)0x02, matrix[3][i]));
            }
        } else {
            for (int i = 0; i < 4; i++) {
                result[0][i] = (byte)(GMul((byte)0x0E, matrix[0][i]) ^ GMul((byte)0x0B, matrix[1][i]) ^ GMul((byte)0x0D, matrix[2][i]) ^ GMul((byte)0x09, matrix[3][i]));
                result[1][i] = (byte)(GMul((byte)0x09, matrix[0][i]) ^ GMul((byte)0x0E, matrix[1][i]) ^ GMul((byte)0x0B, matrix[2][i]) ^ GMul((byte)0x0D, matrix[3][i]));
                result[2][i] = (byte)(GMul((byte)0x0D, matrix[0][i]) ^ GMul((byte)0x09, matrix[1][i]) ^ GMul((byte)0x0E, matrix[2][i]) ^ GMul((byte)0x0B, matrix[3][i]));
                result[3][i] = (byte)(GMul((byte)0x0B, matrix[0][i]) ^ GMul((byte)0x0D, matrix[1][i]) ^ GMul((byte)0x09, matrix[2][i]) ^ GMul((byte)0x0E, matrix[3][i]));
            }
        }

        return result;
    }

    public byte [][] mixColumns(byte [][] matrix) {
        return mixColumns(matrix, false);
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

    /* Function: key_expansion
     * -----------------------
     * Takes 16 byte input key and expands to 176 bytes.
     *
     * The key is expanded to 176 bytes which allows for 10 key uses.
     *
     * input_key: 16 byte key used for expansion
     * expanded_key: is set to resulting expanded key
     */
    public byte [] keyExpansion(byte[] input_key, int rounds) {
        int bytes = 0;
        if (rounds == 10) bytes = 176;
        else if (rounds == 12) bytes = 208;
        else if (rounds == 14) bytes = 240;

        byte[] expanded_keys = new byte[bytes];
        // Set first 16 bytes to input_key
        System.arraycopy(input_key, 0, expanded_keys, 0, 16);

        int bytes_generated = 16;
        int rcon_iteration = 1;
        byte[] temp = new byte[4];

        // Generate the next 160 bytes
        while (bytes_generated < bytes) {
            // Read 4 bytes for the core
            System.arraycopy(expanded_keys, bytes_generated - 4, temp, 0, 4);

            // Perform the core once for each 16 byte key
            if (bytes_generated % 16 == 0)
                key_expansion_core(temp, rcon_iteration++);

            // XOR temp with [bytes_generated-16], and store in expanded_keys
            for (int a = 0; a < 4; a++) {
                expanded_keys[bytes_generated] = (byte) (expanded_keys[bytes_generated - 16] ^ temp[a]);
                bytes_generated++;
            }
        }
        return expanded_keys;
    }

    void key_expansion_core(byte[] in, int i) {
        // Left rotate bytes
        byte temp = in[0];
        in[0] = in[1];
        in[1] = in[2];
        in[2] = in[3];
        in[3] = temp;


        for (int j = 0; j < 4; j++) {
            in[j] = Utilities.substitute(in[j]);
        }

        // RCon XOR
        in[0] ^= Utilities.rcon[i];
    }
}
