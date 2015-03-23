package com.kickstartlab.android.assets.events;

import com.google.zxing.Result;

import java.util.Scanner;

/**
 * Created by awidarto on 12/4/14.
 */
public class ScannerEvent {

    private String command;
    private String mode;
    private Result result;


    public ScannerEvent(String command){
        this.command = command;
    }

    public ScannerEvent(String command, String mode){
        this.command = command;
        this.mode = mode;
    }

    public ScannerEvent(String command, Result result){
        this.command = command;
        this.result = result;
    }

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
        this.result = result;
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
