package te.proxytest;

public class StaticProxyTest implements InterTest {
  
  private ImplTest implTest;
  
  public StaticProxyTest() {
  }
  
  public StaticProxyTest(ImplTest implTest) {
    this.implTest = implTest;
  }
  
  @Override
  public void request() {
    System.out.println("代理的request类");
    implTest.request();
    System.out.println("代理request结束");
  }
  
  @Override
  public void test() {
    System.out.println("代理的test类");
    implTest.test();
    System.out.println("代理test结束");
  }
  
}
