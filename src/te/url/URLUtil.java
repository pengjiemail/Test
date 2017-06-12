package te.url;

import java.io.BufferedReader;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.Map;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;

import org.apache.commons.lang.StringUtils;

public class URLUtil {
  
  /**
   * HTTP请求参数验证成功
   */
  private static final String VERIFY_SUCESS = "verify_http_param_sucess";
  
  public static void main(String[] args) throws IOException {
    
    // String url = "http://fang.com:8080/pms/getUserRoleListByEmail.do";
    // String paramData = "appTag=" + URLEncoder.encode("callCenter", "utf-8") + "&keys="
    // + EncryptUtils.md5("Fang.com") + "&email=" + "pengjie@soufun.com";
    // url = url + "?" + paramData;
    // // StringBuffer sb=getInterfaceData(url, 1000, 3000, "utf-8", "post", paramData);
    // long a = System.currentTimeMillis();
    // StringBuffer sb = getInterfaceData(url, -1, -1, "utf-8", "get", null);
    // long aa = System.currentTimeMillis();
    // System.out.println((aa - a) + "ms");
    // System.out.println("获取到的值:" + sb);
    // System.out.println("sb:" + sb.length());
    // String b = compress(sb.toString());
    // System.out.println("b.length:" + b.length());
    // String c = uncompress(b);
    // System.out.println(c);
    // System.out.println("c.length:" + c.length());
    
    String url = "http://newhousejkn.fang.com/house/webinterface/EBusiness.php?newcode=1211201508&access_key=990af5e56bcbafa22e76d0211809e578";
    // String paramData = "appTag=" + URLEncoder.encode("callCenter", "utf-8") + "&keys="
    // + EncryptUtils.md5("Fang.com") + "&email=" + "pengjie@soufun.com";
    // url = url + "?" + paramData;
    // StringBuffer sb=getInterfaceData(url, 1000, 3000, "utf-8", "post", paramData);
//    long a = System.currentTimeMillis();
//    StringBuffer sb = getInputStreamByGetMethod(url, null, null, 10000, 10000, "GBK");
//    long aa = System.currentTimeMillis();
//    System.out.println((aa - a) + "ms");
//    System.out.println("获取到的值:" + sb);
//    System.out.println("sb:" + sb.length());
//    String b = compress(sb.toString());
//    System.out.println("b.length:" + b.length());
//    String c = uncompress(b);
//    System.out.println(c);
//    System.out.println("c.length:" + c.length());
    
  }
  
  /**
   * 获取httpURLConnection连接
   * 
   * @param path
   *        请求的路径
   * @param connectTimeout
   *        连接超时时间ms(0表示永不超时,-1表示默认超时)
   * @param readTimeout
   *        读取超时时间ms(0表示永不超时,-1表示默认超时)
   * @param isGzip
   *        是不是压缩
   * @param charset
   *        请求的数据的字符集类型
   * @param requestMethod
   *        请求方式("post"或者"get")
   * @param paramData
   *        请求参数(如果为get请求请填写NULL)key1=value1&key2=value2
   * @return httpURLConnection连接
   * @throws IOException
   */
  private static HttpURLConnection getHttpURLConnection(String path, int connectTimeout,
                                                        int readTimeout, boolean isGzip,
                                                        String charset, String requestMethod,
                                                        String paramData) throws IOException {
    HttpURLConnection huc = null;
    OutputStream os = null;
    OutputStreamWriter osw = null;
    try {
      URL url = new URL(path);
      huc = (HttpURLConnection) url.openConnection();
      if (connectTimeout >= 0) {
        huc.setConnectTimeout(connectTimeout);
      }
      if (readTimeout >= 0) {
        huc.setReadTimeout(readTimeout);
      }
      if (isGzip) {
        huc.setRequestProperty("Accept-Encoding", "gzip");
      }
      huc.setRequestProperty("Content-Type", "application/x-www-form-urlencoded");
      // 如果请求为post请求
      if (requestMethod.equalsIgnoreCase("POST")) {
        huc.setDoOutput(true);// 设置url后面的写入
        huc.setUseCaches(false);// 使用post缓存
        huc.setInstanceFollowRedirects(false);// 设置单一线程处理
        huc.setRequestMethod("POST");
        // 如果请求参数不为空，添加请求参数到url中
        if (paramData != null && !paramData.trim().equals("")) {
          os = huc.getOutputStream();
          osw = new OutputStreamWriter(os, charset);
          osw.write(paramData);
          osw.flush();
          osw.close();
        }
      }
    }
    catch (MalformedURLException e) {
      System.out.println("URL连接失败" + e);
    }
    catch (IOException e) {
      System.out.println("urlConnection创建失败" + e);
    }
    finally {
      if (huc != null) {
        huc.disconnect();
      }
      if (os != null) {
        os.close();
      }
      if (osw != null) {
        osw.close();
      }
    }
    return huc;
  }
  
  /**
   * 获取接口数据StringBuffer
   * 
   * @param path
   *        请求的路径
   * @param connectTimeout
   *        连接超时时间ms(0表示永不超时,-1表示默认超时)
   * @param readTimeout
   *        读取超时时间ms(0表示永不超时,-1表示默认超时)
   * @param charset
   *        请求和返回的数据的字符集类型
   * @param requestMethod
   *        请求方式("post"或者"get")
   * @param paramData
   *        请求参数(如果为get请求请填写NULL)key1=value1&key2=value2
   * @return 请求到的数据buffer
   * @throws IOException
   */
  public static StringBuffer getInterfaceData(String path, int connectTimeout, int readTimeout,
                                              String charset, String requestMethod, String paramData)
      throws IOException {
    StringBuffer respBuffer = new StringBuffer();
    HttpURLConnection huc = null;
    InputStream is = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    String tempLine = null;
    try {
      huc = getHttpURLConnection(path, connectTimeout, readTimeout, true, charset, requestMethod,
          paramData);
      if (huc.getResponseCode() >= 300) {
        throw new IOException("url请求没有成功:" + huc.getResponseCode());
      }
      else {
        is = huc.getInputStream();
      }
      isr = new InputStreamReader(is, charset);
      br = new BufferedReader(isr);
      while ((tempLine = br.readLine()) != null) {
        respBuffer.append(tempLine);
      }
      String gzip = huc.getHeaderField("Content-Encoding");
      if (gzip != null && gzip.equalsIgnoreCase("gzip")) {
        String str = uncompress(respBuffer.toString());
        respBuffer.delete(0, respBuffer.length());
        respBuffer.append(str);
      }
      huc.disconnect();
      br.close();
      isr.close();
    }
    catch (MalformedURLException e) {
      System.out.println("URL连接失败" + e);
    }
    catch (IOException e) {
      System.out.println("urlConnection创建失败" + e);
    }
    finally {
      if (huc != null) {
        huc.disconnect();
      }
      if (is != null) {
        is.close();
      }
      if (isr != null) {
        isr.close();
      }
      if (br != null) {
        br.close();
      }
    }
    return respBuffer;
  }
  
  /**
   * 获取接口数据StringBuffer
   * 
   * @param path
   *        请求的路径
   * @param connectTimeout
   *        连接超时时间ms(0表示永不超时,-1表示默认超时)
   * @param readTimeout
   *        读取超时时间ms(0表示永不超时,-1表示默认超时)
   * @param charset
   *        请求和返回的数据的字符集类型
   * @param requestMethod
   *        请求方式("post"或者"get")
   * @param paramData
   *        请求参数(如果为get请求请填写NULL)key1=value1&key2=value2
   * @return 请求到的数据buffer
   * @throws IOException
   */
  public static StringBuffer getInterfaceDataNotZip(String path, int connectTimeout,
                                                    int readTimeout, String charset,
                                                    String requestMethod, String paramData)
      throws IOException {
    StringBuffer respBuffer = new StringBuffer();
    HttpURLConnection huc = null;
    InputStream is = null;
    InputStreamReader isr = null;
    BufferedReader br = null;
    String tempLine = null;
    try {
      huc = getHttpURLConnection(path, connectTimeout, readTimeout, false, charset, requestMethod,
          paramData);
      if (huc.getResponseCode() >= 300) {
        throw new IOException("url请求没有成功:" + huc.getResponseCode());
      }
      else {
        is = huc.getInputStream();
      }
      isr = new InputStreamReader(is, charset);
      br = new BufferedReader(isr);
      while ((tempLine = br.readLine()) != null) {
        respBuffer.append(tempLine);
      }
      String gzip = huc.getHeaderField("Content-Encoding");
      if (gzip != null && gzip.equalsIgnoreCase("gzip")) {
        String str = uncompress(respBuffer.toString());
        respBuffer.delete(0, respBuffer.length());
        respBuffer.append(str);
      }
      huc.disconnect();
      br.close();
      isr.close();
    }
    catch (MalformedURLException e) {
      System.out.println("URL连接失败" + e);
    }
    catch (IOException e) {
      System.out.println("urlConnection创建失败" + e);
    }
    finally {
      if (huc != null) {
        huc.disconnect();
      }
      if (is != null) {
        is.close();
      }
      if (isr != null) {
        isr.close();
      }
      if (br != null) {
        br.close();
      }
    }
    return respBuffer;
  }
  
  /**
   * 获取接口数据流
   * 
   * @param path
   *        请求的路径
   * @param connectTimeout
   *        连接超时时间ms(0表示永不超时,-1表示默认超时)
   * @param readTimeout
   *        读取超时时间ms(0表示永不超时,-1表示默认超时)
   * @param charset
   *        请求的数据的字符集类型
   * @param requestMethod
   *        请求方式("post"或者"get")
   * @param paramData
   *        请求参数(如果为get请求请填写NULL)key1=value1&key2=value2
   * @return 请求到的数据流
   * @throws IOException
   */
  public static InputStream getInputStream(String path, int connectTimeout, int readTimeout,
                                           String charset, String requestMethod, String paramData)
      throws IOException {
    HttpURLConnection huc = null;
    InputStream is = null;
    try {
      huc = getHttpURLConnection(path, connectTimeout, readTimeout, false, charset, requestMethod,
          paramData);
      if (huc.getResponseCode() >= 300) {
        System.out.println("url:" + path + "==connectTimeout:" + connectTimeout + "==readTimeout:"
            + readTimeout + "==parameData" + paramData);
        throw new IOException("url请求没有成功:" + huc.getResponseCode());
      }
      else {
        is = huc.getInputStream();
      }
    }
    catch (MalformedURLException e) {
      System.out.println("url:" + path + "==connectTimeout:" + connectTimeout + "==readTimeout:"
          + readTimeout + "==parameData" + paramData);
      System.out.println("URL连接失败" + e);
    }
    catch (IOException e) {
      System.out.println("url:" + path + "==connectTimeout:" + connectTimeout + "==readTimeout:"
          + readTimeout + "==parameData" + paramData);
      System.out.println("urlConnection创建失败" + e);
    }
    return is;
  }
  
  /**
   * GZIP压缩
   * 
   * @param str
   *        需要压缩的字符串
   * @return 压缩后的字符串
   * @throws IOException
   */
  private static String compress(String str) throws IOException {
    if (str == null || str.length() == 0) {
      return str;
    }
    ByteArrayOutputStream out = new ByteArrayOutputStream();
    GZIPOutputStream gzip = new GZIPOutputStream(out);
    gzip.write(str.getBytes());
    gzip.close();
    String s = out.toString("ISO-8859-1");
    out.close();
    return s;
  }
  
  /**
   * GZIP的解压缩
   * 
   * @param str
   *        需要解压缩的字符串
   * @return 解压后的字符串
   * @throws IOException
   */
  private static String uncompress(String str) throws IOException {
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
    String s = out.toString();
    out.close();
    in.close();
    gunzip.close();
    return s;
  }
  
  /**
   * 根据地址和参数返回字符串
   * 
   * @param url
   *        目的地址
   * @param params
   *        参数
   * @param httpHeaders
   *        请求头设置
   * @param connTimeout
   *        连接超时时长（毫秒）
   * @param readTimeout
   *        读取接口数据超时时长（毫秒）
   * @param outCharset
   *        输出编码
   * @return 返回InputStream网络流
   */
  public static InputStream getInputStreamByPostMethod(String url, Map<String, String> params,
                                                       Map<String, String> httpHeaders,
                                                       int connTimeout, int readTimeout,
                                                       String outCharset) {
    String paramVerifyResult = httpParamVerify(url, connTimeout, readTimeout, "ignore", outCharset);
    if (!VERIFY_SUCESS.equals(paramVerifyResult)) {
      return null;
    }
    StringBuilder msg = new StringBuilder(512);
    msg.append("HttpPost - URL=[").append(url);
    String strParam = "";
    if (params != null) {
      strParam = mapToUrlString(params, outCharset);
    }
    msg.append("], Parameter=[").append(strParam);
    OutputStreamWriter out = null;
    InputStream in = null;
    long timeStart = System.currentTimeMillis();
    try {
      URL realUrl = new URL(url);
      URLConnection conn = realUrl.openConnection();
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent",
          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
      if (httpHeaders != null) {
        for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
          conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
      }
      conn.setConnectTimeout(connTimeout);
      conn.setReadTimeout(readTimeout);
      // 发送POST请求必须设置如下两行
      conn.setDoOutput(true);
      conn.setDoInput(true);
      out = new OutputStreamWriter(conn.getOutputStream(), outCharset);
      // 发送请求参数
      out.write(strParam);
      out.flush();
      in = conn.getInputStream();
      long timeEnd = System.currentTimeMillis();
      msg.append("], TimeUsed=[").append(timeEnd - timeStart).append("]");
      System.out.println(msg.toString());
    }
    catch (Exception e) {
      long timeEnd = System.currentTimeMillis();
      msg.append("], Error=[").append(e);
      msg.append("], TimeUsed=[").append(timeEnd - timeStart).append("]");
      System.out.println(msg.toString());
    }
    finally {
      try {
        if (out != null) {
          out.close();
        }
      }
      catch (IOException ex) {
        System.out.println(ex);
      }
    }
    return in;
  }
  
  /**
   * 根据地址和参数返回字符串
   * 
   * @param url
   *        目的地址
   * @param connTimeout
   *        连接超时时长（毫秒）
   * @param readTimeout
   *        读取接口数据超时时长（毫秒）
   * @param inCharset
   *        输入编码
   * @param outCharset
   *        输出编码
   * @return 返回字符串
   */
  private static String httpParamVerify(String url, int connTimeout, int readTimeout,
                                        String inCharset, String outCharset) {
    if (StringUtils.isBlank(url)) {
      return "请求URL不能为空!";
    }
    if (connTimeout <= 0) {
      return "请求时，必须设置连接时间！";
    }
    if (readTimeout <= 0) {
      return "请求时，必须设置读取时间！";
    }
    if (StringUtils.isBlank(inCharset)) {
      return "请求时,获取输入流编码不能为空!";
    }
    if (StringUtils.isBlank(outCharset)) {
      return "请求时，获取输出流编码不能为空!";
    }
    return VERIFY_SUCESS;
  }
  
  /**
   * map封装的参数转化为请求参数形式
   * 
   * @param params
   *        参数集合
   * @param charset
   *        编码方式
   * @return 请求形式参数
   */
  private static String mapToUrlString(Map<String, String> params, String charset) {
    StringBuilder ret = new StringBuilder(128);
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String param = entry.getValue();
      try {
        param = URLEncoder.encode(param, charset);
      }
      catch (UnsupportedEncodingException e) {
        // ignore it
      }
      if (ret.length() > 0) {
        ret.append("&");
      }
      ret.append(entry.getKey()).append("=").append(param);
    }
    return ret.toString();
  }
  
  /**
   * 发送GET请求返回字符串
   * 
   * @param url
   *        目的地址
   * @param params
   *        参数
   * @param httpHeaders
   *        请求头设置
   * @param connTimeout
   *        连接超时时长（毫秒）
   * @param readTimeout
   *        读取接口数据超时时长（毫秒）
   * @param outCharset
   *        输出编码
   * @return 返回InputStream网络流
   */
  public static InputStream getInputStreamByGetMethod(String url, Map<String, String> params,
                                                      Map<String, String> httpHeaders,
                                                      int connTimeout, int readTimeout,
                                                      String outCharset) {
    String paramVerifyResult = httpParamVerify(url, connTimeout, readTimeout, "ignore", outCharset);
    if (!VERIFY_SUCESS.equals(paramVerifyResult)) {
      return null;
    }
    String realUrlParam = url;
    if (params != null) {
      realUrlParam = joinUrl(url, params, outCharset);
    }
    StringBuilder msg = new StringBuilder(512);
    msg.append("HttpGet - URL=[").append(realUrlParam);
    
    long timeStart = System.currentTimeMillis();
    InputStream in = null;
    try {
      URL realUrl = new URL(realUrlParam);
      URLConnection conn = realUrl.openConnection();
      conn.setRequestProperty("accept", "*/*");
      conn.setRequestProperty("connection", "Keep-Alive");
      conn.setRequestProperty("user-agent",
          "Mozilla/4.0 (compatible; MSIE 6.0; Windows NT 5.1; SV1)");
      conn.setConnectTimeout(connTimeout);
      conn.setReadTimeout(readTimeout);
      if (httpHeaders != null) {
        for (Map.Entry<String, String> entry : httpHeaders.entrySet()) {
          conn.setRequestProperty(entry.getKey(), entry.getValue());
        }
      }
      // 建立实际的连接
      conn.connect();
      in = conn.getInputStream();
      long timeEnd = System.currentTimeMillis();
      msg.append("], TimeUsed=[").append(timeEnd - timeStart).append("]");
      System.out.println(msg.toString());
    }
    catch (Exception e) {
      long timeEnd = System.currentTimeMillis();
      msg.append("], Error=[").append(e);
      msg.append("], TimeUsed=[").append(timeEnd - timeStart).append("]");
      System.out.println(msg.toString());
    }
    return in;
  }
  
  /**
   * 根据地址和参数拼接URL
   * 
   * @param url
   *        请求地址
   * @param params
   *        请求参数
   * @param charset
   *        编码方式
   * @return 拼接后的地址
   */
  private static String joinUrl(String url, Map<String, String> params, String charset) {
    StringBuilder ret = new StringBuilder(url.length() + 128);
    ret.append(url);
    int len = url.length();
    if (url.indexOf('?') > 0) {
      if (!url.endsWith("?")) {
        len--;
      }
    }
    else {
      ret.append('?');
      len++;
    }
    for (Map.Entry<String, String> entry : params.entrySet()) {
      String param = entry.getValue();
      try {
        param = URLEncoder.encode(param, charset);
      }
      catch (UnsupportedEncodingException e) {
        // ignore it
      }
      if (ret.length() > len) {
        ret.append("&");
      }
      ret.append(entry.getKey()).append("=").append(param);
    }
    return ret.toString();
  }
}
