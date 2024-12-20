package kwee.ofxLibrary;

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

import kwee.ing2ofx.main.UserSetting;

public class OfxMetaInfo {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private String bankcode = "";
  private String account = "";
  private String prefix = "";
  private int minDate = 999999999;
  private int maxDate = -1;
  private double balanceAfterTransaction = 0;
  private boolean balanceAfterTransactionFilled = false;
  private String suffix = "";
  private Synonyms m_Synonyms;
  private UserSetting m_UserSetting = UserSetting.getInstance();

  /**
   * Default constructor
   */
  public OfxMetaInfo(String a_bankcode) {
    bankcode = a_bankcode;
    m_Synonyms = m_UserSetting.getSynonyms();
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
    // m_Synonyms = a_MetaInfo.getSynonyms();
  }

//  public Synonyms getSynonyms() {
//    return m_Synonyms;
//  }

//  public void setSynonymes(Synonyms a_Synonyms) {
//    this.m_Synonyms = a_Synonyms;
//  }

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
      String ll_prefix = m_Synonyms.getSynonym(prefix);
      if (!ll_prefix.isEmpty()) {
        l_prefix = ll_prefix + "_";
      }
    } else {
      LOGGER.log(Level.FINE, "Search synonym for account: " + account);
      String ll_prefix = m_Synonyms.getSynonym(account);
      if (!ll_prefix.isEmpty()) {
        l_prefix = ll_prefix;
        LOGGER.log(Level.FINE, "Found synonym for account: " + account + " found: " + ll_prefix);
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
    return Integer.toString(maxDate);
  }

  public double getBalanceAfterTransaction() {
    return balanceAfterTransaction;
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

  public void setBalanceAfterTransaction(double balanceAfterTransaction) {
    this.balanceAfterTransactionFilled = true;
    this.balanceAfterTransaction = balanceAfterTransaction;
  }

  public boolean getbalanceAfterTransactionFilled() {
    return this.balanceAfterTransactionFilled;
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
    l_beanStr = String.join(";", getAccount(), getPrefix(), getSuffix(), getMinDate(), getMaxDate(),
        Double.toString(this.getBalanceAfterTransaction()));
    return l_beanStr;
  }

  public boolean equals(OfxMetaInfo a_metaInfo) {
    boolean bstat = false;
    bstat = a_metaInfo.getAccount().equals(this.account);
    bstat = bstat && a_metaInfo.getPrefix().equals(this.getPrefix());
    bstat = bstat && a_metaInfo.getSuffix().equals(this.getSuffix());
    bstat = bstat && a_metaInfo.getMinDate().equals(this.getMinDate());
    bstat = bstat && a_metaInfo.getMaxDate().equals(this.getMaxDate());

    int cstat = Double.compare(a_metaInfo.getBalanceAfterTransaction(), this.getBalanceAfterTransaction());
    bstat = bstat && (cstat == 0);
    return bstat;
  }

}
