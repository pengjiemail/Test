/**
 * File：QuartzTest.java
 * Package：te
 * Author：pengjie
 * Date：2016-8-2 下午2:03:54
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package te;

import org.quartz.CronScheduleBuilder;
import org.quartz.JobBuilder;
import org.quartz.JobDetail;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SchedulerFactory;
import org.quartz.Trigger;
import org.quartz.TriggerBuilder;
import org.quartz.impl.StdSchedulerFactory;

/**
 * 说明
 * 
 * @author pengjie
 */
public class QuartzTest {
  
  private static SchedulerFactory schedulerFactory = new StdSchedulerFactory();
  
  public static void main(String[] args) throws SchedulerException {
    addJon();
  }
  
  public static void addJon() throws SchedulerException {
    try {
      Scheduler scheduler = schedulerFactory.getScheduler();
      JobDetail jobDetail = JobBuilder.newJob(JobTest.class).withIdentity("name1", "group1").build();
      Trigger trigger = TriggerBuilder.newTrigger().withIdentity("name1", "group1").withSchedule(CronScheduleBuilder.cronSchedule("0 * * * * ?")).build();
      // Trigger trigger = TriggerBuilder.newTrigger().withIdentity("name1",
      // "group1").withSchedule(SimpleScheduleBuilder.simpleSchedule().withIntervalInSeconds(10) //
      // 时间间隔
      // .withRepeatCount(5)).startNow().build();
      scheduler.scheduleJob(jobDetail, trigger);
      scheduler.start();
    }
    catch (SchedulerException e) {
      e.printStackTrace();
      System.err.println("获取调度器失败");
    }
  }
}
