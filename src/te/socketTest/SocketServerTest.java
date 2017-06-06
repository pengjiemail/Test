/**
 * File：SocketServerTest.java
 * Package：te.socketTest
 * Author：pengjie
 * Date：2017年6月6日 上午9:32:50
 * Copyright (C) 2003-2017 搜房资讯有限公司-版权所有
 */
package te.socketTest;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.PrintWriter;
import java.net.ServerSocket;
import java.net.Socket;

/**
 * @author pengjie
 */
public class SocketServerTest {
  
  public static void main(String[] args) throws IOException {
    System.out.println("===================我是服务端控制台================");
    ServerSocket ss = new ServerSocket(10001);
    Socket socket = ss.accept();
    System.out.println("port:" + socket.getPort() + " localport:" + socket.getLocalPort());
    
    // 获取客户端发送来的信息
    InputStream is = socket.getInputStream();
    InputStreamReader isr = new InputStreamReader(is,"UTF-8");
    BufferedReader br = new BufferedReader(isr);
    StringBuilder iSb = new StringBuilder();
    String line = null;
    while((line=br.readLine())!=null){
      iSb.append(line).append("\r\n");
    }
    System.out.println("客户端发来的信息:"+iSb.toString());
    socket.shutdownInput();
    
    // 向客户端发送信息
    OutputStream os = socket.getOutputStream();
    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
    BufferedWriter bw = new BufferedWriter(osw);
    bw.write("我是服务端，你是谁？");
    bw.flush();
    
    os.close();
    osw.close();
    bw.close();
    is.close();
    isr.close();
    br.close();
    socket.close();
    ss.close();
  }
}
