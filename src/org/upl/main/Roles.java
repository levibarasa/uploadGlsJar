/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.upl.main;

import java.io.BufferedReader;
import java.io.FileInputStream;
import java.io.InputStreamReader;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import org.upl.conn.DBConnection;
import org.upl.prop.UplProp;

/**
 *
 * @author Levi
 */
public class Roles {

    public static void uploadFiles() {
        UplProp pr = new UplProp();
        String uploadFilepath = pr.getDBProperty().getProperty("upload.role");
        String line;
        try {
            FileInputStream fs = new FileInputStream(uploadFilepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String[] split;
            while ((line = br.readLine()) != null) {
                split = line.split("\\|");
                String ROLE_DESC = split[0];
                String ROLE_DESCRIPTION = split[1];
                String ENTITY_CRE_FLG = split[2];
                String DEL_FLG = split[3];
                String LCHG_USER_ID = split[4];
                String LCHG_TIME = split[5];
                String RCRE_USER_ID = split[6];
                String RCRE_TIME = split[7];
                String TS_CNT = "01";//split[8];
         
                 
           insertRecord(ROLE_DESC,  ROLE_DESCRIPTION,  ENTITY_CRE_FLG,  DEL_FLG,  LCHG_USER_ID , LCHG_TIME, RCRE_USER_ID,  RCRE_TIME,  TS_CNT);   
            }
            fs.close();
            br.close();
        } catch (Exception asd) {
            System.err.println(asd.getMessage());
        }
    }

    private static void insertRecord(String ROLE_DESC, String ROLE_DESCRIPTION,String  ENTITY_CRE_FLG,String  DEL_FLG,String  LCHG_USER_ID ,String LCHG_TIME,String RCRE_USER_ID,String  RCRE_TIME, String TS_CNT)
  {
      
      SimpleDateFormat in = new SimpleDateFormat("dd-MM-yyyy");
    try
    {
       // output format
      SimpleDateFormat fmt = new SimpleDateFormat("yyyy-MM-dd"); 
      DBConnection dbconn = new DBConnection();
          
String s = "select ROLE_DESC from ROLE_PROFILE_TABLE where ROLE_DESC = ?";
      String s1; 
      if (AdminDb.findDuplicate(s, 1, ROLE_DESC)) {
        s1 = "update ROLE_PROFILE_TABLE set ROLE_DESC=?,ROLE_DESCRIPTION=?"
                + ",ENTITY_CRE_FLG=?,DEL_FLG=?,LCHG_USER_ID=?,LCHG_TIME=try_convert(date, ?, 111),"
                + "RCRE_USER_ID=?,RCRE_TIME=try_convert(date, ?, 111),BANK_ID=? where ROLE_DESC = ?";
      } else {
        s1 = " insert into ROLE_PROFILE_TABLE(ROLE_DESC,ROLE_DESCRIPTION,ENTITY_CRE_FLG,DEL_FLG,"
                + "LCHG_USER_ID,LCHG_TIME,RCRE_USER_ID,RCRE_TIME,BANK_ID) "
                + "values (?,?,?,?,?,try_convert(date, ?, 111),?,try_convert(date, ?, 111),?)";
      }
      Connection conn = dbconn.getDbConnection();
      PreparedStatement ps = conn.prepareStatement(s1);
      ps.setString(1, ROLE_DESC.trim());
      ps.setString(2, ROLE_DESCRIPTION.trim());
      ps.setString(3, ENTITY_CRE_FLG.trim());
      ps.setString(4, DEL_FLG.trim());
      ps.setString(5, LCHG_USER_ID.trim());
      ps.setString(6, fmt.format(in.parse(LCHG_TIME.trim())));
      ps.setString(7, RCRE_USER_ID.trim());
      ps.setString(8, fmt.format(in.parse(RCRE_TIME.trim())));
      ps.setString(9, TS_CNT.trim());
      ps.setString(10, ROLE_DESC.trim());
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
