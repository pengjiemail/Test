package te.proxytest;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class TraceHandler implements InvocationHandler {
  
  private Object subject;
  
  public TraceHandler() {
  }
  
  public TraceHandler(Object subject) {
    this.subject = subject;
  }
  
  @Override
  public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
    System.out.println("subject:" + subject);
    System.out.println("before+------------------");
    System.out.println("method:"+method.getName());
    method.invoke(subject, args);
    System.out.println("after+=====================");
    return null;
  }
  
}
