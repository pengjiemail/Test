package te;

import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

public class ThreadTest {
  
  public static void main(String[] args) throws InterruptedException {
    ScheduledExecutorService ses = Executors.newSingleThreadScheduledExecutor();
    RunableTest run = new RunableTest();
    ses.scheduleAtFixedRate(run, 1000, 10000, TimeUnit.MILLISECONDS);
  }
  
  public static void test() throws InterruptedException{
    Lock lock = new ReentrantLock();
    Condition conditon = lock.newCondition();
    System.err.println("thread test start");
    Thread th1 = new Thread(new Runnable() {
      
      @Override
      public void run() {
        System.out.println("thread1 start");
        for (int i = 0; i < 1000; i++) {
          if (i % 10 == 0) {
            System.out.println("thread1");
          }
        }
        System.out.println("thread1 end");
      }
    });
    Thread th2 = new Thread(new Runnable() {
      
      @Override
      public void run() {
        System.out.println("thread2 start");
        for (int i = 0; i < 1000; i++) {
          if (i % 10 == 0) {
            System.out.println("thread2");
          }
        }
        System.out.println("thread2 end");
      }
    });
    th1.start();
    th2.start();
    th1.join();
    th2.join();
    System.err.println("thread test end");
  }
}
