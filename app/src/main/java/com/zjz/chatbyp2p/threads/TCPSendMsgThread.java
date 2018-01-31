package com.zjz.chatbyp2p.threads;

import android.util.Log;

import com.google.gson.Gson;
import com.zjz.chatbyp2p.bean.MessageTCP;

import java.io.DataOutputStream;
import java.io.IOException;
import java.net.Socket;

public class TCPSendMsgThread extends Thread {

	private Socket socket;
	private DataOutputStream out;
	private MessageTCP message;

	/**
	 * 接收方的ip
	 */
	private String ServerIP;

	public TCPSendMsgThread(String Serverip, MessageTCP m) {
		super();
		this.message = m;
		this.ServerIP = Serverip;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {
			//创建Socket，设置对方的IP地址和端口
			socket = new Socket(ServerIP, 3333);
			out = new DataOutputStream(socket.getOutputStream());
			//发送自己的消息
			out.writeUTF(new Gson().toJson(message, MessageTCP.class));
			out.close();
			socket.close();
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
		}

	}
}