/**
 * File：SocketClientTest.java
 * Package：te.socketTest
 * Author：pengjie
 * Date：2017年6月6日 上午9:32:07
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
import java.net.Socket;
import java.net.UnknownHostException;

/**
 * @author pengjie
 */
public class SocketClientTest {
  public static void main(String[] args) throws UnknownHostException, IOException {
    System.out.println("===================我是客户端控制台================");
    Socket socket = new Socket("127.0.0.1", 10001);
    
    // 向服务端发送信息
    OutputStream os = socket.getOutputStream();
    OutputStreamWriter osw = new OutputStreamWriter(os, "UTF-8");
    BufferedWriter bw = new BufferedWriter(osw);
    bw.write("我是客户端1");
    bw.flush();
    socket.shutdownOutput();
    
    // 接受服务端返回信息
    InputStream is = socket.getInputStream();
    InputStreamReader isr = new InputStreamReader(is,"UTF-8");
    BufferedReader br = new BufferedReader(isr);
    
    StringBuilder iSb = new StringBuilder();
    String line = null;
    while((line=br.readLine())!=null){
      iSb.append(line).append("\n\r");
    }
    System.out.println("服务端返回的信息："+iSb.toString());
    
    os.close();
    osw.close();
    bw.close();
    is.close();
    isr.close();
    br.close();
    socket.close();
  }
}
