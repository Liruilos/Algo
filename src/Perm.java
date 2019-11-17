import java.io.BufferedReader;
import java.io.InputStreamReader;

class Perm extends Thread{

  private static boolean run = true;
  private static BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
  private static int n;
  private static int permCounter = 0;
  private static int permMethodCalls = 0;

  private static int[] a;        // a Arbeitsarray
  private int max;        // maximaler Index
  private boolean mayread = false; // Kontrolle
  Perm (int n) {
    a = new int[n];

    // Indices: 0 .. n-1 // Maximaler Index
    max = n-1;

    // a fuellen
    for (int i=0; i<=max;) a[i]=i++;

    // run-Methode beginnt zu laufen
    this.start ();

  }

  public static void main(String[] args) {
    n = readInt("Please enter the value of n: ");

    Perm perm = new Perm(n);

    while (a != null){

      permCounter++;


      //when the printing is commented out an extra permutation is counted
/*      for (int i : a){
        System.out.print(i + " ");
      }

      System.out.println();*/



      perm.getNext();

    }

    permCounter = permCounter - 1;

    System.out.println("For n = " + n + " unique numbers in a sequence there are " + permCounter + " Permutations.");
    System.out.println("Perm got called " + permMethodCalls + " times.");

    //account for the extra perm that gets added

    float average = (float) permMethodCalls / (float) permCounter;

    System.out.println("The average method calls per permutation: " + average);

  }

  public void run (){// die Arbeits-Methode
    perm (0);
    a = null;
    put ();
  } // end run

  private void perm (int i){ // permutiere ab Index i
    permMethodCalls++;
    if (i >= max) put ();  // eine Permutation fertig
    else {
      for (int j=i; j <= max; j++){ // jedes nach Vorne
        swap (i,j);
        perm (i+1);
      }
      int h = a[i];
      System.arraycopy (a,i+1, a, i,max-i); // shift links
      a[max] = h;
    }
  } // end perm

  private void swap (int i, int j){ // tausche a[i] <-> a[j]
    if (i != j) {
      int h = a[i];
      a[i] = a[j];
      a[j] = h;
    }
  } // end swap

  synchronized int[] getNext (){ // liefert naechste Permutation
    mayread = false;                // a darf geaendert werden
    notify ();                      // wecke anderen Thread
    try{
      while (!mayread) wait ();  // non busy waiting
    } catch (InterruptedException e){}
    return a;                  // liefere Permutationsarray
  } // end getNext

  private synchronized void put (){
    mayread = true;
    notify ();
    try{ if (a!=null)
// uebergebe array
// a wird gelesen
// wecke anderen Thread
      while (mayread) wait ();    // non busy waiting
    } catch (InterruptedException e){}
  } // end put

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