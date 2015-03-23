package com.kickstartlab.android.assets.events;


import com.kickstartlab.android.assets.rest.models.Asset;

/**
 * Created by awidarto on 12/3/14.
 */
public class AssetEvent {

    private String action = "refresh";
    private String rackId = "";
    private Asset asset;

    public AssetEvent(String action){
        this.action = action;
    }

    public AssetEvent(String action, Asset asset){
        this.action = action;
        this.asset = asset;
    }

    public AssetEvent(String action, String rackId){
        this.action = action;
        this.rackId = rackId;
    }

    public AssetEvent(String action, Asset asset , String rackId){
        this.action = action;
        this.asset = asset;
        this.rackId = rackId;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }

    public Asset getAsset() {
        return asset;
    }

    public void setAsset(Asset asset) {
        this.asset = asset;
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
    }
}
