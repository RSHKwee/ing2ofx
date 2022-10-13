package ofxLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Logger;

public class OfxMetaAccounts {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();

  private Map<String, ArrayList<String>> m_OfxAcounts = new LinkedHashMap<String, ArrayList<String>>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  public OfxMetaAccounts(List<OfxTransaction> a_OfxTransactions) {
    m_OfxTransactions = a_OfxTransactions;
    updateOfxMetaInfoMap();
  }

  /**
   * 
   * @param a_OfxTransactions
   * @return
   */
  private void updateOfxMetaInfoMap() {
    m_OfxTransactions.forEach(l_ofxtrans -> {
      if (m_metainfo.containsKey(l_ofxtrans.getAccount())) {
        OfxMetaInfo l_meta = m_metainfo.get(l_ofxtrans.getAccount());
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
          m_metainfo.put(l_ofxtrans.getAccount(), l_meta);
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
        m_metainfo.put(l_ofxtrans.getAccount(), l_meta);
      }
    });
  }

  public List<OfxTransaction> getTransactions() {
    return null;
  }

}
