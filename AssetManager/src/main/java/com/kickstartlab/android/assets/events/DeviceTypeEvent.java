package com.kickstartlab.android.assets.events;

/**
 * Created by awidarto on 3/10/15.
 */
public class DeviceTypeEvent {
    private String action = "refresh";

    public DeviceTypeEvent(){

    }

    public DeviceTypeEvent(String action){
        this.action = action;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
