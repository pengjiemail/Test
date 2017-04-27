/**
 * File：HouseDimensionDx.java
 * Package：com.fang.ebcenter
 * Author：pengjie
 * Date：2016-12-5 下午1:23:01
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package te;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 * 说明
 * 
 * @author pengjie
 */
public class VerticaTest {
  
  public static void main(String[] args) {
    for (int i = 0; i <20; i++) {
      new Thread(new Runnable() {
        
        @Override
        public void run() {
          try {
            Class.forName("com.vertica.jdbc.Driver");
          }
          catch (ClassNotFoundException e) {
            System.err.println("main sql driver error");
            e.printStackTrace();
          }
          Connection con = null;
          final String verticaUrl = "jdbc:vertica://192.168.13.62:5433/db_vertica_test";
          final String verticaUserName = "vdbadmin";
          final String verticaPassword = "soufun.com";
          try {
            con = DriverManager.getConnection(verticaUrl, verticaUserName, verticaPassword);
          }
          catch (SQLException e) {
            System.err.println("main thread connection create error");
            e.printStackTrace();
          }
          
          PreparedStatement ps = null;
          ResultSet rs = null;
          String sql = "SELECT COUNT(DISTINCT HouseID) AS valName FROM ebc_esf_housedimension "
              + "WHERE '20161122'::Integer <= KeyTimeStart AND KeyTimeStart <= '20161207'::Integer AND KeyUserID > 0 AND KeyUserID = '5083302'::Integer;";
          try {
            ps = con.prepareStatement(sql);
            rs = ps.executeQuery();
            if(rs.next()){
              System.out.println(Thread.currentThread().getId()+"--valName:"+rs.getLong(1));
            }
            try {
              Thread.sleep(5000);
            }
            catch (InterruptedException e1) {
              e1.printStackTrace();
            }
          }
          catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }).start();
    }
  }
  
}
