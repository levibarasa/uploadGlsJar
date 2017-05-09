package org.upl.conn;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import org.upl.prop.UplProp;

public class DBConnection
{
  Connection conn;
  UplProp pr;
  
  public DBConnection()
  {
    this.pr = new UplProp();
  }
  
  public Connection getDbConnection()
  {
    try
    {
       Class.forName(pr.getDBProperty().getProperty("database.driver")); 
       
   String url =pr.getDBProperty().getProperty("database.url"); 
   String userName =pr.getDBProperty().getProperty("database.user");
   String pass = pr.getDBProperty().getProperty("database.pass");
   conn = DriverManager.getConnection(url,userName,pass);
    
    }
    catch (ClassNotFoundException asd)
    {
      System.err.println(asd.getMessage());
    }
    catch (SQLException asd)
    {
      System.err.println(asd.getMessage());
    }
    return this.conn;
  }
  
  public static void closeConn(Connection con)
  {
    try
    {
      if ((con != null) && (!con.isClosed())) {
        con.close();
      }
    }
    catch (Exception d)
    {
      System.out.println(d.getMessage());
    }
  }
  
  
//   public static void main(String[] args) {
//           DBConnection   bConnection = new DBConnection();
//           if (bConnection.getDbConnection()!=null)
//           {
//            System.out.println("Connected");    
//           }
//           else
//           {
//
//           System.out.println("Fuck you"); 
//           }
//           
//    }
}
