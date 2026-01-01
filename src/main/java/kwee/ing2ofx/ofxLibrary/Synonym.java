package kwee.ing2ofx.ofxLibrary;

// import kwee.logger.MyLogger;

public class Synonym {
  // private static final Logger LOGGER = MyLogger.getLogger();

  private int m_Sequence = -1;
  private String m_Key = "";
  private String m_Syn = "";

  public Synonym(int a_Seq, String a_Key, String a_Syn) {
    m_Sequence = a_Seq;
    m_Key = a_Key;
    m_Syn = a_Syn;
  }

  public int getSequence() {
    return m_Sequence;
  }

  public void setSequence(int m_Sequence) {
    this.m_Sequence = m_Sequence;
  }

  public String getKey() {
    return m_Key;
  }

  public void setKey(String m_Key) {
    this.m_Key = m_Key;
  }

  public String getSyn() {
    return m_Syn;
  }

  public void setSyn(String m_Syn) {
    this.m_Syn = m_Syn;
  }

}
