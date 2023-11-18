package kwee.ofxLibrary.doc;

/**
 * OfxDocument: Generate OFX Document.
 * 
 * 
 * @author RSH Kwee
 *
 */
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.ResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.banking.AccountType;
import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.common.BalanceInfo;
import com.webcohesion.ofx4j.domain.data.common.Status;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.domain.data.common.TransactionList;
import com.webcohesion.ofx4j.domain.data.common.TransactionType;
import com.webcohesion.ofx4j.domain.data.common.Status.Severity;
import com.webcohesion.ofx4j.domain.data.signon.FinancialInstitution;
import com.webcohesion.ofx4j.domain.data.signon.SignonResponse;
import com.webcohesion.ofx4j.domain.data.signon.SignonResponseMessageSet;
import com.webcohesion.ofx4j.io.AggregateMarshaller;

import kwee.ofxLibrary.OfxMetaInfo;
import kwee.ofxLibrary.OfxTransaction;
import kwee.library.DateToNumeric;

public class OfxDocument {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();
  private int m_maxdateint = 0;
  private String m_Filename = "temp.ofx";

  public OfxDocument(List<OfxTransaction> a_OfxTransactions, Map<String, OfxMetaInfo> a_metainfo) {
    m_OfxTransactions = new LinkedList<OfxTransaction>(a_OfxTransactions);
    m_metainfo = new HashMap<String, OfxMetaInfo>(a_metainfo);
    maxMetaDate();
  }

  public void CreateOfxDocument(String a_FileName) {
    if (!a_FileName.isBlank()) {
      m_Filename = a_FileName;
    }
    ResponseEnvelope ofx = new ResponseEnvelope();
    SortedSet<ResponseMessageSet> msgset = new TreeSet<ResponseMessageSet>();

    // SignOn Message Response
    SignonResponseMessageSet signonMsgSet = new SignonResponseMessageSet();
    signonMsgSet = setSigonMessage("1001", "NCH");
    msgset.add(signonMsgSet);

    Set<String> l_keys = m_metainfo.keySet();
    l_keys.forEach(l_key -> {
      OfxMetaInfo l_metainf = m_metainfo.get(l_key);

      BankAccountDetails bankAccountDetails = new BankAccountDetails();
      AccountType acctype = AccountType.CHECKING;
      bankAccountDetails.setAccountType(acctype); // Set the appropriate account type
      bankAccountDetails.setAccountNumber(l_metainf.getAccount()); // Set the account number
      bankAccountDetails.setBankId(l_metainf.getBankcode());

      BankStatementResponse statRespons = new BankStatementResponse();
      statRespons.setAccount(bankAccountDetails);
      statRespons.setCurrencyCode("EUR");
      BalanceInfo ledgerbal = new BalanceInfo();
      ledgerbal.setAmount(l_metainf.getBalanceAfterTransaction());
      ledgerbal.setAsOfDate(DateToNumeric.String_NumericToDate(l_metainf.getMaxDate() + "2359"));
      statRespons.setLedgerBalance(ledgerbal);

      // Add transactions to the statement (e.g., deposits, withdrawals)
      TransactionList transactionList = new TransactionList();
      transactionList.setStart(DateToNumeric.String_NumericToDate(l_metainf.getMinDate()));
      transactionList.setEnd(DateToNumeric.String_NumericToDate(l_metainf.getMaxDate()));

      List<Transaction> tranlist = transactionList.getTransactions();
      if (tranlist == null) {
        tranlist = new ArrayList<Transaction>();
      }
      for (int i = 0; i < m_OfxTransactions.size(); i++) {
        OfxTransaction ofxtran = m_OfxTransactions.get(i);
        Transaction tran = setXMLTransaction(ofxtran, "EUR");
        tranlist.add(tran);
      }
      transactionList.setTransactions(tranlist);
      statRespons.setTransactionList(transactionList);

      Status stat = new Status();
      Severity sev = Severity.INFO;
      stat.setSeverity(sev);

      BankStatementResponseTransaction statementTransactionResponse = new BankStatementResponseTransaction();
      statementTransactionResponse.setUID("1001");
      statementTransactionResponse.setStatus(stat);
      statementTransactionResponse.setMessage(statRespons);

      BankingResponseMessageSet bankresponse = new BankingResponseMessageSet();
      bankresponse.setStatementResponse(statementTransactionResponse);

      msgset.add(bankresponse);
    });

    ofx.setMessageSets(msgset);

    try {
      Ing2OFXV2Writer writer = new Ing2OFXV2Writer(m_Filename);
      AggregateMarshaller marshaller = new AggregateMarshaller();
      marshaller.setConversion(new Ing2OfxStringConversion("CET"));
      marshaller.marshal(ofx, writer);
      writer.close();
      LOGGER.log(Level.INFO, "Document created: " + m_Filename);
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  private void maxMetaDate() {
    Set<String> l_keys = m_metainfo.keySet();
    l_keys.forEach(l_key -> {
      OfxMetaInfo l_metainf = m_metainfo.get(l_key);
      if ((l_metainf.getIntMaxDate() > m_maxdateint)) {
        m_maxdateint = l_metainf.getIntMaxDate();
        // m_maxdate = l_metainf.getMaxDate();
      }
    });
  }

  private SignonResponseMessageSet setSigonMessage(String a_FinInsId, String a_FinInsOrg) {
    Calendar calendar = Calendar.getInstance();
    calendar.set(Calendar.YEAR, 2023);
    calendar.set(Calendar.MONTH, 4);
    calendar.set(Calendar.DATE, 24);
    calendar.set(Calendar.HOUR, 20);
    calendar.set(Calendar.MINUTE, 0);
    calendar.set(Calendar.SECOND, 0);
    Date date = calendar.getTime();
    // Date date = new Date(123, 4, 24);

    // SignOn Message Response
    SignonResponse signon = new SignonResponse();
    SignonResponseMessageSet signonMsgSet = new SignonResponseMessageSet();

    Status stat = new Status();
    Severity sev = Severity.INFO;
    stat.setSeverity(sev);

    signon.setStatus(stat);
    signon.setLanguage("ENG");
    signon.setTimestamp(date);
    signon.setProfileLastUpdated(date);
    signon.setAccountLastUpdated(date);

    FinancialInstitution finins = new FinancialInstitution();
    finins.setOrganization(a_FinInsOrg);
    finins.setId(a_FinInsId);
    signon.setFinancialInstitution(finins);

    signonMsgSet.setSignonResponse(signon);
    return signonMsgSet;
  }

  private Transaction setXMLTransaction(OfxTransaction a_transaction, String a_Currency) {
    Transaction l_transaction = new Transaction();

    BankAccountDetails accto = new BankAccountDetails();
    accto.setAccountNumber(kwee.ofxLibrary.OfxFunctions.getAccountTo(a_transaction));
    accto.setBankId(kwee.ofxLibrary.OfxFunctions.getAccounttoBIC(a_transaction));

    AccountType acctyp = AccountType.CHECKING;
    accto.setAccountType(acctyp);
    l_transaction.setBankAccountTo(accto);

    l_transaction.setName(a_transaction.getName());
    l_transaction.setMemo(a_transaction.getMemo());
    TransactionType trtype = TransactionType.valueOf(a_transaction.getTrntype());
    l_transaction.setTransactionType(trtype);

    Date dtposted = a_transaction.getDtposted();
    l_transaction.setDatePosted(dtposted);
    l_transaction.setAmount(a_transaction.getTrnamt().doubleValue());
    l_transaction.setId(a_transaction.getFitid());
    return l_transaction;
  }
}
