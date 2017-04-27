/**
 * File：FutureTest.java
 * Package：te
 * Author：pengjie
 * Date：2016-11-21 下午12:15:17
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package te;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * 说明
 * 
 * @author pengjie
 */
public class FutureTest {
  
  public static void main(String[] args) throws InterruptedException, ExecutionException {
    // 构建10个线程池
    ExecutorService threadPool = Executors.newFixedThreadPool(10);
    Date date1 = new Date();
    Future<List<String>> f1 = threadPool.submit(new Callable<List<String>>() {
      
      @Override
      public List<String> call() throws Exception {
        System.out.println("1 Thread start");
        List<String> list = new ArrayList<>();
        list.add("1");
        try {
          Thread.sleep(10000);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }
        list.add("2");
        System.out.println("1 Thread end");
        return list;
      }
    });
    Future<List<String>> f2 = threadPool.submit(new Callable<List<String>>() {
      
      @Override
      public List<String> call() throws Exception {
        System.out.println("2 Thread start");
        List<String> list = new ArrayList<>();
        list.add("4");
        try {
          Thread.sleep(5000);
        }
        catch (InterruptedException e) {
          e.printStackTrace();
        }
        list.add("5");
        System.out.println("2 Thread end");
        return list;
      }
    });
    threadPool.shutdown();
    List<String> list1 = f1.get();
    System.out.println("f1:" + list1);
    List<String> list2 = f2.get();
    System.out.println("f2:" + list2);
    Date date2 = new Date();
    System.out.println("date:" + (date2.getTime() - date1.getTime())*1.0/1000);
    List<String> list = new ArrayList<>();
    list.addAll(list1);
    list.addAll(list2);
    System.out.println(list);
    System.out.println("main Thread");
  }
}
