/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upl.main;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import org.upl.conn.DBConnection;
import org.upl.prop.UplProp;

/**
 *
 * @author Levi
 */
public class Branches {
      
     public static void uploadFiles()
  {
    UplProp pr = new UplProp();
    String filepath = pr.getDBProperty().getProperty("upload.outlet");
     SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy");
    try
    {
      BufferedReader bufferedReader = new BufferedReader(new FileReader(filepath));
      String line;
      Date date =new Date();
      while (((line = bufferedReader.readLine()) != null) && (line.trim().length() >= 10))
      {
        System.out.println("Line content is : " + line);
        String[] lines = line.split("\\|");
         String solId = lines[0];
        String addr1 = lines[13];
        String bankId = lines[4];
        String delFlg = lines[1];
        String homeCurCode = "USH";// lines[64];
        String lchgTime = in.format(date);//lines[36];
        String lchgUserId = "MARK";// lines[34];
        String recreTime = in.format(date);//lines[37];
        String rcreUserId =  "MARK";//lines[35];
        String solDesc = lines[2];
        String solEodStatus = "N";//lines[88];
         
        InsertRecord(solId,  delFlg,  solDesc,  bankId,  addr1 , lchgUserId, rcreUserId,  lchgTime,  recreTime, homeCurCode,  solEodStatus);
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
  
    private static void InsertRecord(String solId,String delFlg,String solDesc,String bankId,String addr1,String lchgUserId,String rcreUserId,String lchgTime,String recreTime,String homeCurCode,String solEodStatus)
  {
      
      SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy");
    try
    {
       // output format
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
      DBConnection dbconn = new DBConnection();
      String s = "select sol_id from service_outlet_table where sol_id = ?";
      String s1; 
      if (AdminDb.findDuplicate(s, 1, solId)) {
        s1 = "update SERVICE_OUTLET_TABLE set ADDR_1=?,BANK_ID=?,DEL_FLG=?,HOME_CRNCY_CODE=?,LCHG_TIME=? ,LCHG_USER_ID=? ,RCRE_TIME=?,RCRE_USER_ID=?, SOL_DESC=?,SOL_EOD_STATUS=? where sol_id = ?";
      } else {
        s1 = " insert into SERVICE_OUTLET_TABLE(ADDR_1,BANK_ID, DEL_FLG,HOME_CRNCY_CODE,LCHG_TIME ,LCHG_USER_ID ,RCRE_TIME,RCRE_USER_ID, SOL_DESC,SOL_EOD_STATUS) values (?,?,?,?,try_convert(date, ?, 111),?,try_convert(date, ?, 111),?,?,?)";
      }
      Connection conn = dbconn.getDbConnection();
      PreparedStatement ps = conn.prepareStatement(s1);
      ps.setString(1, addr1.trim());
      ps.setString(2, bankId.trim());
      ps.setString(3, delFlg.trim());
      ps.setString(4, homeCurCode.trim());
      ps.setString(5, fmt.format(in.parse(lchgTime.trim())));
      ps.setString(6, rcreUserId.trim());
      ps.setString(7, fmt.format(in.parse(recreTime.trim())));
      ps.setString(8, rcreUserId.trim());
      ps.setString(9, solDesc.trim());
      ps.setString(10, solEodStatus.trim());
      
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
        System.err.println(ex.getMessage());
    }
  }
   
}
