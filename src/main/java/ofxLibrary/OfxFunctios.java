package ofxLibrary;

import java.util.List;

public class OfxFunctios {

  static public List<OfxTransaction> pairOFXTrans(List<OfxTransaction> a_OfxTransactions) {
    List<OfxTransaction> l_OfxTransactions = a_OfxTransactions;
    OfxPairTransaction l_filter = new OfxPairTransaction(l_OfxTransactions);
    l_OfxTransactions = l_filter.pair();
    return l_OfxTransactions;
  }

}
