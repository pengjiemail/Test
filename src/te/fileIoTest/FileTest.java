/**
 * File：FileTest.java
 * Package：te.fileIoTest
 * Author：pengjie
 * Date：2017年7月14日 下午8:58:11
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package te.fileIoTest;

import java.io.File;

/**
 * @author pengjie
 */
public class FileTest {
  public static void main(String[] args) {
    File file = new File("test.txt");
    System.out.println(file.length());
  }
}
