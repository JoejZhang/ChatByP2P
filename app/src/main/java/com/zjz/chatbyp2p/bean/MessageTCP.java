package com.zjz.chatbyp2p.bean;

/**
 * Created by zjz on 2017/6/28.
 */

public class MessageTCP {
    private String senderIP;
    private String content;
    private String time;

    public MessageTCP(String senderIP, String content, String time) {
        this.senderIP = senderIP;
        this.content = content;
        this.time = time;
    }

    public String getSenderIP() {
        return senderIP;
    }

    public void setSenderIP(String senderIP) {
        this.senderIP = senderIP;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }
}
