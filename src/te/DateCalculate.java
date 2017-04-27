package te;

import java.util.Calendar;
import java.util.Date;

/**
 * 对日期的一些简单运算
 * 
 * @author liut-kg
 */
public final class DateCalculate {
  
  /**
   * 一年有12个月
   */
  public static final int MONTHS_OF_YEAR = 12;
  
  /**
   * 阻止该类被实例化
   */
  private DateCalculate() {
  }
  
  /**
   * 指定日期加指定天数
   * 
   * @param occurDate
   *        指定日期
   * @param days
   *        指定天数 正负皆可
   * @return 计算后的日期
   * @throws IllegalArgumentException
   *         如果occurDate为空
   */
  public static Date addDays(Date occurDate, int days) {
    return add(occurDate, Calendar.DAY_OF_MONTH, days);
  }
  
  /**
   * 指定日期加指定秒数
   * 
   * @param occurDate
   *        指定日期
   * @param seconds
   *        指定秒数 正负皆可
   * @return 计算后的日期
   * @throws IllegalArgumentException
   *         如果occurDate为空
   */
  public static Date addSeconds(Date occurDate, int seconds) {
    return add(occurDate, Calendar.SECOND, seconds);
  }
  
  /**
   * 指定日期加指定自然月数
   * 例如：2016-01-31 加一个月是 2016-02-29
   * 
   * @param occurDate
   *        指定日期
   * @param months
   *        指定自然月数 正负皆可
   * @return 计算后的日期
   * @throws IllegalArgumentException
   *         如果occurDate为空
   */
  public static Date addMonths(Date occurDate, int months) {
    return add(occurDate, Calendar.MONTH, months);
  }
  
  /**
   * 年月日时分秒的日期 转为 年月日的日期
   * 
   * @param occurDate
   *        指定日期
   * @return 只有年月日的日期实例
   * @throws IllegalArgumentException
   *         如果occurDate为空
   */
  public static Date obtainDateOfYMD(Date occurDate) {
    if (occurDate == null) {
      IllegalArgumentException ex = new IllegalArgumentException("occurDate不能为空，occurDate = "
          + occurDate);
      //BdpLogger.error(ex);
      throw ex;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(occurDate);
    int year = calendar.get(Calendar.YEAR);
    int month = calendar.get(Calendar.MONTH);
    int date = calendar.get(Calendar.DAY_OF_MONTH);
    calendar.set(year, month, date, 0, 0, 0);
    calendar.set(Calendar.MILLISECOND, 0);
    return calendar.getTime();
  }
  
  /**
   * 获取指定日期 年 的数值
   * 例如：2016-01-11 返回 2016
   * 
   * @param occurDate
   *        指定日期
   * @return 年值
   * @throws IllegalArgumentException
   *         如果occurDate为空
   */
  public static int obtainDateOfYearValue(Date occurDate) {
    return obtainDateOfValue(occurDate, Calendar.YEAR);
  }
  
  /**
   * 获取指定日期 月 的数值
   * 例如：2016-01-11 返回 0
   * 
   * @param occurDate
   *        指定日期
   * @return 月值
   * @throws IllegalArgumentException
   *         如果occurDate为空
   */
  public static int obtainDateOfMonthValue(Date occurDate) {
    return obtainDateOfValue(occurDate, Calendar.MONTH);
  }
  
  /**
   * 获取指定日期 时 的数值（24小时制）
   * 例如： 10:04:15 PM 返回 22
   * 
   * @param occurDate
   *        指定日期
   * @return 时值
   * @throws IllegalArgumentException
   *         如果occurDate为空
   */
  public static int obtainDateOfHourValue(Date occurDate) {
    return obtainDateOfValue(occurDate, Calendar.HOUR_OF_DAY);
  }
  
  /**
   * 计算两个指定日期间隔多少天
   * 例如：2016-01-11与2016-02-01相差21天
   * 
   * @param from
   *        指定的一个日期
   * @param to
   *        指定的另一个日期
   *        from和to无需区分谁大谁小
   * @return 间隔天数
   * @throws IllegalArgumentException
   *         如果from或to为空
   */
  public static int daysBetween(Date from, Date to) {
    if (from == null || to == null) {
      IllegalArgumentException ex = new IllegalArgumentException("from或to不能为空，from = " + from
          + "|to = " + to);
//      BdpLogger.error(ex);
      throw ex;
    }
    long fromMS = obtainDateOfYMD(from).getTime();
    long toMS = obtainDateOfYMD(to).getTime();
    return (int) Math.abs((toMS - fromMS) / (24 * 60 * 60 * 1000));
  }
  
  /**
   * 计算两个指定日期间隔多少天
   * 例如：2016-01-11与2016-02-01相差21天
   * 
   * @param from
   *        指定的一个日期
   * @param to
   *        指定的另一个日期
   *        from和to无需区分谁大谁小
   * @return 间隔天数 加天
   * @throws IllegalArgumentException
   *         如果from或to为空
   */
  public static String daysBetweenWithTian(Date from, Date to) {
    return daysBetween(from, to) + "天";
  }
  
  /**
   * 根据用户生日计算年龄
   */
  public static int getAgeByBirthday(Date birthday) {
    Calendar cal = Calendar.getInstance();
    
    if (cal.before(birthday)) {
      throw new IllegalArgumentException("The birthDay is before Now.It's unbelievable!");
    }
    
    int yearNow = cal.get(Calendar.YEAR);
    int monthNow = cal.get(Calendar.MONTH) + 1;
    int dayOfMonthNow = cal.get(Calendar.DAY_OF_MONTH);
    
    cal.setTime(birthday);
    int yearBirth = cal.get(Calendar.YEAR);
    int monthBirth = cal.get(Calendar.MONTH) + 1;
    int dayOfMonthBirth = cal.get(Calendar.DAY_OF_MONTH);
    
    int age = yearNow - yearBirth;
    
    if (monthNow <= monthBirth) {
      if (monthNow == monthBirth) {
        // monthNow==monthBirth
        if (dayOfMonthNow < dayOfMonthBirth) {
          age--;
        }
      }
      else {
        // monthNow>monthBirth
        age--;
      }
    }
    return age;
  }
  
  /**
   * 比较两个日期date1和date2大小，只比较年月日
   * 例如：date1:2016-01-15 和 date2:2016-01-15 返回0
   * date1:2016-01-15 和 date2:2016-01-16 返回-1
   * date1:2016-01-15 和 date2:2016-01-14 返回1
   * 建议这样比较(compareDate(date1, date2) <op> 0)，
   * 其中 <op> 是六个比较运算符（<, ==, >, >=, !=, <=）之一
   * 
   * @param date1
   *        指定的date1
   * @param date2
   *        指定的date2
   * @return 返回比较结果值
   * @throws IllegalArgumentException
   *         如果date1或date2为null
   */
  public static int compareDate(Date date1, Date date2) {
    if (date1 == null || date2 == null) {
      IllegalArgumentException ex = new IllegalArgumentException("date1或date2不能为空，date1 = " + date1
          + "|date2 = " + date2);
//      BdpLogger.error(ex);
      throw ex;
    }
    Calendar cal1 = Calendar.getInstance();
    cal1.setTime(date1);
    Calendar cal2 = Calendar.getInstance();
    cal2.setTime(date2);
    
    if (cal1.get(Calendar.YEAR) > cal2.get(Calendar.YEAR)) {
      return 1;
    }
    else if (cal1.get(Calendar.YEAR) < cal2.get(Calendar.YEAR)) {
      return -1;
    }
    else {
      int val1 = cal1.get(Calendar.DAY_OF_YEAR);
      int val2 = cal2.get(Calendar.DAY_OF_YEAR);
      return (val1 > val2 ? 1 : (val1 == val2 ? 0 : -1));
    }
  }
  
  /**
   * 指定日期加增量，得到一个新的计算后日期实例
   * 
   * @param date
   *        指定日期，不为null
   * @param calendarField
   *        指定的Calendar Field
   * @param amount
   *        增量 正负皆可
   * @return 计算后的日期实例
   * @throws IllegalArgumentException
   *         如果date为空
   */
  private static Date add(Date date, int calendarField, int amount) {
    if (date == null) {
      IllegalArgumentException ex = new IllegalArgumentException("date不能为空，date = " + date);
//      BdpLogger.error(ex);
      throw ex;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    calendar.add(calendarField, amount);
    return calendar.getTime();
  }
  
  /**
   * 指定日期的 年月日时分秒 值
   * 
   * @param date
   *        指定日期
   * @param calendarField
   *        指定的Calendar Field
   * @return 所需值
   * @throws IllegalArgumentException
   *         如果date为空
   */
  private static int obtainDateOfValue(Date date, int calendarField) {
    if (date == null) {
      IllegalArgumentException ex = new IllegalArgumentException("date不能为空，date = " + date);
//      BdpLogger.error(ex);
      throw ex;
    }
    Calendar calendar = Calendar.getInstance();
    calendar.setTime(date);
    return calendar.get(calendarField);
  }
  
  /**
   * 两个日期的差距 天时分秒
   * 
   * @param start
   *        start
   * @param end
   *        end
   * @return 两个日期的差距
   */
  public static String getHHMMSS(Date start, Date end) {
    if (start.compareTo(end) > 0) {
      return "";
    }
    else {
      Calendar calendarStart = Calendar.getInstance();
      calendarStart.setTime(start);
      Calendar calendarEnd = Calendar.getInstance();
      calendarEnd.setTime(end);
      StringBuilder stringBuilder = new StringBuilder();
      long between = (calendarEnd.getTimeInMillis() - calendarStart.getTimeInMillis()) / 1000;
      long days = between / (60 * 60 * 24);
      long hours = (between % ((60 * 60 * 24))) / 3600;
      long minutes = (between % ((60 * 60))) / 60;
      long seconds = between % 60;
      if (days > 0) {
        stringBuilder.append(days);
        stringBuilder.append("天");
      }
      if (hours > 0) {
        stringBuilder.append(hours);
        stringBuilder.append("时");
      }
      if (minutes > 0) {
        stringBuilder.append(minutes);
        stringBuilder.append("分");
      }
      if (seconds > 0) {
        stringBuilder.append(seconds);
        stringBuilder.append("秒");
      }
      return stringBuilder.toString();
    }
    
  }
  
  /**
   * 根据开始时间计算结束时间 例如 2016-03-05 + 3天 = 2016-03-07 包括 05,06,07三天 <br>
   * 用于计算债权和投资的结束日
   * 
   * @param beginDate
   *        开始时间
   * @param calculateType
   *        Integer类型0按月，1按天； String 类型1按月，2按天；其他返回null
   * @param calculateValue
   *        值
   * @return 结束日期
   */
  public static Date calculateEndDate(Date beginDate, Object calculateType, Integer calculateValue) {
    Date endDate = null;
    if (calculateType instanceof String) {
      if ("1".equals(calculateType)) { // 1表示按月算
        endDate = DateCalculate.addDays(DateCalculate.addMonths(beginDate, calculateValue), -1);
      }
      else if ("2".equals(calculateType)) { // 2表示按天算
        endDate = DateCalculate.addDays(beginDate, calculateValue - 1);
      }
    }
    else if (calculateType instanceof Integer) {
      if (calculateType.equals(0)) { // 0表示按月算
        endDate = DateCalculate.addDays(DateCalculate.addMonths(beginDate, calculateValue), -1);
      }
      else if (calculateType.equals(1)) { // 1表示按天算
        endDate = DateCalculate.addDays(beginDate, calculateValue - 1);
      }
    }
    return endDate;
  }
}