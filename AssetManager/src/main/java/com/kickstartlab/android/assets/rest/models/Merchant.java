package com.kickstartlab.android.assets.rest.models;

import com.orm.SugarRecord;

/**
 * Created by awidarto on 12/2/14.
 */
public class Merchant extends SugarRecord<Merchant> {

    private int extId;
    private String street;
    private String district;
    private String province;
    private String city;
    private String country;
    private String zip;
    private String phone;
    private String mobile;
    private String mobile1;
    private String mobile2;
    private String merchantname;
    private String mcEmail;
    private String mcStreet;
    private String mcDistrict;
    private String mcCity;
    private String mcProvince;
    private String mcCountry;
    private String mcZip;
    private String mcPhone;
    private String mcMobile;


    public Merchant(){

    }

    public String toString(){
        return merchantname;
    }

    public int getExtId() {
        return extId;
    }

    public void setExtId(int extId) {
        this.extId = extId;
    }

    public String getStreet() {
        return street;
    }

    public void setStreet(String street) {
        this.street = street;
    }

    public String getDistrict() {
        return district;
    }

    public void setDistrict(String district) {
        this.district = district;
    }

    public String getProvince() {
        return province;
    }

    public void setProvince(String province) {
        this.province = province;
    }

    public String getCity() {
        return city;
    }

    public void setCity(String city) {
        this.city = city;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
    }

    public String getZip() {
        return zip;
    }

    public void setZip(String zip) {
        this.zip = zip;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getMobile() {
        return mobile;
    }

    public void setMobile(String mobile) {
        this.mobile = mobile;
    }

    public String getMobile1() {
        return mobile1;
    }

    public void setMobile1(String mobile1) {
        this.mobile1 = mobile1;
    }

    public String getMobile2() {
        return mobile2;
    }

    public void setMobile2(String mobile2) {
        this.mobile2 = mobile2;
    }

    public String getMerchantname() {
        return merchantname;
    }

    public void setMerchantname(String merchantname) {
        this.merchantname = merchantname;
    }

    public String getMcEmail() {
        return mcEmail;
    }

    public void setMcEmail(String mcEmail) {
        this.mcEmail = mcEmail;
    }

    public String getMcStreet() {
        return mcStreet;
    }

    public void setMcStreet(String mcStreet) {
        this.mcStreet = mcStreet;
    }

    public String getMcDistrict() {
        return mcDistrict;
    }

    public void setMcDistrict(String mcDistrict) {
        this.mcDistrict = mcDistrict;
    }

    public String getMcCity() {
        return mcCity;
    }

    public void setMcCity(String mcCity) {
        this.mcCity = mcCity;
    }

    public String getMcProvince() {
        return mcProvince;
    }

    public void setMcProvince(String mcProvince) {
        this.mcProvince = mcProvince;
    }

    public String getMcCountry() {
        return mcCountry;
    }

    public void setMcCountry(String mcCountry) {
        this.mcCountry = mcCountry;
    }

    public String getMcZip() {
        return mcZip;
    }

    public void setMcZip(String mcZip) {
        this.mcZip = mcZip;
    }

    public String getMcPhone() {
        return mcPhone;
    }

    public void setMcPhone(String mcPhone) {
        this.mcPhone = mcPhone;
    }

    public String getMcMobile() {
        return mcMobile;
    }

    public void setMcMobile(String mcMobile) {
        this.mcMobile = mcMobile;
    }

}