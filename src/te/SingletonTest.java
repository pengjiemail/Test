/**
 * File：SingletonTest.java
 * Package：te
 * Author：pengjie
 * Date：2016-8-8 上午9:01:59
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package te;

/**
 * 说明
 * 
 * @author pengjie
 */
public class SingletonTest {
  
  private static final SingletonTest st = new SingletonTest();
  
  private SingletonTest() {
    
  }
  
  /**
   * 饿汉式
   * getSingletonTest方法说明
   * 
   * @return 参数说明
   */
  public static SingletonTest getSingletonTest() {
    return st;
  }
  
  public void test() {
    System.out.println("测试");
  }
}
