package ofxLibrary;

import java.util.LinkedList;
import java.util.List;
// import java.util.logging.Level;
// import java.util.logging.Logger;
// import java.util.logging.Level;

public class OfxFunctios {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  /**
   * Check if OfxTransaction already is NOT present in list of OfxTransactions.
   * 
   * @param a_OfxTransactions  List of OfxTransactions.
   * @param a_OfxTransactions2 List of OfxTransactions to add.
   * @return Filtered List of OfxTransactions.
   */
  static public List<OfxTransaction> uniqueOfxTransactions(List<OfxTransaction> a_OfxTransactions,
      List<OfxTransaction> a_OfxTransactions2) {
    List<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
    for (int i = 0; i < a_OfxTransactions2.size(); i++) {
      OfxTransaction l_OfxTransaction1 = a_OfxTransactions2.get(i);
      boolean bstat = false;
      int j = 0;
      while (!bstat && j < a_OfxTransactions.size()) {
        OfxTransaction l_OfxTransaction2 = a_OfxTransactions.get(j);
        bstat = l_OfxTransaction2.equals(l_OfxTransaction1);
        j++;
      }
      if (!bstat) {
        l_OfxTransactions.add(l_OfxTransaction1);
      }
    }
    return l_OfxTransactions;
  }

}
