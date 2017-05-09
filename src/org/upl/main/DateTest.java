package org.upl.main;

/**
 *
 * @author Levi
 */
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.upl.conn.DBConnection;

public class DateTest {

    public DateTest() {
        // expected input format in YYYY-MM-DD???
        String startDate = "27-MAR-2017";
        String endDate = "28-MAR-2017";
         int id =2;
        SimpleDateFormat in = new SimpleDateFormat("dd-MMM-yyyy");

        try {
            
            Date start_Date = in.parse(startDate);
            Date end_Date = in.parse(endDate);
            // output format
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd");
           
           String sDate = fmt.format(start_Date);
           String eDate = fmt.format(end_Date);
            //System.out.println(fmt.format(date));
            String query = "insert into Test (id,Date1,Date2) values(?,try_convert(date, ?, 111),try_convert(date,?, 111))";

            DBConnection dbconn = new DBConnection();
            Connection conn = dbconn.getDbConnection();
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1,id );
            ps.setString(2, sDate);
            ps.setString(3, eDate);
            int n = ps.executeUpdate();
            if (n > 0) {
                System.out.println("Successful");
                conn.setAutoCommit(false);
                conn.commit();
                conn.setAutoCommit(true);
            }
            ps.close();
            conn.close();

        } catch (ParseException|SQLException ex) {
            Logger.getLogger(DateTest.class.getName()).log(Level.SEVERE, null, ex);
        }

    }

//    public static void main(String[] args) {
//     new DateTest();
//    }
}
