package te;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.text.DecimalFormat;

/**
 * Created by user on 2016/1/7.
 */
public final class BigDecimalUtils {
  
  /**
   * 私有构造函数
   */
  private BigDecimalUtils() {
    
  }
  
  private static final BigDecimal HUNDRED = new BigDecimal(100);
  
  public static String formatTo2(BigDecimal d) {
    String bigDecimalString = null;
    if (d != null) {
      DecimalFormat df = new DecimalFormat("#.00");
      bigDecimalString = df.format(d);
      if (bigDecimalString.startsWith(".")) {
        bigDecimalString = "0" + bigDecimalString;
      }
    }
    return bigDecimalString;
  }
  
  public static String formatTo4(BigDecimal d) {
    String bigDecimalString = null;
    if (d != null) {
      DecimalFormat df = new DecimalFormat("#.0000");
      bigDecimalString = df.format(d);
      if (bigDecimalString.startsWith(".")) {
        bigDecimalString = "0" + bigDecimalString;
      }
    }
    return bigDecimalString;
  }
  
  public static String formatToPercent(BigDecimal bigDecimal) {
    return bigDecimal != null ? formatTo2(bigDecimal.multiply(HUNDRED)) : null;
  }
  
  /**
   * pengjie add
   * 
   * @param bigDecimal
   *        bigDecimal
   * @return 格式化后的BigDecimal
   */
  public static String formatBigDecimal(BigDecimal bigDecimal) {
    DecimalFormat df = null;
    BigDecimal bigDecimal2 = bigDecimal.stripTrailingZeros();
    if (bigDecimal.compareTo(new BigDecimal(1000)) >= 0) {
      df = new DecimalFormat("#0,000.##");
    }
    else {
      if ((bigDecimal.compareTo(new BigDecimal(1)) >= 0)) {
        df = new DecimalFormat("#0.##");
      }
      else {
        if (bigDecimal.compareTo(new BigDecimal(0.0001)) < 0) {
          return "0";
        }
        else {
          // df = new DecimalFormat("#0.##");
          return bigDecimal.stripTrailingZeros().toString();
        }
      }
    }
    return df.format(bigDecimal2);
  }
  
  /**
   * wangwenbo add
   * 
   * @param bigDecimal
   *        bigDecimal
   * @return 转化为万
   */
  public static BigDecimal formatToWan(BigDecimal bigDecimal) {
    if (bigDecimal == null) {
      return BigDecimal.ZERO;
    }
    else {
      return bigDecimal.divide(new BigDecimal("10000"));
    }
  }
  
  public static String bigDecimalScalePercent(BigDecimal bigDecimal) {
    if (bigDecimal != null) {
      bigDecimal = bigDecimal.setScale(4, RoundingMode.DOWN);
      bigDecimal = bigDecimal.stripTrailingZeros();
      if (BigDecimal.ZERO.compareTo(bigDecimal) == 0) {
        return "0%"; 
      }else{
        DecimalFormat df = new DecimalFormat("#0.##%");
        return df.format(bigDecimal);
      }
    }
    else {
      return "";
    }
  }
  
  public static void main(String[] args) {
    BigDecimal bd = new BigDecimal("9000");
    System.out.println(bigDecimalScalePercent(bd));
  }
}
