package com.zjz.chatbyp2p.threads;

import com.google.gson.Gson;
import com.zjz.chatbyp2p.bean.Friend;
import com.zjz.chatbyp2p.constants.Constants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 运行时需要开启的线程，用于将自身的ip地址发送到同个局域网内的不同设备
 * 利用UDP协议，多播
 */
public class UDPSendIPThread extends Thread {

    private String MyMsg;   //json数据，包括我的ip，昵称，头像。
    private Gson gson;

    public UDPSendIPThread() {
        gson = new Gson();
        String ip = Constants.LOCAL_IP;
        String name = Constants.NAME;
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        Friend friend = new Friend(ip, name, time);
        friend.setUDPMsg(true);
        MyMsg = gson.toJson(friend);
    }

    @Override
    public void run() {
        try {
            Thread.sleep(1000);
        } catch (Exception e) {
        }
        try {
            // 利用UDP协议，构造数据报包，设置端口
            DatagramPacket packet = new DatagramPacket(MyMsg.getBytes(),
                    MyMsg.getBytes().length, InetAddress.getByName(Constants.UDP_ADDRESS), 3333);
            DatagramSocket socket = new DatagramSocket();
            //发送数据包，到所有在线对等方
            socket.send(packet);
            socket.close();
            sleep(2000);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
