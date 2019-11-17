import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.Stack;

/**
 *
 * Credit: Alois Heinz for the Algorithm X methods
 */
public class DLXPentominoDLUX {

  //Input variables
  private static boolean run = true;
  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  private static int n;

  //Matrix variables
  private static DLXNode h = new DLXNode();
  private static int[][] matrix;
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
    //matrices both get an extra row for column headers
    matrix = new int[estTotalPositions][7*(n+1)];
    nodeSetup = new DLXNode[estTotalPositions][7*(n+1)];

    System.out.println("Building the int Matrix...");
    buildHeaders(h);
    calcPossibilities();

    printMatrixHeaders();
    printMatrix();

    System.out.println("Building the Dancing Links based on Matrix...");
    buildRows();
    printNodeSetup();

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
      matrix[0][i] = 1;

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

  private static void calcPossibilities(){

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
          createMatrixEntry(new int[]{rowStarter + i + 1, rowStarter + i + 7, rowStarter + i + 8,
              rowStarter + i + 9, rowStarter + i + 15});
        }
      }

      //pentomino C Tall
      if (row < n - 2) {
        for (int i = 0; i < 6; i++) {
          //C shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1,
              rowStarter + 7 + i,
              rowStarter + 14 + i, rowStarter + 15 + i});
          //reverse C shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1,
              rowStarter + 8 + i,
              rowStarter + 14 + i, rowStarter + 15 + i});
        }
      }

      //pentomino C Wide
      if (row < n - 1) {
        for (int i = 0; i < 5; i++) {
          //U shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 2,
              rowStarter + 7 + i, rowStarter + 8 + i, rowStarter + 9 + i});
          //upside down U shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1, rowStarter + i +2,
              rowStarter + 7 + i, rowStarter + 9 + i});
        }
      }

      //pentomino L Tall
      if (row < n - 3) {
        for (int i = 0; i < 6; i++) {
          //L shape
          createMatrixEntry(new int[]{rowStarter + i,
              rowStarter + i + 7,
              rowStarter + i + 14,
              rowStarter + i + 21, rowStarter + i + 22});

          //backwards L shape
          createMatrixEntry(new int[]{rowStarter + i + 1,
              rowStarter + i + 8,
              rowStarter + i + 15,
              rowStarter + i + 21, rowStarter + i + 22});

          //upsidedown L shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1,
              rowStarter + i + 7,
              rowStarter + i + 14,
              rowStarter + i + 21});

          //upsidedown and backwards L shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1,
              rowStarter + i + 8,
              rowStarter + i + 15,
              rowStarter + i + 22});
        }

      }

      //pentomino L Wide
      if (row < n - 1) {
        for (int i = 0; i < 4; i++) {
          // |___ shape
          createMatrixEntry(new int[]{rowStarter + i,
              rowStarter + i + 7, rowStarter + i + 8, rowStarter + i + 9, rowStarter + i + 10});

          // ___| shape
          createMatrixEntry(new int[]{rowStarter + i + 3,
              rowStarter + i + 7, rowStarter + i + 8, rowStarter + i + 9, rowStarter + i + 10});

          // |--- shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1, rowStarter + i + 2, rowStarter + i + 3,
              rowStarter + i + 7});

          // ---| shape
          createMatrixEntry(new int[]{rowStarter + i, rowStarter + i + 1, rowStarter + i + 2, rowStarter + i + 3,
              rowStarter + i + 10});
        }

      }
    }
  }

  private static void createMatrixEntry(int[] nodePositions){
    for (int i : nodePositions){
      matrix[matrixRowCounter][i] = 1;
    }
    matrixRowCounter++;
  }

  private static void buildRows(){

    //Alt rewrite:
    //create all relevant nodes and stick them in nodeSetupMatrix (unbound)
    //single loop per row to link horizontally not including headers
    //single loop per column starting with headers to link all nodes vertically & set column headers

    for (int row=1; row < matrix.length; row++)
    {
      for (int col=0; col < matrix[row].length; col++)
      {
        if (matrix[row][col] == 1) {
          DLXNode newNode = new DLXNode();
          nodeSetup[row][col] = newNode;
        }
      }
    }

    //link horizontally, every entry finds the next node right to connect to, headers already done with anchor
    for (int row=1; row < nodeSetup.length; row++)
    {
      for (int col=0; col < nodeSetup[row].length; col++)
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
    for (int row=0; row < nodeSetup.length; row++)
    {
      for (int col=0; col < nodeSetup[row].length; col++)
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

  private static DLXNode createNewNode(int row, int col){
    DLXNode newNode = new DLXNode();
    nodeSetup[row][col] = newNode;

    //link with neighbours, finding next node checks the int matrix, so row must be reduced by one
    int rightColIndex = getNextRightNode(row, col);

    //if there is another node in this row make links moving right
    if (rightColIndex != col){
/*      if (nodeSetup[row][leftColIndex] != null){
        newNode.L = nodeSetup[row][leftColIndex];
        nodeSetup[row][leftColIndex].R = newNode;
      } else {
        DLXNode leftNode = createNewNode(row, leftColIndex);
        //newNode.L = leftNode;
        //leftNode.R = newNode;
      }*/
      if (nodeSetup[row][rightColIndex] != null){
        newNode.R = nodeSetup[row][rightColIndex];
        nodeSetup[row][rightColIndex].L = newNode;
      }else {
         DLXNode rightNode = createNewNode(row, rightColIndex);
         newNode.R = rightNode;
         rightNode.L = newNode;
      }
    }
    //there will always be at least one other node in the column due to the header, so no need to check
    int nextUpIndex = getNextUpNode(row, col);
    if (nodeSetup[nextUpIndex][col] != null){
      newNode.U = nodeSetup[nextUpIndex][col];
      nodeSetup[nextUpIndex][col].D = newNode;
    }else {
      DLXNode upNode = createNewNode(nextUpIndex, col);
      //newNode.U = upNode;
      //upNode.D = newNode;
    }

    int nextDownIndex = getNextDownNode(row, col);
    if (nodeSetup[nextDownIndex][col] != null){
      newNode.D = nodeSetup[nextDownIndex][col];
      nodeSetup[nextDownIndex][col].U = newNode;
    }else {
      DLXNode downNode = createNewNode(nextDownIndex, col);
      //newNode.D = downNode;
      //downNode.U = newNode;
    }

    return newNode;
  }

/*  private static int getNextLeftNode(int row, int col){
    for (int c = wrapLeft(col); c != col; c = wrapLeft(c)){
      if (matrix[row][c] == 1){
        return c;
      }
    }
    return col;
  }*/

  private static int getNextRightNode(int row, int col){
    int c = wrapRight(col);
    while (c != col){
      if (matrix[row][c] == 1){
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
      if (matrix[r][col] == 1){
        return r;
      } else {
        r = wrapUp(r);
      }
    }
    return row;
  }

  private static int getNextDownNode(int row, int col){
    for (int r = wrapDown(row); r != row; r = wrapDown(r)){
      if (matrix[r][col] == 1){
        return r;
      }
    }
    return row;
  }

  private static int wrapLeft(int i){
    return i == 0 ? matrix[0].length - 1 : i - 1;
  }

  private static int wrapRight(int i){
    return i == matrix[0].length - 1 ? 0 : i + 1;
  }

  //wrap up from 0 should be 6
  private static int wrapUp(int i){
    return i == 0 ? matrix.length - 1 : i - 1;
  }

  private static int wrapDown(int i){
    return i == matrix.length - 1 ? 0 : i + 1;
  }

  //--------------------------- Algorithm X -----------------------------//

  /**
   * search tries to find and count all complete coverings of the DLX matrix.
   * Is a recursive, depth-first, backtracking algorithm that finds
   * all solutions to the exact cover problem encoded in the DLX matrix.
   * each time all columns are covered, static long cnt is increased
   *
   * @param k: number of level
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
    for (DLXNode i = c.U; i != c; i = i.U)      // forall rows with 1
      for (DLXNode j = i.L; i != j; j = j.L) {   // forall elem in row
        j.D.U = j;                       // un-remove row elem
        j.U.D = j;                       // .. to column list
      }
    c.R.L = c;                           // un-remove header
    c.L.R = c;                           // .. to row list
  }

  //-------------------- Printing Various Outputs -----------------------//

  private static void printTable(){
    //TODO
  }

  private static void printMatrix(){
    for (int[] row : matrix) {
      for (int value : row) {
        if (value == 1){
          System.out.print(" +");
        } else {
          System.out.print("  ");
        }
      }
      System.out.println();
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
