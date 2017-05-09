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
import java.util.ArrayList;
import org.upl.conn.DBConnection;
import org.upl.prop.UplProp;

/**
 *
 * @author Levi
 */
public class Users {
        public static void uploadFiles() {
        UplProp pr = new UplProp();
    String uploadFilepath = pr.getDBProperty().getProperty("upload.user");
        String line;
        try {
            FileInputStream fs = new FileInputStream(uploadFilepath);
            BufferedReader br = new BufferedReader(new InputStreamReader(fs));
            String[] split;
            while ((line = br.readLine()) != null) {
                split = line.split("\\|"); 
                String userName = split[0].trim();
                String solId =split[1].trim();
                String roleId =split[2].trim(); 
                
                InsertRecord(userName, solId, roleId);
                
            }
            fs.close();
            br.close();
        } catch (Exception asd) {
            System.err.println(asd.getMessage());
        } 
    }
     
     private static void InsertRecord(String userName, String solId, String roleId) {
        try {
           DBConnection dbconn = new DBConnection();
           String s = "select USER_NAME from FINACLE_USERS where USER_NAME = ?";
            String s1;
            if (AdminDb.findDuplicate(s, 1, userName)) {
                s1 = "update FINACLE_USERS set USER_NAME = ?, SOL_ID = ?, ROLE_DESC= ? where USER_NAME = ?";
            } else {
                s1 = "insert into FINACLE_USERS (USER_NAME, SOL_ID, ROLE_DESC) values (?,?,?)";
            }
            Connection conn = dbconn.getDbConnection();
            PreparedStatement ps = conn.prepareStatement(s1);
            ps.setString(1, userName.trim());
            ps.setString(2, solId.trim());
            ps.setString(3, roleId.trim()); 
            ps.setString(4, userName.trim());
            int n = ps.executeUpdate();
            if (n > 0) {
                conn.setAutoCommit(false);
                conn.commit();
                conn.setAutoCommit(true);
            }
            ps.close();
            conn.close();
        } catch (SQLException ex) {
            System.out.println(ex.getMessage());
        }
    }
     
     
}
