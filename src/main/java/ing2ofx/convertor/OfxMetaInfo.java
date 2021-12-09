package ing2ofx.convertor;

import java.util.logging.Level;
import java.util.logging.Logger;

public class OfxMetaInfo {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private String account = "";
  private String prefix = "";
  private int minDate = 999999999;
  private int maxDate = -1;
  private String balanceAfterTransaction = "";

  public OfxMetaInfo() {
  }

  public OfxMetaInfo(OfxMetaInfo a_MetaInfo) {
    account = a_MetaInfo.getAccount();
    prefix = a_MetaInfo.getPrefix();
    minDate = a_MetaInfo.getIntMinDate();
    maxDate = a_MetaInfo.getIntMaxDate();
    balanceAfterTransaction = a_MetaInfo.getBalanceAfterTransaction();
  }

  public String getAccount() {
    return account;
  }

  public String getPrefix() {
    return prefix;
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

  public String getBalanceAfterTransaction() {
    return balanceAfterTransaction;
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

  public void setMaxDate(String maxDate) {
    int i_maxDate = -1;
    try {
      i_maxDate = Integer.parseInt(maxDate);
    } catch (Exception e) {
    }
    if (i_maxDate > this.maxDate) {
      this.maxDate = i_maxDate;
    }
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
