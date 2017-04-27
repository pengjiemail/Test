package te;

import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.Charset;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Properties;

/**
 * 字符串加密工具类
 * 
 */
public final class SecurityUtil {
  
  
  /**
   * 私有构造方法
   */
  private SecurityUtil() {
    
  }
  
  /**
   * 获取配置文件单独key值内容
   * @param address
   * 		配置文件路径
   * @param key
   * 		需要获取内容的key值
   * @return
   * 		所需key值得value
   * @throws IOException
   */
  private String getProperties(String address,String key) throws IOException {  
      InputStream inputStream = this.getClass().getClassLoader().getResourceAsStream(address);  
      Properties properties = new Properties();  
      try{  
          properties.load(inputStream);  
      }catch (IOException ioE){  
          ioE.printStackTrace();  
      }finally{  
          inputStream.close();  
      }  
      return properties.getProperty(key);  
  } 
  
  /**
   * 
   * @param sourceStr
   *        要加密的字符串
   * @return 加密后的字符串
   */
  public static String MD5(String sourceStr) {
    String result = "";
    try {
      MessageDigest md = MessageDigest.getInstance("MD5");
      md.update(sourceStr.getBytes(Charset.forName("UTF-8")));
      byte b[] = md.digest();
      int i;
      StringBuffer buf = new StringBuffer("");
      for (int offset = 0; offset < b.length; offset++) {
        i = b[offset];
        if (i < 0) {
          i += 256;
        }
        if (i < 16) {
          buf.append("0");
        }
        buf.append(Integer.toHexString(i));
      }
      result = buf.toString();
    }
    catch (NoSuchAlgorithmException e) {
      System.out.println(e);
    }
    return result;
  }
  
  /**
   * 获取公钥加密,进行接口安全性检查
   * @param key
   * 		接口传回key值
   * @return
   * 		安全性判定
   */
  public static boolean publicKeyMD(String key){
	  //安全判定标识
	  boolean flag=false;
	  //公钥地址
	  String address="META-INF/publicKey.properties";
	  //公钥
	  String getKey="";
	  SecurityUtil util=new SecurityUtil();
	  try {
		getKey=util.getProperties(address,"public.Key");
	} catch (IOException e) {
		System.out.println("读取异常,文件不存在或key不存在");
		e.printStackTrace();
	}
	  if (key.equals(MD5(getKey))) {
		flag=true;
	} 
	  return flag;
  }
}
