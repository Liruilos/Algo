
/**
 * Class DLXNode
 *   represents a matrix element of the cover matrix with value 1
 *   links go to up down left right neigbors, and column header
 *   can also be used as colm header or root of column headers
 *   matrix is sparsely coded
 *   try to do all operations very efficiently
 *   see:
 *      http://en.wikipedia.org/wiki/Dancing_Links
 *      http://arxiv.org/abs/cs/0011047
 *
 * Credit: Alois Heinz
 */
class DLXNode {       // represents 1 element or header
  DLXNode C;           // reference to column-header
  DLXNode L, R, U, D;  // left, right, up, down references


  DLXNode() {
    C = L = R = U = D = this;
  } // supports circular lists

}