package te;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class JdbcTest {
  
  public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException {
    JdbcTest jt = new JdbcTest();
    jt.testOneps();
    // jt.testMysql();
  }
  
  public void testSqlServer() throws SQLException {
    try {
      // Class.forName("com.microsoft.sqlserver.jdbc.SQLServerDriver");
      Class.forName("net.sourceforge.jtds.jdbc.Driver");
    }
    catch (ClassNotFoundException e) {
      System.err.println("创建classDriver失败");
      e.printStackTrace();
    }
    // String url = "jdbc:sqlserver://124.251.46.179:1433;databaseName=Chat_3g_test";
    String url = "jdbc:jtds:sqlserver://192.168.11.108:1433;DatabaseName=Esf_eb_user_chushou_test";
    String userName = "esf_eb_test_admin";
    String pwd = "ae9846F3";
    Connection con = null;
    try {
      con = DriverManager.getConnection(url, userName, pwd);// sql server的连接
      con.setAutoCommit(false);
    }
    catch (SQLException e) {
      System.err.println("创建Connection失败");
      e.printStackTrace();
    }
    String sql = "select * from EB_Data_Index_Sales_Day where searchDate='2016-08-03'";
    PreparedStatement ps = con.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    List<HashMap<String, String>> list = new ArrayList<>();
    while (rs.next()) {
      
    }
  }
  
  public void testMysql() throws ClassNotFoundException, SQLException, NumberFormatException, IOException {
    Class.forName("com.mysql.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://192.168.8.96/ebcenter_test?useUnicode=true&amp;characterEncoding=utf-8", "ebcen_test_admin", "aEF3t6wH");
    con.setReadOnly(true);
    String sql = "update Test set money =100 where id=1";
    PreparedStatement ps = con.prepareStatement(sql);
    int count = ps.executeUpdate();
    System.out.println("count:" + count);
  }
  
  public void testOneps() throws ClassNotFoundException, SQLException {
    Class.forName("com.mysql.jdbc.Driver");
    Connection con = DriverManager.getConnection("jdbc:mysql://localhost/test?useUnicode=true&amp;characterEncoding=utf-8", "root", "root");
    con.setAutoCommit(false);
    String deletesql = "delete from num where id=1";
    PreparedStatement ps = con.prepareStatement(deletesql);
    int count = ps.executeUpdate();
    con.commit();
    System.out.println("delete count:" + count);
    String insertsql = "insert into num(id,num) values(1,2)";
    ps = con.prepareStatement(insertsql);
    count = ps.executeUpdate();
    System.out.println("count insert:"+count);
    
  }
}
