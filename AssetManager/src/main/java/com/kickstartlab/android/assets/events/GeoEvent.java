package com.kickstartlab.android.assets.events;

import android.location.Location;

/**
 * Created by awidarto on 3/21/15.
 */
public class GeoEvent {

    Location location;

    public GeoEvent(Location location){
        this.location = location;
    }

    public String toString(){
        return String.valueOf(this.location.getLatitude()) + "," + String.valueOf(this.location.getLongitude());
    }

    public Location getLocation() {
        return location;
    }

    public void setLocation(Location location) {
        this.location = location;
    }
}
