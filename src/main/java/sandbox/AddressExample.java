package sandbox;

import com.opencsv.CSVReader;
import com.opencsv.CSVParserBuilder;
import com.opencsv.CSVReaderBuilder;
import com.opencsv.CSVWriter;
import com.opencsv.bean.CsvToBeanBuilder;
import com.opencsv.bean.HeaderColumnNameMappingStrategy;

import ing2ofx.convertor.IngTransactions;
import ing2ofx.convertor.OfxTransaction;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.io.StringWriter;
import java.util.List;

/**
 * Copyright 2005 Bytecode Pty Ltd.
 * <p/>
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * <p/>
 * http://www.apache.org/licenses/LICENSE-2.0
 * <p/>
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
public class AddressExample {

  private static final String ADDRESS_FILE1 = "f:/data/Alle_rekeningen_01-01-2021_28-02-2021.csv";
  private static final String ADDRESS_FILE = "f:/data/Alle_spaarrekeningen_31-12-2020_27-11-2021.csv";
  private static final String ADDRESS_FILE2 = "E:\\OneDrive\\Documenten\\misc\\GnuCash\\ing\\Alle_rekeningen_01-01-2020_22-12-2020PuntKomma.csv";

  public static void main(String[] args) throws IOException {

    IngTransactions l_ingtrns = new IngTransactions(new File(ADDRESS_FILE2));
    List<OfxTransaction> l_ofxtrns = l_ingtrns.getOfxTransactions();
    /*
     * CSVReader reader5 = new CSVReader(new FileReader(ADDRESS_FILE), ';');
     * CSVReader reader1 = new CSVReader(new FileReader(ADDRESS_FILE1), ';');
     * CSVReader reader2 = new CSVReader(new FileReader(ADDRESS_FILE1), ',');
     * 
     * CSVReader reader = new CSVReaderBuilder(new FileReader(ADDRESS_FILE1))
     * .withCSVParser(new CSVParserBuilder().withSeparator(';').build()).build();
     * 
     * String[] nextLine; String[] nextLine1; String[] nextLine2; nextLine =
     * reader.readNext(); nextLine1 = reader1.readNext(); nextLine2 =
     * reader2.readNext();
     * 
     * HeaderColumnNameMappingStrategy<Transaction> beanStrategy = new
     * HeaderColumnNameMappingStrategy<Transaction>();
     * beanStrategy.setType(Transaction.class); List<Transaction> beans = new
     * CsvToBeanBuilder<Transaction>(new
     * FileReader(ADDRESS_FILE)).withSeparator(';')
     * .withMappingStrategy(beanStrategy).build().parse();
     * 
     * List<String[]> transactions = reader.readAll();
     * 
     * // String[] nextLine; while ((nextLine = reader.readNext()) != null) {
     * System.out.println("Name: [" + nextLine[0] + "]\nAddress: [" + nextLine[1] +
     * "]\nEmail: [" + nextLine[2] + "]"); } reader.close();
     */
    // Try writing it back out as CSV to the console
    CSVReader reader2 = new CSVReader(new FileReader(ADDRESS_FILE2));
    List<String[]> allElements = reader2.readAll();
    StringWriter sw = new StringWriter();
    CSVWriter writer = new CSVWriter(sw);
    writer.writeAll(allElements);

    System.out.println("\n\nGenerated CSV File:\n\n");
    System.out.println(sw.toString());

  }
}