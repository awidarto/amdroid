package com.kickstartlab.android.assets.events;


import com.kickstartlab.android.assets.rest.models.Location;
import com.kickstartlab.android.assets.rest.models.Rack;

/**
 * Created by awidarto on 12/3/14.
 */
public class RackEvent {

    private String action = "refresh";
    private Rack rack;
    private String locationId = "";

    public RackEvent(String action){
        this.action = action;
    }

    public RackEvent(String action, Rack rack){
        this.action = action;
        this.rack = rack;
    }

    public RackEvent(String action, String locationId){
        this.action = action;
        this.locationId = locationId;

    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Rack getRack() {
        return rack;
    }

    public void setRack(Rack rack) {
        this.rack = rack;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }
}
