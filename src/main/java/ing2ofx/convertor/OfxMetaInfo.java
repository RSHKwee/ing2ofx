package ing2ofx.convertor;

import java.util.logging.Logger;

public class OfxMetaInfo {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private String account = "";
  private String prefix = "";
  private int minDate = 9999999;
  private int maxDate = -1;
  private String balanceAfterTransaction = "";

  public String getAccount() {
    return account;
  }

  public String getPrefix() {
    return prefix;
  }

  public int getMinDate() {
    return minDate;
  }

  public int getMaxDate() {
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

  public void setMinDate(int minDate) {
    if (minDate < this.minDate) {
      this.minDate = minDate;
    }
  }

  public void setMaxDate(int maxDate) {
    if (maxDate > this.maxDate) {
      this.maxDate = maxDate;
    }
  }

  public void setBalanceAfterTransaction(String balanceAfterTransaction) {
    this.balanceAfterTransaction = balanceAfterTransaction;
  }
}
