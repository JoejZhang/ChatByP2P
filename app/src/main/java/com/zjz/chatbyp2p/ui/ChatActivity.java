package com.zjz.chatbyp2p.ui;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.gson.Gson;
import com.zjz.chatbyp2p.R;
import com.zjz.chatbyp2p.adapters.MessageAdapter;
import com.zjz.chatbyp2p.bean.Friend;
import com.zjz.chatbyp2p.bean.MessageTCP;
import com.zjz.chatbyp2p.constants.Constants;
import com.zjz.chatbyp2p.threads.TCPReceFileThread;
import com.zjz.chatbyp2p.threads.TCPSendFileThread;
import com.zjz.chatbyp2p.threads.TCPSendMsgThread;
import com.zjz.chatbyp2p.threads.TCPServerThread;
import com.zjz.chatbyp2p.utils.FileUtils;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class ChatActivity extends AppCompatActivity {

    @InjectView(R.id.btn_chat_back)
    ImageView mBtnChatBack;
    @InjectView(R.id.tv_chat_title)
    TextView mTvChatTitle;
    @InjectView(R.id.lv_chat_message)
    ListView mLvChatMessage;
    @InjectView(R.id.et_chat_send)
    EditText mEtChatSend;
    @InjectView(R.id.btn_chat_send)
    Button mBtnChatSend;
    @InjectView(R.id.ll_chat_send)
    LinearLayout mLlChatSend;
    @InjectView(R.id.btn_chat_file)
    ImageView mBtnChatFile;

    private Friend mFriend;
    private ArrayList<MessageTCP> mlist;
    private MessageAdapter messageAdapter;
    private String mFilePath;
    private Handler handler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);
        ButterKnife.inject(this);
        initData();
        initView();
    }

    private void initData() {
        Intent intent = getIntent();
        mFriend = Constants.sFriendArrayList.get(intent.getIntExtra("position", 0));

        handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                switch (msg.what) {
                    case 1:
                        MessageTCP message = new Gson().fromJson(msg.obj.toString(), MessageTCP.class);
                        mlist.add(message);
                        messageAdapter.notifyDataSetChanged();
                        break;
                    case Constants.FILE_RECIEVE_SUCCESS:
                        Toast.makeText(ChatActivity.this, "文件了接收成功。", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.FILE_SEND_FINISH:
                        Toast.makeText(ChatActivity.this, "文件成功发送。", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.FILE_SEND_FAILED:
                        Toast.makeText(ChatActivity.this, "文件发送失败。", Toast.LENGTH_SHORT).show();
                        break;
                    case Constants.FILE_RECEIVE_FAIL:
                        Toast.makeText(ChatActivity.this, "文件接收失败。", Toast.LENGTH_SHORT).show();
                        break;
                    default:
                        break;
                }
            }
        };
        //开启TCP服务端线程
        new TCPServerThread(handler).start();
        String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
        new TCPReceFileThread(handler, time);

        mlist = new ArrayList<MessageTCP>();
        messageAdapter = new MessageAdapter(getBaseContext(), mlist);
        mLvChatMessage.setAdapter(messageAdapter);

    }

    private void initView() {
        mTvChatTitle.setText(mFriend.getName());

    }


    @OnClick({R.id.btn_chat_back, R.id.btn_chat_send, R.id.btn_chat_file})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_chat_back:
                finish();
                break;
            case R.id.btn_chat_send:
                String time = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss").format(new Date());
                MessageTCP messageTcp = new MessageTCP(Constants.LOCAL_IP, mEtChatSend.getText().toString(), time);
                new TCPSendMsgThread(mFriend.getIp(), messageTcp).start();
                mEtChatSend.setText("");
                mlist.add(messageTcp);
                messageAdapter.notifyDataSetChanged();
                break;
            case R.id.btn_chat_file:
                mFilePath = null;
                openFile();
                break;
        }
    }

    private void openFile() {
        Intent intent = new Intent(Intent.ACTION_GET_CONTENT);
        intent.setType("*/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        try {
            startActivityForResult(Intent.createChooser(intent, "请选择文件!"), 1);
        } catch (android.content.ActivityNotFoundException ex) {
            // Potentially direct the user to the Market with a Dialog
            Toast.makeText(this, "请安装文件管理器", Toast.LENGTH_SHORT).show();
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == Activity.RESULT_OK) {
            // Get the Uri of the selected file
            Uri uri = data.getData();
        //    String path=  FileUtils.getPath(this, uri);

     //       new TCPSendFileThread(getBaseContext(),handler,uri.toString(), mFriend.getIp()).start();
        }

    }
}
