package com.kickstartlab.android.assets.rest.models;

import com.orm.SugarRecord;

/**
 * Created by awidarto on 10/14/14.
 */
public class Asset extends SugarRecord<Asset> {


    private String IP;
    private String OS;
    private String PIC;
    private String PicEmail;
    private String PicPhone;
    private String contractNumber;
    private String SKU;
    private String assetType;
    private String brc1;
    private String brc2;
    private String brc3;
    private String brchead;
    private String createdDate;
    private String defaultpic;
    private String hostName;
    private String itemDescription;
    private String lastUpdate;
    private String locationId;
    private String owner;
    private String rackId;
    private String status;
    private String tags;
    private String type;
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

    private Integer powerStatus;
    private Integer labelStatus;
    private Integer virtualStatus;

    private Integer localEdit;
    private Integer uploaded;
    private Integer deleted;

    public Asset(){

    }

    public Integer getPowerStatus() {
        return powerStatus;
    }

    public void setPowerStatus(Integer powerStatus) {
        this.powerStatus = powerStatus;
    }

    public Integer getLabelStatus() {
        return labelStatus;
    }

    public void setLabelStatus(Integer labelStatus) {
        this.labelStatus = labelStatus;
    }

    public Integer getVirtualStatus() {
        return virtualStatus;
    }

    public void setVirtualStatus(Integer virtualStatus) {
        this.virtualStatus = virtualStatus;
    }

    public Integer getDeleted() {
        return deleted;
    }

    public void setDeleted(Integer deleted) {
        this.deleted = deleted;
    }

    public Integer getLocalEdit() {
        return localEdit;
    }

    public void setLocalEdit(Integer localEdit) {
        this.localEdit = localEdit;
    }

    public Integer getUploaded() {
        return uploaded;
    }

    public void setUploaded(Integer uploaded) {
        this.uploaded = uploaded;
    }

    public String toString(){
        return SKU;
    }

    public String getOS() {
        return OS;
    }

    public void setOS(String OS) {
        this.OS = OS;
    }

    public String getPIC() {
        return PIC;
    }

    public void setPIC(String PIC) {
        this.PIC = PIC;
    }

    public String getPicEmail() {
        return PicEmail;
    }

    public void setPicEmail(String picEmail) {
        PicEmail = picEmail;
    }

    public String getPicPhone() {
        return PicPhone;
    }

    public void setPicPhone(String picPhone) {
        PicPhone = picPhone;
    }

    public String getContractNumber() {
        return contractNumber;
    }

    public void setContractNumber(String contractNumber) {
        this.contractNumber = contractNumber;
    }

    public String getIP() {
        return IP;
    }

    public void setIP(String IP) {
        this.IP = IP;
    }

    public String getSKU() {
        return SKU;
    }

    public void setSKU(String SKU) {
        this.SKU = SKU;
    }

    public String getAssetType() {
        return assetType;
    }

    public void setAssetType(String assetType) {
        this.assetType = assetType;
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

    public String getHostName() {
        return hostName;
    }

    public void setHostName(String hostName) {
        this.hostName = hostName;
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

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getRackId() {
        return rackId;
    }

    public void setRackId(String rackId) {
        this.rackId = rackId;
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

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
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
