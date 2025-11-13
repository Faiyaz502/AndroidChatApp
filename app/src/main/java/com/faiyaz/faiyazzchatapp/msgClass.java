package com.faiyaz.faiyazzchatapp;

import java.time.LocalDateTime;
import java.util.Date;

public class msgClass {
    String msg ;
    String senderId ;

    Date timeStamp;

    public msgClass() {

    }

    public msgClass(String msg, Date timeStamp, String senderId) {
        this.msg = msg;
        this.timeStamp = timeStamp;
        this.senderId = senderId;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public String getSenderId() {
        return senderId;
    }

    public void setSenderId(String senderId) {
        this.senderId = senderId;
    }

    public Date getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(Date timeStamp) {
        this.timeStamp = timeStamp;
    }
}
