package com.example.zhanyuan.finalapp.beans;

import java.io.Serializable;

public class BaseBean implements Serializable {
    private static final long serialVersionUID = 1L;

    private int status;
    private String message;

    public void setStatus(int status) {
        this.status = status;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getStatus() {
        return status;
    }

    public String getMessage() {
        return message;
    }
}
