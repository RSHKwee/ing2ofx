package kwee.ofxLibrary;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.stream.Stream;

/**
 * Synonyms
 * 
 * @author René
 *
 */
public class Synonyms {
  private static final Logger LOGGER = Logger.getLogger(Class.class.getName());
  private Map<String, Synonym> m_Synonyms = new HashMap<String, Synonym>();
  private Map<String, ArrayList<String>> m_Prefixs = new HashMap<String, ArrayList<String>>();

  /**
   * 
   */
  public Synonyms() {
  }

  /**
   * 
   * 
   * @param a_SynonymsFile Path to Synonym file
   */
  public Synonyms(File a_SynonymsFile) {
    String l_SynonymsFile = a_SynonymsFile.getPath();
    LOGGER.log(Level.FINE, "File read:" + l_SynonymsFile);

    // read file into stream, try-with-resources
    try (Stream<String> stream = Files.lines(Paths.get(l_SynonymsFile))) {
      stream.forEach(l_line -> {
        String[] l_elems = l_line.split(";");
        if (l_elems.length >= 3) {
          int lseq = -1;
          try {
            lseq = Integer.parseInt(l_elems[0].strip());
          } catch (NumberFormatException ex) {
            // ex.printStackTrace();
          }

          String l_key = l_elems[1].strip(); // Account
          String l_syn = l_elems[2].strip(); // Prefix
          Synonym l_synonym = new Synonym(lseq, l_key, l_syn);
          m_Synonyms.put(l_key, l_synonym);

          ArrayList<String> l_accs = m_Prefixs.get(l_syn);
          if (l_accs != null) {
            l_accs.add(l_key);
            m_Prefixs.put(l_syn, l_accs);
          } else {
            l_accs = new ArrayList<String>();
            l_accs.add(l_key);
            m_Prefixs.put(l_syn, l_accs);
          }
        }
      });
    } catch (IOException e) {
      LOGGER.log(Level.INFO, "No synonym file");
      // LOGGER.log(Level.SEVERE, Class.class.getName() + ": " + e.getMessage());
    }
  }

  public ArrayList<String> getAccountsByPrefix(String a_Prefix) {
    return m_Prefixs.get(a_Prefix);
  }

  public Set<String> getPrefixes() {
    return m_Prefixs.keySet();
  }

  /**
   * 
   * @param a_key
   * @return Synonym
   */
  public String getSynonym(String a_key) {
    String l_Syn = "";
    if (m_Synonyms.containsKey(a_key)) {
      Synonym l_Synonym = m_Synonyms.get(a_key);
      l_Syn = l_Synonym.getSyn();
    }
    return l_Syn;
  }

  /**
   * Get sequence for Account (key)
   * 
   * @param a_key Account
   * @return Sequencenr. account
   */
  public int getSequence(String a_key) {
    int l_Seq = -1;
    if (m_Synonyms.containsKey(a_key)) {
      Synonym l_Synonym = m_Synonyms.get(a_key);
      l_Seq = l_Synonym.getSequence();
    }
    return l_Seq;
  }

}
