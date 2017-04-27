package te.proxytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;

public class ProxyTest {
  
  public static void main(String[] args) {
    ImplTest it = new ImplTest();
    InvocationHandler ih = new TraceHandler(it);
    InterTest inte = (InterTest) Proxy.newProxyInstance(it.getClass().getClassLoader(), it
        .getClass().getInterfaces(), ih);
    inte.request();
    inte.test();
  }
}
