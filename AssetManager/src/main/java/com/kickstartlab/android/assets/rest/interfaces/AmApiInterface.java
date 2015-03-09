package com.kickstartlab.android.assets.rest.interfaces;

import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.Location;
import com.kickstartlab.android.assets.rest.models.MemberData;
import com.kickstartlab.android.assets.rest.models.Rack;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.POST;
import retrofit.http.Path;

/**
 * Created by awidarto on 10/14/14.
 */
public interface AmApiInterface {

    @FormUrlEncoded
    @POST("/auth/login")
    public void doLogin(@Field("user") String user, @Field("pwd") String pwd, Callback<MemberData> response);

    @GET("/auth/logout/{key}")
    public void logout(
            @Path("key") String key,
            Callback callback
    );

    @GET("/location")
    public void getLocation(Callback<List<Location>> response);

    @GET(("/rack"))
    public void getRack(Callback<List<Rack>> response);

    @GET("/asset")
    public void getAsset(Callback<List<Asset>> response);
}
