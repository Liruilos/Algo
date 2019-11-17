import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.math.BigInteger;
import java.util.HashMap;

/** Contains two algorithms for the week 2 lattice problem, one recursive for counting the
 * total number of nodes of all individual paths, and very fast algorithm for accurately
 * counting the number of possible paths.
 * Created for Week 4 exercises of Algo
 * @author laurenwalton */
public class PathNodes {

  private static boolean run = true;
  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  private static int n;

  private static BigInteger nodeCount;

  private static HashMap<String, BigInteger> storage = new HashMap<>();

  private static HashMap<String, BigInteger> nodeStorage = new HashMap<>();



  public static void main(String[] args) {
    n = readInt("Please enter the value of n: ");

    nodeCount = BigInteger.valueOf(n);

    System.out.println("The number of possible paths from 0,0 to " + n + ",0 is: " + countPaths(n, 0, 0));

    calcNodes(0, 0, Move.START, BigInteger.ZERO);
    System.out.println("The number of nodes passed through is: " + nodeCount);
  }

  private static void calcNodes(int x, int y, Move prevMove, BigInteger nodeCounter){

    if(x == n && y == 0){
      nodeCount = nodeCount.add(nodeCounter);

    } else {

      //move right as long as we are still left of the endpoint
      if(x < n && prevMove != Move.DOWNRIGHT){

        calcNodes(x+1, y, Move.RIGHT, nodeCounter.add(BigInteger.ONE));

      }
      //move diagonally up and left as long as you would not cross the diagonal x,x
      if(x > 0 && y < x - 1){

        calcNodes(x - 1, y + 1, Move.UPLEFT, nodeCounter.add(BigInteger.ONE));

      }
      //move diagonally down and right as long as not below x = 0 line and still left of endpoint
      if(x < n && y > 0 && prevMove != Move.UPLEFT){

        calcNodes(x + 1, y - 1, Move.DOWNRIGHT, nodeCounter.add(BigInteger.ONE));
      }
    }
  }

  private static BigInteger countPaths(int x, int y, int moveType){
    if(x < 0 || y < 0 || y > x){
      return BigInteger.ZERO;
    }
    if (x==0 && y==0){
      return BigInteger.ONE;
    }
    String storageKey = "" + x + "," + y + "," + moveType;

    BigInteger storedValue = storage.get(storageKey);

    if (storedValue != null){
      return storedValue;
    } else {
      storedValue = BigInteger.ZERO;

      //right move always possible
      storedValue = storedValue.add(countPaths(x-1, y, 1));

      //is down and right move possible?
      if (moveType==0 || moveType==2 || moveType==3){
        storedValue = storedValue.add(countPaths(x-1, y+1, 2));
      }

      //is left and up move possible?
      if (moveType==1 || moveType==3){
        storedValue = storedValue.add(countPaths(x+1, y-1, 3));
      }

      //saved the calculated value for future use
      storage.put(storageKey, storedValue);
      return storedValue;

    }
  }

  //Failed attempts at a faster node counter beyond this point
  //-----------------------------------------------------------------------------------//

  //TODO if using countToFrom, remember to refresh storage map each time
  private static BigInteger countNodes(int x, int y, int t){

    if(x < 0 || y < 0 || y > x){
      return BigInteger.ZERO;
    }
    if (x==0 && y==0){
      return BigInteger.valueOf(4);
    }
    String storageKey = "" + x + "," + y + "," + t;

    BigInteger storedValue = nodeStorage.get(storageKey);

    if (storedValue != null){
      return storedValue;
    } else {
      storedValue = BigInteger.ZERO;

      //right move always possible
      storedValue = storedValue.add(countNodes(x - 1, y, 1));

      //is down and right move possible?
      if (t == 0 || t == 2 || t == 3) {
        storedValue = storedValue.add(countNodes(x - 1, y + 1, 2));
      }

      //is left and up move possible?
      if (t == 1 || t == 3) {
        storedValue = storedValue.add(countNodes(x + 1, y - 1, 3));
      }

      //saved the calculated value for future use
      nodeStorage.put(storageKey, storedValue);
      return storedValue;


    }

  }

  private static BigInteger countToFrom(int x, int y, int t, int xEnd, int yEnd){
    if(x < 0 || y < 0 || y > x){
      return BigInteger.ZERO;
    }
    if (x==xEnd && y==yEnd){
      return BigInteger.ONE;
    }
    String storageKey = "" + x + "," + y + "," + t;

    BigInteger storedValue = storage.get(storageKey);

    if (storedValue != null){
      return storedValue;
    } else {
      storedValue = BigInteger.ZERO;

      //right move always possible
      storedValue = storedValue.add(countToFrom(x-1, y, 1, xEnd, yEnd));

      //is down and right move possible?
      if (t==0 || t==2 || t==3){
        storedValue = storedValue.add(countToFrom(x-1, y+1, 2, xEnd, yEnd));
      }

      //is left and up move possible?
      if (t==1 || t==3){
        storedValue = storedValue.add(countToFrom(x+1, y-1, 3, xEnd, yEnd));
      }

      //saved the calculated value for future use
      storage.put(storageKey, storedValue);
      return storedValue;


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

  public enum Move{
    START, RIGHT, DOWNRIGHT, UPLEFT
  }
}
