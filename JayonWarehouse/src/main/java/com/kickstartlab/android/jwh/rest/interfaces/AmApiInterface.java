package com.kickstartlab.android.jwh.rest.interfaces;

import com.kickstartlab.android.jwh.rest.models.Asset;
import com.kickstartlab.android.jwh.rest.models.Location;
import com.kickstartlab.android.jwh.rest.models.Rack;

import java.util.List;

import retrofit.Callback;
import retrofit.http.GET;

/**
 * Created by awidarto on 10/14/14.
 */
public interface AmApiInterface {

    @GET("/location")
    public void getLocation(Callback<List<Location>> response);

    @GET(("/rack"))
    public void getRack(Callback<List<Rack>> response);

    @GET("/asset")
    public void getAsset(Callback<List<Asset>> response);
}
