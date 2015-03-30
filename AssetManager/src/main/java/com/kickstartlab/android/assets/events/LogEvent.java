package com.kickstartlab.android.assets.events;

import com.google.zxing.Result;

/**
 * Created by awidarto on 12/4/14.
 */
public class LogEvent {

    private String command;

    public LogEvent(String command){
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

}
