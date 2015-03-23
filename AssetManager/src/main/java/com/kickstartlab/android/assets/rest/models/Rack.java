package com.kickstartlab.android.assets.rest.models;

import com.orm.SugarRecord;

/**
 * Created by awidarto on 10/14/14.
 */
public class Rack extends SugarRecord<Rack>{
    private String SKU;
    private String brc1;
    private String brc2;
    private String brc3;
    private String brchead;
    private String createdDate;
    private String defaultpic;
    private String itemDescription;
    private String lastUpdate;
    private String locationId;
    private String locationName;
    private String status;
    private String tags;
    private String updatedAt;
    private String extId;
    private String pictureThumbnailUrl;
    private String pictureLargeUrl;
    private String pictureMediumUrl;
    private String pictureFullUrl;
    private String pictureBrchead;
    private String pictureBrc1;
    private String pictureBrc2;
    private String pictureBrc3;
    private Integer deleted;

    public Rack(){

    }

    public String toString(){
        return SKU;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getBrc1() {
        return brc1;
    }

    public void setBrc1(String brc1) {
        this.brc1 = brc1;
    }

    public String getBrc2() {
        return brc2;
    }

    public void setBrc2(String brc2) {
        this.brc2 = brc2;
    }

    public String getBrc3() {
        return brc3;
    }

    public void setBrc3(String brc3) {
        this.brc3 = brc3;
    }

    public String getBrchead() {
        return brchead;
    }

    public void setBrchead(String brchead) {
        this.brchead = brchead;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getDefaultpic() {
        return defaultpic;
    }

    public void setDefaultpic(String defaultpic) {
        this.defaultpic = defaultpic;
    }

    public String getItemDescription() {
        return itemDescription;
    }

    public void setItemDescription(String itemDescription) {
        this.itemDescription = itemDescription;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
    }

    public String getLocationId() {
        return locationId;
    }

    public void setLocationId(String locationId) {
        this.locationId = locationId;
    }

    public String getLocationName() {
        return locationName;
    }

    public void setLocationName(String locationName) {
        this.locationName = locationName;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(String updatedAt) {
        this.updatedAt = updatedAt;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getPictureThumbnailUrl() {
        return pictureThumbnailUrl;
    }

    public void setPictureThumbnailUrl(String pictureThumbnailUrl) {
        this.pictureThumbnailUrl = pictureThumbnailUrl;
    }

    public String getPictureLargeUrl() {
        return pictureLargeUrl;
    }

    public void setPictureLargeUrl(String pictureLargeUrl) {
        this.pictureLargeUrl = pictureLargeUrl;
    }

    public String getPictureMediumUrl() {
        return pictureMediumUrl;
    }

    public void setPictureMediumUrl(String pictureMediumUrl) {
        this.pictureMediumUrl = pictureMediumUrl;
    }

    public String getPictureFullUrl() {
        return pictureFullUrl;
    }

    public void setPictureFullUrl(String pictureFullUrl) {
        this.pictureFullUrl = pictureFullUrl;
    }

    public String getPictureBrchead() {
        return pictureBrchead;
    }

    public void setPictureBrchead(String pictureBrchead) {
        this.pictureBrchead = pictureBrchead;
    }

    public String getPictureBrc1() {
        return pictureBrc1;
    }

    public void setPictureBrc1(String pictureBrc1) {
        this.pictureBrc1 = pictureBrc1;
    }

    public String getPictureBrc2() {
        return pictureBrc2;
    }

    public void setPictureBrc2(String pictureBrc2) {
        this.pictureBrc2 = pictureBrc2;
    }

    public String getPictureBrc3() {
        return pictureBrc3;
    }

    public void setPictureBrc3(String pictureBrc3) {
        this.pictureBrc3 = pictureBrc3;
    }

}
