package org.upl.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import org.upl.conn.DBConnection;
import org.upl.prop.UplProp;

public class UploadFilesGls
{
 
  
  public static void uploadFiles()
  {
    UplProp pr = new UplProp();
    String filepath = pr.getDBProperty().getProperty("upload.customer");
    try
    {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
      
      int count = 0;
      String line;
      while (((line = bufferedReader.readLine()) != null) && (line.trim().length() >= 100))
      {
        System.out.println("Line content is : " + line);
        String[] lines = line.split("\\|");
        String ACID = lines[0];
        String ACCT_CRNCY_CODE = lines[1];
        String ACCT_MGR_USER_ID = lines[2];
        String ACCT_NAME = lines[3];
        String ACCT_OPN_DATE = lines[4];
        String ACCT_OWNERSHIP = lines[5];
        String CLR_BAL_AMT = lines[6];
        String CUST_ID = lines[7];
        String DEL_FLG = lines[8];
        String DRWNG_POWER = lines[9];
        String ENTITY_CRE_FLG = lines[10];
        String FORACID = lines[11];
        String LAST_MODIFIED_DATE = lines[12];
        String LCHG_TIME = lines[13];
        String LCHG_USER_ID = lines[14];
        String LIEN_AMT = lines[15];
        String RCRE_TIME = lines[16];
        String RCRE_USER_ID = lines[17];
        String NCT_LIM = lines[18];
        String SCHM_CODE = lines[19];
        String SCHM_TYPE = lines[20];
        String SOL_ID = lines[21];
        InsertRecord(ACID, ACCT_CRNCY_CODE, ACCT_MGR_USER_ID, ACCT_NAME, ACCT_OPN_DATE, ACCT_OWNERSHIP, CLR_BAL_AMT, CUST_ID, DEL_FLG, DRWNG_POWER, ENTITY_CRE_FLG, FORACID, LAST_MODIFIED_DATE, LCHG_TIME, LCHG_USER_ID, LIEN_AMT, RCRE_TIME, RCRE_USER_ID, NCT_LIM, SCHM_CODE, SCHM_TYPE, SOL_ID);
        count += 1;
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
  
  private static void InsertRecord(String ACID, String ACCT_CRNCY_CODE, String ACCT_MGR_USER_ID, String ACCT_NAME, String ACCT_OPN_DATE, String ACCT_OWNERSHIP, String CLR_BAL_AMT, String CUST_ID, String DEL_FLG, String DRWNG_POWER, String ENTITY_CRE_FLG, String FORACID, String LAST_MODIFIED_DATE, String LCHG_TIME, String LCHG_USER_ID, String LIEN_AMT, String RCRE_TIME, String RCRE_USER_ID, String NCT_LIM, String SCHM_CODE, String SCHM_TYPE, String SOL_ID)
  {
    try
    {
      String s = "select acid from GENERAL_ACCT_MAST_TABLE where acid = ?";
      String s1; 
      if (AdminDb.findDuplicate(s, 1, ACID.trim()))
      {
        System.out.println("Record alredy found ... ");
        s1 = "update GENERAL_ACCT_MAST_TABLE set ACCT_CRNCY_CODE=?,ACCT_MGR_USER_ID=?,ACCT_NAME=?,ACCT_OPN_DATE =try_convert(date, ?, 111),ACCT_OWNERSHIP =?,CLR_BAL_AMT=?,CUST_ID=?,DEL_FLG=?,DRWNG_POWER=?,ENTITY_CRE_FLG=?,FORACID=?,LAST_MODIFIED_DATE=try_convert(date, ?, 111), LCHG_TIME=try_convert(date, ?, 111),LCHG_USER_ID=?,LIEN_AMT=?,RCRE_TIME=try_convert(date, ?, 111),RCRE_USER_ID=?,SANCT_LIM=?,SCHM_CODE=?,SCHM_TYPE=?,SOL_ID=? where ACID =?";
      }
      else
      {
        System.out.println("Fresh insert ... ");
        s1 = "insert into GENERAL_ACCT_MAST_TABLE (ACCT_CRNCY_CODE,ACCT_MGR_USER_ID,ACCT_NAME,ACCT_OPN_DATE,ACCT_OWNERSHIP,CLR_BAL_AMT,CUST_ID,DEL_FLG,DRWNG_POWER,ENTITY_CRE_FLG,FORACID,LAST_MODIFIED_DATE, LCHG_TIME,LCHG_USER_ID,LIEN_AMT,RCRE_TIME,RCRE_USER_ID,SANCT_LIM,SCHM_CODE,SCHM_TYPE ,SOL_ID,ACID) VALUES (?, ?, ?,try_convert(date, ?, 111), ?, ?, ?, ?, ?, ?, ?, try_convert(date, ?, 111), try_convert(date, ?, 111), ?,?,try_convert(date, ?, 111),?,?,?,?,?,?)";
      }
      DBConnection dbconn = new DBConnection();
      Connection conn = dbconn.getDbConnection();
      PreparedStatement ps = conn.prepareStatement(s1);
      ps.setString(1, ACCT_CRNCY_CODE.trim());
      ps.setString(2, ACCT_MGR_USER_ID.trim());
      ps.setString(3, ACCT_NAME.trim());
      ps.setString(4, ACCT_OPN_DATE.trim());
      ps.setString(5, ACCT_OWNERSHIP.trim());
      ps.setBigDecimal(6, parseAmounts(CLR_BAL_AMT.trim()));
      ps.setString(7, CUST_ID.trim());
      ps.setString(8, DEL_FLG.trim());
      ps.setBigDecimal(9, parseAmounts(DRWNG_POWER.trim()));
      ps.setString(10, ENTITY_CRE_FLG.trim());
      ps.setString(11, FORACID.trim());
      ps.setString(12, LAST_MODIFIED_DATE.trim());
      ps.setString(13, LCHG_TIME.trim());
      ps.setString(14, LCHG_USER_ID.trim());
      ps.setBigDecimal(15, parseAmounts(LIEN_AMT.trim()));
      ps.setString(16, RCRE_TIME.trim());
      ps.setString(17, RCRE_USER_ID.trim());
      ps.setBigDecimal(18, parseAmounts(NCT_LIM.trim()));
      ps.setString(19, SCHM_CODE.trim());
      ps.setString(20, SCHM_TYPE.trim());
      ps.setString(21, SOL_ID.trim());
      ps.setString(22, ACID.trim());
      int n = ps.executeUpdate();
      if (n > 0)
      {
        conn.setAutoCommit(false);
        conn.commit();
        conn.setAutoCommit(true);
      }
      ps.close();
      conn.close();
    }
    catch (SQLException ex)
    {
      System.out.println(ex.getMessage());
    }
  }
  
  public static BigDecimal parseAmounts(String in)
  {
    String inp = in.replaceAll("[^\\d.]", "");
    try
    {
      BigDecimal Amt;
      if (in.substring(0, 1).equalsIgnoreCase("-")) {
        Amt = new BigDecimal(inp).negate();
      }
      return new BigDecimal(inp);
    }
    catch (Exception e)
    {
      System.out.println("big decimal error " + e.getMessage());
    }
    return BigDecimal.ZERO;
  }
}
