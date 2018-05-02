package com.example.admin.election;

/**
 * Created by admin on 01.05.2018.
 */

public class ConnectionSettings {
    String ip;

    public ConnectionSettings(String ip) {
        this.ip = ip;
    }

    public String getIp() {
        return "http://"+ip+":4567";
    }

    public void setIp(String ip) {
        this.ip = ip;
    }
}
