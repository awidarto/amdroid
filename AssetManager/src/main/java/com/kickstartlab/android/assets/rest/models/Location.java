package com.kickstartlab.android.assets.rest.models;

import com.orm.SugarRecord;

/**
 * Created by awidarto on 10/14/14.
 */
public class Location extends SugarRecord<Location>{
    private String name;
    private String slug;
    private String venue;
    private String address;
    private String phone;
    private String latitude;
    private String longitude;
    private String category;
    private String description;
    private String tags;
    private String createdDate;
    private String lastUpdate;
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

    public Location(){

    }

    public String toString(){
        return name;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSlug() {
        return slug;
    }

    public void setSlug(String slug) {
        this.slug = slug;
    }

    public String getVenue() {
        return venue;
    }

    public void setVenue(String venue) {
        this.venue = venue;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getLatitude() {
        return latitude;
    }

    public void setLatitude(String latitude) {
        this.latitude = latitude;
    }

    public String getLongitude() {
        return longitude;
    }

    public void setLongitude(String longitude) {
        this.longitude = longitude;
    }

    public String getCategory() {
        return category;
    }

    public void setCategory(String category) {
        this.category = category;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getTags() {
        return tags;
    }

    public void setTags(String tags) {
        this.tags = tags;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getLastUpdate() {
        return lastUpdate;
    }

    public void setLastUpdate(String lastUpdate) {
        this.lastUpdate = lastUpdate;
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
