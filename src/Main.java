import java.io.FileReader;
import  java.sql.*;
import com.opencsv.CSVReader;
import oracle.jdbc.driver.DBConversion;

public class Main {
    public static void main(String[] args)
    {
        CSVConv.insertDataToStudentInfoAndCar_color();
        CSVConv.addToFriendsTable();
        CSVConv.makeReport();
    }
}