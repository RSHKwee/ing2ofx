package zandbak;

import java.time.LocalDate;

public class DateComparison {
  public static void main(String[] args) {
    LocalDate date1 = LocalDate.of(2023, 5, 27);
    LocalDate date2 = LocalDate.of(2023, 5, 27);

    // Compare dates using compareTo
    int comparisonResult = date1.compareTo(date2);

    if (comparisonResult < 0) {
      System.out.println(date1 + " is before " + date2);
    } else if (comparisonResult > 0) {
      System.out.println(date1 + " is after " + date2);
    } else {
      System.out.println(date1 + " is equal to " + date2);
    }
  }
}
