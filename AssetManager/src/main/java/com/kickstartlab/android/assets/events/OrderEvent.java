package com.kickstartlab.android.assets.events;

import com.kickstartlab.android.assets.rest.models.OrderItem;

/**
 * Created by awidarto on 12/3/14.
 */
public class OrderEvent {

    private String action = "refresh";
    private String merchantId = "";
    private OrderItem orderItem;

    public OrderEvent(String action){
        this.action = action;
    }

    public OrderEvent(String action, OrderItem orderItem){
        this.action = action;
        this.orderItem = orderItem;
    }

    public OrderEvent(String action, String merchantId){
        this.action = action;
        this.merchantId = merchantId;
    }

    public String getMerchantId() {
        return merchantId;
    }

    public void setMerchantId(String merchantId) {
        this.merchantId = merchantId;
    }

    public OrderItem getOrderItem() {
        return orderItem;
    }

    public void setOrderItem(OrderItem orderItem) {
        this.orderItem = orderItem;
    }

    public String getAction() {
        return action;
    }

    public void setAction(String action) {
        this.action = action;
    }
}
