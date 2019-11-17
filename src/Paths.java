import java.math.BigInteger;

/** Contains several algorithms for a specific lattice path problem - slow recursive
 * but very accurate and 2 less accurate but much faster algorithms using divide and
 * conquer principle and dynamic programming by saving already calculated values to a
 * BigInteger 2D array (n^2 memory usage).
 * Created for Week 2 exercises of Algo
 * @author laurenwalton */
public class Paths {

  public static IO io = new IO();

  private static int n;

  private static BigInteger[][] savedValues;

  private static BigInteger paths;

  public static void main(String[] args) {

    System.out.println("We're going to solve a lattice problem!");
    System.out.println("How many possibilities are there from (0,0) to (n,0)?");


    n = io.readInt("Please enter the value of n: ");

    while (n < 2){
      System.out.println("Please choose a larger value for n");
      n = io.readInt("Please enter the value of n: ");
    }

    //savedValues = new BigInteger[n+1][n+1];

    //Uncomment below for very slow but accurate recursive algorithm
    paths = BigInteger.ZERO;
    calcPath(0, 0, Move.START);
    System.out.println("The number of possible paths for n = " + n + " is: " + paths);


    //Uncomment this section for endpoint-to-startpoint algorithm
    /*
    calculatePath(n, 0, Move.START);
    if (savedValues[n][0] != null){
      System.out.println("The number of possible paths for n = " + n + " is: " + savedValues[n][0]);
    }*/

    //Very fast but slightly inaccurate - still has issues with nodes that can be passed through without needing to
    //know the value at that node
/*    calcForwardPath(0, 0, Move.START);
    if (savedValues[0][0] != null){
      System.out.println("The number of possible paths for n = " + n + " is: " + savedValues[0][0]);
    }*/
  }


  /**
   * Faster algorithm using dynamic programming + divide and conquer, no of paths is stored
   * and added based on previous nodes to find total -- moves forwards
   * @param x initial value is 0
   * @param y intial value is 0
   * @param prevMove the move that was made last, necessary to ensure rule requirements are met
   * @return number of paths for given n value
   */
  public static BigInteger calcForwardPath(int x, int y, Paths.Move prevMove) {

    //set some known base values
    savedValues[n][0] = BigInteger.ONE;
    savedValues[n-1][1] = BigInteger.ONE;

    //set values outside of triangle as 0 so are not accidentally calculated
    for(int i = n; i > (n+1)/2; i--){
      savedValues[i][n + 1 - i] = BigInteger.ZERO;
    }

    //if the current point is already in storage, just return the no. of paths from there
    if (savedValues[x][y] != null){
      return savedValues[x][y];
      //if current point not in storage, calc by dividing into smaller problems and check/calculate these recursively
    } else {
      savedValues[x][y] = BigInteger.ZERO;

      //move diagonally up and left as long as you would not cross the diagonal x,x
      if(x > 0 && y < x - 1){
        savedValues[x][y] = savedValues[x][y].add(savedValues[x-1][y+1] != null ? savedValues[x-1][y+1] : calcForwardPath(x-1, y+1, Paths.Move.UPLEFT));
      }

      //move right as long as we are still left of the endpoint
      if(x < n && prevMove != Paths.Move.DOWNRIGHT){
        savedValues[x][y] = savedValues[x][y].add(savedValues[x+1][y] != null ? savedValues[x+1][y] : calcForwardPath(x+1, y, Paths.Move.RIGHT));
      }

      //move diagonally down and right as long as not below x = 0 line and still left of endpoint
      if(x < n && y > 0 && prevMove != Paths.Move.UPLEFT){
        savedValues[x][y] = savedValues[x][y].add(savedValues[x+1][y-1] != null ? savedValues[x+1][y-1] : calcForwardPath(x+1, y-1, Paths.Move.DOWNRIGHT));
      }

      //if it is possible to do the quick down/right then up/left move then calc that here
      if(x+y != 5 && x < n && y > 0 && prevMove != Paths.Move.UPLEFT && prevMove != Paths.Move.DOWNRIGHT) {
        savedValues[x][y] = savedValues[x][y].add(BigInteger.ONE);
      }

      return savedValues[x][y];
    }
  }

  /**
   * Faster algorithm using dynamic programming + divide and conquer, no of paths is stored
   * and added based on previous nodes to find total
   * @param x initial value is n (works from endpoint backwards)
   * @param y intial value is 0
   * @param nextMove the move that the calculation is based off from the endpoint, necessary to ensure rule
   *                 requirements are met
   * @return number of paths for given n value
   */
  public static BigInteger calculatePath(int x, int y, Move nextMove) {

    //set some known base values
    savedValues[1][0] = BigInteger.ONE;
    savedValues[2][0] = BigInteger.ONE;
    savedValues[1][1] = BigInteger.ONE;


    //if the current point is already in storage, just return the no. of paths from there
    if (savedValues[x][y] != null){


      return savedValues[x][y];
      //if current point not in storage, calc by dividing into smaller problems and check/calculate these recursively
    } else {
      savedValues[x][y] = BigInteger.ZERO;

      //move possible from diagonal below right
      if (x < n && y > 0 && nextMove != Move.DOWNRIGHT){

        //edge case where direct down and up is not calculated because the node was already considered and
        //path doesn't actually pass through it just touches the node
        savedValues[x][y] = savedValues[x][y].add(BigInteger.ONE);

        savedValues[x][y] = savedValues[x][y].add(savedValues[x+1][y-1] != null ? savedValues[x+1][y-1] : calculatePath(x+1, y-1, Move.UPLEFT));
      }

      //move possible from left as long as if would not come from over the diagonal line y=x
      if (x > 0  && y < x){
        savedValues[x][y] = savedValues[x][y].add(savedValues[x-1][y] != null ? savedValues[x-1][y] : calculatePath(x-1, y, Move.RIGHT));
      }

      //move possible from diagonal above left
      if (x > 0 && y < x - 2 && nextMove!= Move.RIGHT){
        savedValues[x][y] = savedValues[x][y].add(savedValues[x-1][y+1] != null ? savedValues[x-1][y+1] : calculatePath(x-1, y+1, Move.DOWNRIGHT));
      }

      return savedValues[x][y];
    }
  }


  /**
   * Slow algorithm, starts from the beginning and calcs recursively adding each path as it is found
   * @param x initial value should be 0
   * @param y initial value should be 0
   * @param prevMove pass in the last move that was made to check that the rules are being followed
   */
  public static void calcPath(int x, int y, Move prevMove){
    if(x == n && y == 0){
      paths = paths.add(BigInteger.ONE);
    } else {
      //move right as long as we are still left of the endpoint
      if(x < n && prevMove != Move.DOWNRIGHT){
        calcPath(x+1, y, Move.RIGHT);
      }
      //move diagonally up and left as long as you would not cross the diagonal x,x
      if(x > 0 && y < x - 1){
        calcPath(x - 1, y + 1, Move.UPLEFT);

      }
      //move diagonally down and right as long as not below x = 0 line and still left of endpoint
      if(x < n && y > 0 && prevMove != Move.UPLEFT){
        calcPath(x + 1, y - 1, Move.DOWNRIGHT);
      }
    }
  }

  /**
   * An enum for storing the possible moves that can be made by this lattice path problem
   */
  public enum Move{
    START, RIGHT, DOWNRIGHT, UPLEFT
  }

}
