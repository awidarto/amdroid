package com.kickstartlab.android.assets.rest.models;

import com.orm.SugarRecord;

/**
 * Created by awidarto on 3/14/15.
 */
public class AssetImages extends SugarRecord<AssetImages> {

    private String ns;
    private String url;
    private String temp_dir;
    private String name;
    private String type;
    private String size;
    private String parentId;
    private String parentClass;
    private String fileId;
    private Integer isImage;
    private Integer isAudio;
    private Integer isVideo;
    private Integer isPdf;
    private Integer isDoc;
    private String pictureFullUrl;
    private String pictureLargeUrl;
    private String pictureMediumUrl;
    private String pictureThumbnailUrl;
    private Integer deleted;


    private String extId;
    private String extUrl;
    private String uri;
    private String imageId;

    private String latitude;
    private String longitude;

    private Integer isDefault;
    private Integer isLocal;
    private Integer uploaded;

    public AssetImages(){

    }

    public String toString(){
        return this.uri;
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

    public String getNs() {
        return ns;
    }

    public void setNs(String ns) {
        this.ns = ns;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getTemp_dir() {
        return temp_dir;
    }

    public void setTemp_dir(String temp_dir) {
        this.temp_dir = temp_dir;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getSize() {
        return size;
    }

    public void setSize(String size) {
        this.size = size;
    }

    public String getParentId() {
        return parentId;
    }

    public void setParentId(String parentId) {
        this.parentId = parentId;
    }

    public String getParentClass() {
        return parentClass;
    }

    public void setParentClass(String parentClass) {
        this.parentClass = parentClass;
    }

    public String getFileId() {
        return fileId;
    }

    public void setFileId(String fileId) {
        this.fileId = fileId;
    }

    public Integer getIsImage() {
        return isImage;
    }

    public void setIsImage(Integer isImage) {
        this.isImage = isImage;
    }

    public Integer getIsAudio() {
        return isAudio;
    }

    public void setIsAudio(Integer isAudio) {
        this.isAudio = isAudio;
    }

    public Integer getIsVideo() {
        return isVideo;
    }

    public void setIsVideo(Integer isVideo) {
        this.isVideo = isVideo;
    }

    public Integer getIsPdf() {
        return isPdf;
    }

    public void setIsPdf(Integer isPdf) {
        this.isPdf = isPdf;
    }

    public Integer getIsDoc() {
        return isDoc;
    }

    public void setIsDoc(Integer isDoc) {
        this.isDoc = isDoc;
    }

    public String getPictureFullUrl() {
        return pictureFullUrl;
    }

    public void setPictureFullUrl(String pictureFullUrl) {
        this.pictureFullUrl = pictureFullUrl;
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

    public String getPictureThumbnailUrl() {
        return pictureThumbnailUrl;
    }

    public void setPictureThumbnailUrl(String pictureThumbnailUrl) {
        this.pictureThumbnailUrl = pictureThumbnailUrl;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public String getExtId() {
        return extId;
    }

    public void setExtId(String extId) {
        this.extId = extId;
    }

    public String getImageId() {
        return imageId;
    }

    public void setImageId(String imageId) {
        this.imageId = imageId;
    }

    public Integer getIsLocal() {
        return isLocal;
    }

    public void setIsLocal(Integer isLocal) {
        this.isLocal = isLocal;
    }

    public String getExtUrl() {
        return extUrl;
    }

    public void setExtUrl(String extUrl) {
        this.extUrl = extUrl;
    }

    public Integer getIsDefault() {
        return isDefault;
    }

    public void setIsDefault(Integer isDefault) {
        this.isDefault = isDefault;
    }

    public String getUri() {
        return uri;
    }

    public void setUri(String uri) {
        this.uri = uri;
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }
}
