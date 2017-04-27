package te;

import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import redis.clients.jedis.Jedis;

public class JedisTest {
  public static void main(String[] args) {
//    Person p = new Person();
//    p.setName("zhangsna");
    JedisSaveObjectTest<Person> jso = new JedisSaveObjectTest<>();
//    jso.saveObject("personTest", p);
    Person p2 = jso.getObject("personTest");
    if(p2!=null){
      System.out.println(p2.getName());
    }
  }
  public static void test() {
    Jedis jedis = new Jedis("localhost");
    jedis.del("1");
    jedis.select(1);
    jedis.move("1", 0);
  }
  public static void testPatternKey() {
    Jedis jedis = new Jedis("localhost");
    Set<String> keys = jedis.keys("EB_NewHouseProj*");
    for (String temp : keys) {
      System.out.println(temp);
    }
  }
  public static void testStringSet() {
    Jedis jedis = new Jedis("localhost");
    // jedis.setnx("1", "3");
    // jedis.incr("1");
    String a = jedis.getSet("1", "ss");
    System.out.println(a);
  }
  public static void testHashSet() {
    Jedis jedis = new Jedis("localhost");
    long l1 = jedis.hset("0c39d8b8-cfc7-48ea-9831-7c5f0432109f", "ProjCode", "1010761235");
    System.err.println(l1);
    long l2 = jedis.hsetnx("0c39d8b8-cfc7-48ea-9831-7c5f0432109f", "ProjName", "梵悦108");
    System.err.println(l2);
  }
  public static void testHashGet() {
    Jedis jedis = new Jedis("localhost");
    Map<String, String> map = jedis.hgetAll("0c39d8b8-cfc7-48ea-9831-7c5f0432109f");
    for (Entry<String, String> temp : map.entrySet()) {
      System.out.println(temp.getKey());
      System.out.println(temp.getValue());
    }
  }
  
}
