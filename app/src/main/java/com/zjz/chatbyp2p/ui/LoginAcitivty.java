package com.zjz.chatbyp2p.ui;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.zjz.chatbyp2p.MainActivity;
import com.zjz.chatbyp2p.R;
import com.zjz.chatbyp2p.constants.Constants;

import butterknife.ButterKnife;
import butterknife.InjectView;
import butterknife.OnClick;

public class LoginAcitivty extends AppCompatActivity {


    @InjectView(R.id.et_login_name)
    EditText mEtLoginName;
    @InjectView(R.id.btn_login_login)
    Button mBtnLoginLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.inject(this);
    }

    @OnClick(R.id.btn_login_login)
    public void onViewClicked() {
        if(mEtLoginName.getText().toString().equals("")){
            Toast.makeText(this,"请输入名字",Toast.LENGTH_SHORT).show();
        }else{
            Constants.NAME = mEtLoginName.getText().toString();
            startActivity(new Intent(this, MainActivity.class));
        }
    }
}
