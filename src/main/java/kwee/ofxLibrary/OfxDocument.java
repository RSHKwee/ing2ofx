package kwee.ofxLibrary;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

public class OfxDocument {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private List<OfxTransaction> m_OfxTransactions = new LinkedList<OfxTransaction>();
  private Map<String, OfxMetaInfo> m_metainfo = new HashMap<String, OfxMetaInfo>();

  OfxDocument() {
  }

  public OfxDocument(List<OfxTransaction> a_OfxTransactions, Map<String, OfxMetaInfo> a_metainfo) {
    m_OfxTransactions = a_OfxTransactions;
    m_metainfo = a_metainfo;
  }

  private ArrayList<String> OfxXmlHeader() {
    DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyyMMdd");
    LocalDateTime now = LocalDateTime.now();
    String datestr = dtf.format(now);

    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("<OFX>");
    l_regels.add("  <SIGNONMSGSRSV1>");
    l_regels.add("    <SONRS>                            <!-- Begin signon -->");
    l_regels.add("      <STATUS>                        <!-- Begin status aggregate -->");
    l_regels.add("         <CODE>0</CODE>               <!-- OK -->");
    l_regels.add("         <SEVERITY>INFO</SEVERITY>");
    l_regels.add("      </STATUS>");
    l_regels.add("      <DTSERVER>" + datestr + "</DTSERVER>   <!-- Oct. 29, 1999, 10:10:03 am -->");
    l_regels.add("      <LANGUAGE>ENG</LANGUAGE>        <!-- Language used in response -->");
    l_regels.add("      <DTPROFUP>" + datestr + "</DTPROFUP>   <!-- Last update to profile-->");
    l_regels.add("      <DTACCTUP>" + datestr + "</DTACCTUP>   <!-- Last account update -->");
    l_regels.add("      <FI>                            <!-- ID of receiving institution -->");
    l_regels.add("         <ORG>NCH</ORG>               <!-- Name of ID owner -->");
    l_regels.add("         <FID>1001</FID>              <!-- Actual ID -->");
    l_regels.add("      </FI>");
    l_regels.add("    </SONRS>                           <!-- End of signon -->");
    l_regels.add("  </SIGNONMSGSRSV1>");
    l_regels.add("  <BANKMSGSRSV1>");
    l_regels.add("   <STMTTRNRS>                        <!-- Begin response -->");
    l_regels.add("      <TRNUID>1001</TRNUID>           <!-- Client ID sent in request -->");
    l_regels.add("      <STATUS>                     <!-- Start status aggregate -->");
    l_regels.add("         <CODE>0</CODE>            <!-- OK -->");
    l_regels.add("         <SEVERITY>INFO</SEVERITY>");
    l_regels.add("      </STATUS>");
    return l_regels;
  }

  private ArrayList<String> OfxXmlFooter() {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("   </STMTTRNRS>                        <!-- End of accounts -->");
    l_regels.add("  </BANKMSGSRSV1>");
    l_regels.add("</OFX>");
    return l_regels;
  }

  ArrayList<String> m_regels = new ArrayList<String>();
  String m_Filename = "temp.ofx";

  public void CreateOfxDocument(String a_FileName) {
    if (!a_FileName.isBlank()) {
      m_Filename = a_FileName;
    }

    m_regels.clear();
    m_regels = OfxXmlHeader();

    OfxXmlTransactions l_xmlTransactions = new OfxXmlTransactions(m_OfxTransactions, m_metainfo);
    m_regels.addAll(l_xmlTransactions.OfxXmlTransactionsBody());

    m_regels.addAll(OfxXmlFooter());
    LOGGER.log(Level.INFO, "Create OFX file " + m_Filename);
    kwee.library.TxtBestand.DumpXmlBestand(m_Filename, m_regels);
  }
}
