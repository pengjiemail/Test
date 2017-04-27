package te;

import java.io.BufferedReader;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;

public class Shuapiao {
  
  public static void main(String[] args) {
    
//    Thread th1 = new Thread(new Runnable() {
//      
//      @Override
//      public void run() {
//        for (int i = 1; i <= 1000; i++) {
//          try {
//            Thread.sleep(500);
//          }
//          catch (InterruptedException e) {
//            e.printStackTrace();
//          }
//          System.out.println("Thread 1 i:" + i);
//          read();
//        }
//      }
//    });
//    th1.start();
//    
//    Thread th2 = new Thread(new Runnable() {
//      
//      @Override
//      public void run() {
//        for (int i = 1; i <= 1000; i++) {
//          try {
//            Thread.sleep(500);
//          }
//          catch (InterruptedException e) {
//            e.printStackTrace();
//          }
//          System.out.println("Thread 2 i:" + i);
//          read();
//        }
//      }
//    });
//    th2.start();
    
    for (int i = 1; i <= 100; i++) {
      try {
        Thread.sleep(500);
      }
      catch (InterruptedException e) {
        e.printStackTrace();
      }
      System.out.println("main i:" + i);
      read();
    }
  }
  
  public static void read() {
    
    URLConnection urlConnection = null;
    
    try {
      //193641 193640
      URL url = new URL("http://app2.vote.cntv.cn/makeClickAction.do?callback=jQuery1720014652297106742651_1492140047723&type=jsonp&voteId=15514&items_1130788=193641&timstamp=1492140296052&");
      urlConnection = url.openConnection();
      urlConnection.setConnectTimeout(5000);
      urlConnection.setReadTimeout(5000);
      urlConnection.setRequestProperty("User-Agent", "Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
      urlConnection.setRequestProperty("Content-Type", "application/x-www-form-urlencodedl;charset=utf-8");
      urlConnection
          .setRequestProperty(
              "Cookie",
              "BIGipServertoupiao=1799570954.20480.0000; JSESSIONID=FB747440A13D0CE3CC751007A877C781; BIGipServerpool_vote_1-5-9-10=2217494026.20480.0000; cuid=43ebebe4-12ca-c03c-641b-f02589a6c6e7--1492139737236\",\"Mozilla/5.0 (Windows NT 6.1; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/54.0.2840.59 Safari/537.36");
      BufferedReader bufReader = new BufferedReader(new InputStreamReader(urlConnection.getInputStream(), "gbk"));
      String str = "";
      String line = null;
      while ((line = bufReader.readLine()) != null) {
        str += line;
      }
      System.out.println(str);
    }
    catch (Exception e) {
      System.err.println("网站异常了" + e.getMessage());
    }
  }
}
