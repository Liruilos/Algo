import java.math.BigInteger;
import java.util.HashMap;

public class Geldwechsel2 {
  // Indices 0 1 2 3 4
  static int betrag[] = {1, 2, 5, 9, 11};
  static int n = betrag.length; // Anzahl Muenzen
  // # Wechselarten fuer Betrag g und Muenzen mit Indices <= i
  //static BigInteger w[][]; // Tabelle
  static HashMap<String, BigInteger> z;

  static BigInteger x(BigInteger g, BigInteger i) {

    if (g.compareTo(BigInteger.ZERO) < 0) {
      return new BigInteger("0");
    } else if (i.compareTo(BigInteger.ZERO) == 0) {
      if (g.mod(BigInteger.ONE).compareTo(BigInteger.ZERO) == 0) {
        return BigInteger.ONE;
      } else {
        return BigInteger.ZERO;
      }
    } else if (getValue(i, g).compareTo(BigInteger.ZERO) != 0) { // z.getValue(
      return getValue(i, g);
    } else {
      System.out.println(g);
      putValue(i, g, x(g, i.subtract(BigInteger.ONE)).add(x(g.subtract(BigInteger.valueOf(betrag[i.intValue()])), i)));
      return getValue(i, g);

    }
  }


  public static void main(String[] args) {
    BigInteger d = new BigInteger("3").pow(7);
    z = new HashMap<>();

    int g = d.intValue();

    fillHashMap(g + 1, n);
    System.out.println(getValue(new BigInteger("3"), new BigInteger("0")));

    System.out.println("Den Betrag " + g + " kann man auf "
        + x(d, BigInteger.valueOf(n - 1)) + " verschiedene Arten wecheln.");
  }

  static void putValue(BigInteger a, BigInteger b, BigInteger c) {
    String s = a + "-" + b;
    z.put(s, c);
  }

  static BigInteger getValue(BigInteger a, BigInteger b) {
    String s = a + "-" + b;
    return z.get(s);
  }

  static void fillHashMap(int a, int b) {
    for (int j = 0; j < b; j++) {
      for (int i = 0; i < a; i++) {
        String s = j + "-" + i;
        z.put(s, BigInteger.ZERO);
      }
    }
  }
}
