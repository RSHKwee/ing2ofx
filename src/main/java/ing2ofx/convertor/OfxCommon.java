package ing2ofx.convertor;

import java.util.ArrayList;
import java.util.logging.Logger;

public class OfxCommon {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  /*
   * @formatter:off
   * OFX Header:
   <OFX>
     <SIGNONMSGSRSV1>
       <SONRS>                            <!-- Begin signon -->
         <STATUS>                        <!-- Begin status aggregate -->
            <CODE>0</CODE>               <!-- OK -->
            <SEVERITY>INFO</SEVERITY>
         </STATUS>
         <DTSERVER>%(nowdate)s</DTSERVER>   <!-- Oct. 29, 1999, 10:10:03 am -->
         <LANGUAGE>ENG</LANGUAGE>        <!-- Language used in response -->
         <DTPROFUP>%(nowdate)s</DTPROFUP>   <!-- Last update to profile-->
         <DTACCTUP>%(nowdate)s</DTACCTUP>   <!-- Last account update -->
         <FI>                            <!-- ID of receiving institution -->
            <ORG>NCH</ORG>               <!-- Name of ID owner -->
            <FID>1001</FID>              <!-- Actual ID -->
         </FI>
       </SONRS>                           <!-- End of signon -->
     </SIGNONMSGSRSV1>
     <BANKMSGSRSV1>
               <STMTTRNRS>                        <!-- Begin response -->
         <TRNUID>1001</TRNUID>           <!-- Client ID sent in request -->
         <STATUS>                     <!-- Start status aggregate -->
            <CODE>0</CODE>            <!-- OK -->
            <SEVERITY>INFO</SEVERITY>
         </STATUS>"""
   *  See OFXTransactions
         ........
        
   * OFX Footer:         
     </BANKMSGSRSV1>
   </OFX>
 
   * @formatter:on
   */

  public ArrayList<String> OfxXmlHeader(String a_nowdate) {
    ArrayList<String> l_regels = new ArrayList<String>();
    //@formatter:off
    l_regels.add("   <OFX>"); 
    l_regels.add("     <SIGNONMSGSRSV1>");
    l_regels.add("       <SONRS>                            <!-- Begin signon -->");
    l_regels.add("         <STATUS>                        <!-- Begin status aggregate -->");
    l_regels.add("            <CODE>0</CODE>               <!-- OK -->"); 
    l_regels.add("            <SEVERITY>INFO</SEVERITY>");
    l_regels.add("         </STATUS>");
    l_regels.add("         <DTSERVER>" + a_nowdate + "</DTSERVER>   <!-- Oct. 29, 1999, 10:10:03 am -->");
    l_regels.add("         <LANGUAGE>ENG</LANGUAGE>        <!-- Language used in response -->");
    l_regels.add("         <DTPROFUP>" + a_nowdate + "</DTPROFUP>   <!-- Last update to profile-->"); 
    l_regels.add("         <DTACCTUP>" + a_nowdate + "</DTACCTUP>   <!-- Last account update -->");
    l_regels.add("         <FI>                            <!-- ID of receiving institution -->");
    l_regels.add("            <ORG>NCH</ORG>               <!-- Name of ID owner -->");
    l_regels.add("            <FID>1001</FID>              <!-- Actual ID -->");
    l_regels.add("         </FI>");
    l_regels.add("       </SONRS>                           <!-- End of signon -->");
    l_regels.add("     </SIGNONMSGSRSV1>");
    l_regels.add("     <BANKMSGSRSV1>");
    l_regels.add("      <STMTTRNRS>                        <!-- Begin response -->");
    l_regels.add("         <TRNUID>1001</TRNUID>           <!-- Client ID sent in request -->");
    l_regels.add("         <STATUS>                     <!-- Start status aggregate -->");
    l_regels.add("            <CODE>0</CODE>            <!-- OK -->");
    l_regels.add("            <SEVERITY>INFO</SEVERITY>\r\n");
          l_regels.add( "         </STATUS>");
    //@formatter:on
    return l_regels;
  }

  public ArrayList<String> OfxXmlFooter() {
    ArrayList<String> l_regels = new ArrayList<String>();
    l_regels.add("      </STMTTRNRS>                        <!-- End of accounts -->");
    l_regels.add("     </BANKMSGSRSV1>");
    l_regels.add("   </OFX>");
    return l_regels;
  }

}
