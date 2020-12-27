package service;

import com.almasb.fxgl.core.collection.PropertyMap;
import com.almasb.fxgl.core.serialization.Bundle;
import com.almasb.fxgl.dsl.FXGL;
import com.almasb.fxgl.net.Client;
import config.Config;
import input.GameControl;
import util.NetworkUtils;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ConnectException;
import java.net.Socket;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Properties;

public class TCPClient {
    public void udpClientConnect() {
        SimpleDateFormat format = new SimpleDateFormat("hh-MM-ss");
        String msg = "Start connect#";
        Properties props = new Properties();
        try {
            props.load(new FileInputStream("src/config.properties"));
        }
        catch (IOException e){
            System.out.println("no such file or directory");
        }
        while(true){
            System.out.println("send time : " + format.format(new Date()));
            String GameServerIP = props.getProperty("ip");
            try {
                if(sendAndReceive(GameServerIP, TCPService.SERVICE_PORT, msg).equals("connect success")) {
                    Client<Bundle> udpClient = FXGL.getNetService().newUDPClient(GameServerIP, Config.GameNetworkPort);
                    udpClient.connectAsync();
                    FXGL.getWorldProperties().setValue("client", udpClient);
                    udpClient.setOnConnected(bundleConnection -> {
                        NetworkUtils.getMultiplayerService().addEntityReplicationReceiver(bundleConnection, FXGL.getGameWorld());
                        NetworkUtils.getMultiplayerService().addInputReplicationSender(bundleConnection,FXGL.getGameWorld());
                    });
                    client.close();
                    break;
                }
            }
            catch (ConnectException e){

            } catch (IOException e) {
                e.printStackTrace();
            }
            System.out.println("receive time : " + format.format(new Date()));
        }
    }
    private Socket client;
    private String sendAndReceive(String ip, int port, String msg) throws ConnectException {
        //这里比较重要，需要给请求信息添加终止符，否则服务端会在解析数据时，一直等待
        msg = msg+TCPService.END_CHAR;
        StringBuilder receiveMsg = new StringBuilder();
        //开启一个链接，需要指定地址和端口
        try {
            client = new Socket(ip, port);
            //向输出流中写入数据，传向服务端
            OutputStream out = client.getOutputStream();
            out.write(msg.getBytes());

            //从输入流中解析数据，输入流来自服务端的响应
            InputStream in = client.getInputStream();
            for (int c = in.read(); c != TCPService.END_CHAR; c = in.read()) {
                if(c==-1)
                    break;
                receiveMsg.append((char)c);
            }
        }catch (Exception e){
            throw new ConnectException();
        }
        return receiveMsg.toString();
    }
}