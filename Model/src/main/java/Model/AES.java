package Model;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class AES {
    public byte [] cipher(byte [] data, String key, boolean encryption) {
        byte [] expanded_keys = AES.keyExpansion(key.getBytes(StandardCharsets.UTF_8));
        ArrayList<Byte> result = new ArrayList<Byte>();

        byte [] packet = new byte [16];
        for (int i = 0; i < data.length; i++) {
            packet[i % 16] = data[i];
            if (i % 16 == 15 || i == data.length - 1) {
                byte [] newPacket;
                if (encryption) {
                    newPacket = encrypt(packet, expanded_keys);
                } else {
                    newPacket = decrypt(packet, expanded_keys);
                }
                for (int j = 0; j < packet.length; j++) {
                    result.add(newPacket[j]);
                }
                packet = new byte [16];
            }
        }
        int resultSize = result.size();
        if (!encryption) {
            boolean zeros = true;
            int i = result.size() - 1;
            while (zeros) {
                if (result.get(i) == 0) {
                    resultSize--;
                    i--;
                } else {
                    zeros = false;
                }
            }
        }
        byte [] resultArray = new byte[resultSize];
        for (int k = 0; k < resultSize; k++) {
            resultArray[k] = result.get(k);
        }
        return resultArray;
    }

    public byte [] encrypt(byte [] data, byte[] expandedKey) {
        byte [][] state = IO.makeByteMatrix(data);
        int rounds = 10;


        byte [] nextKey = new byte[16];
        addRoundKey(state, expandedKey);

        for (int i = 0; i < rounds - 1; i++) {
            substituteBytes(state);
            state = shiftRows(state);
            state = mixColumns(state);
            System.arraycopy(expandedKey, 16 * (i + 1), nextKey, 0, 16);
            addRoundKey(state, nextKey);
        }

        substituteBytes(state);
        state = shiftRows(state);
        System.arraycopy(expandedKey, rounds * 16, nextKey, 0, 16);
        addRoundKey(state, nextKey);

        return IO.byteArrayFromMatrix(state);
    }

    byte [] decrypt(byte [] data, byte[] expandedKey) {
        int rounds = 10;
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


    private byte GMul(byte a, byte b) {
        byte p = 0;

        for (int counter = 0; counter < 8; counter++) {
            if ((b & 1) != 0) {
                p ^= a;
            }

            boolean hi_bit_set = (a & 0x80) != 0;
            a <<= 1;
            if (hi_bit_set) {
                a ^= 0x1B;
            }
            b >>= 1;
        }

        return p;
    }

    public static byte [] keyExpansion(byte[] input_key) {
        int bytes = 176;

        byte[] expanded_keys = new byte[bytes];
        System.arraycopy(input_key, 0, expanded_keys, 0, 16);

        int bytes_generated = 16;
        int rcon_iteration = 1;
        byte[] temp = new byte[4];

        while (bytes_generated < bytes) {
            System.arraycopy(expanded_keys, bytes_generated - 4, temp, 0, 4);

            if (bytes_generated % 16 == 0)
                key_expansion_core(temp, rcon_iteration++);

            for (int a = 0; a < 4; a++) {
                expanded_keys[bytes_generated] = (byte) (expanded_keys[bytes_generated - 16] ^ temp[a]);
                bytes_generated++;
            }
        }
        return expanded_keys;
    }

    static void key_expansion_core(byte[] in, int i) {
        byte temp = in[0];
        in[0] = in[1];
        in[1] = in[2];
        in[2] = in[3];
        in[3] = temp;

        for (int j = 0; j < 4; j++) {
            in[j] = Utilities.substitute(in[j]);
        }

        in[0] ^= Utilities.rcon[i];
    }
}
