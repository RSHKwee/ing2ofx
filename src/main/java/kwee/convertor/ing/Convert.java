package kwee.convertor.ing;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

import kwee.convertor.ing.ingLibrary.IngSavingTransaction;
import kwee.convertor.ing.ingLibrary.IngSavingTransactionEng;
import kwee.convertor.ing.ingLibrary.IngTransaction;
import kwee.convertor.ing.ingLibrary.IngTransactionEng;

/**
 * Convert ING transactions with English heading, ING changed it on ?2 oct 2023?
 * to the original transactions with Dutch heading...
 * 
 * @author René
 *
 */
public class Convert {

  /**
   * Convert ING Saving transaction
   * 
   * @param a_Tran English saving transaction
   * @return Dutch saving transaction
   */
  public static IngSavingTransaction ConvSavingTran(IngSavingTransactionEng a_Tran) {
    IngSavingTransaction l_tran = new IngSavingTransaction();

    String outputDate = "";
    SimpleDateFormat outputDateFormat = new SimpleDateFormat("yyyyMMdd");

    Date date = a_Tran.getDatum();
    outputDate = outputDateFormat.format(date);

    l_tran.setDatum(outputDate);
    l_tran.setOmschrijving(a_Tran.getOmschrijving());
    l_tran.setRekening(a_Tran.getRekening());
    l_tran.setRekeningNaam(a_Tran.getRekeningNaam());
    l_tran.setTegenrekening(a_Tran.getTegenrekening());
    l_tran.setAf_Bij(a_Tran.getAf_Bij());
    l_tran.setBedrag(a_Tran.getBedrag());
    l_tran.setValuta(a_Tran.getValuta());
    l_tran.setMutatiesoort(a_Tran.getMutatiesoort());
    l_tran.setMededelingen(a_Tran.getMededelingen());
    l_tran.setSaldo_na_mutatie(a_Tran.getSaldo_na_mutatie());
    return l_tran;
  }

  /**
   * Convert ING transaction
   * 
   * @param a_Tran English transaction
   * @return Dutch transaction
   */
  public static IngTransaction ConvTran(IngTransactionEng a_Tran) {
    IngTransaction l_tran = new IngTransaction();

    l_tran.setDatum(a_Tran.getDatum());
    l_tran.setOmschrijving(a_Tran.getOmschrijving());
    l_tran.setRekening(a_Tran.getRekening());
    l_tran.setTegenrekening(a_Tran.getTegenrekening());
    l_tran.setCode(a_Tran.getCode());
    l_tran.setAf_Bij(a_Tran.getAf_Bij());
    l_tran.setBedrag(a_Tran.getBedrag());
    l_tran.setMutatiesoort(a_Tran.getMutatiesoort());
    l_tran.setMededelingen(a_Tran.getMededelingen());
    l_tran.setSaldo_na_mutatie(a_Tran.getSaldo_na_mutatie());
    l_tran.setTag(a_Tran.getTag());
    return l_tran;
  }
}
