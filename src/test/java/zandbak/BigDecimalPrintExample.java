package zandbak;

import java.math.BigDecimal;
import java.text.DecimalFormat;

public class BigDecimalPrintExample {
  public static void main(String[] args) {
    BigDecimal positiveBigDecimal = new BigDecimal("123.456789");
    BigDecimal negativeBigDecimal = new BigDecimal("-987.654321");

    // Using DecimalFormat for custom formatting
    DecimalFormat decimalFormat = new DecimalFormat("0.00");

    String formattedPositive = decimalFormat.format(positiveBigDecimal);
    String formattedNegative = decimalFormat.format(negativeBigDecimal);

    System.out.println("Positive number: " + formattedPositive);
    System.out.println("Negative number: " + formattedNegative);
  }
}
