/**
 * 
 */
package kwee.convertor.ing.convertor;

import java.io.File;
import java.net.URL;
import java.util.List;

import junit.framework.TestCase;
import kwee.convertor.ing.ingLibrary.IngSavingTransaction;
import kwee.convertor.ing.ingLibrary.IngTransaction;

/**
 * @author René
 *
 */
public class IngTransactionsTest extends TestCase {
  private String c_IngTransFile = "Alle_rekeningen.csv";
  private String c_IngSavingTransFile = "Alle_spaarrekeningen.csv";
  private String c_SynonymFile = "Synoniem.txt";

  private File m_IngFile;
  private File m_IngSavingFile;
  private File m_SynonymFile;

  /**
   * @throws java.lang.Exception
   */

  @Override
  public void setUp() throws Exception {
    // Get the URL of synonym file
    URL resourceUrl = getClass().getClassLoader().getResource(c_SynonymFile);
    if (resourceUrl != null) {
      // Get the resource directory path
      String resourceDirectory = resourceUrl.getPath();
      m_SynonymFile = new File(resourceDirectory);
    }

    // Get the URL of csv ING Savings file
    resourceUrl = getClass().getClassLoader().getResource(c_IngSavingTransFile);
    if (resourceUrl != null) {
      // Get the resource directory path
      String resourceDirectory = resourceUrl.getPath();
      m_IngSavingFile = new File(resourceDirectory);
    }

    // Get the URL of csv ING file
    resourceUrl = getClass().getClassLoader().getResource(c_IngTransFile);
    if (resourceUrl != null) {
      // Get the resource directory path
      String resourceDirectory = resourceUrl.getPath();
      m_IngFile = new File(resourceDirectory);
    }
  }

  /**
   * @throws java.lang.Exception
   */

  @Override
  public void tearDown() throws Exception {
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#IngTransactions(java.io.File, java.io.File)}.
   */

  public void testIngTransactions() {
    IngTransactions l_ingSavingtrans = new IngTransactions(m_IngSavingFile, m_SynonymFile);
    assertNotNull(l_ingSavingtrans);

    IngTransactions l_ingtrans = new IngTransactions(m_IngFile, m_SynonymFile);
    assertNotNull(l_ingtrans);
  }

  /**
   * Test method for {@link kwee.convertor.ing.convertor.IngTransactions#load()}.
   */
  public void testLoad() {
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile, m_SynonymFile);
    l_transSaving.load();
    l_transSaving.getIngSavingTransactions();

    IngTransactions l_trans = new IngTransactions(m_IngFile, m_SynonymFile);
    l_trans.load();
    l_trans.getIngTransactions();
    // fail("Not yet implemented");

  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#isSavingCsvFile()}.
   */
  public void testIsSavingCsvFile() {
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile, m_SynonymFile);
    l_transSaving.load();
    l_transSaving.getIngSavingTransactions();
    assertEquals(l_transSaving.isSavingCsvFile(), true);

    IngTransactions l_trans = new IngTransactions(m_IngFile, m_SynonymFile);
    l_trans.load();
    l_trans.getIngTransactions();
    assertEquals(l_trans.isSavingCsvFile(), false);
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getIngTransactions()}.
   */
  public void testGetIngTransactions() {
    fail("Not yet implemented");

    IngTransactions l_trans = new IngTransactions(m_IngFile, m_SynonymFile);
    l_trans.load();
    List<IngTransaction> l_Transactions = l_trans.getIngTransactions();

    List<IngSavingTransaction> l_SavingTransactions = l_trans.getIngSavingTransactions();

  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getIngSavingTransactions()}.
   */
  public void testGetIngSavingTransactions() {
    fail("Not yet implemented");
    IngTransactions l_transSaving = new IngTransactions(m_IngSavingFile, m_SynonymFile);
    l_transSaving.load();
    List<IngSavingTransaction> l_SavingTransactions = l_transSaving.getIngSavingTransactions();

    List<IngTransaction> l_Transactions = l_transSaving.getIngTransactions();

  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getOfxTransactions()}.
   */
  public void testGetOfxTransactions() {
    fail("Not yet implemented");

  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getOfxMetaInfo()}.
   */
  public void testGetOfxMetaInfo() {
    fail("Not yet implemented");
  }

  /**
   * Test method for
   * {@link kwee.convertor.ing.convertor.IngTransactions#getUniqueIds()}.
   */
  public void testGetUniqueIds() {
    fail("Not yet implemented");
  }

}
