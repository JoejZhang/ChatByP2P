package com.zjz.chatbyp2p.threads;

import android.os.Handler;
import android.os.Message;

import com.zjz.chatbyp2p.constants.Constants;

import java.io.DataInputStream;
import java.io.IOException;
import java.net.Socket;

/**
 * 维持服务器与单个客户端的联接线程，负责接收客户端发来的信息
 */
public class TCPClientThread extends Thread {

	Socket clientSocket;
	Handler handler;
	//服务器端的输入，输出流
	DataInputStream in = null;
	
//	TCPServerThread tCPServerThread;
	
	public TCPClientThread(Handler h,Socket socket){
		
		this.handler = h;
		clientSocket = socket;
//		this.tCPServerThread = tCPServerThread;
		try {
			//创建服务器输入/输出流
			in = new DataInputStream(clientSocket.getInputStream());
		} catch (IOException e) {
			// TODO: handle exception
			e.printStackTrace();
			System.out.println("建立IO通道失败");
		}
	}
	/* 
	 * 监听看连接的客户端是否有消息发送
	 */
	public void run(){
		
		while(true){
			
			try {
				//读入客户端发送过来的信息
				String message = in.readUTF();
					if(message != null){
						Message msg = new Message();
						msg.what = Constants.NEWMSG;
						msg.obj = message;
						handler.sendMessage(msg);
						TCPServerThread.clients.remove(this);
						finalize();
						break;
					}
//				}
			} catch (IOException e) {
				// TODO: handle exception
				break;
			}
		}
	}
	public void finalize() throws IOException{
		if(!clientSocket.isClosed()){
			clientSocket.close();
		}
		clientSocket = null;
		in.close();
	}
}
