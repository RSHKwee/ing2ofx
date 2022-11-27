package ofxLibrary;

import java.io.File;
/**
 * Meta information storage for an account and its transactions.
 * <br>
 * Per account the following information is stored:
 * <li>Account name.
 * <li>File prefix for OFX transactions.
 * <li>Period start- and end date.
 * <li>Period end date balance.
 * <br>
 * 
 * @author rshkw
 */
import java.util.logging.Level;
import java.util.logging.Logger;

public class OfxMetaInfo {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private String bankcode = "";
  private String account = "";
  private String prefix = "";
  private int minDate = 999999999;
  private int maxDate = -1;
  private String balanceAfterTransaction = "";
  private String suffix = "";
  private Synonymes m_Synonymes = new Synonymes();

  /**
   * Default constructor
   */
  // public OfxMetaInfo(String a_bankcode) {
  // bankcode = a_bankcode;
  // }

  /**
   * Default constructor
   */
  public OfxMetaInfo(String a_bankcode, File a_SynonymesFile) {
    bankcode = a_bankcode;
    m_Synonymes = new Synonymes(a_SynonymesFile);
    LOGGER.log(Level.INFO, "Read Synonymes File" + a_SynonymesFile);
  }

  /**
   * Copy constructor
   * 
   * @param a_MetaInfo OFX Meta information
   */
  public OfxMetaInfo(OfxMetaInfo a_MetaInfo) {
    bankcode = a_MetaInfo.getBankcode();
    account = a_MetaInfo.getAccount();
    prefix = a_MetaInfo.getPrefix();
    minDate = a_MetaInfo.getIntMinDate();
    maxDate = a_MetaInfo.getIntMaxDate();
    balanceAfterTransaction = a_MetaInfo.getBalanceAfterTransaction();
    m_Synonymes = a_MetaInfo.getSynonymes();
  }

  public Synonymes getSynonymes() {
    return m_Synonymes;
  }

  public void setSynonymes(Synonymes a_Synonymes) {
    this.m_Synonymes = a_Synonymes;
  }

  public String getBankcode() {
    return bankcode;
  }

  /**
   * Get account
   * 
   * @return account
   */
  public String getAccount() {
    return account;
  }

  public String getPrefix() {
    String l_prefix = "";
    if (!prefix.isEmpty()) {
      String ll_prefix = m_Synonymes.GetSynonyme(prefix);
      if (!ll_prefix.isEmpty()) {
        l_prefix = ll_prefix + "_";
      }
    } else {
      String ll_prefix = m_Synonymes.GetSynonyme(account);
      if (!ll_prefix.isEmpty()) {
        l_prefix = ll_prefix;
      }
    }
    return l_prefix + prefix;
  }

  public String getMinDate() {
    return Integer.toString(minDate);
  }

  public int getIntMinDate() {
    return minDate;
  }

  public String getMaxDate() {
    return Integer.toString(maxDate);
  }

  public int getIntMaxDate() {
    return maxDate;
  }

  public String getBalanceDate() {
    if (balanceAfterTransaction.isBlank()) {
      return "190001010001"; // 1-1-1900 00:01
    } else {
      return Integer.toString(maxDate);
    }
  }

  public String getBalanceAfterTransaction() {
    if (balanceAfterTransaction.isBlank()) {
      return "0";
    } else {
      return balanceAfterTransaction;
    }
  }

  public String getSuffix() {
    return suffix;
  }

  public void setSuffix(String suffix) {
    this.suffix = suffix;
  }

  public void setBankcode(String bankcode) {
    this.bankcode = bankcode;
  }

  public void setAccount(String account) {
    this.account = account;
  }

  public void setPrefix(String prefix) {
    this.prefix = prefix;
  }

  public void setMinDate(String minDate) {
    int i_minDate = 999999999;
    try {
      i_minDate = Integer.parseInt(minDate);
    } catch (Exception e) {
    }
    if (i_minDate < this.minDate) {
      this.minDate = i_minDate;
    }
  }

  public boolean setMaxDate(String maxDate) {
    boolean bstat = false;
    int i_maxDate = -1;
    try {
      i_maxDate = Integer.parseInt(maxDate);
    } catch (Exception e) {
    }
    if (i_maxDate > this.maxDate) {
      this.maxDate = i_maxDate;
      bstat = true;
    }
    return bstat;
  }

  public void setBalanceAfterTransaction(String balanceAfterTransaction) {
    this.balanceAfterTransaction = balanceAfterTransaction;
  }

  public void printLog() {
    LOGGER.log(Level.INFO, "");
    LOGGER.info("Account           : " + getAccount());
    if (!getPrefix().isEmpty()) {
      LOGGER.info("File prefix       : " + getPrefix());
    }
    LOGGER.info("Period : " + getMinDate() + " til " + getMaxDate());
    LOGGER.info("Balance on end period: €" + getBalanceAfterTransaction());
  }

  @Override
  public String toString() {
    String l_beanStr;
    l_beanStr = String.join(";", getAccount(), getPrefix(), getMinDate(), getMaxDate(), getBalanceAfterTransaction());
    return l_beanStr;
  }
}
