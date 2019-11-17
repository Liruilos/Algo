import java.math.BigInteger;

public class Geldwechsel {
  // Indices 01234567
  static int betrag [] = {1, 2, 5, 9, 11};
  static int n = betrag.length; // Anzahl Muenzen


static BigInteger w [][];
static BigInteger w (BigInteger g, int l, int i) {

    /*return g < 0 ? 0 :
    i==0 ?
      (g%betrag[0]==0?1:0)

      : w[g][i]!=0 ? w[g][i] :

    (w [g][i] = w (g, i-1) + w (g-betrag[i], i)) ;
    }*/

  return g.compareTo(BigInteger.ZERO) == -1 ? BigInteger.ZERO :
      i == 0 ?
          (g.mod(new BigInteger(String.valueOf(betrag[0]))).equals(BigInteger.ZERO) ? BigInteger.ONE : BigInteger.ZERO) :
          !w[l][i].equals(BigInteger.ZERO) ? w[l][i] :
              (w[l][i] = w(g, l, i-1).add(w(g.subtract(new BigInteger(String.valueOf(betrag[i]))) , l - betrag[i], i)));


}
public static void main (String[] args){
    String initialValue = "19683"; // g lesen 19683
    int l = Integer.parseInt(initialValue);
    BigInteger g = new BigInteger(initialValue);
    w = new BigInteger[l+1][n]; // w dimensionieren
  for (int j = 0; j < l+1; j++) {
    for (int i = 0; i < n; i++) {
      w[j][i] = BigInteger.ZERO;
    }
  }
    System.out.println ("Den Betrag "+g+" kann man auf "+ w (g, l,n-1) + " verschiedene Arten wecheln.");
  }
}
