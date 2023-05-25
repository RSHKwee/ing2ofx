package kwee.ofxLibrary;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedHashSet;
import java.util.LinkedList;
import java.util.List;
// import java.util.logging.Level;
// import java.util.logging.Logger;
// import java.util.logging.Level;
import java.util.Map;
import java.util.Set;

import kwee.library.TxtBestand;

public class OfxFunctions {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  /**
   * Check if OfxTransaction already is NOT present in list of OfxTransactions.
   * 
   * @param a_OfxTransactions  List of OfxTransactions.
   * @param a_OfxTransactions2 List of OfxTransactions to add.
   * @return Filtered List of OfxTransactions.
   */
  static public List<OfxTransaction> uniqueOfxTransactions(List<OfxTransaction> a_OfxTransactions,
      List<OfxTransaction> a_OfxTransactions2) {
    List<OfxTransaction> l_OfxTransactions = new LinkedList<OfxTransaction>();
    for (int i = 0; i < a_OfxTransactions2.size(); i++) {
      OfxTransaction l_OfxTransaction1 = a_OfxTransactions2.get(i);
      boolean bstat = false;
      int j = 0;
      while (!bstat && j < a_OfxTransactions.size()) {
        OfxTransaction l_OfxTransaction2 = a_OfxTransactions.get(j);
        bstat = l_OfxTransaction2.equals(l_OfxTransaction1);
        j++;
      }
      if (!bstat) {
        l_OfxTransactions.add(l_OfxTransaction1);
      }
    }
    return l_OfxTransactions;
  }

  /**
   * Add MetaInfo to existing MetaInfo.
   * 
   * @param a_MetaInfo1       Base MetaInfo
   * @param a_MetaInfo2       MetaInfo to be added
   * @param a_OfxTransactions List of transactions to be added
   * @return Sum of MetaInfo's.
   */
  static public Map<String, OfxMetaInfo> addMetaInfo(Map<String, OfxMetaInfo> a_MetaInfo1,
      Map<String, OfxMetaInfo> a_MetaInfo2, List<OfxTransaction> a_OfxTransactions) {

    // Determine accounts/keys in transactions to be added.
    Set<String> l_Keys = new LinkedHashSet<String>();
    a_OfxTransactions.forEach(tran -> {
      l_Keys.add(tran.getAccount());
    });

    // Add metainfo for transactions
    Map<String, OfxMetaInfo> l_metainfo = new HashMap<String, OfxMetaInfo>(a_MetaInfo1);
    l_Keys.forEach(key -> {
      OfxMetaInfo ll_OfxMetaInfo = a_MetaInfo2.get(key);
      l_metainfo.put(key, ll_OfxMetaInfo);
    });
    return l_metainfo;
  }

  static public void dumpMetaInfo(String a_OutputFile, Map<String, OfxMetaInfo> a_metainfo) {
    ArrayList<String> l_Regels = new ArrayList<String>();
    Set<String> l_AccKeys = a_metainfo.keySet();
    l_Regels.add("Prefix; Account; BalanceDate; Balance; BankCode; MaxDate; MinDate");

    l_AccKeys.forEach(l_AccKey -> {
      OfxMetaInfo l_metainfo = a_metainfo.get(l_AccKey);
      String l_Regel = l_metainfo.getPrefix() + ";" + l_metainfo.getAccount() + "; "
          + formatDate(l_metainfo.getBalanceDate()) + "; "
          + l_metainfo.getBalanceAfterTransaction().replaceAll("\\.", ",") + "; " + l_metainfo.getBankcode() + "; "
          + formatDate(l_metainfo.getMaxDate()) + "; " + formatDate(l_metainfo.getMinDate());
      l_Regels.add(l_Regel);
    });

    TxtBestand.DumpBestand(a_OutputFile, l_Regels);
  }

  static String formatDate(String a_date) {
    // 20230101 => 01-01-2023
    String s_date = "";
    s_date = a_date.substring(6, 8) + "-" + a_date.substring(4, 6) + "-" + a_date.substring(0, 4);
    return s_date;
  }

  /**
   * Create a unique fitid for an OFX Transaction.
   * 
   * @param a_ofxtrans OFX Transaction
   * @param a_UniqueId set of already generated Fit-id's
   * @return A unique fitid
   */
  static public String createUniqueId(OfxTransaction a_ofxtrans, Set<String> a_UniqueId) {
    String uniqueid = "";
    /*
     * @formatter:off
    String memo = l_ofxtrans.getMemo();
    String time = "";
     * time = "" matches = re.search("\s([0-9]{2}:[0-9]{2})\s", memo) if matches:
     * time = matches.group(1).replace(":", "")

    Pattern patt = Pattern.compile("([0-9]{2}:[0-9]{2})");
    Matcher matcher = patt.matcher(memo);
    if (matcher.find()) {
      time = matcher.group(1).replace(":", ""); // you can get it from desired index as well
    }
     * @formatter:on
    */
    String fitid = a_ofxtrans.getDtposted() + a_ofxtrans.getTrnamt().replace(",", "").replace("-", "").replace(".", "");
    uniqueid = fitid;
    if (a_UniqueId.contains(fitid)) {
      // # Make unique by adding time and sequence nr.
      int idcount = 0;
      uniqueid = fitid;
      while (a_UniqueId.contains(uniqueid)) {
        idcount = idcount + 1;
        // uniqueid = fitid + time + Integer.toString(idcount);
        uniqueid = fitid + Integer.toString(idcount);
      }
      a_UniqueId.add(uniqueid);
    } else {
      a_UniqueId.add(fitid);
    }
    return uniqueid;
  }

}
