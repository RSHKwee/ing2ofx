package ing2ofx.convertor;

public class OfxCommon {
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
         
   *  See OFXTransactions
         ........
        
   * OFX Footer:         
     </BANKMSGSRSV1>
   </OFX>
 
   * @formatter:on
   */
}
