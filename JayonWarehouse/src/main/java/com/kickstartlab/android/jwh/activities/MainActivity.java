package com.kickstartlab.android.jwh.activities;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuCompat;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.kickstartlab.android.jwh.R;
import com.kickstartlab.android.jwh.events.MerchantEvent;
import com.kickstartlab.android.jwh.events.OrderEvent;
import com.kickstartlab.android.jwh.events.ScannerEvent;
import com.kickstartlab.android.jwh.fragments.AssetListFragment;
import com.kickstartlab.android.jwh.fragments.LocationListFragment;
import com.kickstartlab.android.jwh.fragments.MerchantListFragment;
import com.kickstartlab.android.jwh.fragments.MessageDialogFragment;
import com.kickstartlab.android.jwh.fragments.OrderListFragment;
import com.kickstartlab.android.jwh.fragments.RackListFragment;
import com.kickstartlab.android.jwh.fragments.ScannerFragment;
import com.kickstartlab.android.jwh.rest.interfaces.AmApiInterface;
import com.kickstartlab.android.jwh.rest.interfaces.JwhApiInterface;
import com.kickstartlab.android.jwh.rest.models.Asset;
import com.kickstartlab.android.jwh.rest.models.Location;
import com.kickstartlab.android.jwh.rest.models.Merchant;
import com.kickstartlab.android.jwh.rest.models.OrderItem;
import com.kickstartlab.android.jwh.rest.models.Rack;
import com.kickstartlab.android.jwh.rest.models.ResultObject;
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


public class MainActivity extends ActionBarActivity implements
        FragmentManager.OnBackStackChangedListener,
        MessageDialogFragment.MessageDialogListener {

    private static final String DATABASE_NAME = "jwh.db";
    private static final String SCREEN_LOCATION = "location";
    private static final String SCREEN_SCAN = "scan";

    private Integer current_merchant;

    Toolbar mToolbar;

    SmoothProgressBar mProgressBar;

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

        if (savedInstanceState == null) {
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.container, new MerchantListFragment(),"merchant_fragment")
                    .setBreadCrumbTitle(getResources().getString(R.string.app_name))
                    //.addToBackStack("merchant_fragment")
                    .commit();
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
            Intent i = new Intent(this, SettingsActivity.class );
            startActivity(i);
        }

        if(id == R.id.action_refresh){
            refreshOrders(String.valueOf(current_merchant));
        }

        if(id == R.id.action_scan){
            doScan(String.valueOf(current_merchant));
        }

        if(id == R.id.action_merchant){

            refreshMerchants();
        }

        /*
        if(id == R.id.action_location){

            refreshLocations();
        }

        if(id == R.id.action_rack){
            refreshRack();
        }

        if(id == R.id.action_asset){
            refreshAsset();
        }
        */

        if(id == android.R.id.home){
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onDestroy() {
        File source =  new File("data/data/com.kickstartlab.android.jwh/databases/" + DATABASE_NAME);
        File dest =  new File(Environment.getExternalStorageDirectory() + "/" + DATABASE_NAME);

        Log.i("save db",dest.toString());

        extractDb(source, dest);

        super.onDestroy();
    }

    /* EVENTS */

    public void onEvent(MerchantEvent me){
        if(me.getAction() == "select"){
            FragmentManager fm = getSupportFragmentManager();

            fm.beginTransaction()
                    .replace(R.id.container, OrderListFragment.newInstance(me.getMerchant().getExtId(),me.getMerchant().getMerchantname()),"order_fragment")
                    .setBreadCrumbTitle(me.getMerchant().getMerchantname())
                    .addToBackStack("order_fragment")
                    .commit();
            getSupportActionBar().setTitle(me.getMerchant().getMerchantname());
            Toast.makeText(this, me.getMerchant().getMerchantname(),Toast.LENGTH_SHORT ).show();

            current_merchant = me.getMerchant().getExtId();
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
        DialogFragment fragment = MessageDialogFragment.newInstance("Order Info", message, device,
                buyer, recipient,deliverydate,
                deliverytype, courier, merchant, deliveryid, invoice,zone, this);
        fragment.show( getSupportFragmentManager(), "scan_results");
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

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog, String mDeliveryId) {
        // Resume the camera
        String deliveryId = mDeliveryId;
        Log.i("current delivery Id", deliveryId);
        updateStatus(deliveryId);
        EventBus.getDefault().post(new ScannerEvent("resume"));
    }





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

    public void doScan(String id){
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.container, ScannerFragment.newInstance(id, getResources().getString(R.string.action_scan)), "scan_fragment")
                .addToBackStack("scan_fragment")
                .setBreadCrumbTitle(getResources().getString(R.string.action_scan))
                .commit();
        getSupportActionBar().setTitle(getResources().getString(R.string.action_scan));
    }


    public void onAssetInteraction(String id){

    }

    public void onAssetInfoInteraction(Uri uri){

    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            return rootView;
        }
    }
}
