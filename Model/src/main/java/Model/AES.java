package Model;

public class AES {

    private final int [][] matrixToMixColumns ={{2, 3, 1, 1},
                                                {1, 2, 3, 1},
                                                {1, 1, 2,3},
                                                {3, 1, 1, 2}};


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

    //public byte [][] mixColumns(byte [][] matrix) {
        //for(int i = 0; i < 16; i++) {

        //}
    //}

/*    public byte multipleColumn(byte [][] matrix, int columnIndex, int rowIndex) {
        byte result = 0;
        for (int i = 0; i < 4; i ++) {
            result ^= (byte) (matrixToMixColumns[rowIndex][i] * matrix[i][columnIndex]);
        }
        return (byte)result;
    }*/

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
