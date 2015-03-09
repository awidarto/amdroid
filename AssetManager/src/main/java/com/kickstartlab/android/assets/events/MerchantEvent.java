package com.kickstartlab.android.assets.events;

import com.kickstartlab.android.assets.rest.models.Merchant;
import com.kickstartlab.android.assets.rest.models.Merchant;

/**
 * Created by awidarto on 12/3/14.
 */
public class MerchantEvent {

    private String action = "refresh";

    private Merchant merchant;

    public MerchantEvent(String action){
        this.action = action;
    }

    public MerchantEvent(String action, Merchant merchant){
        this.action = action;
        this.merchant = merchant;
    }

    public Merchant getMerchant() {
        return merchant;
    }

    public void setMerchant(Merchant merchant) {
        this.merchant = merchant;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
