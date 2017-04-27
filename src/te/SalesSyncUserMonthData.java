package te;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.UUID;

public class SalesSyncUserMonthData {
  
  public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException {
    SalesSyncUserMonthData jt = new SalesSyncUserMonthData();
    jt.testSqlServer();
  }
  
  public void testSqlServer() throws SQLException, ParseException {
    try {
      Class.forName("net.sourceforge.jtds.jdbc.Driver");
    }
    catch (ClassNotFoundException e) {
      System.err.println("创建classDriver失败");
      e.printStackTrace();
    }
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
    String sql = "select * from EB_Data_Index_Sales_Day where searchDate='2016-06-01'";
    PreparedStatement ps = con.prepareStatement(sql);
    ResultSet rs = ps.executeQuery();
    List<SalesEntity> list = new ArrayList<>();
    while (rs.next()) {
      SalesEntity salesEntity = new SalesEntity();
      salesEntity.setId(UUID.randomUUID().toString());
      salesEntity.setUserId(rs.getInt("UserId"));
      salesEntity.setCity(rs.getString("city"));
      salesEntity.setCompanyId(rs.getInt("companyId"));
      salesEntity.setGroupId(rs.getInt("groupId"));
      salesEntity.setAreaId(rs.getInt("areaId"));
      salesEntity.setBigAreaId(rs.getInt("bigAreaId"));
      salesEntity.setCenterShopId(rs.getInt("centerShopId"));
      salesEntity.setSearchMonth(7);
      salesEntity.setYear(2016);
      list.add(salesEntity);
    }
    System.out.println("list.size:" + list.size());
    Date d1 = new Date();
    String insert = "insert into EB_Data_Index_Sales_Month(ID,UserId,InsertTime,UpdateTime,SearchMonth,City,CompanyId,GroupId,AreaId,BigAreaId,CenterShopId,year) values(?,?,?,?,?,?,?,?,?,?,?,?)";
    PreparedStatement p = con.prepareStatement(insert);
    for (SalesEntity sales : list) {
      p.setString(1, sales.getId());
      p.setInt(2, sales.getUserId());
      p.setString(3, "2016-07-01 00:00:00");
      p.setString(4, "2016-07-01 00:00:00");
      p.setInt(5, sales.getSearchMonth());
      p.setString(6, sales.getCity());
      p.setInt(7, sales.getCompanyId());
      p.setInt(8, sales.getGroupId());
      p.setInt(9, sales.getAreaId());
      p.setInt(10, sales.getBigAreaId());
      p.setInt(11, sales.getCenterShopId());
      p.setInt(12, sales.getYear());
      p.addBatch();
    }
    p.executeBatch();
    con.commit();
    Date d2 = new Date();
    System.out.println("-----" + (d2.getTime() - d1.getTime())/1000);
  }
  
}
