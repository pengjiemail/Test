/**
 * File：JobTest.java
 * Package：te
 * Author：pengjie
 * Date：2016-8-2 下午2:18:08
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package te;

import org.quartz.Job;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;


/**
 * 说明
 * @author pengjie
 */
public class JobTest implements Job{

  @Override
  public void execute(JobExecutionContext arg0) throws JobExecutionException {
    System.out.println("job运行了");
  }
  
}
