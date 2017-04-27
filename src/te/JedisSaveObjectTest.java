/**
 * File：JedisSaveObjectTest.java
 * Package：te
 * Author：pengjie
 * Date：2016-9-13 上午10:48:47
 * Copyright (C) 2003-2016 搜房资讯有限公司-版权所有
 */
package te;

import java.io.IOException;
import java.io.NotSerializableException;

import redis.clients.jedis.Jedis;

import com.mchange.v2.ser.SerializableUtils;

/**
 * 说明
 * 
 * @author pengjie
 */
public class JedisSaveObjectTest<T> {
  
  public void saveObject(String key, T obj) {
    try {
      Jedis jedis = new Jedis("localhost");
      byte[] value=SerializableUtils.toByteArray(obj);
      jedis.set(key.getBytes(), value);
    }
    catch (NotSerializableException e) {
      System.err.println("序列化失败");
    }
  }
  
  @SuppressWarnings("unchecked")
  public T getObject(String key){
    Jedis jedis = new Jedis("localhost");
    byte[] v=jedis.get(key.getBytes());
    try {
      T obj = (T) SerializableUtils.fromByteArray(v);
      return obj;
    }
    catch (ClassNotFoundException | IOException e) {
      System.out.println("反序列化失败");
    }
    return null;
  }
  
}
