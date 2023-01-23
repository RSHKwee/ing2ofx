package ofxLibrary;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.Set;

public class OfxMetaAccounts {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();

  private Map<String, LinkedList<OfxTransaction>> m_OfxAcounts = new LinkedHashMap<String, LinkedList<OfxTransaction>>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  public OfxMetaAccounts(List<OfxTransaction> a_OfxTransactions, Map<String, OfxMetaInfo> a_metainfo) {
    m_OfxTransactions = a_OfxTransactions;
    m_metainfo = a_metainfo;
    updateOfxMetaInfoMap();
  }

  /**
   * 
   * @param a_OfxTransactions
   * @return
   */
  private void updateOfxMetaInfoMap() {
    try {
      m_OfxTransactions.forEach(l_ofxtrans -> {

        try {
          // Fill OfxAccounts
          if (l_ofxtrans.getAccount() == null) {
            LOGGER.log(Level.INFO, "Acount is null");
          } else {
            LOGGER.log(Level.FINE, "Get transactions for Account : " + l_ofxtrans.getAccount());
          }
          if (m_OfxAcounts != null) {
            if (!m_OfxAcounts.isEmpty()) {
              try {
                if (m_OfxAcounts.get(l_ofxtrans.getAccount()) != null) {

                  if (!m_OfxAcounts.get(l_ofxtrans.getAccount()).isEmpty()) {
                    try {
                      LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>(
                          m_OfxAcounts.get(l_ofxtrans.getAccount()));
                      l_OfxTransactions.add(l_ofxtrans);
                      m_OfxAcounts.put(l_ofxtrans.getAccount(), l_OfxTransactions);
                    } catch (Exception e) {
                      LOGGER.log(Level.INFO, e.getMessage());
                    }
                  } else {
                    try {
                      LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
                      l_OfxTransactions.add(l_ofxtrans);
                      m_OfxAcounts.put(l_ofxtrans.getAccount(), l_OfxTransactions);
                    } catch (Exception e) {
                      LOGGER.log(Level.INFO, e.getMessage());
                    }
                  }
                } else {
                  try {
                    LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
                    l_OfxTransactions.add(l_ofxtrans);
                    m_OfxAcounts.put(l_ofxtrans.getAccount(), l_OfxTransactions);
                  } catch (Exception e) {
                    LOGGER.log(Level.INFO, e.getMessage());
                  }
                }
              } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage());
              }
            } else {
              try {
                LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
                l_OfxTransactions.add(l_ofxtrans);
                m_OfxAcounts.put(l_ofxtrans.getAccount(), l_OfxTransactions);
              } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage());
              }
            }
          } else {
            try {
              m_OfxAcounts = new LinkedHashMap<String, LinkedList<OfxTransaction>>();
              LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
              l_OfxTransactions.add(l_ofxtrans);
              m_OfxAcounts.put(l_ofxtrans.getAccount(), l_OfxTransactions);
            } catch (Exception e) {
              LOGGER.log(Level.INFO, e.getMessage());
            }
          }
          LOGGER.log(Level.FINE, " ");
        } catch (Exception e) {
          LOGGER.log(Level.INFO, e.getMessage());
        }
      });
      LOGGER.log(Level.FINE, " ");
    } catch (Exception e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
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
