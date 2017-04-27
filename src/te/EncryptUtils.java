package te;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;

import org.apache.commons.codec.binary.Base64;

/**
 * 字符串加密工具类
 * 
 */
public final class EncryptUtils {
  
  /**
   * MD5加密
   * 
   * @param sourceStr
   *        要加密的字符串
   * @return 加密后的字符串
   */
  public static String md5(String sourceStr) {
    MessageDigest md5 = null;
    try {
      md5 = MessageDigest.getInstance("MD5");
    }
    catch (Exception e) {
      return "";
    }
    char[] charArray = sourceStr.toCharArray();
    byte[] byteArray = new byte[charArray.length];
    for (int i = 0; i < charArray.length; i++) {
      byteArray[i] = (byte) charArray[i];
    }
    byte[] md5Bytes = md5.digest(byteArray);
    
    StringBuffer hexValue = new StringBuffer();
    
    for (int i = 0; i < md5Bytes.length; i++) {
      int val = ((int) md5Bytes[i]) & 0xff;
      if (val < 16) {
        hexValue.append("0");
      }
      hexValue.append(Integer.toHexString(val));
    }
    
    return hexValue.toString();
  }
  
  /**
   * 私有构造方法
   */
  private EncryptUtils() {
    
  }
  /***
   * Base64解码
   * 
   * @param s
   *        Base64加密的字符串
   * @return 解密后的字符串
   */
  public static String decode(String s) {
    if (s == null) {
      return null;
    }
    try {
      return new String(Base64.decodeBase64(s));
    }
    catch (Exception e) {
      System.out.println("EncryptUtils.decode():Base64解密失败!");
      return null;
    }
    
  }
  
  /**
   * 二进制数据编码为BASE64字符串
   * 
   * @param s
   *        需要加密的字符串
   * @return 加密后的字符串
   */
  public static String encode(String s) {
    try {
      return new String(Base64.encodeBase64(s.getBytes("utf-8")));
    }
    catch (UnsupportedEncodingException e) {
      System.out.println("EncryptUtils.decode():Base64加密失败!");
      return null;
    }
  }
public static void main(String[] args) {
  System.out.println(md5("Fang.com"));
}
}
