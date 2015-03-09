package com.kickstartlab.android.assets.events;


import com.kickstartlab.android.assets.rest.models.Location;

/**
 * Created by awidarto on 12/3/14.
 */
public class LocationEvent {

    private String action = "refresh";

    private Location location;

    public LocationEvent(String action){
        this.action = action;
    }

    public LocationEvent(String action, Location location){
        this.action = action;
        this.location = location;
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
