package eu.happycoders.string2int;

public class HexDemo_2_Unsigned {

    public static void main(String[] args) {
        int hex = 0xCAFEBABE;
        System.out.println("hex        = " + hex);
        System.out.println("hex binary = " + Integer.toBinaryString(hex));
        String s = Integer.toHexString(hex);
        System.out.println("s          = " + s);
        int i = Integer.parseUnsignedInt(s, 16);
        System.out.println("i          = " + i);
    }

}
