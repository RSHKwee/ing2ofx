package ofxLibrary;

import java.math.BigDecimal;
import java.util.LinkedList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.swing.JLabel;
import javax.swing.JProgressBar;

public class OfxPairTransaction {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();

  private JProgressBar m_ProgressBar;
  private JLabel m_Progresslabel;
  private int m_Processed = 0;
  private int m_Number = -1;

  public OfxPairTransaction(List<OfxTransaction> a_OfxTransactions, JProgressBar a_ProgressBar,
      JLabel a_Progresslabel) {
    m_OfxTransactions = a_OfxTransactions;

    m_ProgressBar = a_ProgressBar;
    m_Progresslabel = a_Progresslabel;
  }

  /**
   * Check if transactions are "partners"
   * 
   * Account from is Account to, Transaction dates are the same, Sum of amounts is
   * zero.
   * 
   * @param a_tran1
   * @param a_tran2
   * @return
   */
  private boolean checkTrans(OfxTransaction a_tran1, OfxTransaction a_tran2) {
    boolean l_bstat = false;
    if ((-1 == a_tran2.getOfxTranPair()) && (-1 == a_tran1.getOfxTranPair())) {
      String l_account1 = a_tran1.getAccount().replaceAll("-", "");
      String l_account2 = a_tran2.getAccountto().replaceAll("-", "");

      l_bstat = (l_account1.equalsIgnoreCase(l_account2));
      if (l_bstat) {
        LOGGER.log(Level.FINEST, "Account.");
      }
      l_bstat = l_bstat && (a_tran1.getDtposted().equalsIgnoreCase(a_tran2.getDtposted()));
      try {
        BigDecimal bd1 = new BigDecimal(a_tran1.getTrnamt().replaceAll(",", "."));
        BigDecimal bd2 = new BigDecimal(a_tran2.getTrnamt().replaceAll(",", "."));
        BigDecimal bdDiff = bd1.add(bd2);
        l_bstat = l_bstat && (bdDiff.compareTo(BigDecimal.ZERO) == 0);
      } catch (Exception e) {
        System.out.println(e.getMessage());
      }
    }
    return l_bstat;
  }

  /**
   * Filter transactions and search for pairs.
   * 
   * @return List of OFX Transactions.
   */
  public List<OfxTransaction> pair() {
    m_Processed = -1;
    m_Number = m_OfxTransactions.size();
    m_ProgressBar.setMaximum(m_Number);
    m_Progresslabel.setVisible(true);
    m_ProgressBar.setVisible(true);

    verwerkProgress();

    for (int i = 0; i < m_OfxTransactions.size(); i++) {
      OfxTransaction l_OfxTransaction1 = m_OfxTransactions.get(i);
      boolean bstat = false;
      int j = i;
      while (!bstat && j < m_OfxTransactions.size()) {
        OfxTransaction l_OfxTransaction2 = m_OfxTransactions.get(j);
        bstat = checkTrans(l_OfxTransaction1, l_OfxTransaction2);
        if (bstat) {
          String fitid1 = l_OfxTransaction1.getFitid();
          String fitid2 = l_OfxTransaction2.getFitid();
          l_OfxTransaction2.setFitid(fitid1);

          l_OfxTransaction2.setOfxTranPair(i);
          m_OfxTransactions.set(j, l_OfxTransaction2);

          l_OfxTransaction1.setOfxTranPair(j);
          m_OfxTransactions.set(i, l_OfxTransaction1);
          LOGGER.log(Level.FINE, "Fit id adjusted: " + fitid2 + " -> " + fitid1);
        }
        j++;
      }
      verwerkProgress();
    }
    m_Progresslabel.setVisible(false);
    m_ProgressBar.setVisible(false);

    return m_OfxTransactions;
  }

  /**
   * Display progress processed files.
   */
  private void verwerkProgress() {
    m_Processed++;
    try {
      m_ProgressBar.setValue(m_Processed);
      Double v_prog = ((double) m_Processed / (double) m_Number) * 100;
      Integer v_iprog = v_prog.intValue();
      m_Progresslabel.setText(v_iprog.toString() + "% (" + m_Processed + " of " + m_Number + " transactions)");
    } catch (Exception e) {
      // Do nothing
    }
  }

}
