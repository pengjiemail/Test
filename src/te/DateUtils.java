package te;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 日期工具类
 * @author pengmaokui
 */
public final class DateUtils {
  /**
   * private constructor
   */
  private DateUtils(){

  }
  
  
  /**
   * 计算时间差 (天数)
   * @param startDate
   * @param endDate
   * @return 时间差 (天数)
   * @throws ParseException 
   */
  public static Integer countDateGapToDays(Date startDate, Date endDate) throws ParseException {
    SimpleDateFormat sd = new SimpleDateFormat("yyyy-MM-dd");
    startDate = sd.parse(sd.format(startDate));
    endDate = sd.parse(sd.format(endDate));
    Long diff = endDate.getTime() - startDate.getTime(); 
    Long gapDays = diff / (1000 * 60 * 60 * 24);
    return gapDays.intValue();
  }
  
}
