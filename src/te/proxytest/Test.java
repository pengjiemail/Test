package te.proxytest;

public class Test {
  
  public static void main(String[] args) {
    ImplTest imt = new ImplTest();
    InterTest it = new StaticProxyTest(imt);
    it.request();
  }
}
