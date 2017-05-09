package org.upl.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.upl.conn.DBConnection;
import org.upl.prop.UplProp;

public class DemandGeneration {

    public static void uploadFiles() {
        UplProp pr = new UplProp();
        String filepath = pr.getDBProperty().getProperty("upload.demands");
        try {
            BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
            String line;
            while (((line = bufferedReader.readLine()) != null) && (line.trim().length() >= 10)) {
                System.out.println("Line content is : " + line);
                String[] lines = line.split("\\|");
                String ACID = lines[2];
                String Disb_date = lines[1];
                String Disb_amt = lines[0];

                InsertRecord(ACID, Disb_date, Disb_amt);
            }
            bufferedReader.close();
        } catch (IOException e) {
            System.out.print("The Exception reported is : " + e);
        } catch (NumberFormatException e) {
            System.out.print("The Exception reported is : " + e);
        }
    }

    private static void InsertRecord(String ACID, String dis_date, String dis_amt) {
        SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy");
        try {
            // output format
            SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
            DBConnection dbconn = new DBConnection();
           // String s = "select acid from loan_demands_table where acid = ?";
            String s1;
//            if (AdminDb.findDuplicate(s, 1, ACID)) {
//                System.out.println("Record alredy found ... ");
//                s1 = "update loan_demands_table set dmd_amt = ?, dmd_date = try_convert(date, ?, 111),status='N' where acid = ?";
//            } else {
                System.out.println("Fresh insert ... ");
                s1 = "insert into LOAN_DEMANDS_TABLE (DMD_AMT, DMD_DATE, ACID,STATUS) values (?,try_convert(date, ?, 111),?,'N')";
           // }
            Connection conn = dbconn.getDbConnection();
            PreparedStatement ps = conn.prepareStatement(s1);
            ps.setBigDecimal(1, UploadFilesGls.parseAmounts(dis_amt.trim()));
            ps.setString(2,   fmt.format(in.parse(dis_date.trim())));
            ps.setString(3, ACID.trim());
            int n = ps.executeUpdate();
            if (n > 0) {
                conn.setAutoCommit(false);
                conn.commit();
                conn.setAutoCommit(true);
            }
            ps.close();
            conn.close();
        } catch (SQLException|ParseException  ex) {
            System.out.println(ex.getMessage());
        }
    }

      
//   public static void main(String[] args) {
//      uploadFiles();
//
//        ArrayList all = getLoanDemandsList();
//        for (int i = 0; i < all.size(); i++) {
//            ArrayList one = (ArrayList) all.get(i);
//
////            String ACID = (String) one.get(2);
////            String Disb_date = (String) one.get(1);
////            String Disb_amt = (String) one.get(0);
//            System.out.println((String) one.get(0) + " " + (String) one.get(1) + " " + (String) one.get(2));
//        }
 //}
}
