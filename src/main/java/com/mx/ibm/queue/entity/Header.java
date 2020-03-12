package com.mx.ibm.queue.entity;

import lombok.ToString;

@ToString
public class Header {

    private String message;

    private String dateSent;

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDateSent() {
        return dateSent;
    }

    public void setDateSent(String dateSent) {
        this.dateSent = dateSent;
    }
}
