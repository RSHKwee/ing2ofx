package ofxLibrary;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class OfxFunctios {

  static public List<OfxTransaction> pairOFXTrans(List<OfxTransaction> a_OfxTransactions) {
    List<OfxTransaction> l_OfxTransactions = a_OfxTransactions;
    OfxPairTransaction l_filter = new OfxPairTransaction(l_OfxTransactions);
    l_OfxTransactions = l_filter.pair();
    return l_OfxTransactions;
  }

  /**
   * 
   * @param a_OfxTransactions
   * @return
   */
  static public Map<String, OfxMetaInfo> updateOfxMetaInfoMap(List<OfxTransaction> a_OfxTransactions) {
    Map<String, OfxMetaInfo> l_metaInfoMap = new HashMap<String, OfxMetaInfo>();
    a_OfxTransactions.forEach(l_ofxtrans -> {
      if (l_metaInfoMap.containsKey(l_ofxtrans.getAccount())) {
        OfxMetaInfo l_meta = l_metaInfoMap.get(l_ofxtrans.getAccount());
        try {
          String sDtPosted = l_ofxtrans.getDtposted();
          l_meta.setMaxDate(sDtPosted);
          if (l_meta.getMaxDate().equalsIgnoreCase(sDtPosted)) {
            if (l_meta.getBalanceAfterTransaction().isBlank()) {
              l_meta.setBalanceAfterTransaction(l_ofxtrans.getSaldo_na_mutatie());
            }
          }
          l_meta.setMaxDate(sDtPosted);
          l_meta.setMinDate(sDtPosted);

          if (l_meta.getPrefix().isBlank()) {
            if (!l_ofxtrans.getAccountto().isBlank()) {
              l_meta.setPrefix(l_ofxtrans.getAccountto());
            }
          }
          l_metaInfoMap.put(l_ofxtrans.getAccount(), l_meta);
        } catch (Exception e) {
        }
      } else {
        OfxMetaInfo l_meta = new OfxMetaInfo();
        l_meta.setAccount(l_ofxtrans.getAccount());
        String sDtPosted = l_ofxtrans.getDtposted();
        l_meta.setBalanceAfterTransaction(l_ofxtrans.getSaldo_na_mutatie());
        l_meta.setMaxDate(sDtPosted);
        l_meta.setMinDate(sDtPosted);

        if (l_meta.getPrefix().isBlank()) {
          if (!l_ofxtrans.getAccountto().isBlank()) {
            l_meta.setPrefix(l_ofxtrans.getAccountto());
          }
        }
        l_metaInfoMap.put(l_ofxtrans.getAccount(), l_meta);
      }
    });
    return l_metaInfoMap;
  }
}
