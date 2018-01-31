package com.zjz.chatbyp2p.threads;

import android.os.Handler;
import android.os.Message;
import android.text.TextUtils;
import android.util.Log;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.zjz.chatbyp2p.ChatApp;
import com.zjz.chatbyp2p.bean.Friend;
import com.zjz.chatbyp2p.constants.Constants;

import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UDPGetIPsThread extends Thread {

    private boolean StopFlag = true;
    private Handler handler;
    private String mIpAddress;
    private Gson mGson;

    byte[] inbuf = new byte[100];// 默认缓冲的大小
    // 用来发送和接收数据报包的套接字
    DatagramSocket socket;

    public UDPGetIPsThread(Handler h) {
        mIpAddress = Constants.LOCAL_IP;
        this.handler = h;
        mGson = new GsonBuilder().create();
    }

    @Override
    public void run() {

        try {
            socket = new DatagramSocket(3333);
            while (StopFlag) {
                if (socket == null)
                    socket = new DatagramSocket(3333);
                try {
                    // 构造 DatagramPacket，用来接收长度为 length 的数据包
                    DatagramPacket packet = new DatagramPacket(inbuf,
                            inbuf.length);
                    synchronized (socket) {
                        try {
                            // 从此套接字接收数据报包
                            socket.receive(packet);
                        } catch (Exception e) {
                            e.printStackTrace();
                            //break;
                        }
                    }
                    String s = new String(packet.getData(), "UTF-8");
                    //选取地址部分
                    String senderIP = packet.getAddress().toString().split("/")[1];
                    //储存发送方IP；
                    Constants.SENDER_IP = senderIP;

                  //  判断收到的是否是自身发送的
                    if (TextUtils.isEmpty(senderIP) || senderIP.equals(mIpAddress)) {
                        Log.e("haha", "ip一致" + senderIP + "     " + mIpAddress);
                        continue;
                    }
                    //创建对象
                    Friend friend1 = mGson.fromJson(s.substring(0, s.indexOf("}") + 1), Friend.class);

                    // 判断是否已经保存对方ip
                    if (!isContainsIP(senderIP)) {
                        //储存对等方的信息
                        Constants.sFriendArrayList.add(friend1);
                    }
                    //判断是接收到UDP广播
                    if (friend1.getUDPMsg()) {
                        // 同时将自身的ip地址发送给对方
                        String ip = mIpAddress;
                        String name = Constants.NAME;
                        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());

                        Friend friend = new Friend(ip, name, time);
                        String MyMsg = mGson.toJson(friend);

                        //向发送方ip发送自己数据（ip，昵称，时间）
                        DatagramPacket packet2 = new DatagramPacket(
                                MyMsg.getBytes(), MyMsg.getBytes().length,
                                InetAddress.getByName(senderIP), 3333);
                        sleep(1000);
                        socket.send(packet2);
                        Log.e("haha", "已发送回复数据包");
                    }
                    else{
                    }
                    //显示在网段中搜索到在线的用户
                    Message message = new Message();
                    message.what = 0;
                    // 截取不乱码的部分
                    handler.sendMessage(message);

                } catch (Exception e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        }
    }

    public void finalize() {
        if (socket == null)
            return;
        else {
            StopFlag = false;
            socket.close();
            socket = null;
        }
    }

    private boolean isContainsIP(String ip) {

        for (int i = 0; i < Constants.sFriendArrayList.size(); i++) {
            Log.e("haha", "对比" + Constants.sFriendArrayList.get(i).getIp() + "和" + ip);
            if (Constants.sFriendArrayList.get(i).getIp().equals(ip)) {
                return true;
            }
        }
        return false;
    }

}
