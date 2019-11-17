import java.math.BigInteger;

public class LatticePath {

    public static IO io = new IO();

    private static int n;
    private static BigInteger paths;
    private static String path;
    private static BigInteger[][] savedValues;


    public static void main(String[] args) {
	// write your code here

        paths = BigInteger.ZERO;
        path = "";

        System.out.println("We're going to solve a lattice problem!");
        System.out.println("From (0,0) to (n,n) we can make the following moves: Right, Up and Left followed by Right, Down and Right followed by Right.");
        System.out.println("How many possibilities are there?");


        n = io.readInt("Please enter the value of n: ");

        getStringPath(0, 0, path);
        calculatePath(0, 0);

        System.out.println("The number of possible paths for n = " + n + " is: " + paths);

    }

    public static void calculatePath(int x, int y) {
        if(x == n && y == n){
            paths = paths.add(BigInteger.ONE);
        } else {
            //move right as long as we are still left of the endpoint and either below the endpoint or leaving
            //enough space to the right to get down using the down and left move
            if(x < n && y <= (n + (n - x + 1)/2)){
                calculatePath(x+1, y);
            }
            //move up as long as there would still be room to move down and right to get to the endpoint
            if(x > 0 && y < (n + (n - x)/2)){
                calculatePath(x, y+1);
            }
            //move down and right 2 spaces as long as we are at least 2 spaces left of the endpoint and it wouldn't
            //take us into negative y values
            if((x < (n-1)) && y > 0){
                calculatePath(x+2, y-1);
            }
        }
    }

    public static void getStringPath(int x, int y, String path) {
        if(x == n && y == n){
            System.out.println(path);
        } else {
            //move right as long as we are still left of the endpoint and either below the endpoint or leaving
            //enough space to the right to get down using the down and left move
            if(x < n && y <= (n + (n-x+1)/2)){
                getStringPath(x+1, y, path + "right, ");
            }
            //move up as long as there would still be room to move down and right to get to the endpoint
            if(x > 0 && y < (n + (n-x)/2)){
                getStringPath(x, y+1, path + "up, ");
            }
            //move down and right 2 spaces as long as we are at least 2 spaces left of the endpoint and it wouldn't
            //take us into negative y values
            if((x < (n-1)) && y > 0){
                getStringPath(x+2, y-1, path + "down and right, ");
            }
        }
    }


    /**
     * Safe Copy
     * Faster algorithm using dynamic programming + divide and conquer, no of paths is stored
     * and added based on previous nodes to find total
     * @param x initial value is n (works from endpoint backwards)
     * @param y intial value is 0
     * @param nextMove the move that the calculation is based off from the endpoint, necessary to ensure rule
     *                 requirements are met
     * @return number of paths for given n value
     */
    public static BigInteger calculatePath(int x, int y, Paths.Move nextMove) {

        //set some known base values
        savedValues[1][0] = BigInteger.ONE;
        savedValues[2][0] = BigInteger.ONE;
        savedValues[1][1] = BigInteger.ONE;


        //if the current point is already in storage, just return the no. of paths from there
        if (savedValues[x][y] != null) {
            return savedValues[x][y];
            //if current point not in storage, calc by dividing into smaller problems and check/calculate these recursively
        } else {
            savedValues[x][y] = BigInteger.ZERO;

            //move possible from diagonal above left
            if (x > 0 && y < x - 2 && nextMove != Paths.Move.RIGHT) {
                savedValues[x][y] = savedValues[x][y].add(savedValues[x - 1][y + 1] != null ? savedValues[x - 1][y + 1] : calculatePath(x - 1, y + 1, Paths.Move.DOWNRIGHT));
                System.out.println("A diagonal move down right was possible from " + (x - 1) + "," + (y + 1));
            }

            //move possible from left as long as if would not come from over the diagonal line y=x
            if (x > 0 && y < x) {
                savedValues[x][y] = savedValues[x][y].add(savedValues[x - 1][y] != null ? savedValues[x - 1][y] : calculatePath(x - 1, y, Paths.Move.RIGHT));
                System.out.println("A move right was possible from " + (x - 1) + "," + (y));
            }

            //move possible from diagonal below right
            if (x < n && y > 0 && nextMove != Paths.Move.DOWNRIGHT) {

                savedValues[x][y] = savedValues[x][y].add(savedValues[x + 1][y - 1] != null ? savedValues[x + 1][y - 1] : calculatePath(x + 1, y - 1, Paths.Move.UPLEFT));
                System.out.println("A diagonal move up and left was possible from " + (x + 1) + "," + (y - 1));
            }


            System.out.println("Saved value for (" + x + "," + y + ") is now " + savedValues[x][y]);
            return savedValues[x][y];
        }
    }

}
