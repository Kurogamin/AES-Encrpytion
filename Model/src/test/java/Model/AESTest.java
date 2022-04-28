package Model;

import org.junit.jupiter.api.Test;

import java.io.Console;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.*;

class AESTest {

    AES aes = new AES();

    @Test
    void shiftRows() {
        SBox sBox = new SBox();

        byte left = (byte) 0b11110000;
        //assertEquals(sBox.substitute(left), (byte) 0x8c);
        //assertEquals(sBox.substitute(sBox.substitute(left), true), left);

        byte [][] matrix = new byte[4][4];
        int i = 0;
        for (int j = 0; j < 4; j++) {
            for (int k = 0; k < 4; k++) {
                matrix[j][k] = (byte) i++;
            }
        }
        //printMatrix(matrix);
        System.out.println();
        matrix = aes.shiftRows(matrix);
        //printMatrix(matrix);
        System.out.println();
        //matrix = aes.shiftRows(matrix, true);
        //printMatrix(matrix);
    }

    @Test
    void substituteBytes() {
        int [][] testOriginalMatrix =  {{0xEA, 0x04, 0x65, 0x85},
                {0x83, 0x45, 0x5D, 0x96},
                {0x5C, 0x33, 0x98, 0xB0},
                {0xF0, 0x2D, 0xAD, 0xC5}};
        int [][] testSubstitutedMatrix =   {{0x87, 0xF2, 0x4D, 0x97},
                {0xEC, 0x6E, 0x4C, 0x90},
                {0x4A, 0xC3, 0x46, 0xE7},
                {0x8C, 0xD8, 0x95, 0xA6}};

        byte [][] testOriginalByteMatrix = aes.convertIntToByteArray(testOriginalMatrix);

        byte [][] testSubstitutedByteMatrix = aes.convertIntToByteArray(testSubstitutedMatrix);

        byte [][] testWorkingMatrix = Arrays.copyOf(testOriginalByteMatrix, testOriginalByteMatrix.length);

        assertArrayEquals(aes.substituteBytes( testWorkingMatrix, false), testSubstitutedByteMatrix);

        assertArrayEquals(aes.substituteBytes(testWorkingMatrix, true), testOriginalByteMatrix);
    }

    @Test
    void multipleColumn() {

        int [][] testMatrix =  {{0x87, 0xF2, 0x4D, 0x97},
                                {0x6E, 0x4C, 0x90, 0xEC},
                                {0x46, 0xE7, 0x4A, 0xC3},
                                {0xA6, 0x8c, 0xD8, 0x95}};

        //byte [][] testByteMatrix = aes.convertIntToByteArray(testMatrix);
        //System.out.println(14 ^ 74);
        //assertEquals(aes.multipleColumn(testByteMatrix, 0, 2), 0x47);
    }
}