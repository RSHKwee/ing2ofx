package kwee.convertor.sns.convertor;

/**
 * Convert SNS transactions to OFX transactions.
 * 
 * @author René
 *
 */
import java.io.File;
import java.io.FileInputStream;
import java.math.BigDecimal;
// import java.math.BigDecimal;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Collectors;

import kwee.camt053parser.Camt053Parser;
import kwee.ing2ofx.generated.camt053.AccountStatement2;
import kwee.ing2ofx.generated.camt053.CashBalance3;
import kwee.ing2ofx.generated.camt053.CreditDebitCode;
import kwee.ing2ofx.generated.camt053.Document;
import kwee.ing2ofx.generated.camt053.EntryDetails1;
import kwee.ing2ofx.generated.camt053.ReportEntry2;

import kwee.library.DateToNumeric;
import kwee.logger.MyLogger;
import kwee.convertor.sns.snsLibrary.SnsTransaction;
import kwee.ofxLibrary.OfxFunctions;
import kwee.ofxLibrary.OfxMetaInfo;
import kwee.ofxLibrary.OfxTransaction;

public class SnsTransactions {
  private static final Logger LOGGER = MyLogger.getLogger();

  private String m_bankcode = "SNSBNL2A";
  private Camt053Parser m_reader;
  private String m_File;
  private Set<String> m_UniqueIds = new LinkedHashSet<>();
  private String m_FileName = "";

  private List<SnsTransaction> m_Transactions;
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  /**
   * Constructor.
   * 
   * @param a_file XML File with SNS transactions
   */
  public SnsTransactions(File a_file) {
    m_File = a_file.getAbsolutePath();
    m_FileName = kwee.library.FileUtils.getFileNameWithoutExtension(a_file);
  }

  /**
   * Determine type of SNS transactions (saving or normal). <br>
   * Process the transactions and convert them to OFX transactions.
   */
  public void load() {
    Level l_Level = Level.FINEST;
    try {
      m_reader = new Camt053Parser();
      FileInputStream fileInputStream = new FileInputStream(new File(m_File));

      Document camt053Document = m_reader.parse(fileInputStream);
      m_bankcode = camt053Document.getBkToCstmrStmt().getStmt().getFirst().getAcct().getSvcr().getFinInstnId().getBIC();

      // Get all statements (usually one per bank statement)
      List<AccountStatement2> accountStatement2List = camt053Document.getBkToCstmrStmt().getStmt();
      for (AccountStatement2 accountStatement2 : accountStatement2List) {
        String l_IBANNr = accountStatement2.getAcct().getId().getIBAN();
        List<CashBalance3> l_balances = accountStatement2.getBal();
        l_balances.forEach(ll_balance -> {
          BigDecimal l_balValue = ll_balance.getAmt().getValue();
          double ld_balValue = l_balValue.doubleValue();
          Date l_balDate = ll_balance.getDt().getDt().toGregorianCalendar().getTime();
          String ls_balDate = DateToNumeric.dateToNumeric(l_balDate);
          String ls_baltype = ll_balance.getTp().getCdOrPrtry().getCd().toString();
          if (ls_baltype.toUpperCase().contains("CLBD")) {
            LOGGER.log(l_Level, "Balancetype: " + ls_baltype);
            OfxMetaInfo l_meta = m_metainfo.get(l_IBANNr);
            if (null == l_meta) {
              l_meta = new OfxMetaInfo(m_bankcode);
            }

            l_meta.setAccount(l_IBANNr);
            l_meta.setMinDate(ls_balDate);
            if (l_meta.setMaxDate(ls_balDate)) {
              if (CreditDebitCode.DBIT == ll_balance.getCdtDbtInd()) {
                l_meta.setBalanceAfterTransaction(-1.0 * ld_balValue);
              } else {
                l_meta.setBalanceAfterTransaction(ld_balValue);
              }
            }
            l_meta.setSuffix(m_FileName);
            m_metainfo.put(l_IBANNr, l_meta);
          }
        });

        for (ReportEntry2 reportEntry2 : accountStatement2.getNtry()) {
          OfxTransaction l_ofxtrans = new OfxTransaction(m_bankcode);
          l_ofxtrans.setAccount(l_IBANNr);
          if (CreditDebitCode.DBIT == reportEntry2.getCdtDbtInd()) {
            l_ofxtrans.setTrntype("CREDIT");
          }
          if (CreditDebitCode.CRDT == reportEntry2.getCdtDbtInd()) {
            l_ofxtrans.setTrntype("DEBIT");
          }

          Date l_tranDate = reportEntry2.getBookgDt().getDt().toGregorianCalendar().getTime();
          String ls_tranDate = DateToNumeric.dateToNumeric(l_tranDate);

          l_ofxtrans.setDtposted(l_tranDate);

          LOGGER.log(l_Level, "Credit or debit: " + reportEntry2.getCdtDbtInd());
          LOGGER.log(l_Level, "Booking date: " + reportEntry2.getBookgDt().getDt().toGregorianCalendar().getTime()
              + " (" + ls_tranDate + ")");

          List<EntryDetails1> entryDetails1List = reportEntry2.getNtryDtls();

          // Get payment details of the entry
          for (EntryDetails1 entryDetails1 : entryDetails1List) {
            // This is NOT a batch, but individual payments
            try {
              if (entryDetails1.getBtch() == null) {

                if (CreditDebitCode.DBIT == reportEntry2.getCdtDbtInd()) {
                  // Outgoing (debit) payments, show recipient (creditors) information, money was
                  // transferred from the bank (debtor) to a client (creditor)
                  // Outgoing (debit) payments, show recipient (creditors) information, money was
                  // transferred from the bank (debtor) to a client (creditor)
                  if (entryDetails1.getTxDtls().get(0).getRltdPties() != null) {
                    LOGGER.log(l_Level,
                        "Creditor name: " + entryDetails1.getTxDtls().get(0).getRltdPties().getCdtr().getNm());
                    LOGGER.log(l_Level, "Creditor IBAN: "
                        + entryDetails1.getTxDtls().get(0).getRltdPties().getCdtrAcct().getId().getIBAN());

                    l_ofxtrans.setName(entryDetails1.getTxDtls().get(0).getRltdPties().getCdtr().getNm());
                    l_ofxtrans
                        .setAccountto(entryDetails1.getTxDtls().get(0).getRltdPties().getCdtrAcct().getId().getIBAN());
                  } else {
                    l_ofxtrans.setName("");
                    l_ofxtrans.setAccountto("");
                  }

                  BigDecimal lamnt = new BigDecimal(-1.0);
                  try {
                    lamnt = lamnt.multiply(reportEntry2.getAmt().getValue());
                    LOGGER.log(l_Level,
                        "Report amount: -" + reportEntry2.getAmt().getValue() + " " + reportEntry2.getAmt().getCcy());
                  } catch (Exception e) {
                    // TODO
                    LOGGER.log(Level.INFO, "Ammount error 1");
                  }
                  l_ofxtrans.setTrnamt(lamnt);
                  l_ofxtrans.setTrntype("CREDIT");

                  String l_memo = "";
                  try {
                    l_memo = entryDetails1.getTxDtls().get(0).getRmtInf().getUstrd().stream()
                        .collect(Collectors.joining(","));
                    LOGGER.log(l_Level, "Creditor remittance information (payment description): " + l_memo);
                  } catch (Exception e) {
                    l_memo = reportEntry2.getAddtlNtryInf();
                  }

                  if (l_memo == null) {
                    l_memo = "";
                  }

                  l_memo = l_memo.replaceAll("( )+", " ");
                  l_memo = l_memo.replaceAll("  ", " ");
                  l_ofxtrans.setMemo(l_memo);
                  LOGGER.log(l_Level, "Memo: " + l_memo);
                  if (l_ofxtrans.getName().isBlank()) {
                    l_ofxtrans.setName(l_memo);
                  }

                }
                if (CreditDebitCode.CRDT == reportEntry2.getCdtDbtInd()) {
                  // Incoming (credit) payments, show origin (debtor) information, money was
                  // transferred from a client (debtor) to the bank (creditor)
                  if (entryDetails1.getTxDtls().get(0).getRltdPties() != null) {
                    LOGGER.log(l_Level,
                        "Debtor name: " + entryDetails1.getTxDtls().get(0).getRltdPties().getDbtr().getNm());
                    LOGGER.log(l_Level, "Debtor IBAN: "
                        + entryDetails1.getTxDtls().get(0).getRltdPties().getDbtrAcct().getId().getIBAN());

                    l_ofxtrans.setName(entryDetails1.getTxDtls().get(0).getRltdPties().getDbtr().getNm());
                    l_ofxtrans
                        .setAccountto(entryDetails1.getTxDtls().get(0).getRltdPties().getDbtrAcct().getId().getIBAN());
                  } else {
                    l_ofxtrans.setName("");
                    l_ofxtrans.setAccountto("");
                  }

                  BigDecimal lamnt = new BigDecimal(1.0);
                  try {
                    lamnt = lamnt.multiply(reportEntry2.getAmt().getValue());
                  } catch (Exception e) {
                    // TODO
                    LOGGER.log(Level.INFO, "Amount error 2");
                  }
                  l_ofxtrans.setTrnamt(lamnt);
                  l_ofxtrans.setTrntype("DEBIT");

                  String l_memo = "";
                  try {
                    l_memo = entryDetails1.getTxDtls().get(0).getRmtInf().getUstrd().stream()
                        .collect(Collectors.joining(","));
                  } catch (Exception e) {
                    l_memo = reportEntry2.getAddtlNtryInf();
                  }

                  if (l_memo == null) {
                    l_memo = "";
                  }

                  l_memo = l_memo.replaceAll("( )+", " ");
                  l_memo = l_memo.replaceAll("  ", " ");
                  l_ofxtrans.setMemo(l_memo);
                  LOGGER.log(l_Level, "Memo: " + l_memo);

                  if (l_ofxtrans.getName().isBlank()) {
                    l_ofxtrans.setName(l_memo);
                  }
                }

                String l_fitid = OfxFunctions.createUniqueId(l_ofxtrans, m_UniqueIds);
                m_UniqueIds.add(l_fitid);
                l_ofxtrans.setFitid(l_fitid);

                l_ofxtrans.setSaving(false);
                l_ofxtrans.setSource(m_FileName);
                m_OfxTransactions.add(l_ofxtrans);
              }
            } catch (Exception e) {
              LOGGER.log(Level.INFO, e.getMessage());
            }
          }
        }
      }
      LOGGER.log(Level.INFO, "Transactions read: " + Integer.toString(m_OfxTransactions.size()));
    } catch (Exception e) {
      LOGGER.log(Level.INFO, e.getMessage());
    }
  }

  /**
   * Returns true when the processed transactions are saving transactions.
   * 
   * @return True for Saving transactions
   */
  public boolean isSavingCsvFile() {
    return false;
  }

  /**
   * Return a list of normal transactions or null when savings transactions are processed.
   * 
   * @return List of normal transactions
   */
  public List<SnsTransaction> getSnsTransactions() {
    return m_Transactions;
  }

  public Set<String> getUniqueIds() {
    return m_UniqueIds;
  }

  /**
   * Return a list of OFX transactions.
   * 
   * @return List of saving transactions
   */
  public List<OfxTransaction> getOfxTransactions() {
    return m_OfxTransactions;
  }

  /**
   * Returns meta information of the OFX transactions.
   * 
   * @return OFX Meta information
   */
  public Map<String, OfxMetaInfo> getOfxMetaInfo() {
    return m_metainfo;
  }
}
