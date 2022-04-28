package Model;

public class TESTS {
    public static void main(String args[]) {
        byte left = (byte) 0b11110000;
        System.out.println(SBox.substitute(left) == (byte) 0x8c);
        System.out.println(SBox.substitute(SBox.substitute(left), true) == left);
    }

}
