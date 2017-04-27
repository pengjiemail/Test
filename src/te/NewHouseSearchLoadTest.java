package te;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.regex.Pattern;


/**
 * 新房的压力测试工具：读取Access日志中的Url，启动N个线程模拟客户端同时向接口提交
 * 这些Url请求。
 *
 * 该工具也可以用来测试其他GET方式为主的服务接口
 */
public class NewHouseSearchLoadTest {

  //                                    - TaskRunner1
  //                                   /
  //              Task                /
  // TaskFeeder ----------> TaskQueue --> TaskRunner2
  //                                  \
  //                                   \
  //                                    - TaskRunner3
  class LoadTask {
    String uri;
    Map<String, String> params;
    public LoadTask(String uri, Map<String, String> params) {
      this.uri = uri;
      this.params = params;
    }
  }

  // 保存任务的队列
  class TaskQueue {
    List<LoadTask> queue = Collections.synchronizedList(new LinkedList<LoadTask>());

    public void offer(LoadTask task) {
      task.uri = task.uri;
      queue.add(task);
    }

    public LoadTask poll() {
      queue.isEmpty();
      if (queue.size() == 0) {
        return null;
      }
      
      return queue.remove(0);
    }

    public int size() {
      return queue.size();
    }
    
    public void clear() {
      queue.clear();
    }

    public void dump(PrintWriter out) throws IOException {
      out.println("queue.size:" + queue.size());
    }
  }

  // 读取Access日志文件中的Url，向队列中填充任务
  class TaskFeeder extends Thread {
    private File logFile;
    private TaskQueue queue;
    public TaskFeeder(File logFile, TaskQueue queue) {
      this.logFile = logFile;
      this.queue = queue;
    }

    //123.103.34.179 - - [23/Aug/2010:22:34:36 +0800] "GET /search?start=80&num=10&category=0&correct=true&strCity=%B3%A4%B4%BA&strPurpose=%D7%A1%D5%AC&PageNo=9 HTTP/1.0" 200 20401 "http://newhouse.changchun.soufun.com" "-"
    //2016-05-25 10:59:15.350 [com.fang.bdp.core.web.spring.interceptor.PageLoadingTimeInterceptor--preHandle] INFO  com.fang.bdp.core.web.spring.interceptor.PageLoadingTimeInterceptor [PageLoadingTimeInterceptor.java:69] - PageLoadingTimeInterceptor.preHandle - Url=http://txdaiservice.light.fang.com//dtb/getCreditDetailList.do, Params={pageindex=1pageSize=5;tenderid=de3617356288434197ea60813a5db415;}
    final Pattern LOG_PATTERN = Pattern.compile(" "); 
    private void feed() {
      BufferedReader reader = null;
      try {
        reader = new BufferedReader(new InputStreamReader(new FileInputStream(logFile), "GBK"));
        String line = null;
        while ((line = reader.readLine()) != null) {
          queue.offer(new LoadTask(line, null));
          while (queue.size() >= 1000) {
            try {
              sleep(100);
            } catch (InterruptedException e) {
              e.printStackTrace();
            }
          }
        }
      } catch (IOException ioe) {
        System.err.println("feeder - failed to read request from file '" + logFile.getAbsolutePath() + "', because:" + ioe);
        System.exit(-1);
      } finally {
        if (reader != null) {
          try {
            reader.close();
          } catch (IOException e) {
            e.printStackTrace();
          }
        }
      }
    }

    @Override
    public void run() {
      feed();
    }
  }

  // 从队列中提取任务并执行的线程
  class TaskRunner extends Thread {
    TaskQueue queue;
    int trytimes = 0;
    public TaskRunner(TaskQueue queue) {
      this.queue = queue;
    }

    // 调用服务端的接口，提交Url请求
    private void doTask(LoadTask task) {
      String result = HttpUtils.get(task.uri, task.params, null, 50000, 50000, "utf-8", "utf-8");
      //HttpUtils.post(task.uri, task.params, null, 50000, 50000, "utf-8", "gbk");
      /*StringBuffer u = new StringBuffer(128);
      u.append("http://").append(host).append(":").append(port).append(task.uri);
      URL url = null;
      HttpURLConnection conn = null;

      int totalBytes = 0;
      long timestamp0 = System.currentTimeMillis();
      long duration1 = 0l;
      long duration2 = 0l;
      long duration3 = 0l;
      long duration4 = 0l;
      int readtimes = 0;
      int minBytes = Integer.MAX_VALUE;
      try {
        url = new URL(u.toString());
        conn = (HttpURLConnection) url.openConnection();
        conn.setRequestProperty("Connection", "Closed");
        conn.connect();
        long timestamp1 = System.currentTimeMillis();
        // 建立连接所耗时间
        duration1 = timestamp1 - timestamp0;

        InputStream is = conn.getInputStream();
        long timestamp2 = System.currentTimeMillis();
        // 数据可读所耗时间
        duration2 = timestamp2 - timestamp1;

        byte[] buffer = new byte[1024 * 10];
        for (int bytes = is.read(buffer, 0, buffer.length); bytes >= 0; readtimes++) {
          if (readtimes == 0) {
            long timestamp3 = System.currentTimeMillis();
            // 读到第一个数据段所耗时间
            duration3 = timestamp3 - timestamp2;
          }
          if (bytes < minBytes) {
            minBytes = bytes;
          }

          bytes = is.read(buffer, 0, buffer.length);
          totalBytes += bytes;
        }
        long timestamp4 = System.currentTimeMillis();
        // 读完所有数据所耗时间
        duration4 = timestamp4 - timestamp2;
        is.close();
      } catch (IOException ioe) {
        ioe.printStackTrace();
        return -1;
      } finally {
        if (conn != null) {
          conn.disconnect();
        }
      }

      long timestamp5 = System.currentTimeMillis();
      long timeUsed = timestamp5 - timestamp0;
      StringBuffer msg = new StringBuffer(128);
      msg.append(sdf.format(new Date()))
        .append(" [INFO ] - runner:").append(getName())
        .append(" - ").append(task.uri).append(", ")
        .append(totalBytes).append(", ")
        .append(timeUsed).append("ms used:(")
        .append(duration1).append(",")
        .append(duration2).append(",")
        .append(duration3).append(",")
        .append(duration4).append(",")
        .append((readtimes-1)).append(",")
        .append(minBytes).append(")");
      System.out.println(msg.toString());
      return totalBytes;*/
    }

    public void run() {
      while (true) {
        LoadTask task = queue.poll();
        if (task == null) {
          trytimes ++;
          if (trytimes >= 10) {
            System.out.println("[INFO ] - runner:" + getName() + " - over!");
            break;
          }
          try {
            sleep(1000);
          } catch (InterruptedException e) {
            e.printStackTrace();
          }
        } else {
          trytimes = 0;
          // long timeStart = System.currentTimeMillis();
          doTask(task);
          // long timeEnd = System.currentTimeMillis();
          // StringBuffer msg = new StringBuffer(128);
          // msg.append(sdf.format(new Date()))
          //  .append(" [INFO ] - runner:").append(getName())
          //  .append(" - ").append(task.uri).append(", ")
          //  .append(rv).append(", ")
          //  .append((timeEnd - timeStart)).append("ms used.");
          // System.out.println(msg.toString());
        }
      }
    }
  }

  public static void main(String[] args) {
    int runnerCount = 3;
    File logFile = null;
    for (int i=0; i<args.length; i++) {
      if ("-log".equals(args[i])) {
        logFile = new File(args[++i]);
      } else if ("-thread".equals(args[i])) {
        runnerCount = Integer.parseInt(args[++i]);
        runnerCount = (runnerCount < 3) ? 3 : runnerCount;
      }
    }

    if (logFile == null) {
      System.out.println("usage: java " + NewHouseSearchLoadTest.class.getName() + " -log $LOG-FILE -thread $THREAD-NUMBER");
      return;
    }
    
    NewHouseSearchLoadTest test = new NewHouseSearchLoadTest();
    TaskQueue queue = test.new TaskQueue();
    TaskFeeder feeder = test.new TaskFeeder(logFile, queue);
    feeder.start();
    
    TaskRunner[] runners = new TaskRunner[runnerCount];
    for (int i=0; i<runners.length; i++) {
      runners[i] = test.new TaskRunner(queue);
      runners[i].start();
    }
  }
}
