package te;

import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang.StringUtils;

import com.caucho.message.encode.StringEncoder;


public class StringtoHtmlUtils {
  
  public static String escapeStringUtils(String html) {
    if(html==null) {
      return "";
    }
    html = StringUtils.replace(html, "&", "&amp;");
    html = StringUtils.replace(html, "'", "&apos;");
    html = StringUtils.replace(html, "\"", "&quot;");
    html = StringUtils.replace(html, "\t", "&nbsp;&nbsp;");// 替换跳格
    html = StringUtils.replace(html, " ", "&nbsp;");// 替换空格
    html = StringUtils.replace(html, "<", "&lt;");
    html = StringUtils.replace(html, ">", "&gt;");
    html = StringUtils.replace(html, "，", ",");
    html = StringUtils.replace(html, "×", "&times;");
    html = StringUtils.replace(html, "÷", "&divide;");
    
    return html;
  }  
  
  public static String unEscapeStringUtils(String html) {
    if(html==null) {
      return "";
    }
    html = StringUtils.replace(html, "&apos;", "'");
    html = StringUtils.replace(html, "&quot;", "\"");
    html = StringUtils.replace(html, "&nbsp;&nbsp;", "\t");// 替换跳格
    html = StringUtils.replace(html, "&nbsp;", " ");// 替换空格
    html = StringUtils.replace(html, "&lt;", "<");
    html = StringUtils.replace(html, "&gt;", ">");
    html = StringUtils.replace(html, ",", "，");
    html = StringUtils.replace(html, "&times;", "×");
    html = StringUtils.replace(html, "&divide;", "÷");
    html = StringUtils.replace(html, "&amp;", "&");
    
    return html;
  }
  
  public static void main(String[] args) {
    String html = "C:\fakepath\test.html";
    System.out.println(html);
  }
}
