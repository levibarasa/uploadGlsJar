package org.upl.main;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import org.upl.conn.DBConnection;
import org.upl.prop.UplProp;

public class Loans
{
  public static void uploadFiles()
  {
    UplProp pr = new UplProp();
    String filepath = pr.getDBProperty().getProperty("upload.loan");
    try
    {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
      String line;
      while (((line = bufferedReader.readLine()) != null) && (line.trim().length() >= 10))
      {
        System.out.println("Line content is : " + line);
        String[] lines = line.split("\\|");
        String ACID = lines[5];
        String Disb_date = lines[1];
        String Disb_amt = lines[0];
        String rephase_amt = lines[2];
        String advan_ins = lines[3];
        String upfront_amt = lines[4];
        InsertRecord(ACID, Disb_date, Disb_amt, rephase_amt, advan_ins, upfront_amt);
      }
      bufferedReader.close();
    }
    catch (IOException e)
    {
      System.out.print("The Exception reported is : " + e);
    }
    catch (NumberFormatException e)
    {
      System.out.print("The Exception reported is : " + e);
    }
  }
  
  private static void InsertRecord(String ACID, String dis_date, String dis_amt, String rephase_amt, String advan_ins, String upfront_amt)
  {
      SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy");
    try
    {
      DBConnection dbconn = new DBConnection();
      String s = "select acid from LOAN_DETAILS_TABLE where acid = ?";
      String s1; 
      if (AdminDb.findDuplicate(s, 1, ACID)) {
        s1 = "update LOAN_DETAILS_TABLE set DIS_AMT= ?, DIS_SHDL_DATE=try_convert(date, ?, 111),"
                + " REPHASEMENT_PRINCIPAL=?, NUM_ADVANCE_INSTLMNT=?, UPFRONT_INSTL_AMT=? where acid = ?";
      } else {
        s1 = " insert into LOAN_DETAILS_TABLE(DIS_AMT, DIS_SHDL_DATE, REPHASEMENT_PRINCIPAL,"
                + " NUM_ADVANCE_INSTLMNT, UPFRONT_INSTL_AMT, ACID)"
                + " values (?,try_convert(date, ?, 111),?,?,?,?)";
      }
      
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
      Connection conn = dbconn.getDbConnection();
      PreparedStatement ps = conn.prepareStatement(s1);
      ps.setBigDecimal(1, UploadFilesGls.parseAmounts(dis_amt.trim()));
      ps.setString(2,   fmt.format(in.parse(dis_date.trim())));
      ps.setBigDecimal(3, UploadFilesGls.parseAmounts(rephase_amt.trim()));
      ps.setString(4, advan_ins);
      ps.setBigDecimal(5, UploadFilesGls.parseAmounts(upfront_amt.trim()));
      ps.setString(6, ACID.trim());
      int n = ps.executeUpdate();
      if (n > 0)
      {
        conn.setAutoCommit(false);
        conn.commit();
        conn.setAutoCommit(true);
      }
      DBConnection.closeConn(conn);
    }
    catch (SQLException|ParseException  ex)
    {
      System.out.println(ex.getMessage());
    }
  }
  
  
//    public static void main(String[] args) {
//     uploadFiles();   
//    }
  
}
