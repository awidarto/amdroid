package com.kickstartlab.android.jwh.rest.models;

/**
 * Created by awidarto on 12/5/14.
 */
public class ResultObject {
    String status;
    String timestamp;

    public ResultObject() {
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
