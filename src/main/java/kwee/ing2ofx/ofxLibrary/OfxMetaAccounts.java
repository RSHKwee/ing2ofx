package kwee.ing2ofx.ofxLibrary;

import java.util.ArrayList;
/**
 * Create a map of OFX-Transactions per account and Account Metainfo per account.
 * 
 * 
 * 
 * @author René
 *
 */
import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

import kwee.logger.MyLogger;

import java.util.Set;

public class OfxMetaAccounts {
  private static final Logger LOGGER = MyLogger.getLogger();

  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, LinkedList<OfxTransaction>> m_OfxAcountsTransactions = new LinkedHashMap<String, LinkedList<OfxTransaction>>();
  private Map<String, OfxMetaInfo> m_metaAccountsInfo = new HashMap<String, OfxMetaInfo>();
  private Map<String, ArrayList<String>> m_PrefixAccounts = new LinkedHashMap<String, ArrayList<String>>();

  public OfxMetaAccounts(List<OfxTransaction> a_OfxTransactions, Map<String, OfxMetaInfo> a_metaAccountsInfo) {
    m_OfxTransactions = a_OfxTransactions;
    m_metaAccountsInfo = a_metaAccountsInfo;
    updateOfxMetaInfoMap();
    updatePrefixAccounts();
  }

  /**
   * Get all transactions.
   */
  public List<OfxTransaction> getTransactions() {
    return m_OfxTransactions;
  }

  /**
   * Get list of transactions for given Account.
   * 
   * @param a_Account Given account.
   * @return List of transactions.
   */
  public List<OfxTransaction> getTransactions(String a_Account) {
    return m_OfxAcountsTransactions.get(a_Account);
  }

  /**
   * Get Accounts
   * 
   * @return List of Accounts
   */
  public Set<String> getAccounts() {
    return m_OfxAcountsTransactions.keySet();
  }

  public OfxMetaInfo getOfxMetaInfo(String a_Account) {
    return m_metaAccountsInfo.get(a_Account);
  }

  public Map<String, OfxMetaInfo> getAccountsOfxMetaInfo() {
    return m_metaAccountsInfo;
  }

  public ArrayList<String> getAccountPerPrefix(String a_Prefix) {
    return m_PrefixAccounts.get(a_Prefix);
  }

  public Set<String> getPrefixs() {
    return m_PrefixAccounts.keySet();
  }

  // Private functions
  /**
   * Create / Update Transactions per account. Fill data structure m_OfxAcountsTransactions. Key is Account, value is
   * list of Transactions.
   */
  private void updateOfxMetaInfoMap() {
    try {
      m_OfxTransactions.forEach(l_ofxtrans -> {
        try {
          // Fill OfxAccountsTransactions
          if (l_ofxtrans.getAccount() == null) {
            LOGGER.log(Level.INFO, "Acount is null");
          } else {
            LOGGER.log(Level.FINE, "Get transactions for Account : " + l_ofxtrans.getAccount());
          }
          if (m_OfxAcountsTransactions != null) {
            if (!m_OfxAcountsTransactions.isEmpty()) {
              try {
                if (m_OfxAcountsTransactions.get(l_ofxtrans.getAccount()) != null) {

                  if (!m_OfxAcountsTransactions.get(l_ofxtrans.getAccount()).isEmpty()) {
                    try {
                      LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>(
                          m_OfxAcountsTransactions.get(l_ofxtrans.getAccount()));
                      l_OfxTransactions.add(l_ofxtrans);
                      m_OfxAcountsTransactions.put(l_ofxtrans.getAccount(), l_OfxTransactions);
                    } catch (Exception e) {
                      LOGGER.log(Level.INFO, e.getMessage());
                    }
                  } else {
                    try {
                      LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
                      l_OfxTransactions.add(l_ofxtrans);
                      m_OfxAcountsTransactions.put(l_ofxtrans.getAccount(), l_OfxTransactions);
                    } catch (Exception e) {
                      LOGGER.log(Level.INFO, e.getMessage());
                    }
                  }
                } else {
                  try {
                    LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
                    l_OfxTransactions.add(l_ofxtrans);
                    m_OfxAcountsTransactions.put(l_ofxtrans.getAccount(), l_OfxTransactions);
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
                m_OfxAcountsTransactions.put(l_ofxtrans.getAccount(), l_OfxTransactions);
              } catch (Exception e) {
                LOGGER.log(Level.INFO, e.getMessage());
              }
            }
          } else {
            try {
              m_OfxAcountsTransactions = new LinkedHashMap<String, LinkedList<OfxTransaction>>();
              LinkedList<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
              l_OfxTransactions.add(l_ofxtrans);
              m_OfxAcountsTransactions.put(l_ofxtrans.getAccount(), l_OfxTransactions);
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

  /**
   * Create / Update Prefix Accounts structure.
   */
  private void updatePrefixAccounts() {
    Set<String> keys = m_metaAccountsInfo.keySet();
    keys.forEach(key -> {
      OfxMetaInfo l_metainf = m_metaAccountsInfo.get(key);
      String l_account = l_metainf.getAccount();
      String l_prefix = l_metainf.getPrefix();
      if (l_prefix.isBlank()) {
        l_prefix = l_account;
      }
      // ArrayList<String> aap = m_PrefixAccounts.get(l_prefix);
      if (m_PrefixAccounts.get(l_prefix) == null) {
        ArrayList<String> l_prefixAccounts = new ArrayList<String>();
        l_prefixAccounts.add(l_account);
        m_PrefixAccounts.put(l_prefix, l_prefixAccounts);
      } else {
        ArrayList<String> l_prefixAccounts = new ArrayList<String>(m_PrefixAccounts.get(l_prefix));
        l_prefixAccounts.add(l_account);
        m_PrefixAccounts.put(l_prefix, l_prefixAccounts);
      }
    });
  }
}
