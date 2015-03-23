package com.kickstartlab.android.assets.rest.interfaces;

import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.AssetImages;
import com.kickstartlab.android.assets.rest.models.DeviceType;
import com.kickstartlab.android.assets.rest.models.Location;
import com.kickstartlab.android.assets.rest.models.MemberData;
import com.kickstartlab.android.assets.rest.models.Rack;
import com.kickstartlab.android.assets.rest.models.ResultObject;

import java.util.List;

import retrofit.Callback;
import retrofit.http.Body;
import retrofit.http.Field;
import retrofit.http.FormUrlEncoded;
import retrofit.http.GET;
import retrofit.http.Multipart;
import retrofit.http.POST;
import retrofit.http.PUT;
import retrofit.http.Part;
import retrofit.http.Path;
import retrofit.http.Query;
import retrofit.mime.TypedFile;

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

    @GET("/assettype")
    public void getAssetType(Callback<List<DeviceType>> response);

    @POST("/asset")
    public void sendAsset( @Body Asset asset, Callback<ResultObject> result );

    @PUT("/asset/{id}")
    public void updateAsset( @Path("id") String id ,@Body Asset asset, Callback<ResultObject> result );

    @GET("/img")
    public void getImage( @Query("id") String id, @Query("cls") String cls ,Callback<List<AssetImages>> response);

    @Multipart
    @POST("/upload")
    public void uploadImage(
        @Query("ns") String ns,
        @Query("parid") String parent_id,
        @Query("parclass") String parent_class,
        @Query("fid") String file_id,
        @Query("img") String img_id,
        @Part("imagefile") TypedFile file,
        Callback<ResultObject> result
    );
}
