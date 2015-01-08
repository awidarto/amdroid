package com.kickstartlab.android.jwh.rest.interfaces;

import com.kickstartlab.android.jwh.rest.models.Merchant;
import com.kickstartlab.android.jwh.rest.models.OrderItem;
import com.kickstartlab.android.jwh.rest.models.ResultObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;
import retrofit.http.Path;

/**
 * Created by awidarto on 12/3/14.
 */
public interface JwhApiInterface {
    @GET("/jwmerchant/key/12345678/format/json")
    public void getMerchants(Callback<List<Merchant>> response);

    @GET("/jwpickup/key/12345678/format/json/did/{devicename}/mid/{merchantid}/date/{date}")
    public void getOrders( @Path("devicename") String devicename,
                           @Path("merchantid") String merchantid,
                           @Path("date") String date,
                           Callback<List<OrderItem>> response);
    @GET("/jwstatus/status/{status}/did/{did}/key/12345678/format/json")
    public void setStatus(@Path("status") String status,
                          @Path("did") String deliveryId,
                          Callback<ResultObject> response );

}
