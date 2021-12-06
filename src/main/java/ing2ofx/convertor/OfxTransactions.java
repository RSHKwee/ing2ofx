package ing2ofx.convertor;

public class OfxTransactions {
  /*
   * @formatter:off
   * for each account:
      <STMTTRNRS>                        <!-- Begin response -->
         <TRNUID>1001</TRNUID>           <!-- Client ID sent in request -->
         <STATUS>                     <!-- Start status aggregate -->
            <CODE>0</CODE>            <!-- OK -->
            <SEVERITY>INFO</SEVERITY>
         </STATUS>""" % {"nowdate": nowdate}
         
    * for trns in csv.transactions:
    *  if trns['account'] == account:
         <STMTRS>                            <!-- Begin statement response -->
            <CURDEF>EUR</CURDEF>
            <BANKACCTFROM>                   <!-- Identify the account -->
               <BANKID>INGBNL2A</BANKID>     <!-- Routing transit or other FI ID -->
               <ACCTID>%(account)s</ACCTID>  <!-- Account number -->
               <ACCTTYPE>CHECKING</ACCTTYPE> <!-- Account type -->
            </BANKACCTFROM>                  <!-- End of account ID -->
            <BANKTRANLIST>                   <!-- Begin list of statement trans. -->
               <DTSTART>%(mindate)s</DTSTART>
               <DTEND>%(maxdate)s</DTEND>""" % {"account": account, "mindate": mindate, "maxdate": maxdate}
         
   * 
   * See OfxTransaction v:
               <STMTTRN>                          <!-- Begin statement response -->
                ......
               </STMTTRN>""" % {"maxdate": maxdate}
   * OfxTransaction ^
   
            </BANKTRANLIST>                   <!-- End list of statement trans. -->
            <LEDGERBAL>                       <!-- Ledger balance aggregate -->
               <BALAMT>""" + saldonatran + """</BALAMT>
               <DTASOF>%(maxdate)s2359</DTASOF>  <!-- Bal date: Last date in transactions, 11:59 pm -->
            </LEDGERBAL>                      <!-- End ledger balance -->
         </STMTRS>""" % {"maxdate": maxdate}
         
   * Each account:
      </STMTTRNRS>                        <!-- End of transaction -->
  
   * @formatter:on
   */
}
