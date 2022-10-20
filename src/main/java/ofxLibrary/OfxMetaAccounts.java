package ofxLibrary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
// import java.util.logging.Logger;
import java.util.Set;

public class OfxMetaAccounts {
  // private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();

  private Map<String, LinkedList<OfxTransaction>> m_OfxAcounts = new LinkedHashMap<String, LinkedList<OfxTransaction>>();
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

      // Fill MetaInfo
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

          if (l_ofxtrans.isSaving()) {
            if (l_meta.getPrefix().isBlank()) {
              if (!l_ofxtrans.getAccountto().isBlank()) {
                l_meta.setPrefix(l_ofxtrans.getAccountto());
              }
            }
          }
          l_meta.setSuffix(l_ofxtrans.getSource());
          m_metainfo.put(l_ofxtrans.getAccount(), l_meta);
        } catch (Exception e) {
        }
      } else {
        OfxMetaInfo l_meta = new OfxMetaInfo(l_ofxtrans.getBankCode());
        l_meta.setAccount(l_ofxtrans.getAccount());
        String sDtPosted = l_ofxtrans.getDtposted();
        l_meta.setBalanceAfterTransaction(l_ofxtrans.getSaldo_na_mutatie());
        l_meta.setMaxDate(sDtPosted);
        l_meta.setMinDate(sDtPosted);

        if (l_ofxtrans.isSaving()) {
          if (l_meta.getPrefix().isBlank()) {
            if (!l_ofxtrans.getAccountto().isBlank()) {
              l_meta.setPrefix(l_ofxtrans.getAccountto());
            }
          }
        }
        l_meta.setSuffix(l_ofxtrans.getSource());
        m_metainfo.put(l_ofxtrans.getAccount(), l_meta);
      }

      // Fill OfxAccounts
      LinkedList<OfxTransaction> l_OfxTransactions = m_OfxAcounts.get(l_ofxtrans.getAccount());
      if (l_OfxTransactions == null) {
        l_OfxTransactions = new LinkedList<OfxTransaction>();
      }
      l_OfxTransactions.add(l_ofxtrans);
      m_OfxAcounts.put(l_ofxtrans.getAccount(), l_OfxTransactions);
    });
  }

  public List<OfxTransaction> getTransactions() {
    return m_OfxTransactions;
  }

  public List<OfxTransaction> getTransactions(String a_Account) {
    return m_OfxAcounts.get(a_Account);
  }

  public Set<String> getAccounts() {
    return m_OfxAcounts.keySet();
  }

  public OfxMetaInfo getOfxMetaInfo(String a_Account) {
    return m_metainfo.get(a_Account);
  }

  public Map<String, OfxMetaInfo> getAccountsOfxMetaInfo() {
    return m_metainfo;
  }
}
