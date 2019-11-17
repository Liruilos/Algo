import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigDecimal;
import java.math.BigInteger;

/** The class 'IO' is an updated version of InteractiveIO that lets you read in many data formats directly.
 * Now includes a default value for read-ins in case of an input exception.
 * Created for Week 5 exercises of IProg. Also used in week 6, week 7 and week 10.
 * @author laurenwalton */
class IO {

    private static boolean run = true;

    private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));

    public static void write(String s) {
        writeAndFlush(s);
    }

    public static void writeln(String s) {
        writeAndFlush(s + "\n");
    }


    public static String promptAndRead(String s) {
        while (run) {
            try {
                writeAndFlush(s);
                return br.readLine();
            } catch (Exception e) {
                System.out.println("That was not a valid input, please try again.");
            }
        }
        return "";
    }

    private static void writeAndFlush(String s) {
        System.out.print(s);
        System.out.flush();
    }

    public static int readInt(String s) {
        while (run) {
            try {
                return Integer.parseInt(promptAndRead(s).trim());
            } catch (Exception e) {
                System.out.println("That was not a valid integer, please try again.");
            }
        }
        return 0;

    }

    public static long readLong(String s) {
        while (run) {
            try {
                return Long.parseLong(promptAndRead(s).trim());
            } catch (Exception e) {
                System.out.println("That was not a valid integer, please try again.");
            }
        }
        return 0;
    }

    public static float readFloat(String s) {
        while (run) {
            try {
                return Float.parseFloat(promptAndRead(s).trim());
            } catch (Exception e) {
                System.out.println("That was not a valid integer, please try again.");
            }
        }
        return 0;
    }

    public static double readDouble(String s) {
        while (run) {
            try {
                return Double.parseDouble(promptAndRead(s).trim());
            } catch (Exception e) {
                System.out.println("That was not a valid integer, please try again.");
            }
        }
        return 0;
    }

    public static BigInteger readBigInteger(String s) {
        while (run) {
            try {
                return new BigInteger(promptAndRead(s).trim());
            } catch (Exception e) {
                System.out.println("That was not a valid integer, please try again.");
            }


        }
        return BigInteger.ZERO;
    }

    public static BigDecimal readBigDecimal(String s) {
        while (run) {
            try {
                return new BigDecimal(promptAndRead(s).trim());
            } catch (Exception e) {

                System.out.println("That was not a valid integer, please try again.");

            }

        }
        return BigDecimal.ZERO;
    }

}