package org.upl.main;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import org.upl.conn.DBConnection;

public class AdminDb
{
  public static int dbWork(String sql, int params, String args)
  {
    DBConnection dbconn = new DBConnection();
    try
    {
      Connection conn = dbconn.getDbConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      if (params > 0)
      {
        System.out.println("Input values " + args);
        String[] vals = args.split("\\s*,\\s*");
        int l = vals.length;
        System.out.println("No of elements in array " + l);
        int g = 0;
        while (g < params)
        {
          ps.setString(g + 1, vals[g]);
          g++;
        }
      }
      int n = ps.executeUpdate();
      if (n > 0)
      {
        conn.setAutoCommit(false);
        conn.commit();
        conn.setAutoCommit(true);
        return 1;
      }
      DBConnection.closeConn(conn);
    }
    catch (SQLException ex)
    {
      System.out.println(ex.getMessage());
    }
    return 0;
  }
  
  public static boolean findDuplicate(String sql, int params, String args)
  {
    DBConnection dbconn = new DBConnection();
    try
    {
      Connection conn = dbconn.getDbConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      if (params > 0)
      {
        String[] vals = args.split("\\s*,\\s*");
        int g = 0;
        while (g < params)
        {
          ps.setString(g + 1, vals[g]);
          g++;
        }
      }
      ResultSet r = ps.executeQuery();
      if (r.next()) {
        return true;
      }
      DBConnection.closeConn(conn);
    }
    catch (SQLException ex)
    {
      System.out.println(ex.getMessage());
      return false;
    }
    return false;
  }
  
  public static String getValue(String sql, int w, int params, String args)
  {
    String k = "";
    DBConnection dbconn = new DBConnection();
    try
    {
      Connection conn = dbconn.getDbConnection();
      PreparedStatement ps = conn.prepareStatement(sql);
      if (params > 0)
      {
        String[] vals = args.split("\\s*,\\s*");
        int g = 0;
        while (g < params)
        {
          ps.setString(g + 1, vals[g]);
          g++;
        }
      }
      ResultSet r = ps.executeQuery();
      while (r.next()) {
        if (w == 1)
        {
          k = r.getString(1);
        }
        else
        {
          String b = r.getString(1);
          for (int q = 2; q <= w;)
          {
            b = b + "," + r.getString(q);
            q++;
          }
          k = b;
        }
      }
      DBConnection.closeConn(conn);
    }
    catch (SQLException ex)
    {
      System.out.println(ex.getMessage());
    }
    return k;
  }
}
