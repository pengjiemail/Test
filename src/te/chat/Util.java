/**
 * File：Util.java
 * Package：te.chat
 * Author：pengjie
 * Date：2017年6月5日 下午2:43:50
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package te.chat;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * @author pengjie
 */
public class Util {
  /**
  
    * 获得当前时间
  
    *
  
    * @return
  
    */
  
   public static String getTime() {
  
       SimpleDateFormat df = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");// 设置日期格式
  
       return df.format(new Date()).toString();
  
   }
  
  
  
   /**
  
    * 获得client的名字
  
    *
  
    * @param message
  
    * @return
  
    */
  
   public static String getClientName(String message) {
  
       String name = null;
  
       int len = message.indexOf("]$ ");
  
       name = message.substring(1, len);
  
       return name;
  
   }
  
  
  
   /**
  
    * 获得消息的正文部分
  
    *
  
    * @param message
  
    * @return
  
    */
  
   public static String getContent(String message) {
  
       String content = null;
  
       int len = message.indexOf("]$ ");
  
       content = message.substring(len + 3);
  
       return content.trim();
  
      }
}
