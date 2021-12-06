package ing2ofx.convertor;

public class OfxTransaction {
  /*
 * @formatter:off
 *          
                <ACCTID>%(account)s</ACCTID>  <!-- Account number -->

               ofxfile.write(message_begin)

                for trns in csv.transactions:
                    if trns['account'] == account:
                        message_transaction = """
               <STMTTRN>
                  <TRNTYPE>%(trntype)s</TRNTYPE>
                  <DTPOSTED>%(dtposted)s</DTPOSTED>
                  <TRNAMT>%(trnamt)s</TRNAMT>
                  <FITID>%(fitid)s</FITID>
                  <NAME>%(name)s</NAME>
                  <BANKACCTTO>
                     <BANKID></BANKID>
                     <ACCTID>%(accountto)s</ACCTID>
                     <ACCTTYPE>CHECKING</ACCTTYPE>
                  </BANKACCTTO>
                  <MEMO>%(memo)s</MEMO>
               </STMTTRN>""" % trns
                        ofxfile.write(message_transaction)

 * @formatter:on
 */
}
