import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;

public class Brackets {


  private static boolean run = true;
  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  private static int n;

  private static BigInteger[] catalanNumberStorage;
  private static BigInteger valueToReduce;


  //used for recursive algorithm
  private static BigInteger totalBracketArrangements = BigInteger.ZERO;

  public static void main(String[] args) {

    //prepare storage
    catalanNumberStorage = new BigInteger[501];

    catalanNumberStorage[1] = BigInteger.ONE;

    while(run) {

      n = readInt("Please enter the value of n: ");

      if (n < 0 || n > 500) {
        n = readInt("The value of n should be between 0 and 500");
      }

      //recursiveBracketCounter("", 0);
      //System.out.println("Total possibilities of bracket arrangements is: " + totalBracketArrangements);

      valueToReduce = calcAllBracketPossibilities(n);

      System.out.println("Catalan number calculated! Reducing this to accurate number of brackets");

      calcCorrectBrackets(valueToReduce, 0);

      System.out.println("Bracket possibilities: " + valueToReduce);

    }
  }

  //start index at 0, indicates current open bracket
  private static void calcCorrectBrackets(BigInteger currentValue, int index){
    if (index == n){
      return;
    } else {
      if (index == 0){
        valueToReduce = valueToReduce.divide(BigInteger.valueOf(3));
        calcCorrectBrackets(valueToReduce, index+1);
      } else {
        valueToReduce = valueToReduce.subtract(currentValue.divide(BigInteger.valueOf(3)));
        calcCorrectBrackets(currentValue.divide(BigInteger.valueOf(3)), index+1);
      }
    }
  }

  private static BigInteger calcAllBracketPossibilities(int n){
    return (BigInteger.valueOf(3).pow(n)).multiply(catalan(n));
  }

  private static BigInteger catalan(int n)
  {
    // Base case
    if (n <= 1) return BigInteger.ONE;
    // Case is stored
    if (catalanNumberStorage[n] != null){
      return catalanNumberStorage[n];
    }

    // catalan(n) is sum of catalan(i)*catalan(n-i-1)
    BigInteger result = BigInteger.ZERO;
    for (int i=0; i<n; i++)
      result = result.add(catalan(i).multiply(catalan(n-i-1)));
    catalanNumberStorage[n] = result;

    return result;
  }

  private static void recursiveBracketCounter(String builtBrackets, int totalOpen){
    if (builtBrackets.length() == n*2){
      totalBracketArrangements = totalBracketArrangements.add(BigInteger.ONE);
      System.out.println("Finished Arrangement: " + builtBrackets);

    } else {

      //open round as long as max open not reached
      if (totalOpen < n){
        recursiveBracketCounter(builtBrackets + "(", totalOpen + 1);
      }
      //open square as long as max open not reached and at least one round bracket opened
      if (totalOpen < n && builtBrackets.contains("(")){
        recursiveBracketCounter(builtBrackets + "[",totalOpen + 1);
      }
      //open curly as long as max open not reached and at least one square bracket opened
      if (totalOpen < n && builtBrackets.contains("[")){
        recursiveBracketCounter(builtBrackets + "{",totalOpen + 1);
      }

      if (builtBrackets.length() != 0) {

        String filledBracketsRemoved = findReducedString(builtBrackets);

        if (filledBracketsRemoved.length() == 0){
          return;
        }

        String lastChar = filledBracketsRemoved.substring(filledBracketsRemoved.length() - 1);

        //close round basic
        if (lastChar.equals("(")){
          recursiveBracketCounter(builtBrackets + ")", totalOpen);
        }
        //close square basic
        if (lastChar.equals("[")){
          recursiveBracketCounter(builtBrackets + "]", totalOpen);
        }
        //close curly basic
        if (lastChar.equals("{")){
          recursiveBracketCounter(builtBrackets + "}", totalOpen);
        }

      }


    }

  }


  private static String findReducedString(String existingBrackets){
    //charAt
    //replaceAll
    if (existingBrackets.contains("()") || existingBrackets.contains("[]") || existingBrackets.contains("{}")) {
      existingBrackets = existingBrackets.replaceAll("\\(\\)", "");
      existingBrackets = existingBrackets.replaceAll("\\[]", "");
      existingBrackets = existingBrackets.replaceAll("\\{}", "");
      return findReducedString(existingBrackets);
    } else {
      return existingBrackets;
    }
  }




  private static int readInt(String s) {
    while (run) {
      try {
        System.out.print(s);
        System.out.flush();

        return Integer.parseInt(br.readLine().trim());
      } catch (Exception e) {
        System.out.println("That was not a valid integer, please try again.");
      }
    }
    return 0;
  }

}
