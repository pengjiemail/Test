/**
 * File：IntWriterTest.java
 * Package：te.fileIoTest
 * Author：pengjie
 * Date：2017年7月14日 下午8:29:15
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package te.fileIoTest;

import java.io.BufferedOutputStream;
import java.io.BufferedWriter;
import java.io.DataOutputStream;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;

/**
 * @author pengjie
 */
public class IntWriterTest {
  
  public static void main(String[] args) {
    File file = new File("test.txt");
    if (!file.exists()) {
      try {
        file.createNewFile();
      }
      catch (IOException e) {
        System.err.println("文件写异常");
        return;
      }
    }
    int i = 1;
    FileOutputStream fos = null;
    try {
      fos = new FileOutputStream(file, true);
    }
    catch (FileNotFoundException e) {
      System.out.println("文件流写异常");
      return;
    }
    System.out.println(file.length());
    String str = String.valueOf(i);
    try {
      fos.write(str.getBytes("utf-8"), 0, str.length());
    }
    catch (IOException e) {
      System.out.println("文件写入错误");
      return;
    }
    finally {
      try {
        fos.flush();
        fos.close();
      }
      catch (IOException e) {
        System.out.println("文件关闭错误");
      }
    }
    System.out.println(file.length());
  }
}
