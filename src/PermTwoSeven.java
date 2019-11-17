import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;

/** Contains a recursive algorithm for printing all possible permutations of number from 1 to n,
 * where any two asjacent numbers must have a different of either 2 or 7.
 * Created for Week 4 exercises of Algo
 * @author laurenwalton */
public class PermTwoSeven {

  private static boolean run = true;
  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  private static int n;
  private static ArrayList<Integer> possibleNumbers = new ArrayList<>();
  private static ArrayList<Integer> perm = new ArrayList<>();
  private static int permCounter = 0;


  public static void main(String[] args) {
    n = readInt("Please Enter the value of n: ");

    for (int i = 1; i <= n; i++){
      possibleNumbers.add(i);
    }

    recursivePermFinder(perm, possibleNumbers);

    System.out.println("For the value of n = " + n + " there are " + permCounter + " permutations.");

  }

  private static void recursivePermFinder(ArrayList<Integer> existingPerm, ArrayList<Integer> availablePossibilities){
    for (Integer i : availablePossibilities){
      ArrayList<Integer> newPossibilities = (ArrayList<Integer>) availablePossibilities.clone();
      ArrayList<Integer> newPerm = (ArrayList<Integer>) existingPerm.clone();

      if (newPerm.isEmpty() || checkTwoOrSeven(newPerm.get(newPerm.size() - 1), i)){
        newPossibilities.remove(i);
        newPerm.add(i);

        if (newPossibilities.isEmpty()){
          arraylistPrintAndCount(newPerm);
          return;
        }

        recursivePermFinder(newPerm, newPossibilities);
      }

    }

  }

  private static void backtrackPermFinder(){
    int currentIndex = 0;
    while(!possibleNumbers.isEmpty()){
      int currentNumber = possibleNumbers.get(currentIndex);
      perm.add(currentNumber);

    }
  }

  private static boolean checkTwoOrSeven(int i, int j){
    return Math.abs(i - j) == 2 || Math.abs(i - j) == 7;
  }

  private static void arraylistPrintAndCount(ArrayList<Integer> list){
    for (int i : list){
      System.out.print(i + " ");
    }
    System.out.println();
    permCounter++;
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
