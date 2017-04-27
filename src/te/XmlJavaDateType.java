/**
 * File：XmlJavaDateType.java
 * Package：te
 * Author：pengjie
 * Date：2017-2-15 下午6:01:52
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package te;

import java.text.SimpleDateFormat;
import java.util.Date;

import javax.xml.bind.annotation.adapters.XmlAdapter;

/**
 * 说明
 * 
 * @author pengjie
 */
public class XmlJavaDateType extends XmlAdapter<String, Date> {
  
  private SimpleDateFormat sf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
  
  @Override
  public Date unmarshal(String v) throws Exception {
    return sf.parse(v);
  }
  
  @Override
  public String marshal(Date v) throws Exception {
    return sf.format(v);
  }
  
}
