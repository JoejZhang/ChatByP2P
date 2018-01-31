package com.zjz.chatbyp2p.constants;

import com.zjz.chatbyp2p.bean.Friend;

import java.util.ArrayList;

/**
 * Created by zjz on 2017/6/27.
 */

public class Constants {
    public static  String LOCAL_IP = "";
    public static  String SENDER_IP = "";
    public static String NAME = "";
    public static String UDP_ADDRESS = "";
    public static ArrayList<Friend> sFriendArrayList;


    public static final int NEWMSG = 1;
    public static final int FILE_SEND_FINISH = 0;
    public static final int FILE_RECIEVE_SUCCESS = 4;
    public static final int FILE_SEND_FAILED = 2;
    public static final int FILE_RECEIVE_FAIL = 3;


}
