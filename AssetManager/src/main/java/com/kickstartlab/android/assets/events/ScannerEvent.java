package com.kickstartlab.android.assets.events;

import java.util.Scanner;

/**
 * Created by awidarto on 12/4/14.
 */
public class ScannerEvent {

    private String command;
    private String mode;



    public ScannerEvent(String command){
        this.command = command;
    }

    public ScannerEvent(String command, String mode){
        this.command = command;
        this.mode = mode;
    }

    public String getCommand() {
        return command;
    }

    public void setCommand(String command) {
        this.command = command;
    }

    public String getMode() {
        return mode;
    }

    public void setMode(String mode) {
        this.mode = mode;
    }
}
