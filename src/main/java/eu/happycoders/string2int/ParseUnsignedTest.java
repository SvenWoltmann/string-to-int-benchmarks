
package eu.happycoders.string2int;

public class ParseUnsignedTest {

    public static void main(String[] args) {
        System.out
                .printf(
                        "%11s  %21s  %21s  %21s  %21s%n",
                        "String",
                        "parseInt()",
                        "int as hex",
                        "parseUnsignedInt()",
                        "unsigned int as hex");

        parse("-2147483649");
        parse("-2147483648");
        parse("-1000000000");
        parse("-1");
        parse("0");
        parse("1000000000");
        parse("2147483647");
        parse("2147483648");
        parse("3000000000");
        parse("4294967295");
        parse("4294967296");
    }

    private static void parse(String s) {
        System.out.printf("%11s", s);

        try {
            int i = Integer.parseInt(s);
            System.out.printf("  %21d", i);
            System.out.printf("  %21s", Integer.toHexString(i));
        } catch (NumberFormatException ex) {
            System.out.printf("  %21s", "NumberFormatException");
            System.out.printf("  %21s", "N/A");
        }

        try {
            int i = Integer.parseUnsignedInt(s);
            System.out.printf("  %21d", i);
            System.out.printf("  %21s%n", Integer.toHexString(i));
        } catch (NumberFormatException ex) {
            System.out.printf("  %21s", "NumberFormatException");
            System.out.printf("  %21s%n", "N/A");
        }
    }

}
