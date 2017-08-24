package te;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.swing.text.NumberFormatter;

import org.apache.commons.io.IOUtils;

public class Read {
  
  public static void main(String[] args) throws Exception {
    // double d=0.1;ssss
    // NumberFormat nf=new DecimalFormat("0.00");
    // System.out.println(nf.format(d));
    read();
  }
  
  private static void deleteFile(int index, String path) {
    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
    Calendar ca = Calendar.getInstance();
    Date date = new Date();
    ca.setTime(date);
    List<String> list = new ArrayList<String>();
    list.add(sd.format(ca.getTime()));
    for (int i = 1; i < index; i++) {
      ca.add(Calendar.DAY_OF_MONTH, -1);
      list.add(sd.format(ca.getTime()));
    }
    System.out.println(list.toString());
    File file = new File(path);
    File[] files = file.listFiles();
    for (File temp : files) {
      long time = temp.lastModified();
      ca.setTimeInMillis(time);
      String fileDate = sd.format(ca.getTime());
      System.out.println(fileDate);
      if (!list.contains(fileDate)) {
        if (temp.isFile()) {
          temp.delete();
        }
        else {
          deleteFile(index, temp.getAbsolutePath());
        }
        temp.delete();
      }
    }
  }
  
  public void test1() {
    String date = "2012-12-12 23:34:34";
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    Date formatDate = null;
    try {
      formatDate = df.parse(date);
    }
    catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    System.out.println(formatDate);
    Calendar c = Calendar.getInstance();
    System.out.println(c.getTime());
    c.setTime(formatDate);
    System.out.println(c.getTime());
    c.set(Calendar.DAY_OF_MONTH, 1);
    System.out.println(c.getTime());
  }
  
  public void test2() {
    Date date = new Date();
    SimpleDateFormat sdf = new SimpleDateFormat("HH:mm");
    String str = sdf.format(date);
    System.out.println(str);
    if (str.equals("10:35")) {
      System.out.println(1);
    }
    else {
      System.out.println(0);
    }
  }
  
  public void test3() {
    String date = "2013年12月";
    String ddate = date + "01";
    String sdate = ddate.replace("年", "-");
    String tdate = sdate.replace("月", "-");
    System.out.println("=======" + tdate);
    SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd");
    Date formateDate = null;
    try {
      formateDate = df.parse(tdate);
    }
    catch (ParseException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    Calendar cal = Calendar.getInstance();
    cal.setTime(formateDate);
    cal.set(Calendar.DAY_OF_MONTH, 1);
    int i = cal.getActualMaximum(Calendar.DAY_OF_MONTH);
    List<String> list = new ArrayList<String>();
    list.add(df.format(cal.getTime()));
    for (int j = 1; j < i; j++) {
      cal.add(Calendar.DAY_OF_MONTH, +1);
      list.add(df.format(cal.getTime()));
    }
    for (int k = 0; k < list.size(); k++) {
      System.out.println("StringTest.main()" + list.get(k));
    }
  }
  
  public static void read() {
    try {
      // http://www.23wx.com/html/14/14042/13180769.html
      URL url = new URL("http://www.23wx.com/html/41/41102/18415025.html");
      URLConnection urlConnection = url.openConnection();
      urlConnection.setRequestProperty("User-Agent",
          "Mozilla/4.0 (compatible; MSIE 5.0; Windows NT; DigExt)");
      BufferedReader bufReader = new BufferedReader(new InputStreamReader(
          urlConnection.getInputStream(), "gbk"));
      String str = "";
      String line = null;
      while ((line = bufReader.readLine()) != null) {
        str += line;
      }
      Pattern pattern = Pattern.compile("[^x00-xff]");
      Matcher matcher = pattern.matcher(str);
      StringBuffer str2 = new StringBuffer();
      int i = 0;
      while (matcher.find()) {
        str2.append(matcher.group());
        if (str2.length() % 70 == 0) {
          System.out.println(str2);
          str2.delete(0, str2.length());
        }
        i++;
        if (i % 5 == 0) {
          System.out.println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
        }
      }
    }
    catch (MalformedURLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
    catch (IOException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
  
  public static void readLocal() throws IOException {
    String path = "E:/读书巴士/books/book1";
    File file = new File(path);
    File[] files = file.listFiles();
    for (File temp : files) {
      if (temp.isFile()) {
        if (temp.getName().substring(0, 4).equals("0392")) {
          BufferedReader bufReader = new BufferedReader(new InputStreamReader(new FileInputStream(
              temp), "utf-8"));
          String line = null;
          StringBuffer str = new StringBuffer();
          while ((line = bufReader.readLine()) != null) {
            str.append(line);
          }
          int j = 60;
          for (int i = 0; i < str.length(); i += j) {
            if (i + j >= str.length()) {
              System.out.println(str.substring(i, str.length()));
              System.out
                  .println("{resin-21} Root WebApplicationContext: initialization completed in 2415 ms");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out.println("FrameworkServlet 'springtest': initialization started");
              System.out
                  .println("Refreshing WebApplicationContext for namespace 'springtest-servlet': startup date [Wed May 27 14:04:19 CST 2015]; parent: Root WebApplicationContext");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out
                  .println("Loading XML bean definitions from ServletContext resource [/WEB-INF/classes/springtest-servlet.xml]");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out
                  .println("JSR-250 'javax.annotation.ManagedBean' found and supported for component scanning");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out
                  .println("JSR-330 'javax.inject.Named' annotation found and supported for component scanning");
              System.out
                  .println("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
              System.out
                  .println("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
            }
            else {
              System.out.println(str.substring(i, i + j));
              System.out
                  .println("{resin-21} Root WebApplicationContext: initialization completed in 2415 ms");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out.println("FrameworkServlet 'springtest': initialization started");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out
                  .println("Refreshing WebApplicationContext for namespace 'springtest-servlet': startup date [Wed May 27 14:04:19 CST 2015]; parent: Root WebApplicationContext");
              System.out
                  .println("Loading XML bean definitions from ServletContext resource [/WEB-INF/classes/springtest-servlet.xml]");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out
                  .println("JSR-250 'javax.annotation.ManagedBean' found and supported for component scanning");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out
                  .println("JSR-330 'javax.inject.Named' annotation found and supported for component scanning");
              System.out
                  .println("{resin-21} WebApp[production/webapp/default/resintest,STARTING] Initializing Spring FrameworkServlet 'springtest'");
              System.out
                  .println("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
              System.out
                  .println("JSR-330 'javax.inject.Inject' annotation found and supported for autowiring");
            }
          }
        }
      }
    }
    
  }
  
  public static void tran() {
    String path = "F:/读书巴士/books";
    File file = new File(path);
    File[] files = file.listFiles();
    for (File temp : files) {
      if (temp.isFile()) {
        String fileName = temp.getName().substring(0, 4);
        temp.renameTo(new File(path + "/绝世武神/" + fileName + ".txt"));
      }
    }
  }
}
