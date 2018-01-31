package com.zjz.chatbyp2p.bean;

/**
 * Created by zjz on 2017/6/27.
 */

public class Friend {
    private String ip;
    private String name;
    private String time;
    private Boolean isUDPMsg = false;

    public Friend(String ip, String name, String time) {
        this.ip = ip;
        this.name = name;
        this.time = time;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public Boolean getUDPMsg() {
        return isUDPMsg;
    }

    public void setUDPMsg(Boolean UDPMsg) {
        isUDPMsg = UDPMsg;
    }
}
