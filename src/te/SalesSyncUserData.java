package te;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.UUID;

public class SalesSyncUserData {
  
  public static void main(String[] args) throws ClassNotFoundException, SQLException, IOException, ParseException {
    SalesSyncUserData jt = new SalesSyncUserData();
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
      salesEntity.setUserId(rs.getInt("UserId"));
      salesEntity.setCity(rs.getString("city"));
      salesEntity.setCompanyId(rs.getInt("companyId"));
      salesEntity.setGroupId(rs.getInt("groupId"));
      salesEntity.setAreaId(rs.getInt("areaId"));
      salesEntity.setBigAreaId(rs.getInt("bigAreaId"));
      salesEntity.setCenterShopId(rs.getInt("centerShopId"));
      list.add(salesEntity);
    }
    System.out.println("list.size:" + list.size());
    String dateStr = "2016-06-01";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Calendar c = Calendar.getInstance(Locale.CHINA);
    c.setTime(df.parse(dateStr));
    c.set(Calendar.DAY_OF_MONTH, c.getActualMaximum(Calendar.DAY_OF_MONTH));
    Date endDate = c.getTime();
    c.set(Calendar.DAY_OF_MONTH, 1);
    Date startDate = c.getTime();
    Date temp = startDate;
    List<SalesEntity> result = new ArrayList<>();
    while (temp.before(endDate)) {
      String day = df.format(temp);
      for (SalesEntity se : list) {
        SalesEntity r = new SalesEntity();
        r.setId(UUID.randomUUID().toString());
        r.setInsertTime(day + " 00:00:01");
        r.setUpdateTime(day + " 00:00:01");
        r.setSearchDay(day);
        r.setUserId(se.getUserId());
        r.setCity(se.getCity());
        r.setCompanyId(se.getCompanyId());
        r.setGroupId(se.getGroupId());
        r.setAreaId(se.getAreaId());
        r.setBigAreaId(se.getBigAreaId());
        r.setCenterShopId(se.getCenterShopId());
        result.add(r);
      }
      c.add(Calendar.DAY_OF_MONTH, 1);
      temp = c.getTime();
    }
    String day = df.format(temp);
    for (SalesEntity se : list) {
      SalesEntity r = new SalesEntity();
      r.setId(UUID.randomUUID().toString());
      r.setInsertTime(day + " 00:00:01");
      r.setUpdateTime(day + " 00:00:01");
      r.setSearchDay(day);
      r.setUserId(se.getUserId());
      r.setCity(se.getCity());
      r.setCompanyId(se.getCompanyId());
      r.setGroupId(se.getGroupId());
      r.setAreaId(se.getAreaId());
      r.setBigAreaId(se.getBigAreaId());
      r.setCenterShopId(se.getCenterShopId());
      result.add(r);
    }
    System.out.println("result.size():" + result.size());
    Date d1 = new Date();
    String insert = "insert into EB_Data_Index_Sales_Day(ID,UserId,InsertTime,UpdateTime,SearchDate,City,CompanyId,GroupId,AreaId,BigAreaId,CenterShopId) values(?,?,?,?,?,?,?,?,?,?,?)";
    PreparedStatement p = con.prepareStatement(insert);
    for (SalesEntity sales : result) {
      p.setString(1, sales.getId());
      p.setInt(2, sales.getUserId());
      p.setString(3, sales.getInsertTime());
      p.setString(4, sales.getUpdateTime());
      p.setString(5, sales.getSearchDay());
      p.setString(6, sales.getCity());
      p.setInt(7, sales.getCompanyId());
      p.setInt(8, sales.getGroupId());
      p.setInt(9, sales.getAreaId());
      p.setInt(10, sales.getBigAreaId());
      p.setInt(11, sales.getCenterShopId());
      p.addBatch();
    }
    p.executeBatch();
    con.commit();
    Date d2 = new Date();
    System.out.println("-----" + (d2.getTime() - d1.getTime())/1000);
  }
  
}
