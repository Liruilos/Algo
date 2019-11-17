import java.io.BufferedReader;
import java.io.InputStreamReader;

/**
 * An Implementation of the Algorithm X with a Dancing Links structure where Pentominos
 * and Dominos are fitted into a n x 7 area. Calculates the number of solutions very quickly
 * until about n = 6, at which point it starts to slow down considerably.
 * A table of already calculated values is printed at program start.
 * Created for Week 6 exercises of Algo
 * @author laurenwalton
 * Credit: Alois Heinz for the Algorithm X methods
 */
public class DLXPentominoDLUX {

  //Input variables
  private static boolean run = true;
  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  private static int n;

  //Matrix variables
  private static DLXNode h = new DLXNode();
  private static DLXNode[][] nodeSetup;
  private static int matrixRowCounter = 1; //row 0 is headers
  private static int cnt = 0;


  public static void main(String[] args) {


    printTable();

    n = readInt("Please enter the value of n: ");

    if (n < 0) {
      n = readInt("The value of n should be greater than 0, please try again: ");
    }

    //not all shapes can fit in all sizes of board
    //matrix gets an extra row for column headers
    int estTotalPositions;
    if (n > 2){
      estTotalPositions = 6*(n+2) + 7*(n-1) + 5*(n-2) + 10*(n-1) + 12*(n-2) + 24*(n-3) + 16*(n-1);
    } else if (n > 1) {
      estTotalPositions = 6*(n+2) + 7*(n-1) + 10*(n-1) + 8*(n-1);
    } else {
      estTotalPositions = 6*(n+1);
    }

    //first value is row (max value of different configurations of dominos/pentominos)
    //second value is column (as long as the board has spaces)
    nodeSetup = new DLXNode[estTotalPositions][7*n];

    System.out.println("Building the Node Matrix...");
    buildHeaders(h);
    calcRows();

    System.out.println("Building the Dancing Links based on Matrix...");
    buildRows();
    //printNodeSetup();

    System.out.println("Starting to search for number of solutions...");
    search(0);

    System.out.println("The number of solutions for this size is: " + cnt);

  }

  //----------------------- Building The Headers ------------------------//

  private static void buildHeaders(DLXNode anchor){

    for (int i = 0; i < n*7; i++){
      //make a new header and add it to the matrices
      DLXNode newHeader = new DLXNode();
      nodeSetup[0][i] = newHeader;

      //on the left bind to previous node (initially the anchor node)
      if (i == 0){
        //first node links left to anchor
        newHeader.L = anchor;
        anchor.R = newHeader;
      } else if (i == n*7 - 1){
        //last node links left AND right to anchor
        newHeader.L = nodeSetup[0][i-1];
        nodeSetup[0][i-1].R = newHeader;

        newHeader.R = anchor;
        anchor.L = newHeader;
      } else {
        //every other node sets left links based on index
        newHeader.L = nodeSetup[0][i-1];
        nodeSetup[0][i-1].R = newHeader;
      }

    }

  }


  //------------------------- Building The Rows -------------------------//

  private static void calcRows(){

    //for every row, fill in the possibilities - createMatrixEntry takes a list of board positions
    // eg {0, 1} would be a horizontal domino in the top left corner
    //each of these rows is a row on the board, as limited pieces can fit at the edges
    for (int row = 0; row < n; row++) {
      int rowStarter = row*7;
      //domino -
      for (int i = 0; i < 6; i++) {
        createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1});
      }
      //domino |
      if (row < n - 1) {
        for (int i = 0; i < 7; i++) {
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 7});
        }
      }
      //pentomino +
      if (row < n - 2) {
        for (int i = 0; i < 5; i++) {
          createMatrixEntry(new int[]{
              rowStarter + i + 1,
              rowStarter + i + 7, rowStarter + i + 8, rowStarter + i + 9,
              rowStarter + i + 15
          });
        }
      }

      //pentomino C Tall
      if (row < n - 2) {
        for (int i = 0; i < 6; i++) {
          //C shape
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 1,
              rowStarter + 7 + i,
              rowStarter + 14 + i, rowStarter + 15 + i
          });
          //reverse C shape
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 1,
              rowStarter + 8 + i,
              rowStarter + 14 + i, rowStarter + 15 + i
          });
        }
      }

      //pentomino C Wide
      if (row < n - 1) {
        for (int i = 0; i < 5; i++) {
          //U shape |__|
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 2,
              rowStarter + 7 + i, rowStarter + 8 + i, rowStarter + 9 + i
          });
          //upside down U shape
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 1, rowStarter + i +2,
              rowStarter + 7 + i, rowStarter + 9 + i
          });
        }
      }

      //pentomino L Tall
      if (row < n - 3) {
        for (int i = 0; i < 6; i++) {
          //L shape
          createMatrixEntry(new int[]{
              rowStarter + i,
              rowStarter + i + 7,
              rowStarter + i + 14,
              rowStarter + i + 21, rowStarter + i + 22
          });

          //backwards L shape
          createMatrixEntry(new int[]{
              rowStarter + i + 1,
              rowStarter + i + 8,
              rowStarter + i + 15,
              rowStarter + i + 21, rowStarter + i + 22
          });

          //upsidedown L shape
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 1,
              rowStarter + i + 7,
              rowStarter + i + 14,
              rowStarter + i + 21
          });

          //upsidedown and backwards L shape
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 1,
              rowStarter + i + 8,
              rowStarter + i + 15,
              rowStarter + i + 22
          });
        }
      }

      //pentomino L Wide
      if (row < n - 1) {
        for (int i = 0; i < 4; i++) {
          // |___ shape
          createMatrixEntry(new int[]{
              rowStarter + i,
              rowStarter + i + 7, rowStarter + i + 8, rowStarter + i + 9, rowStarter + i + 10
          });

          // ___| shape
          createMatrixEntry(new int[]{
              rowStarter + i + 3,
              rowStarter + i + 7, rowStarter + i + 8, rowStarter + i + 9, rowStarter + i + 10
          });

          // |--- shape
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 1, rowStarter + i + 2, rowStarter + i + 3,
              rowStarter + i + 7
          });

          // ---| shape
          createMatrixEntry(new int[]{
              rowStarter + i, rowStarter + i + 1, rowStarter + i + 2, rowStarter + i + 3,
              rowStarter + i + 10
          });
        }
      }
    }
  }

  private static void createMatrixEntry(int[] nodePositions){
    for (int i : nodePositions){
      nodeSetup[matrixRowCounter][i] = new DLXNode();
    }
    matrixRowCounter++;
  }

  private static void buildRows(){

    //link horizontally, every entry finds the next node right to connect to, headers already done with anchor
    for (int row = 1; row < nodeSetup.length; row++)
    {
      for (int col = 0; col < nodeSetup[row].length; col++)
      {
        if (nodeSetup[row][col] != null) {
          DLXNode currentNode = nodeSetup[row][col];
          int nextRightIndex = getNextRightNode(row, col);
          currentNode.R = nodeSetup[row][nextRightIndex];
          nodeSetup[row][nextRightIndex].L = currentNode;
        }
      }
    }

    //link vertically, every entry finds the next node up to connect to, and also set column header
    for (int row = 0; row < nodeSetup.length; row++)
    {
      for (int col = 0; col < nodeSetup[row].length; col++)
      {
        if (nodeSetup[row][col] != null) {
          DLXNode currentNode = nodeSetup[row][col];
          int nextUpIndex = getNextUpNode(row, col);
          currentNode.U = nodeSetup[nextUpIndex][col];
          nodeSetup[nextUpIndex][col].D = currentNode;

          currentNode.C = nodeSetup[0][col];
        }
      }
    }
  }

  private static int getNextRightNode(int row, int col){
    int c = wrapRight(col);
    while (c != col){
      if (nodeSetup[row][c] != null){
        return c;
      } else {
        c = wrapRight(c);
      }
    }
    return col;
  }

  private static int getNextUpNode(int row, int col){
    int r = wrapUp(row);
    while (r != row){
      if (nodeSetup[r][col] != null){
        return r;
      } else {
        r = wrapUp(r);
      }
    }
    return row;
  }

  private static int wrapRight(int i){
    return i == nodeSetup[0].length - 1 ? 0 : i + 1;
  }

  private static int wrapUp(int i){
    return i == 0 ? nodeSetup.length - 1 : i - 1;
  }

  //--------------------------- Algorithm X -----------------------------//

  /**
   * search tries to find and count all complete coverings of the DLX matrix.
   * Is a recursive, depth-first, backtracking algorithm that finds
   * all solutions to the exact cover problem encoded in the DLX matrix.
   * each time all columns are covered, static long cnt is increased
   *
   * @param k: number of levels (depth of search)
   */
  public static void search(int k) { // finds & counts solutions
    if (h.R == h) {
      cnt++;
      return;
    }     // if empty: count & done
    DLXNode c = h.R;                   // choose next column c
    cover(c);                          // remove c from columns
    for (DLXNode r = c.D; r != c; r = r.D) {  // forall rows with 1 in c
      for (DLXNode j = r.R; j != r; j = j.R) // forall 1-elements in row
        cover(j.C);                    // remove column
      search(k + 1);                    // recursion
      for (DLXNode j = r.L; j != r; j = j.L) // forall 1-elements in row
        uncover(j.C);                  // backtrack: un-remove
    }
    uncover(c);                        // un-remove c to columns
  }

  /**
   * cover "covers" a column c of the DLX matrix
   * column c will no longer be found in the column list
   * rows i with 1 element in column c will no longer be found
   * in other column lists than c
   * so column c and rows i are invisible after execution of cover
   *
   * @param c: header element of column that has to be covered
   */
  public static void cover(DLXNode c) { // remove column c
    c.R.L = c.L;                         // remove header
    c.L.R = c.R;                         // .. from row list
    for (DLXNode i = c.D; i != c; i = i.D)      // forall rows with 1
      for (DLXNode j = i.R; i != j; j = j.R) {   // forall elem in row
        j.D.U = j.U;                     // remove row element
        j.U.D = j.D;                     // .. from column list
      }
  }

  /**
   * uncover "uncovers" a column c of the DLX matrix
   * all operations of cover are undone
   * so column c and rows i are visible again after execution of uncover
   *
   * @param c: header element of column that has to be uncovered
   */
  public static void uncover(DLXNode c) {//undo remove col c
    for (DLXNode i = c.U; i != c; i = i.U)      // for all rows with 1
      for (DLXNode j = i.L; i != j; j = j.L) {   // for all elem in row
        j.D.U = j;                       // un-remove row elem
        j.U.D = j;                       // .. to column list
      }
    c.R.L = c;                           // un-remove header
    c.L.R = c;                           // .. to row list
  }

  //-------------------- Printing Various Outputs -----------------------//

  private static void printTable(){
    int[] nValues = new int[]{0, 1, 2, 3, 4, 5, 6, 7, 8};
    int[] solutions = new int[]{1, 0, 33, 160, 7483, 99742, 2367340, 36233454, 739687549};

    System.out.println(" n Value | # Solutions");
    System.out.println("--------------------------");
    for (int i : nValues){
      System.out.println("   " + i + "     | " + solutions[i]);
    }
  }

  private static void printNodeSetup(){
    System.out.println();
    System.out.println("Node Setup Matrix:");
    System.out.println();

    for (DLXNode[] row : nodeSetup) {
      for (DLXNode node : row) {
        if (node != null){
          System.out.print(" +");
        } else {
          System.out.print("  ");
        }
      }
      System.out.println();
    }
  }

  private static void printMatrixHeaders(){
    //anchor
    System.out.println(" h");
    System.out.println(" |");
    System.out.println(" v");

    //headers
    DLXNode node = h.R;
    int index = 1;
    while(node != h){
      System.out.print(" " + index);
      index++;
      node = node.R;
    }
    System.out.println();
    for (int i = 0; i < n*7; i++){
      if (i < 10) {
        System.out.print("--");
      } else if (i < 100){
        System.out.print("---");
      } else {
        System.out.print("----");
      }
    }
    System.out.println();
  }

  //---------------------- Accepting Valid Input ------------------------//

  private static int readInt(String s) {
    while (run) {
      try {
        System.out.print(s);
        System.out.flush();
        return Integer.parseInt(br.readLine().trim());
      } catch (Exception e) {
        System.out.println("That was not a valid integer.");
      }
    }
    return 0;
  }

}
