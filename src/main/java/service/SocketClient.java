package service;

import java.io.*;
import java.net.InetAddress;
import java.net.Socket;
import java.net.URL;
import java.util.Properties;

public class SocketClient {
    // 搭建客户端
    public void clientConnect() throws IOException {
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/config.properties"));
        }
        catch (IOException e){
            System.out.println("no such file or directory");
        }
        String host = props.getProperty("ip");
        while (true)

            try {
                // 1、创建客户端Socket，指定服务器地址和端口

                //下面是你要传输到另一台电脑的IP地址和端口
                Socket socket = new Socket(host, 5209);
//                System.out.println("客户端启动成功");
                // 2、获取输出流，向服务器端发送信息
                // 向本机的52000端口发出客户请求
//            BufferedReader br = new BufferedReader(new InputStreamReader(System.in));
                // 由系统标准输入设备构造BufferedReader对象
                PrintWriter write = new PrintWriter(socket.getOutputStream());
                write.println("connect");
                write.flush();
                // 由Socket对象得到输出流，并构造PrintWriter对象
                //3、获取输入流，并读取服务器端的响应信息
                BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));

                String content = in.readLine();
                if (content.equals("connected")) {
                    // 在系统标准输出上打印读入的字符串
//                    System.out.println("服务:" + content);
                }
                //4、关闭资源
                write.close(); // 关闭Socket输出流
                in.close(); // 关闭Socket输入流
                socket.close(); // 关闭Socket
                break;
            } catch (Exception e) {
                System.out.println("can not listen to:" + e);// 出错，打印出错信息
            }
    }
}