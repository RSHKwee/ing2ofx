package ofxLibrary;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
// import java.util.logging.Level;
// import java.util.logging.Logger;
// import java.util.logging.Level;
import java.util.Map;
import java.util.Set;

public class OfxFunctions {
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

  /**
   * Add MetaInfo to existing MetaInfo.
   * 
   * @param a_MetaInfo1 Base MetaInfo
   * @param a_MetaInfo2 MetaInfo to be added
   * @return Sum of MetaInfo's.
   */
  static public Map<String, OfxMetaInfo> addMetaInfo(Map<String, OfxMetaInfo> a_MetaInfo1,
      Map<String, OfxMetaInfo> a_MetaInfo2) {
    Map<String, OfxMetaInfo> l_metainfo = new HashMap<String, OfxMetaInfo>(a_MetaInfo1);
    Set<String> l_Keys = a_MetaInfo2.keySet();
    l_Keys.forEach(key -> {
      OfxMetaInfo ll_OfxMetaInfo = a_MetaInfo2.get(key);
      l_metainfo.put(key, ll_OfxMetaInfo);
    });
    return l_metainfo;
  }

}
