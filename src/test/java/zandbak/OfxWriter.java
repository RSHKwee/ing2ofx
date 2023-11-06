package zandbak;

import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import com.webcohesion.ofx4j.domain.data.ResponseEnvelope;
import com.webcohesion.ofx4j.domain.data.ResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.banking.AccountType;
import com.webcohesion.ofx4j.domain.data.banking.BankAccountDetails;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponse;
import com.webcohesion.ofx4j.domain.data.banking.BankStatementResponseTransaction;
import com.webcohesion.ofx4j.domain.data.banking.BankingResponseMessageSet;
import com.webcohesion.ofx4j.domain.data.common.BalanceInfo;
import com.webcohesion.ofx4j.domain.data.common.Currency;
import com.webcohesion.ofx4j.domain.data.common.Status;
import com.webcohesion.ofx4j.domain.data.common.Status.Severity;
import com.webcohesion.ofx4j.domain.data.common.Transaction;
import com.webcohesion.ofx4j.domain.data.common.TransactionList;
import com.webcohesion.ofx4j.domain.data.common.TransactionType;
import com.webcohesion.ofx4j.domain.data.signon.FinancialInstitution;
import com.webcohesion.ofx4j.domain.data.signon.SignonResponse;
import com.webcohesion.ofx4j.domain.data.signon.SignonResponseMessageSet;

import com.webcohesion.ofx4j.io.AggregateMarshaller;
import com.webcohesion.ofx4j.io.v2.OFXV2Writer;

import kwee.ofxLibrary.OfxTransaction;

public class OfxWriter {

  public static void main(String[] argv) {
    ResponseEnvelope ofx = new ResponseEnvelope();
    Date now = new java.util.Date();

    // SignOn Message Response
    SignonResponseMessageSet signonMsgSet = new SignonResponseMessageSet();
    signonMsgSet = setSigonMessage();

    BankAccountDetails bankAccountDetails = new BankAccountDetails();
    AccountType acctype = AccountType.CHECKING;
    bankAccountDetails.setAccountType(acctype); // Set the appropriate account type
    bankAccountDetails.setAccountNumber("12345678"); // Set the account number
    bankAccountDetails.setBankId("INGB");

    BankStatementResponse statRespons = new BankStatementResponse();
    statRespons.setAccount(bankAccountDetails);
    statRespons.setCurrencyCode("EUR");
    BalanceInfo ledgerbal = new BalanceInfo();
    ledgerbal.setAmount(0);
    ledgerbal.setAsOfDate(now);
    statRespons.setLedgerBalance(ledgerbal);

    Transaction statementTransaction = new Transaction();
    Currency currency = new Currency();
    currency.setCode("EUR");
    currency.setExchangeRate(0.0f);
    statementTransaction.setCurrency(currency);
    TransactionType trtype = TransactionType.PAYMENT;
    statementTransaction.setTransactionType(trtype);
    statementTransaction.setDatePosted(now);
    statementTransaction.setAmount(100.0);
    statementTransaction.setId("6666");

    // Add transactions to the statement (e.g., deposits, withdrawals)
    TransactionList transactionList = new TransactionList();
    transactionList.setStart(now);
    transactionList.setEnd(now);

    // Populate the transaction list with your financial transactions
    List<Transaction> tranlist = transactionList.getTransactions();
    if (tranlist == null) {
      tranlist = new ArrayList<Transaction>();
    }
    tranlist.add(statementTransaction);
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

    SortedSet<ResponseMessageSet> msgset = new TreeSet<ResponseMessageSet>();
    msgset.add(signonMsgSet);
    msgset.add(bankresponse);
    ofx.setMessageSets(msgset);

    try {
      OFXV2Writer writer = new OFXV2Writer(new FileOutputStream("output.ofx"));
      AggregateMarshaller marshaller = new AggregateMarshaller();
      marshaller.marshal(ofx, writer);
      writer.close();
      System.out.println("It works ????");
    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  static SignonResponseMessageSet setSigonMessage() {
    Date now = new java.util.Date();

    // SignOn Message Response
    SignonResponse signon = new SignonResponse();
    SignonResponseMessageSet signonMsgSet = new SignonResponseMessageSet();

    Status stat = new Status();
    Severity sev = Severity.INFO;
    stat.setSeverity(sev);

    signon.setStatus(stat);
    signon.setLanguage("ENG");
    signon.setTimestamp(now);

    FinancialInstitution finins = new FinancialInstitution();
    finins.setOrganization("NCH");
    finins.setId("1001");
    signon.setFinancialInstitution(finins);

    signonMsgSet.setSignonResponse(signon);
    return signonMsgSet;
  }

  static Transaction setXMLTransaction(OfxTransaction a_transaction) {
    Transaction l_transaction = new Transaction();

    BankAccountDetails accto = new BankAccountDetails();
    accto.setAccountNumber(kwee.ofxLibrary.OfxFunctions.getAccountTo(a_transaction));
    accto.setBankId(kwee.ofxLibrary.OfxFunctions.getAccounttoBIC(a_transaction));

    AccountType acctyp = AccountType.CHECKING;
    accto.setAccountType(acctyp);
    l_transaction.setBankAccountTo(accto);

    Currency currency = new Currency();
    currency.setCode("EUR");
    currency.setExchangeRate(0.0f);
    l_transaction.setCurrency(currency);

    TransactionType trtype = TransactionType.valueOf(a_transaction.getTrntype());
    l_transaction.setTransactionType(trtype);

    Date dtposted = kwee.library.DateToNumeric.String_NumericToDate(a_transaction.getDtposted());
    l_transaction.setDatePosted(dtposted);
    l_transaction.setAmount(Double.valueOf(a_transaction.getTrnamt()));
    l_transaction.setId(a_transaction.getFitid());
    return l_transaction;
  }
}
