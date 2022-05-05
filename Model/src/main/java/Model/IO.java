package Model;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public class IO {
    public byte[] readFileToByteArray(String filePath) {
        Path path = Paths.get(filePath);

        try {
            return Files.readAllBytes(path);
        } catch (IOException e) {
            System.out.println("No such file I guess");
            return new byte[0];
        }
    }

    public static byte [][] makeByteMatrix(byte[] packet) {
        if (packet.length > 16) {
            System.out.println("Packet too large(more than 16 bytes)");
            return new byte[0][0];
        }
        byte [][] matrix = new byte[4][4];
        for (byte [] row : matrix) {
            for (byte packetByte : row) {
                packetByte = 0;
            }
        }
        for (double i = 0; i < packet.length; i++) {
            matrix[(int) (i % 4)][(int) Math.floor(i / 4)] = packet[(int) i];
        }

        return matrix;
    }

    public static byte[] byteArrayFromMatrix(byte [][] matrix) {
        byte [] output = new byte[matrix.length * matrix[0].length];
        for (int i = 0; i < 4; i++) {
            for (int j = 0; j < 4; j++) {
                output[i * 4 + j] = matrix[j][i];
            }
        }
        return output;
    }
}
