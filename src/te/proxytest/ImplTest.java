package te.proxytest;

public class ImplTest implements InterTest {
  
  @Override
  public void request() {
    System.out.println("真实对象调用");
  }
  
  @Override
  public void test() {
    System.out.println("test===========");
  }
  
}
