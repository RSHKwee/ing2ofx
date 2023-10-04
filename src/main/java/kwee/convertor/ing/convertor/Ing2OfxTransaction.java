package kwee.convertor.ing.convertor;

/**
 * Conversion methods for conversion ING- to OFX transaction.
 * 
 * @author René
 *
 */
import java.util.Map;
//import java.util.logging.Logger;

import kwee.convertor.ing.ingLibrary.IngSavingTransaction;
import kwee.convertor.ing.ingLibrary.IngSavingTransactionEng;
import kwee.convertor.ing.ingLibrary.IngTransaction;
import kwee.convertor.ing.ingLibrary.IngTransactionEng;
import kwee.ofxLibrary.OfxTransaction;

public class Ing2OfxTransaction {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  static String m_bankcode = "INGBNL2A";

  /**
   * Conversion of an ING Saving transaction to an OFX transaction.
   * 
   * @param a_trans ING Saving transaction
   * @return OFX Transaction
   */
  static public OfxTransaction convertSavingToOfx(IngSavingTransaction a_trans) {
    OfxTransaction l_ofxtrans = new OfxTransaction(m_bankcode);
    l_ofxtrans.setAccount(a_trans.getRekening().replaceAll(" ", ""));
    l_ofxtrans.setTrntype(transType("xx", a_trans.getAf_Bij()));
    l_ofxtrans.setDtposted(a_trans.getDatum().replaceAll("-", ""));

    if (a_trans.getAf_Bij().equalsIgnoreCase("Bij")) {
      l_ofxtrans.setTrnamt(a_trans.getBedrag());
    } else {
      l_ofxtrans.setTrnamt("-" + a_trans.getBedrag());
    }

    l_ofxtrans.setName(xmlFriendlyName(a_trans.getOmschrijving()));
    l_ofxtrans.setAccountto(a_trans.getTegenrekening());
    l_ofxtrans.setMemo(xmlFriendlyName(a_trans.getMededelingen()));
    l_ofxtrans.setSaldo_na_mutatie(a_trans.getSaldo_na_mutatie());
    return l_ofxtrans;
  }

  static public OfxTransaction convertSavingEngToOfx(IngSavingTransactionEng a_trans) {
    OfxTransaction l_ofxtrans = new OfxTransaction(m_bankcode);
    l_ofxtrans.setAccount(a_trans.getRekening().replaceAll(" ", ""));
    l_ofxtrans.setTrntype(transType("xx", a_trans.getAf_Bij()));
    l_ofxtrans.setDtposted(a_trans.getDatum().replaceAll("-", ""));

    if (a_trans.getAf_Bij().equalsIgnoreCase("Bij")) {
      l_ofxtrans.setTrnamt(a_trans.getBedrag());
    } else {
      l_ofxtrans.setTrnamt("-" + a_trans.getBedrag());
    }

    l_ofxtrans.setName(xmlFriendlyName(a_trans.getOmschrijving()));
    l_ofxtrans.setAccountto(a_trans.getTegenrekening());
    l_ofxtrans.setMemo(xmlFriendlyName(a_trans.getMededelingen()));
    l_ofxtrans.setSaldo_na_mutatie(a_trans.getSaldo_na_mutatie());
    return l_ofxtrans;
  }

  /**
   * Conversion of an ING transaction to an OFX transaction.
   * 
   * @param a_trans ING transaction
   * @return OFX Transaction
   */
  static public OfxTransaction convertToOfx(IngTransaction a_trans) {
    OfxTransaction l_ofxtrans = new OfxTransaction(m_bankcode);
    l_ofxtrans.setAccount(a_trans.getRekening().replaceAll(" ", ""));
    l_ofxtrans.setTrntype(transType(a_trans.getCode(), a_trans.getAf_Bij()));
    l_ofxtrans.setDtposted(a_trans.getDatum());

    if (a_trans.getAf_Bij().equalsIgnoreCase("Bij")) {
      l_ofxtrans.setTrnamt(a_trans.getBedrag());
    } else {
      l_ofxtrans.setTrnamt("-" + a_trans.getBedrag());
    }

    l_ofxtrans.setName(xmlFriendlyName(a_trans.getOmschrijving()));
    l_ofxtrans.setAccountto(a_trans.getTegenrekening());
    l_ofxtrans.setMemo(xmlFriendlyName(a_trans.getMededelingen()));
    l_ofxtrans.setSaldo_na_mutatie(a_trans.getSaldo_na_mutatie());

    if (a_trans.getTegenrekening().isBlank()) {
      String l_memo = a_trans.getMededelingen();
      String[] l_parts = l_memo.split(" ");
      if (l_parts[0].equals("Van") || l_parts[0].equals("Naar")) {
        if (l_parts.length >= 3) {
          l_ofxtrans.setAccountto(l_parts[3]);
        }
      }
    }
    return l_ofxtrans;
  }

  static public OfxTransaction convertToOfxEng(IngTransactionEng a_trans) {
    OfxTransaction l_ofxtrans = new OfxTransaction(m_bankcode);
    l_ofxtrans.setAccount(a_trans.getRekening().replaceAll(" ", ""));
    l_ofxtrans.setTrntype(transType(a_trans.getCode(), a_trans.getAf_Bij()));
    l_ofxtrans.setDtposted(a_trans.getDatum());

    if (a_trans.getAf_Bij().equalsIgnoreCase("Bij")) {
      l_ofxtrans.setTrnamt(a_trans.getBedrag());
    } else {
      l_ofxtrans.setTrnamt("-" + a_trans.getBedrag());
    }

    l_ofxtrans.setName(xmlFriendlyName(a_trans.getOmschrijving()));
    l_ofxtrans.setAccountto(a_trans.getTegenrekening());
    l_ofxtrans.setMemo(xmlFriendlyName(a_trans.getMededelingen()));
    l_ofxtrans.setSaldo_na_mutatie(a_trans.getSaldo_na_mutatie());

    if (a_trans.getTegenrekening().isBlank()) {
      String l_memo = a_trans.getMededelingen();
      String[] l_parts = l_memo.split(" ");
      if (l_parts[0].equals("Van") || l_parts[0].equals("Naar")) {
        if (l_parts.length >= 3) {
          l_ofxtrans.setAccountto(l_parts[3]);
        }
      }
    }
    return l_ofxtrans;
  }

  /**
   * Determine the Transaction type.
   * 
   * @param a_code  ING transaction code
   * @param a_afbij Debit or Credit
   * @return OFX Transaction code
   */
  private static String transType(String a_code, String a_afbij) {
    String l_code = "OTHER";
    Map<String, String> codex = Map.of("GT", "PAYMENT", "BA", "POS", "GM", "ATM", "DV", "xx", "OV", "xx", "VZ", "xx",
        "IC", "DIRECTDEBIT", "ST", "DIRECTDEP");

    if (codex.containsKey(a_code)) {
      if (codex.get(a_code).equalsIgnoreCase("xx")) {
        if (a_afbij.equalsIgnoreCase("Bij") || a_afbij.equalsIgnoreCase("Credit")) {
          l_code = "CREDIT";
        } else {
          l_code = "DEBIT";
        }
      } else {
        l_code = codex.get(a_code);
      }
    } else {
      if (a_code.equalsIgnoreCase("xx")) {
        if (a_afbij.equalsIgnoreCase("Bij")) {
          l_code = "CREDIT";
        } else {
          l_code = "DEBIT";
        }
      } else {
        l_code = "OTHER";
      }
    }
    return l_code;
  }

  /**
   * Convert & to a HTML / XML friendly format.
   * 
   * @param a_name String
   * @return HTML / XML Friendly string.
   */
  private static String xmlFriendlyName(String a_name) {
    String l_name = a_name;
    l_name = l_name.strip().replaceAll("  ", " ").replace("&", "&amp");
    return l_name;
  }
}
