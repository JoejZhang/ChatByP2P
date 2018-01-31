package com.zjz.chatbyp2p.threads;

import android.content.ContentResolver;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import com.zjz.chatbyp2p.constants.Constants;

import java.io.DataOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.Socket;

public class TCPSendFileThread extends Thread {

	private Socket socket;
	private String uriString;
	private byte[] buffer = new byte[5120];
	private Handler handler;
	private Context mContext;
	/**
	 * 接收方的ip
	 */
	private String ServerIP;

	public TCPSendFileThread(Context context ,Handler h, String uriString, String serverip) {
		super();
		this.handler = h;
		this.uriString = uriString;
		this.ServerIP = serverip;
		mContext = context;
	}

	@Override
	public void run() {
		// TODO Auto-generated method stub
		super.run();

		try {
			socket = new Socket(ServerIP, 3334);
		//	File file = new File(path);
		//	Log.e("haha","1文件路径:"+ file.exists());
			ContentResolver cr =  mContext.getContentResolver();
			InputStream in = cr.openInputStream(Uri.parse(uriString));
		//	FileInputStream in = new FileInputStream(file);
			Log.e("haha","8文件路径:");
			DataOutputStream out = new DataOutputStream(
					socket.getOutputStream());
		//	Log.e("haha","2文件路径:"+ path);
			while (in.read(buffer) != -1) {
				out.write(buffer);
			}
			in.close();
			out.close();
			socket.close();
			socket = null;
			buffer = null;
			// 通知主界面
	//		Log.e("haha","3文件路径:"+ path);
			Message msg = new Message();
			msg.what = Constants.FILE_SEND_FINISH;
			handler.sendMessage(msg);
		//	Log.e("haha","4文件路径:"+ path);
		//	handler.obtainMessage(Constants.FILERECEIVEFINISH).sendToTarget();
		} catch (IOException e) {
			// TODO: handle exception
			Log.i("TCPSendMsgThread", "发送文件失败！");
			Message msg = new Message();
			msg.what = Constants.FILE_SEND_FAILED;
			handler.sendMessage(msg);
			e.printStackTrace();
		}

	}
}
