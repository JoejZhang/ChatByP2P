package com.zjz.chatbyp2p;

import android.content.Context;
import android.content.Intent;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.zjz.chatbyp2p.adapters.FriendsAdapter;
import com.zjz.chatbyp2p.bean.Friend;
import com.zjz.chatbyp2p.constants.Constants;
import com.zjz.chatbyp2p.threads.UDPGetIPsThread;
import com.zjz.chatbyp2p.threads.UDPSendIPThread;
import com.zjz.chatbyp2p.ui.ChatActivity;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.InetAddress;
import java.net.MulticastSocket;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class MainActivity extends AppCompatActivity {


    @InjectView(R.id.tv_main)
    TextView mTvMain;
    @InjectView(R.id.btn_main)
    Button mBtnMain;
    @InjectView(R.id.lv_main_friends)
    ListView mLvMainFriends;

    private boolean isSender = false;
    private FriendsAdapter mFriendsAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.inject(this);

        //   初始化静态变量
        //储存本地ip
        Constants.LOCAL_IP = getIp();
        Constants.UDP_ADDRESS = Constants.LOCAL_IP.substring(0, Constants.LOCAL_IP.lastIndexOf(".") + 1) + "255";
        Constants.sFriendArrayList = new ArrayList<Friend>();

        initView();

        Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what){
                    case 0:       Toast.makeText(getBaseContext(),Constants.sFriendArrayList.get(0).getName(),Toast.LENGTH_SHORT).show();
                        mLvMainFriends.setAdapter(mFriendsAdapter);
                        break;
                    default:break;
                }
            }
        };

        new UDPGetIPsThread(handler).start();

    }
    private void initView() {
        mFriendsAdapter = new FriendsAdapter(this);
        mLvMainFriends.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new Intent(getBaseContext(), ChatActivity.class);
                intent.putExtra("position",position);
                startActivity(intent);
            }
        });
    }


    private String getIp() {
        WifiManager wm = (WifiManager) getApplicationContext().getSystemService(Context.WIFI_SERVICE);

// 检查Wifi状态
        if (!wm.isWifiEnabled()) {
            Toast.makeText(this, "清先打开wifi。", Toast.LENGTH_SHORT)
                    .show();
            //System.exit(0);
            return "获取不到IP";
        } else {
            // wm.setWifiEnabled(true);
            WifiInfo wi = wm.getConnectionInfo();
// 获取32位整型IP地址
            int ipInt = wi.getIpAddress();
// 把整型地址转换成“*.*.*.*”地址
            String ip = intToIp(ipInt);
            return ip;
        }
    }

    private String intToIp(int i) {
        return (i & 0xFF) + "." + ((i >> 8) & 0xFF) + "." + ((i >> 16) & 0xFF)
                + "." + (i >> 24 & 0xFF);

    }

    private void sendDatagramPacket() {

        MulticastSocket multicastSocket;
        try {
            multicastSocket = new MulticastSocket();
            InetAddress address = InetAddress.getByName("xxx.x.x.x"); // 必须使用D类地址
            multicastSocket.joinGroup(address); // 以D类地址为标识，加入同一个组才能实现广播

            while (true) {
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                byte[] buf = time.getBytes();
                DatagramPacket datagramPacket = new DatagramPacket(buf, buf.length);
                datagramPacket.setAddress(address); // 接收地址和group的标识相同
                datagramPacket.setPort(3333); // 发送至的端口号

                multicastSocket.send(datagramPacket);
                Thread.sleep(1000);
            }
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (InterruptedException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }


    }


    @OnClick({R.id.btn_main})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_main:
                isSender = true;
                Toast.makeText(this, Constants.LOCAL_IP + "\n" + Constants.UDP_ADDRESS, Toast.LENGTH_SHORT).show();
                new UDPSendIPThread().start();
                break;
            default:
        }
    }
}
