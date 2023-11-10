package zandbak;

import java.text.SimpleDateFormat;
import java.util.Date;

public class DateConversion {
    public static void main(String[] args) {
        String dateString = "26-5-2023"; // Replace this with your input date string
        SimpleDateFormat inputFormat = new SimpleDateFormat("dd-MM-yyyy");

        try {
            Date date = inputFormat.parse(dateString);
            System.out.println("Parsed date: " + date);
        } catch (java.text.ParseException e) {
            System.out.println("Invalid date format");
        }
    }
}
