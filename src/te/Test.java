package te;

import java.io.BufferedInputStream;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.io.StringWriter;
import java.io.UnsupportedEncodingException;
import java.math.BigDecimal;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URLDecoder;
import java.net.URLEncoder;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.sql.SQLException;
import java.text.DecimalFormat;
import java.text.NumberFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayDeque;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Deque;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Queue;
import java.util.SortedSet;
import java.util.TreeSet;
import java.util.UUID;
import java.util.Vector;
import java.util.logging.FileHandler;
import java.util.logging.Formatter;
import java.util.logging.Level;
import java.util.logging.LogRecord;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;

import javax.xml.bind.JAXB;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.Marshaller;
import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactoryConfigurationError;

import net.sf.json.xml.XMLSerializer;

import org.apache.commons.collections.ListUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.IOUtils;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import te.BuilderTest.Builder;
import te.url.URLUtil;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.caucho.quercus.annotation.This;
import com.caucho.util.BeanUtil;

public class Test {
  
  private final static StringBuffer s = new StringBuffer("a");
  
  private static List<String> list = Arrays.asList("a", "b");
  
  private final static Logger logger = Logger.getLogger("s");
  
  private final static SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  private final static SimpleDateFormat sdfDay = new SimpleDateFormat("yyyy-MM-dd");
  
  /**
   * 
   * @param args
   * @throws UnsupportedEncodingException
   * @throws NoSuchAlgorithmException
   * @throws ParseException
   * @throws ClassNotFoundException
   * @throws IllegalAccessException
   * @throws InstantiationException
   * @throws IOException
   * @throws ParserConfigurationException
   * @throws TransformerFactoryConfigurationError
   * @throws TransformerException
   */
  public static void main(String[] args) throws Exception {
    File file = new File("D:\\1.zip");
    ZipFile zipFile = new ZipFile(file);
    System.out.println(zipFile.size());
  }
  
  public static void getFinancailData() throws IOException{
    SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
    String timestamp = sf.format(new Date());
    System.out.println("timestamp:" + timestamp);
    String start = "2017-03-07";
    String end = "2017-03-07";
    String city = "北京";
    // 测试站-JRTJApiC，正式站-JRTJApiZ
    String key = "JRTJApiZ";
    String str = key + "city" + city + "end" + end + "start" + start + "timestamp" + timestamp + key;
    System.out.println("加密前:" + str);
    String sign = SecurityUtil.MD5(str).toUpperCase();
    System.out.println("sign:" + sign);
    // System.out.println("sign2:" + SecurityUtil.MD5(str));
    
    String url = "http://interface.jrpt.fang.com/StatisticsJrptApi/MCStatics/GetDataList";
    String paramData = "city=" + city + "&start=" + start + "&end=" + end + "&timestamp=" + timestamp + "&sign=" + sign;
//    String paramData = "city=全国&start=" + start + "&end=" + end + "&timestamp=" + timestamp + "&sign=" + sign;
    System.out.println("url:" + url);
    System.out.println("paramData:" + paramData);
    String respBody = URLUtil.getInterfaceDataNotZip(url, 5000, 5000, "utf-8", "post", paramData).toString();
    System.err.println(respBody);
  }
  
  public static String md5(String sourceStr) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    }
    catch (Exception e) {
      System.out.println("EncryptUtils.md5():md5加密失败!" + e);
      return "";
    }
    byte[] byteArray;
    try {
      byteArray = sourceStr.getBytes("UTF-8");
    }
    catch (UnsupportedEncodingException e) {
      byteArray = sourceStr.getBytes();
    }
    byte[] md5Bytes = md5.digest(byteArray);
    
    StringBuilder hexValue = new StringBuilder();
    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }
    
    return hexValue.toString();
  }
  
  static class MyFormatter extends Formatter {
    
    @Override
    public String format(LogRecord record) {
      SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
      return sf.format(record.getMillis()) + "-" + record.getThreadID() + "-" + record.getLevel() + "-" + record.getMessage() + "\r\n";
    }
    
  }
  
  // 压缩
  public static String compress(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return str;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(out);
    gzip.write(str.getBytes());
    gzip.close();
    return out.toString("ISO-8859-1");
  }
  
  // 解压缩
  public static String uncompress(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return str;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    ByteArrayInputStream in = new ByteArrayInputStream(str.getBytes("ISO-8859-1"));
    GZIPInputStream gunzip = new GZIPInputStream(in);
    byte[] buffer = new byte[256];
    int n;
    while ((n = gunzip.read(buffer)) >= 0) {
      out.write(buffer, 0, n);
    }
    // toString()使用平台默认编码，也可以显式的指定如toString("GBK")
    return out.toString();
  }
  
  public static int test(Integer... a) {
    try {
      a[0] = 1;
      int b = 1 / 0;
    }
    catch (Exception e) {
      a[0] = 3;
    }
    finally {
      a[0] = 2;
    }
    return a[0];
  }
  
  private static String MD5(String sourceStr) {
    String result = "";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(sourceStr.getBytes());
      byte b[] = md.digest();
      int i;
      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        i = b[offset];
        if (i < 0)
          i += 256;
        if (i < 16)
          buf.append("0");
        buf.append(Integer.toHexString(i));
      }
      result = buf.toString();
      System.out.println("MD5(" + sourceStr + ",32) = " + result);
      System.out.println("MD5(" + sourceStr + ",16) = " + buf.toString().substring(8, 24));
    }
    catch (NoSuchAlgorithmException e) {
      System.out.println(e);
    }
    return result;
  }
  
  public void test2() throws IOException {
    SimpleDateFormat sd = new SimpleDateFormat("mm:ss:SSS");
    System.out.println(sd.format(new Date()));
    String path1 = "E:/tempData/test.txt";
    String path2 = "";
    for (int i = 0; i < 20; i++) {
      path2 = "E:/temp1/" + i + ".txt";
      /*
       * InputStream input=new FileInputStream(new File(path1));
       * OutputStream output=new FileOutputStream(new File(path2));
       * IOUtils.copy(input, output);
       */
      FileUtils.copyFile(new File(path1), new File(path2), false);
    }
    System.out.println(sd.format(new Date()));
    for (int i = 100; i < 120; i++) {
      path2 = "E:/temp1/" + i + ".txt";
      InputStream input = new BufferedInputStream(new FileInputStream(new File(path1)));
      OutputStream output = new FileOutputStream(new File(path2));
      IOUtils.copy(input, output);
      IOUtils.closeQuietly(output);
    }
    System.out.println(sd.format(new Date()));
  }
  
  public static String add(String s) {
    s += "b";
    return s;
  }
  
  /*
   * private void test1() throws ParseException{
   * String start="2014-12-11 00:00";
   * SimpleDateFormat sd=new SimpleDateFormat("yyyy-MM-dd HH:mm");
   * Date begin=sd.parse(start);
   * Date end=sd.parse("2014-12-11 23:59");
   * Date truncateTime = DateUtils.truncate(begin, Calendar.MINUTE);
   * System.out.println(sd.format(DateUtils.addMinutes(begin, 5)));
   * Date truncateTime = DateUtils.truncate(begin, Calendar.HOUR_OF_DAY);
   * int count = 0;
   * int interval = 15;
   * for (Date time = begin; time.getTime() < end.getTime(); time = DateUtils.addMinutes(time,
   * interval), truncateTime = DateUtils
   * .truncate(time, Calendar.HOUR_OF_DAY)) {
   * System.out.println("time:"+DateFormatUtils.format(time, "H:mm"));
   * System.out.println("truncateTime:"+DateFormatUtils.format(truncateTime, "H:mm"));
   * 
   * if (time.equals(truncateTime)) {
   * } else {
   * System.out.println(DateFormatUtils.format(time, "M月d日H:mm"));
   * }
   * count++;
   * }
   * System.out.println("count:"+count);
   * }
   */
}
