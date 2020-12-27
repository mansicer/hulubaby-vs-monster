package service;

import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.net.Server;
import config.Config;
import util.NetworkUtils;

import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;

public class TCPService {
    public static final String SERVICE_IP = "127.0.0.1";
    public static final int SERVICE_PORT = 12345;
    public static final char END_CHAR = '#';
    public Server<Bundle> server;
    public void udpServerStart() {
        TCPService service = new TCPService();
        //启动服务端
        service.startService(SERVICE_IP, SERVICE_PORT);
    }

    private void startService(String serverIP, int serverPort){
        try {
            //封装服务端地址
            InetAddress serverAddress = InetAddress.getByName(serverIP);
            //建立服务端
            try(ServerSocket service = new ServerSocket(serverPort, 10, serverAddress)){
                StringBuilder receiveMsg = new StringBuilder();
                //接受一个连接，该方法会阻塞程序，直到一个链接到来
                try(Socket connect = service.accept()){
                    //获得输入流
                    InputStream in = connect.getInputStream();

                    //解析输入流，遇到终止符结束，该输入流来自客户端
                    for (int c = in.read(); c != END_CHAR; c = in.read()) {
                        if(c ==-1)
                            break;
                        receiveMsg.append((char)c);
                    }

                    String response = "" + END_CHAR;
                    //组建响应信息
                    if(receiveMsg.toString().equals("Start connect")) {
                        response = "connect success" + END_CHAR;
                    }
                    //获取输入流，并通过向输出流写数据的方式发送响应

                    server = FXGL.getNetService().newUDPServer(Config.GameNetworkPort);
                    server.startAsync();
                    FXGL.getWorldProperties().setValue("server", server);
                    server.setOnConnected(bundleConnection -> {
                        NetworkUtils.getMultiplayerService().addInputReplicationReceiver(bundleConnection);
                    });

                    OutputStream out = connect.getOutputStream();
                    out.write(response.getBytes());
                    service.close();
                }catch (Exception e){
                    e.printStackTrace();
                }finally {

                }
            }catch (Exception e){
                e.printStackTrace();
            }
        } catch (UnknownHostException e) {
            e.printStackTrace();
        }
    }
}