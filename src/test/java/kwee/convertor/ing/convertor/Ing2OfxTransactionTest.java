package kwee.convertor.ing.convertor;

import java.io.File;
import java.net.URL;

import junit.framework.TestCase;

public class Ing2OfxTransactionTest extends TestCase {
  private String c_IngTransFile = "Alle_rekeningen.csv";
  private String c_IngSavingTransFile = "Alle_spaarrekeningen.csv";
  private String c_SynonymFile = "Synoniem.txt";

  @Override
  public void setUp() {
  }

  @Override
  public void tearDown() {
  }

  public void testConvertSavingToOfx() {
    fail("Not yet implemented");
    File csvFile = null;
    File synFile = null;
    // Get the URL of csv file
    URL resourceUrl = getClass().getClassLoader().getResource(c_IngSavingTransFile);
    if (resourceUrl != null) {
      // Get the resource directory path
      String resourceDirectory = resourceUrl.getPath();
      csvFile = new File(resourceDirectory);
    }

    // Get the URL of synonym file
    resourceUrl = getClass().getClassLoader().getResource(c_SynonymFile);
    if (resourceUrl != null) {
      // Get the resource directory path
      String resourceDirectory = resourceUrl.getPath();
      synFile = new File(resourceDirectory);
    }

    IngTransactions l_trans = new IngTransactions(csvFile, synFile);
    l_trans.getIngSavingTransactions();

  }

  public void testConvertToOfx() {
    fail("Not yet implemented");
    File csvFile = null;
    File synFile = null;
    // Get the URL of csv file
    URL resourceUrl = getClass().getClassLoader().getResource(c_IngTransFile);
    if (resourceUrl != null) {
      // Get the resource directory path
      String resourceDirectory = resourceUrl.getPath();
      csvFile = new File(resourceDirectory);
    }

    // Get the URL of synonym file
    resourceUrl = getClass().getClassLoader().getResource(c_SynonymFile);
    if (resourceUrl != null) {
      // Get the resource directory path
      String resourceDirectory = resourceUrl.getPath();
      synFile = new File(resourceDirectory);
    }

    IngTransactions l_trans = new IngTransactions(csvFile, synFile);
    l_trans.getIngTransactions();

  }

}
