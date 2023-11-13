/*
 * Copyright 2008 Web Cohesion
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package kwee.ofxLibrary.doc;

import com.webcohesion.ofx4j.OFXSettings;
import com.webcohesion.ofx4j.io.v2.OFXV2Writer;

import java.io.*;
import java.time.LocalDate;
import java.time.LocalTime;

/**
 * OFX writer to XML, suitable for OFX version 2.0.
 *
 * @author Ryan Heaton
 */
public class Ing2OFXV2Writer extends OFXV2Writer {
  String m_Filename = "";

  public Ing2OFXV2Writer(OutputStream out) {
    super(out);
  }

  public Ing2OFXV2Writer(Writer writer) {
    super(writer);
  }

  public void setOutputFilename(String a_OutputFilename) {
    m_Filename = a_OutputFilename;
  }

  @Override
  public void close() throws IOException {
    LocalDate today = LocalDate.now();
    LocalTime time = LocalTime.now();
    print("\r\n" + " <!-- " + m_Filename + " -->" + "\r\n");
    print(" <!-- Generated on " + today.toString() + " " + time.toString() + " -->" + "\r\n");
    flush();
    this.writer.close();
  }
}
