package com.kickstartlab.android.assets.activities;

import android.content.Intent;
import android.content.SharedPreferences;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.LocationSource;
import com.kickstartlab.android.assets.events.AssetEvent;
import com.kickstartlab.android.assets.events.DeviceTypeEvent;
import com.kickstartlab.android.assets.events.ImageEvent;
import com.kickstartlab.android.assets.events.LocationEvent;
import com.kickstartlab.android.assets.events.MerchantEvent;
import com.kickstartlab.android.assets.events.OrderEvent;
import com.kickstartlab.android.assets.events.RackEvent;
import com.kickstartlab.android.assets.events.ScannerEvent;
import com.kickstartlab.android.assets.fragments.AssetDetailFragment;
import com.kickstartlab.android.assets.fragments.OrderListFragment;
import com.kickstartlab.android.assets.fragments.ScannerFragment;
import com.kickstartlab.android.assets.fragments.SettingsFragment;
import com.kickstartlab.android.assets.rest.interfaces.JwhApiInterface;
import com.kickstartlab.android.assets.rest.models.AssetImages;
import com.kickstartlab.android.assets.rest.models.DeviceType;
import com.kickstartlab.android.assets.rest.models.Merchant;
import com.kickstartlab.android.assets.rest.models.OrderItem;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.fragments.AssetListFragment;
import com.kickstartlab.android.assets.fragments.LocationListFragment;
import com.kickstartlab.android.assets.fragments.RackListFragment;
import com.kickstartlab.android.assets.rest.interfaces.AmApiInterface;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.Location;
import com.kickstartlab.android.assets.rest.models.Rack;
import com.kickstartlab.android.assets.rest.models.ResultObject;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.nio.channels.FileChannel;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

import de.greenrobot.event.EventBus;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import retrofit.Callback;
import retrofit.RestAdapter;
import retrofit.RetrofitError;
import retrofit.client.Response;
import retrofit.mime.TypedFile;


public class MainActivity extends ActionBarActivity implements
        FragmentManager.OnBackStackChangedListener {

    private static final String SCREEN_LOCATION = "location";
    private static final String SCREEN_SCAN = "scan";

    private String current_mode;
    private String current_location;
    private String current_rack;
    private String current_asset;

    Toolbar mToolbar;

    SmoothProgressBar mProgressBar;

    SharedPreferences spref;
    String akey;
    String uid;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //requestWindowFeature(Window.FEATURE_INDETERMINATE_PROGRESS);

        setContentView(R.layout.activity_main);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);
        mProgressBar = (SmoothProgressBar) findViewById(R.id.loadProgressBar);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        mProgressBar.setVisibility(View.GONE);

        EventBus.getDefault().register(this);
        getSupportFragmentManager().addOnBackStackChangedListener(this);

        spref = PreferenceManager.getDefaultSharedPreferences(this);

        akey = spref.getString("auth","");

        uid = spref.getString("uid","");

        if("".equalsIgnoreCase(akey)){
            showLogin();
        }else{
            if (savedInstanceState == null) {
                getSupportFragmentManager().beginTransaction()
                        .add(R.id.container, new LocationListFragment(),"location_fragment")
                        .setBreadCrumbTitle(getResources().getString(R.string.app_name))
                                //.addToBackStack("merchant_fragment")
                        .commit();
            }

        }



    }


    @Override
    public void onBackStackChanged() {
        String title;
        FragmentManager fm = getSupportFragmentManager();
        int bc = fm.getBackStackEntryCount();
        Log.i("BC",String.valueOf(bc));
        if(bc > 0){
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            title = fm.getBackStackEntryAt(bc - 1).getBreadCrumbTitle().toString();

            getSupportActionBar().setTitle(title);
        }else{
            getSupportActionBar().setDisplayHomeAsUpEnabled(false);
            getSupportActionBar().setTitle(getResources().getString(R.string.app_name));
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        if (id == R.id.action_settings) {
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.container, SettingsFragment.newInstance("Do","Setting"),"setting_fragment")
                    .setBreadCrumbTitle(getString(R.string.settings_title))
                    .addToBackStack("setting_fragment")
                    .commit();
            getSupportActionBar().setTitle(getString(R.string.settings_title));

            //Intent i = new Intent(this, SettingsActivity.class );
            //startActivity(i);
        }

        if(id == R.id.action_scan_rack){
            current_mode = "rack";
            doScan(current_rack, current_mode);
        }

        if(id == R.id.action_scan_location){
            current_mode = "location";
            doScan(current_location, current_mode);
        }

        if(id == R.id.action_scan_asset){
            current_mode = "asset";
            doScan(current_rack, current_mode);
        }

        if(id == R.id.action_refresh){
            //refreshOrders(String.valueOf(current_merchant));
        }

        if(id == R.id.action_scan){
            //doScan(String.valueOf(current_merchant));
        }

        if(id == R.id.action_merchant){

            refreshMerchants();
        }


        if(id == R.id.action_refresh_location){

            refreshLocations();
        }

        if(id == R.id.action_refresh_rack){
            refreshRack(current_location);
        }

        if(id == R.id.action_refresh_asset){
            //refreshAsset();
        }

        if(id == R.id.action_edit_asset){
            EventBus.getDefault().post(new AssetEvent("editAsset"));
        }

        if(id == R.id.action_logout){
            doSignOut();
        }

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    /* EVENTS */

    public void onEvent(ScannerEvent se){
        if(se.getCommand() == "scan"){
            String mode = se.getMode();

            if(mode.equalsIgnoreCase("rack")){
                doScan(current_rack, mode);
            }

            if(mode.equalsIgnoreCase("location")){
                doScan(current_location, mode);
            }

            if(mode.equalsIgnoreCase("asset")){
                doScan(current_rack, mode);
            }
        }
    }

    public void onEvent(LocationEvent le){
        if(le.getAction() == "select"){
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.container, RackListFragment.newInstance(le.getLocation().getExtId(), le.getLocation().getName() ),"rack_fragment")
                    .setBreadCrumbTitle(le.getLocation().getName())
                    .addToBackStack("rack_fragment")
                    .commit();
            getSupportActionBar().setTitle(le.getLocation().getName());
            Toast.makeText(this, le.getLocation().getName(),Toast.LENGTH_SHORT ).show();

            current_location = le.getLocation().getExtId();
        }

        if(le.getAction() == "refreshImage"){
            refreshImages(le.getLocation().getExtId(),"location");
        }
    }

    public void onEvent(RackEvent re){
        if(re.getAction() == "select"){
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.container, AssetListFragment.newInstance(re.getRack().getExtId(), re.getRack().getSKU()),"asset_fragment")
                    .setBreadCrumbTitle(re.getRack().getSKU())
                    .addToBackStack("asset_fragment")
                    .commit();
            getSupportActionBar().setTitle(re.getRack().getSKU());
            Toast.makeText(this, re.getRack().getSKU(),Toast.LENGTH_SHORT ).show();

            current_rack = re.getRack().getExtId();
        }else if(re.getAction() == "refreshById"){
            refreshRack(re.getLocationId());
        }else if(re.getAction() == "refreshImage"){
            refreshImages(re.getRack().getExtId(),"rack");
        }

    }

    public void onEvent(AssetEvent ae){
        if(ae.getAction() == "select"){
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.container, AssetDetailFragment.newInstance(ae.getAsset().getExtId(), ae.getAsset().getSKU()), "asset_detail_fragment")
                    .setBreadCrumbTitle(ae.getAsset().getSKU())
                    .addToBackStack("asset_detail_fragment")
                    .commit();
            getSupportActionBar().setTitle(ae.getAsset().getSKU());
            Toast.makeText(this, ae.getAsset().getSKU(), Toast.LENGTH_SHORT).show();

            current_rack = ae.getAsset().getRackId();
        }else if(ae.getAction() == "refreshById"){
            refreshAsset(ae.getRackId());
        }else if(ae.getAction() == "syncAsset"){
            Asset asset = ae.getAsset();
            syncAsset( asset );
        }else if(ae.getAction() == "upsyncAsset"){
            Asset asset = ae.getAsset();
            upsyncAsset(asset);
        }else if(ae.getAction() == "refreshImage"){
            refreshImages(ae.getAsset().getExtId(),"asset");
        }else if(ae.getAction() == "moveRack"){
            Asset asset = ae.getAsset();
            String rackId = ae.getRackId();
            asset.setRackId(rackId);
            asset.setLocalEdit(1);
            asset.save();
        }

    }

    public void onEvent(ImageEvent im){
        if( im.getAction() == "sync" ){
            refreshImages( im.getEntityId() , im.getEntityType());
        }
        if( im.getAction() == "upsync" ){
            uploadImages();
        }
    }

    public void onEvent(MerchantEvent me){
        if(me.getAction() == "select"){
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.container, OrderListFragment.newInstance(me.getMerchant().getExtId(), me.getMerchant().getMerchantname()),"order_fragment")
                    .setBreadCrumbTitle(me.getMerchant().getMerchantname())
                    .addToBackStack("order_fragment")
                    .commit();
            getSupportActionBar().setTitle(me.getMerchant().getMerchantname());
            Toast.makeText(this, me.getMerchant().getMerchantname(),Toast.LENGTH_SHORT ).show();

            //current_merchant = me.getMerchant().getExtId();
        }
    }

    public void onEvent(DeviceTypeEvent dt){
        if("sync".equalsIgnoreCase(dt.getAction())){
            refreshAssetType();
        }
    }

    public void onEvent(OrderEvent oe){
        if(oe.getAction() == "select"){
            OrderItem o = oe.getOrderItem();
            String message = new StringBuilder("ID : ")
                    .append(o.getDeliveryId())
                    .append(System.lineSeparator())
                    .append("Type : ").append(o.getDeliveryType())
                    .append(System.lineSeparator())
                    .append("No Kode Toko : ").append(o.getMerchantTransId())
                    .append(System.lineSeparator())
                    .append("Zone : ").append(o.getBuyerdeliveryzone())
                    .append(System.lineSeparator())
                    .append("Shipping Address : ")
                    .append(System.lineSeparator())
                    .append(o.getShippingAddress())
                    .append(System.lineSeparator())
                    .append(o.getBuyerdeliverycity())
                    .toString();
            String device = o.getDeviceName();
            String courier = o.getCourierName();
            String merchant = o.getMerchantName();
            String deliveryid = o.getDeliveryId();
            String invoice = o.getMerchantTransId();
            String buyer = o.getBuyerName();
            String recipient = o.getRecipientName();
            String deliverydate = o.getAssignmentDate();
            String deliverytype = o.getDeliveryType();
            String zone = o.getBuyerdeliveryzone();

            try{
                deliverydate = deliverydate.substring(0,10);
            }catch(NullPointerException e){
                deliverydate = "";
            }
            showMessageDialog(message, device,
                    buyer, recipient,deliverydate, deliverytype, courier, merchant, deliveryid, invoice, zone);
            Log.i("dialog", "open");
            //Toast.makeText(this, oe.getOrderItem().toString(),Toast.LENGTH_SHORT ).show();
        }else if(oe.getAction() == "refreshById"){
            refreshOrders(oe.getMerchantId());
        }else if(oe.getAction()== "status"){
            Log.i("event delivery id", oe.getMerchantId());
            updateStatus(oe.getMerchantId());
        }
    }

    public void showMessageDialog(String message,String device,
                                  String buyer, String recipient,String deliverydate,
                                  String deliverytype,
                                  String courier,String merchant,String deliveryid,String invoice, String zone ) {
        /*
        DialogFragment fragment = MessageDialogFragment.newInstance("Order Info", message, device,
                buyer, recipient,deliverydate,
                deliverytype, courier, merchant, deliveryid, invoice,zone, this);
        fragment.show( getSupportFragmentManager(), "scan_results");*/

         String mMessage = message;
         String mDevice = device;
         String mCourier = courier;
         String mMerchant = merchant;
         final String mDeliveryId = deliveryid;
         String mInvoice = invoice;
         String mBuyerName = buyer;
         String mRecipientName = recipient;
         String mDeliveryDate = deliverydate;
         String mDeliveryType = deliverytype;
         String mDeliveryZone = zone;

        LayoutInflater inflater = this.getLayoutInflater();
        View mview = inflater.inflate(R.layout.dialog_message_layout,null);

        TextView txtDevice = (TextView) mview.findViewById(R.id.deviceValue);
        TextView txtCourier = (TextView) mview.findViewById(R.id.courierValue);
        TextView txtDeliveryId = (TextView) mview.findViewById(R.id.deliveryIdValue);
        TextView txtInvoice = (TextView) mview.findViewById(R.id.invoiceValue);
        TextView txtMerchant = (TextView) mview.findViewById(R.id.merchantValue);
        TextView txtDeliveryDate = (TextView) mview.findViewById(R.id.dateValue);
        TextView txtRecipientName = (TextView) mview.findViewById(R.id.recipientValue);
        TextView txtBuyerName = (TextView) mview.findViewById(R.id.buyerValue);
        TextView txtDeliveryType = (TextView) mview.findViewById(R.id.typeValue);
        TextView txtZone = (TextView) mview.findViewById(R.id.zoneValue);

        mDeliveryType = ("Delivery Only".equalsIgnoreCase(mDeliveryType))?"DO":mDeliveryType;

        txtDevice.setText(mDevice);
        txtCourier.setText(mCourier);
        txtDeliveryId.setText(mDeliveryId);
        txtMerchant.setText(mMerchant);
        txtInvoice.setText(mInvoice);
        txtBuyerName.setText(mBuyerName);
        txtRecipientName.setText(mRecipientName);
        txtDeliveryDate.setText(mDeliveryDate);
        txtDeliveryType.setText(mDeliveryType);
        txtZone.setText(mDeliveryZone);


        if("COD".equalsIgnoreCase(mDeliveryType) || "CCOD".equalsIgnoreCase(mDeliveryType) ){
            txtDeliveryType.setBackgroundColor( getResources().getColor(R.color.red) );
            txtDeliveryType.setTextColor( getResources().getColor(R.color.white) );
        }else{
            txtDeliveryType.setBackgroundColor( getResources().getColor(R.color.green) );
            txtDeliveryType.setTextColor(getResources().getColor(R.color.white));
        }

        new MaterialDialog.Builder(this)
                .title(R.string.dialog_order_title)
                .positiveText(R.string.ok_button)
                .positiveColor(R.color.green)
                .negativeText(R.string.cancel_button)
                .negativeColor(R.color.red)
                .customView(mview, true)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  //super.onPositive(dialog);
                                  String deliveryId = mDeliveryId;
                                  Log.i("current delivery Id", deliveryId);
                                  updateStatus(deliveryId);
                                  EventBus.getDefault().post(new ScannerEvent("resume"));

                              }

                              @Override
                              public void onNegative(MaterialDialog dialog) {
                                  EventBus.getDefault().post(new ScannerEvent("resume"));
                                  super.onNegative(dialog);
                              }

                              @Override
                              public void onNeutral(MaterialDialog dialog) {
                                  super.onNeutral(dialog);
                              }
                          }

                )
                .show();
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
        Log.i("dialog", "closed");
        EventBus.getDefault().post(new ScannerEvent("resume"));
    }

    /*
    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog, String mDeliveryId) {
        // Resume the camera
        String deliveryId = mDeliveryId;
        Log.i("current delivery Id", deliveryId);
        updateStatus(deliveryId);
        EventBus.getDefault().post(new ScannerEvent("resume"));
    }
    */




    public void refreshMerchants(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.jwh_api_base_url))
                .build();
        JwhApiInterface jwhApiInterface = restAdapter.create(JwhApiInterface.class);

        setProgressVisibility(true);

        jwhApiInterface.getMerchants(new Callback<List<Merchant>>() {
            @Override
            public void success(List<Merchant> merchants, Response response) {
                Log.i("merchants retrieved", String.valueOf(merchants.size()) );
                for(int i = 0; i < merchants.size(); i++){

                    Select select = Select.from(Merchant.class).where(Condition.prop("ext_id").eq(merchants.get(i).getExtId()))
                            .limit("1");

                    if(select.count() > 0){
                        Merchant mcb = (Merchant) select.first();
                        Merchant mc = merchants.get(i);

                        mcb.setMcCity(mc.getMcCity());
                        mcb.setMcDistrict(mc.getMcDistrict());
                        mcb.setMcEmail(mc.getMcEmail());
                        mcb.setMcPhone(mc.getMcPhone());
                        mcb.setMcStreet(mc.getMcStreet());
                        mcb.setMerchantname(mc.getMerchantname());
                        mcb.setMcMobile(mc.getMcMobile());
                        mcb.setProvince(mc.getProvince());

                        mcb.save();
                        Log.i("merchant updated",mc.getMerchantname());
                    }else{
                        merchants.get(i).save();
                        Log.i("merchant saved", merchants.get(i).getMerchantname());
                    }
                }
                setProgressVisibility(false);

                EventBus.getDefault().post(new MerchantEvent("refresh"));

            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisibility(false);
                Log.i("merchant get failure", error.toString());
                EventBus.getDefault().post(new MerchantEvent("refresh"));
            }
        });

    }

    public void refreshOrders(String merchantId){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.jwh_api_base_url))
                .build();
        JwhApiInterface jwhApiInterface = restAdapter.create(JwhApiInterface.class);

        Log.i("order merchant",merchantId);

        setProgressVisibility(true);

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        jwhApiInterface.getOrders("JY-DEV",merchantId, today , new Callback<List<OrderItem>>() {
            @Override
            public void success(List<OrderItem> orderItems, Response response) {
                try{
                    Log.i("orders retrieved", String.valueOf(orderItems.size()) );
                    for(int i = 0; i < orderItems.size(); i++){

                        Select select = Select.from(OrderItem.class).where(Condition.prop("delivery_id").eq(orderItems.get(i).getDeliveryId()))
                                .limit("1");

                        if(select.count() > 0){
                            OrderItem ocb = (OrderItem) select.first();
                            OrderItem oc = orderItems.get(i);

                            ocb.setDeviceId(oc.getDeviceId());
                            ocb.setDeviceName(oc.getDeviceName());
                            ocb.setCourierId(oc.getCourierId());
                            ocb.setCourierName(oc.getCourierName());
                            ocb.setBuyerdeliveryzone(oc.getBuyerdeliveryzone());
                            ocb.setStatus(oc.getStatus());
                            ocb.setPickupStatus(oc.getPickupStatus());

                            ocb.save();
                            Log.i("merchant updated",oc.toString());
                        }else{
                            orderItems.get(i).save();

                            Log.i("order saved", orderItems.get(i).toString());
                        }
                    }
                    setProgressVisibility(false);

                    EventBus.getDefault().post(new OrderEvent("refresh"));

                }catch(Exception e){
                    setProgressVisibility(false);
                    Toast.makeText(MainActivity.this,getResources().getString(R.string.no_order_found),Toast.LENGTH_SHORT).show();
                    Log.i("Order Exception",e.toString());
                    EventBus.getDefault().post(new OrderEvent("refresh"));
                }

            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisibility(false);
                Log.i("order get failure", error.toString());
                EventBus.getDefault().post(new OrderEvent("refresh"));
            }
        });

    }

    public void refreshLocations(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();
        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        amApiInterface.getLocation(new Callback<List<Location>>() {
            @Override
            public void success(List<Location> locations, Response response) {
                Log.i("REFRESH LOCATION","GET LOCATION SUCCESS");
                for(int i = 0; i < locations.size(); i++){

                    Select select = Select.from(Location.class).where(Condition.prop("ext_id").eq(locations.get(i).getExtId()))
                            .limit("1");

                    if(select.count() > 0){
                        Location loc = (Location) select.first();
                        Location lin = locations.get(i);

                        loc.setName(lin.getName());
                        loc.setSlug(lin.getSlug());
                        loc.setAddress(lin.getAddress());
                        loc.setCategory(lin.getCategory());
                        loc.setDescription(lin.getDescription());
                        loc.setLatitude(lin.getLatitude());
                        loc.setLongitude(lin.getLongitude());
                        loc.setPhone(lin.getPhone());
                        loc.setTags(lin.getTags());
                        loc.setVenue(lin.getVenue());
                        loc.setPictureThumbnailUrl(lin.getPictureThumbnailUrl());
                        loc.setPictureMediumUrl(lin.getPictureMediumUrl());
                        loc.setPictureLargeUrl(lin.getPictureLargeUrl());
                        loc.setPictureFullUrl(lin.getPictureFullUrl());

                        loc.save();
                    }else{
                        locations.get(i).save();
                    }
                }
                setProgressVisibility(false);

                EventBus.getDefault().post(new LocationEvent("refresh"));
            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisibility(false);
                Log.i("order get failure", error.toString());
                EventBus.getDefault().post(new LocationEvent("refresh"));
            }
        });
    }

    public void refreshRack(String locationId){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();
        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        amApiInterface.getRack(new Callback<List<Rack>>() {
            @Override
            public void success(List<Rack> racks, Response response) {
                for (int i = 0; i < racks.size(); i++) {

                    Select select = Select.from(Rack.class).where(Condition.prop("ext_id").eq(racks.get(i).getExtId()))
                            .limit("1");

                    if (select.count() > 0) {
                        Rack rob = (Rack) select.first();
                        Rack rin = racks.get(i);

                        rob.setItemDescription(rin.getItemDescription());
                        rob.setSKU(rin.getSKU());
                        rob.setLocationId(rin.getLocationId());
                        rob.setTags(rin.getTags());
                        rob.setLocationName(rin.getLocationName());
                        rob.setStatus(rin.getStatus());
                        rob.save();

                    } else {
                        racks.get(i).save();
                    }

                }
                setProgressVisibility(false);
                EventBus.getDefault().post(new RackEvent("refresh"));

            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisibility(false);
                Log.i("order get failure", error.toString());
                EventBus.getDefault().post(new RackEvent("refresh"));
            }
        });
    }

    public void refreshAsset(String rackId){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();
        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        amApiInterface.getAsset(new Callback<List<Asset>>() {
            @Override
            public void success(List<Asset> assets, Response response) {
                for (int i = 0; i < assets.size(); i++) {

                    Select select = Select.from(Asset.class)
                            .where(Condition.prop("ext_id").eq(assets.get(i).getExtId()))
                            .limit("1");

                    if (select.count() > 0) {
                        Asset aso = (Asset) select.first();
                        Asset asi = assets.get(i);

                        aso.setIP(asi.getIP());
                        aso.setSKU(asi.getSKU());
                        aso.setHostName(asi.getHostName());
                        aso.setTags(asi.getTags());
                        aso.setItemDescription(asi.getItemDescription());
                        aso.setAssetType(asi.getAssetType());
                        aso.setStatus(asi.getStatus());
                        aso.setLocationId(asi.getLocationId());
                        aso.setOwner(asi.getOwner());
                        aso.setRackId(asi.getRackId());

                        if(aso.getLocalEdit() == 0){
                            aso.setLocalEdit(0);
                            aso.setUploaded(1);
                            aso.save();
                        }

                        if( asi.getPictureMediumUrl() == null || asi.getPictureMediumUrl() == "" || asi.getPictureMediumUrl() == "null" ){

                        }else{

                            Select seldef = Select.from(AssetImages.class)
                                    .where(Condition.prop("is_default").eq(1),
                                            Condition.prop("ext_id").eq(asi.getExtId()) );

                            if(seldef.count() > 0 ){
                                AssetImages defimg = (AssetImages) seldef.first();

                                Log.i("DEF IMAGE", defimg.getExtId() + " " + String.valueOf(defimg.getIsDefault()) + " " + asi.getPictureMediumUrl() );

                                defimg.setExtUrl(asi.getPictureMediumUrl());
                                defimg.save();
                            }else{
                                AssetImages am = new AssetImages();
                                am.setExtId(asi.getExtId());
                                am.setExtUrl(asi.getPictureMediumUrl());
                                am.setUploaded(1);
                                am.setIsLocal(0);
                                am.setIsDefault(1);
                                am.save();
                            }

                        }



                    } else {
                        Asset newasset = assets.get(i);
                        newasset.setLocalEdit(0);
                        newasset.setUploaded(1);
                        newasset.save();

                        if( "".equalsIgnoreCase( newasset.getPictureMediumUrl() ) == false ){
                            AssetImages am = new AssetImages();
                            am.setExtId(newasset.getExtId());
                            am.setExtUrl(newasset.getPictureMediumUrl());
                            am.setUploaded(1);
                            am.setIsLocal(0);
                            am.save();
                        }
                    }

                }

                setProgressVisibility(false);
                EventBus.getDefault().post(new AssetEvent("refresh"));

            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisibility(false);
                Log.i("order get failure", error.toString());
                EventBus.getDefault().post(new AssetEvent("refresh"));
            }
        });
    }

    public void refreshAssetType(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();
        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        amApiInterface.getAssetType(new Callback<List<DeviceType>>() {
            @Override
            public void success(List<DeviceType> deviceTypes, Response response) {
                DeviceType.deleteAll(DeviceType.class);

                for (int i = 0; i < deviceTypes.size(); i++) {
                    DeviceType deviceType = deviceTypes.get(i);
                    deviceType.save();
                }

                setProgressVisibility(false);
                EventBus.getDefault().post(new DeviceTypeEvent("refresh"));

            }

            @Override
            public void failure(RetrofitError error) {
                setProgressVisibility(false);
            }
        });
    }

    public void refreshImages(String entityId, String entityType ){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();
        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        amApiInterface.getImage(entityId, entityType, new Callback<List<AssetImages>>() {
            @Override
            public void success(List<AssetImages> assetImages, Response response) {

                for(int i=0;i< assetImages.size();i++ ){
                    Select select = Select.from(AssetImages.class)
                            .where(Condition.prop("parent_id").eq(assetImages.get(i).getParentId()),
                                    Condition.prop("ext)id").eq(assetImages.get(i).getExtId()) )
                            .limit("1");

                    if (select.count() > 0) {
                        AssetImages aso = (AssetImages) select.first();
                        AssetImages asi = assetImages.get(i);

                        aso.setName(asi.getName());
                        aso.setSize(asi.getSize());
                        aso.setNs(asi.getNs());
                        aso.setType(asi.getType());
                        aso.setParentClass(asi.getParentClass());
                        aso.setFileId(asi.getFileId());
                        aso.setUrl(asi.getUrl());
                        aso.setExtUrl(asi.getUrl());
                        aso.setPictureFullUrl(asi.getPictureFullUrl());
                        aso.setPictureLargeUrl(asi.getPictureLargeUrl());
                        aso.setPictureMediumUrl(asi.getPictureMediumUrl());
                        aso.setPictureThumbnailUrl(asi.getPictureThumbnailUrl());

                        aso.setTemp_dir(asi.getTemp_dir());
                        aso.setIsDefault(asi.getIsDefault());
                        aso.setIsImage(asi.getIsImage());
                        aso.setIsAudio(asi.getIsAudio());
                        aso.setIsVideo(asi.getIsVideo());
                        aso.setIsDoc(asi.getIsDoc());
                        aso.setIsPdf(asi.getIsPdf());

                        aso.setIsLocal(0);
                        aso.setUploaded(1);
                        aso.setDeleted(asi.getDeleted());
                        aso.save();

                        Asset as =  (Asset) Select.from(Asset.class).where(Condition.prop("extId").eq(asi.getParentId())).first();

                        if(as != null){
                            as.setPictureFullUrl(asi.getPictureFullUrl());
                            as.setPictureLargeUrl(asi.getPictureLargeUrl());
                            as.setPictureMediumUrl(asi.getPictureMediumUrl());
                            as.setPictureThumbnailUrl(asi.getPictureThumbnailUrl());
                            as.save();
                        }

                    }else{
                        assetImages.get(i).save();
                        AssetImages asi = assetImages.get(i);

                        Asset as =  (Asset) Select.from(Asset.class).where(Condition.prop("extId").eq(asi.getParentId())).first();

                        if(as != null){
                            as.setPictureFullUrl(asi.getPictureFullUrl());
                            as.setPictureLargeUrl(asi.getPictureLargeUrl());
                            as.setPictureMediumUrl(asi.getPictureMediumUrl());
                            as.setPictureThumbnailUrl(asi.getPictureThumbnailUrl());
                            as.save();
                        }
                    }

                }
                setProgressVisibility(false);
            }

            @Override
            public void failure(RetrofitError error) {

                setProgressVisibility(false);
            }
        });

    }

    public void uploadImages(){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setLogLevel(RestAdapter.LogLevel.HEADERS)
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();

        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        List<AssetImages> images = Select.from(AssetImages.class).where(Condition.prop("uploaded").eq(0)).list();

        Log.i("IMAGE COUNT", String.valueOf(images.size()) );

        for(int i = 0; i < images.size();i++){
            AssetImages aim = images.get(i);

            Uri furi = Uri.parse(aim.getUri());

            Log.i("UPLOADED IMAGE URI", aim.getUri());

            TypedFile imagefile = new TypedFile("image/jpg", new File( aim.getUri() ) );

            Log.i("UPLOADED IMAGE STRING", imagefile.toString());

            amApiInterface.uploadImage(aim.getNs(),
                    aim.getParentId(),
                    aim.getParentClass(),
                    aim.getFileId(),
                    aim.getExtId(),
                    imagefile, new Callback<ResultObject>() {
                @Override
                public void success(ResultObject resultObject, Response response) {
                    Log.i("MESSAGE", resultObject.getMessage());
                    setProgressVisibility(false);
                }

                @Override
                public void failure(RetrofitError error) {
                    Log.i("UPLOAD ERROR", error.toString());
                    setProgressVisibility(false);
                }
            });

        }

    }

    public void syncAsset(Asset asset){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();

        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        amApiInterface.sendAsset(asset, new Callback<ResultObject>() {
            @Override
            public void success(ResultObject resultObject, Response response) {
                Log.i("SYNC RESULT", resultObject.getStatus());
                setProgressVisibility(false);

            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("SYNC ERR", error.toString() );
                setProgressVisibility(false);

            }
        });

    }

    public void upsyncAsset(Asset asset){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.api_base_url))
                .build();

        AmApiInterface amApiInterface = restAdapter.create(AmApiInterface.class);

        setProgressVisibility(true);

        amApiInterface.updateAsset( asset.getExtId() ,asset, new Callback<ResultObject>() {
            @Override
            public void success(ResultObject resultObject, Response response) {
                Log.i("UPSYNC RESULT", resultObject.getStatus());
                setProgressVisibility(false);
            }

            @Override
            public void failure(RetrofitError error) {
                Log.i("UPSYNC ERR", error.toString() );
                setProgressVisibility(false);
            }
        });

    }

    public void updateStatus(String deliveryId){
        RestAdapter restAdapter = new RestAdapter.Builder()
                .setEndpoint(getResources().getString(R.string.jwh_api_base_url))
                .build();
        JwhApiInterface jwhApiInterface = restAdapter.create(JwhApiInterface.class);

        Log.i("order delivery id",deliveryId);

        setProgressVisibility(true);

        String today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());

        jwhApiInterface.setStatus(getResources().getString(R.string.wh_status_accept_wh), deliveryId, new Callback<ResultObject>() {
                    @Override
                    public void success(ResultObject resultObject, Response response) {
                        Log.i("update status result", resultObject.getStatus());
                        setProgressVisibility(false);
                    }

                    @Override
                    public void failure(RetrofitError error) {
                        Log.i("update status error",error.toString());
                        setProgressVisibility(false);
                    }
                }
            );

        Select select = Select.from(OrderItem.class).where(Condition.prop("delivery_id").eq(deliveryId))
                .limit("1");
        if(select.count() > 0){
            OrderItem oi = (OrderItem) select.first();
            oi.setWarehouseStatus(getResources().getString(R.string.wh_status_long_accept_wh));
            oi.save();
            EventBus.getDefault().post(new OrderEvent("refresh",oi.getMerchantId()));
        }
    }

    private void setProgressVisibility(Boolean v){
        if(v == true){
            mProgressBar.setVisibility(View.VISIBLE);
        }else{
            mProgressBar.setVisibility(View.GONE);
        }
    }

    public static void extractDb(File sourceFile, File destFile) {

        FileChannel source = null;
        FileChannel destination = null;

        try {
            if (!destFile.exists()) {
                destFile.createNewFile();
            }

            source = new FileInputStream(sourceFile).getChannel();
            destination = new FileOutputStream(destFile).getChannel();
            destination.transferFrom(source, 0, source.size());

        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (source != null) {
                    source.close();
                }
                if (destination != null) {
                    destination.close();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public void doScan(String id, String mode){

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ScannerFragment.newInstance(id, mode), "scan_fragment")
                .addToBackStack("scan_fragment")
                .setBreadCrumbTitle(getResources().getString(R.string.action_scan))
                .commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.action_scan));
    }


    private void showLogin(){
        Intent intent;
        intent = new Intent(this,LoginActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
        startActivity(intent);
        //finish();
    }

    private void doSignOut(){
        SharedPreferences.Editor editor = spref.edit();
        editor.putString("auth","");
        editor.putString("uid","");
        editor.commit();
        showLogin();
    }

}
