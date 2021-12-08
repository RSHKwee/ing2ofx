package ing2ofx.convertor;

import java.util.logging.Logger;

public class OfxMetaInfo {
  // private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private String account = "";
  private String prefix = "";
  private int minDate = 999999999;
  private int maxDate = -1;
  private String balanceAfterTransaction = "";

  public String getAccount() {
    return account;
  }

  public String getPrefix() {
    return prefix;
  }

  public String getMinDate() {
    return Integer.toString(minDate);
  }

  public String getMaxDate() {
    return Integer.toString(maxDate);
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
}
