package eu.happycoders.string2int;

public class HexDemo_3_Long {

    public static void main(String[] args) {
        int hex = 0xCAFEBABE;
        System.out.println("hex        = " + hex);
        System.out.println("hex binary = " + Integer.toBinaryString(hex));
        String s = Integer.toHexString(hex);
        System.out.println("s          = " + s);
        long l = Long.parseLong(s, 16);
        System.out.println("l          = " + l);
        System.out.println("l binary = " + Long.toBinaryString(l));
        System.out.println("l hex    = " + Long.toHexString(l));
        Thread.currentThread().interrupt();
    }

}
