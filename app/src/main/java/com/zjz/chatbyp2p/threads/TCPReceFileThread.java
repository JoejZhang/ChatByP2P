package com.zjz.chatbyp2p.threads;

import android.annotation.SuppressLint;
import android.os.Handler;
import android.util.Log;

import com.zjz.chatbyp2p.constants.Constants;

import java.io.DataInputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

public class TCPReceFileThread extends Thread {

	ServerSocket serverSocket;
	private Handler handler;
	private String filePath;
	// 端口常量
	public static final int PORT = 3334;
	private byte[] buffer = new byte[5120];
	/*
	 * 创建一个vector对象，用于存储客户连接的clientThread对象 clientThread类维持服务器与单个客户端的连接线程
	 * 负责接收客户端发来的信息
	 */
	public static Vector<TCPClientThread> clients = new Vector<TCPClientThread>();

	// 用于存储客户端发过来的信息

	public TCPReceFileThread(Handler h, String fpath) {

		this.handler = h;
		this.filePath = fpath;
		try {
			// 创建ServerSocket类对象
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	@SuppressLint("SdCardPath")
	public void run() {

		try {
			// 获取客户端连接，并返回一个新的socket对象
			Socket socket = serverSocket.accept();
			DataInputStream in = new DataInputStream(socket.getInputStream());
			Log.e("haha","接收文件");
			int len;
			File file = new File("/sdcard/ChatByP2P/Receive/");
			if (!file.exists())
				file.mkdirs();
			File rece = new File(file, filePath.substring(filePath.lastIndexOf("/")+1));
			if (rece.exists())
				rece.createNewFile();
            FileOutputStream outputStream = new FileOutputStream(rece);
            while((len = in.read(buffer)) != -1){
            	outputStream.write(buffer,0,len);
            }
//            System.out.println("接收完成！");
            socket.close();
            socket = null;
            serverSocket.close();
            serverSocket = null;
            in.close();
            outputStream.close();
            //通知主界面更新
            handler.obtainMessage(Constants.FILE_RECIEVE_SUCCESS).sendToTarget();
		} catch (IOException e) {
			// TODO: handle exception
			handler.obtainMessage(Constants.FILE_RECEIVE_FAIL).sendToTarget();
		}

	}

}
