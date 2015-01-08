package com.kickstartlab.android.jwh.events;

import java.util.Scanner;

/**
 * Created by awidarto on 12/4/14.
 */
public class ScannerEvent {

    private String command;

    public ScannerEvent(String command){
        this.command = command;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }
}
