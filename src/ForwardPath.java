import java.math.BigInteger;

public class ForwardPath {

  public static IO io = new IO();
  private static boolean running = true;

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

    savedValues = new BigInteger[n+1][n+1];

    //Uncomment below for slow recursive algorithm
    //paths = BigInteger.ZERO;
    //calcPath(0, 0, Move.START);
    //System.out.println("The number of possible paths for n = " + n + " is: " + paths);


    calcForwardPath(0, 0, Paths.Move.START);
    if (savedValues[0][0] != null){
      System.out.println("The number of possible paths for n = " + n + " is: " + savedValues[0][0]);
      running = false;
    }

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
}
