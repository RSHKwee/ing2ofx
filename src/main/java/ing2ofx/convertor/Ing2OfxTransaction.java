package ing2ofx.convertor;

import java.util.Map;
//import java.util.logging.Logger;

public class Ing2OfxTransaction {
//  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());

  static public OfxTransaction convertSavingToOfx(IngSavingTransaction a_trans) {
    OfxTransaction l_ofxtrans = new OfxTransaction();
    l_ofxtrans.setAccount(a_trans.getRekening().replaceAll(" ", ""));
    l_ofxtrans.setTrntype(transType("xx", a_trans.getAf_Bij()));
    l_ofxtrans.setDtposted(a_trans.getDatum().replaceAll("-", ""));

    if (a_trans.getAf_Bij().equalsIgnoreCase("Bij")) {
      l_ofxtrans.setTrnamt(a_trans.getBedrag());
    } else {
      l_ofxtrans.setTrnamt("-" + a_trans.getBedrag());
    }

    l_ofxtrans.setName(stripName(a_trans.getOmschrijving()));
    l_ofxtrans.setAccountto(a_trans.getTegenrekening());
    l_ofxtrans.setMemo(stripName(a_trans.getMededelingen()));
    return l_ofxtrans;
  }

  static public OfxTransaction convertToOfx(IngTransaction a_trans) {
    OfxTransaction l_ofxtrans = new OfxTransaction();
    l_ofxtrans.setAccount(a_trans.getRekening().replaceAll(" ", ""));
    l_ofxtrans.setTrntype(transType(a_trans.getCode(), a_trans.getAf_Bij()));
    l_ofxtrans.setDtposted(a_trans.getDatum());

    if (a_trans.getAf_Bij().equalsIgnoreCase("Bij")) {
      l_ofxtrans.setTrnamt(a_trans.getBedrag());
    } else {
      l_ofxtrans.setTrnamt("-" + a_trans.getBedrag());
    }

    l_ofxtrans.setName(stripName(a_trans.getOmschrijving()));
    l_ofxtrans.setAccountto(a_trans.getTegenrekening());
    l_ofxtrans.setMemo(stripName(a_trans.getMededelingen()));
    return l_ofxtrans;
  }
  /*
   * @formatter:off
    private String account;
    private String trntype;
    private String dtposted; x
    private String trnamt; x
    private String fitid;
    private String name; x
    private String accountto; x
    private String memo; x 
    
    
        # Transaction type, must be one of TRANSACTION_TYPES # "Code"
      "CREDIT",       # Generic credit
      "DEBIT",        # Generic debit
      "INT",          # Interest earned or paid
      "DIV",          # Dividend
      "FEE",          # FI fee
      "SRVCHG",       # Service charge
      "DEP",          # Deposit
      "ATM",          # ATM debit or credit             # GM
      "POS",          # Point of sale debit or credit   # BA
      "XFER",         # Transfer
      "CHECK",        # Check
      "PAYMENT",      # Electronic payment              # GT
      "CASH",         # Cash withdrawal
      "DIRECTDEP",    # Direct deposit                  # ST
      "DIRECTDEBIT",  # Merchant initiated debit        # IC
      "REPEATPMT",    # Repeating payment/standing order
      "OTHER"         # Other                           # DV OV VZ 
  
          codesx = {'GT': 'PAYMENT', 'BA': 'POS', 'GM': 'ATM', 'DV': 'xx',
                  'OV': 'xx', 'VZ': 'xx', 'IC': 'DIRECTDEBIT', 'ST': 'DIRECTDEP'}

                    # Map the code into ofx TRNTYPE
                if row['Code'] in codesx:
                    if codesx[row['Code']] == 'xx':
                        if row['Af Bij'] == 'Bij':
                            trntype = 'CREDIT'
                        else:
                            trntype = 'DEBIT'
                    else:
                        trntype = codesx[row['Code']]
                else:
                    trntype = 'OTHER'
   @formatter:on
   */

  private static String transType(String a_code, String a_afbij) {
    String l_code = "OTHER";
    Map<String, String> codex = Map.of("GT", "PAYMENT", "BA", "POS", "GM", "ATM", "DV", "xx", "OV", "xx", "VZ", "xx",
        "IC", "DIRECTDEBIT", "ST", "DIRECTDEP");

    if (codex.containsKey(a_code)) {
      if (codex.get(a_code).equalsIgnoreCase("xx")) {
        if (a_afbij.equalsIgnoreCase("Bij")) {
          l_code = "CREDIT";
        } else {
          l_code = "DEBIT";
        }
      } else {
        l_code = codex.get(a_code);
      }
    } else {
      l_code = "OTHER";
    }
    return l_code;
  }

  private static String stripName(String a_name) {
    String l_name = a_name;
    l_name = l_name.strip().replaceAll("  ", " ").replace("&", "&amp");
    return l_name;
  }
}
