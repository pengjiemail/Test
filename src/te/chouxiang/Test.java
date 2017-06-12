/**
 * File：Test.java
 * Package：te.chouxiang
 * Author：pengjie
 * Date：2017年6月10日 下午4:27:24
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package te.chouxiang;

/**
 * @author pengjie
 */
public class Test {
  
  public static void main(String[] args) {
    Animal a = new MaoNing();
    String sound = a.getSound();
    System.out.println(sound);
  }
}
