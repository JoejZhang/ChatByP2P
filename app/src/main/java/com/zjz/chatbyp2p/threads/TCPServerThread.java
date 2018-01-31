package com.zjz.chatbyp2p.threads;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Vector;

import android.os.Handler;

public class TCPServerThread extends Thread {

	ServerSocket serverSocket;
	private Handler handler;
	// 端口常量
	public static final int PORT = 3333;
	private static boolean StopFalg = true;
	/*
	 * 创建一个vector对象，用于存储客户连接的clientThread对象 clientThread类维持服务器与单个客户端的连接线程
	 * 负责接收客户端发来的信息
	 */
	public static Vector<TCPClientThread> clients = new Vector<TCPClientThread>();
	// 用于存储客户端发过来的信息

	public TCPServerThread(Handler h) {
	
		this.handler = h;
		try {
			// 创建ServerSocket类对象
			serverSocket = new ServerSocket(PORT);
		} catch (IOException e) {
			// TODO: handle exception
		}
	}

	// 注意：一旦监听到有新的客户端创建即new Socket（ip,PORT）被执行，
	// 就创建一个ClientThread来维持服务器与这个客户端的连接
	public void run() {

		while (StopFalg) {

			try {
				// 获取客户端连接，并返回一个新的socket对象
				Socket socket = serverSocket.accept();

				// 创建clientThread并启动
				// 启动ClientsThread之后，可以监听改连接对应的客户端是否发送过来信息
				// 并获取信息
				TCPClientThread tCPClientThread = new TCPClientThread(handler,socket);
				tCPClientThread.start();
				if (socket != null) {

					synchronized (clients) {

						// 将客户端连接加入到vector数组中保存
						clients.addElement(tCPClientThread);
					}
				}
			} catch (IOException e) {
				// TODO: handle exception
				continue;
			}
			System.out.println("当前连接总数：" + clients.size());
		}

	}

	public void finalize() {

		//释放其他线程
		for(int i = 0,size = clients.size();i < size;i++){
			try {
				clients.get(i).finalize();
			} catch (IOException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		}
		//清空所有线程
		clients.clear();
		//停止循环
		StopFalg = false;
		try {
			serverSocket.close();
		} catch (IOException e) {
			// TODO: handle exception
			serverSocket = null;
		}
	}
}
