package com.zjz.chatbyp2p.adapters;

import android.content.Context;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.zjz.chatbyp2p.R;
import com.zjz.chatbyp2p.bean.Friend;
import com.zjz.chatbyp2p.constants.Constants;

import java.util.List;

/**
 * Created by zjz on 2017/6/28.
 */

public class FriendsAdapter extends BaseAdapter {

    private Context mContext;
    public FriendsAdapter(Context context) {
        mContext = context;
    }

    @Override
    public int getCount() {
        return Constants.sFriendArrayList.size();
    }

    @Override
    public Object getItem(int position) {
        return Constants.sFriendArrayList.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        final Holder holder;
        if(convertView == null){
             holder = new Holder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.item_friends_list,null);
            holder.tvName = (TextView)convertView.findViewById(R.id.tv_friend_name);
            holder.tvIp = (TextView)convertView.findViewById(R.id.tv_friend_ip);
            convertView.setTag(holder);
        }
        else {
            holder = (Holder)convertView.getTag();
        }
        Friend friend = Constants.sFriendArrayList.get(position);
        holder.tvName.setText(friend.getName());
        holder.tvIp.setText(friend.getIp());

        return convertView;
    }

    class Holder{
        TextView tvName;
        TextView tvIp;
    }
}

