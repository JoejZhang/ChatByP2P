package com.zjz.chatbyp2p.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjz.chatbyp2p.R;
import com.zjz.chatbyp2p.bean.MessageTCP;
import com.zjz.chatbyp2p.constants.Constants;

import java.util.ArrayList;

/**
 * Created by zjz on 2017/6/28.
 */

public class MessageAdapter extends BaseAdapter {
    private ArrayList<MessageTCP> mList;
    private Context mContext;

    public MessageAdapter(Context context ,ArrayList<MessageTCP> list) {
        mList = list;
        mContext = context;
    }

    @Override
    public int getCount() {
        return mList.size();
    }

    @Override
    public Object getItem(int position) {
        return mList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
//        Holder holder ;
//        if(convertView == null){
//            holder = new Holder();
        View view;
        TextView tv_name;
        TextView tv_content;

            if(mList.get(position).getSenderIP().equals(Constants.LOCAL_IP)){
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_right,null);
                tv_name = (TextView) view.findViewById(R.id.tv_chat_right_name);
                tv_content = (TextView)view.findViewById(R.id.tv_chat_right_content);
            }else{
                view = LayoutInflater.from(mContext).inflate(R.layout.item_chat_left,null);
                tv_name = (TextView) view.findViewById(R.id.tv_chat_left_name);
                tv_content = (TextView)view.findViewById(R.id.tv_chat_left_content);
            }
        tv_name.setText(mList.get(position).getTime());

        tv_content.setText(mList.get(position).getContent());
        Log.e("haha",mList.get(position).getTime()+"还有内容"+mList.get(position).getContent());
        return view;
    }
    class Holder {
        TextView tv_name;
        TextView tv_content;
    }
}
