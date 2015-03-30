package com.kickstartlab.android.assets.rest.interfaces;

import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.AssetImages;
import com.kickstartlab.android.assets.rest.models.DeviceType;
import com.kickstartlab.android.assets.rest.models.Location;
import com.kickstartlab.android.assets.rest.models.MemberData;
import com.kickstartlab.android.assets.rest.models.Rack;
import com.kickstartlab.android.assets.rest.models.ResultObject;
import com.kickstartlab.android.assets.rest.models.ScanLog;

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
    public void getLocation(@Query("key") String key, Callback<List<Location>> response);

    @GET(("/rack"))
    public void getRack(@Query("key") String key, Callback<List<Rack>> response);

    @POST("/rack")
    public void sendRack(@Query("key") String key,  @Body Rack rack, Callback<ResultObject> result );

    @PUT("/rack/{id}")
    public void updateRack(@Query("key") String key,  @Path("id") String id ,@Body Rack rack, Callback<ResultObject> result );

    @PUT("/sync/racks")
    public void updateRackBatch( @Query("key") String key, @Query("batch") Integer batch, @Body List<Rack> racks, Callback<ResultObject> result );

    @GET("/asset")
    public void getAsset(@Query("key") String key, Callback<List<Asset>> response);

    @GET("/assettype")
    public void getAssetType(@Query("key") String key, Callback<List<DeviceType>> response);

    @POST("/asset")
    public void sendAsset(@Query("key") String key,  @Body Asset asset, Callback<ResultObject> result );

    @PUT("/asset/{id}")
    public void updateAsset(@Query("key") String key,  @Path("id") String id ,@Body Asset asset, Callback<ResultObject> result );

    @PUT("/sync/assets")
    public void updateAssetBatch( @Query("key") String key, @Query("batch") Integer batch, @Body List<Asset> assets, Callback<ResultObject> result );

    @POST("/sync/scanlog")
    public void sendScanlogBatch( @Query("key") String key, @Query("batch") Integer batch, @Body List<ScanLog> logs, Callback<List<ResultObject>> result );

    @GET("/img")
    public void getImage( @Query("key") String key,  @Query("id") String id, @Query("cls") String cls ,Callback<List<AssetImages>> response);

    @Multipart
    @POST("/upload")
    public void uploadImage(
        @Query("key") String key,
        @Query("ns") String ns,
        @Query("parid") String parent_id,
        @Query("parclass") String parent_class,
        @Query("fid") String file_id,
        @Query("img") String img_id,
        @Part("imagefile") TypedFile file,
        Callback<ResultObject> result
    );
}
