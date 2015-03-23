package com.kickstartlab.android.assets.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.events.AssetEvent;
import com.kickstartlab.android.assets.events.ScannerEvent;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.AssetImages;
import com.kickstartlab.android.assets.rest.models.DeviceType;
import com.kickstartlab.android.assets.ui.SquareImageView;
import com.kickstartlab.android.assets.utils.DbDateUtil;
import com.kickstartlab.android.assets.utils.RandomStringGenerator;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.rengwuxian.materialedittext.MaterialEditText;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;
import nl.changer.polypicker.ImagePickerActivity;

public class AddAssetActivity extends ActionBarActivity {

    private static final int INTENT_REQUEST_GET_CODE = 4554;
    private int INTENT_REQUEST_GET_IMAGES = 1667;

    Toolbar mToolbar;
    SmoothProgressBar mProgressBar;

    MaterialEditText sku,desc,ip,host,os,pic,pic_email,pic_phone,contract,owner;

    Spinner asset_type;

    Asset asset;

    String rackId, rackName , extId;

    LinearLayout image_container;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_edit_asset);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mProgressBar = (SmoothProgressBar) findViewById(R.id.loadProgressBar);

        mProgressBar.setVisibility(View.GONE);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        Bundle extra = getIntent().getExtras();

        rackId = extra.getString("rackId");
        rackName = extra.getString("rackName");

        //create local extId
        extId = RandomStringGenerator.generateRandomString(24, RandomStringGenerator.Mode.HEX);

        extId = extId.toLowerCase();

        this.getSupportActionBar().setTitle("Add Asset");

        this.getSupportActionBar().setHomeButtonEnabled(true);

        LinearLayout detail_container = (LinearLayout) findViewById(R.id.detail_container);
        image_container = (LinearLayout) findViewById(R.id.image_container);

        sku = makeEditText(this,"Serial Number / Asset Code");
        desc = makeEditText(this,"Description");
        ip = makeEditText(this, "IP Address");
        host = makeEditText(this, "Host");
        os = makeEditText(this, "OS");
        pic = makeEditText(this, "PIC");
        pic_email = makeEditText(this, "PIC Email");
        pic_phone = makeEditText(this, "PIC Phone");
        contract = makeEditText(this, "Contract Number");
        //asset_type = makeEditText(this,"Asset Type");
        owner = makeEditText(this,"Owner");

        asset_type = new Spinner(this);


        List<DeviceType> types = Select.from(DeviceType.class)
                .orderBy("type collate nocase").list();

        TextView asset_type_label = new TextView(this);
        asset_type_label.setTextColor(R.color.primary_dark);
        asset_type_label.setTextSize(11);
        asset_type_label.setText("Asset Type");

        ArrayAdapter<DeviceType> typeAdapter = new ArrayAdapter<DeviceType>(this,android.R.layout.simple_spinner_dropdown_item, types );

        asset_type.setAdapter(typeAdapter);

        detail_container.addView(sku);
        detail_container.addView(ip);
        detail_container.addView(host);
        detail_container.addView(os);
        detail_container.addView(contract);
        detail_container.addView(owner);
        detail_container.addView(pic);
        detail_container.addView(pic_email);
        detail_container.addView(pic_phone);

        detail_container.addView(asset_type_label);
        detail_container.addView(asset_type);
        //detail_container.addView(type);
        detail_container.addView(desc);

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fab_save_item);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                saveAsset();
                /*
                asset = new Asset();

                String createdDate = DbDateUtil.getDateTime();
                String lastUpdate = DbDateUtil.getDateTime();

                asset.setRackId(rackId);
                asset.setCreatedDate(createdDate);
                asset.setLastUpdate(lastUpdate);
                asset.setExtId(extId);
                asset.setSKU(sku.getText().toString());
                asset.setIP(ip.getText().toString());
                asset.setHostName(host.getText().toString());
                asset.setOS(os.getText().toString());
                asset.setContractNumber(contract.getText().toString());
                asset.setOwner(owner.getText().toString());
                asset.setPIC(pic.getText().toString());
                asset.setPicEmail(pic_email.getText().toString());
                asset.setPicPhone(pic_phone.getText().toString());
                asset.setAssetType( asset_type.getSelectedItem().toString() );
                asset.setItemDescription(desc.getText().toString());

                asset.setLocalEdit(1);
                asset.setUploaded(0);

                asset.save();

                Log.i("asset",asset.getIP());

                EventBus.getDefault().post(new AssetEvent("refresh"));
                finish();

                * */
            }
        });

        FloatingActionButton fabScanSerial = (FloatingActionButton) findViewById(R.id.fab_add_serial);

        fabScanSerial.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ScannerActivity.class);
                startActivityForResult(intent, INTENT_REQUEST_GET_CODE);
            }
        });

        FloatingActionButton fabAddImage = (FloatingActionButton) findViewById(R.id.fab_add_image);
        fabAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(context, ImagePickerActivity.class);
                intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 3);  // allow only upto 3 images to be selected.
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
            }
        });



    }

    public void saveAsset(){
        asset = new Asset();

        String createdDate = DbDateUtil.getDateTime();
        String lastUpdate = DbDateUtil.getDateTime();

        asset.setRackId(rackId);
        asset.setCreatedDate(createdDate);
        asset.setLastUpdate(lastUpdate);
        asset.setExtId(extId);
        asset.setSKU(sku.getText().toString());
        asset.setIP(ip.getText().toString());
        asset.setHostName(host.getText().toString());
        asset.setOS(os.getText().toString());
        asset.setContractNumber(contract.getText().toString());
        asset.setOwner(owner.getText().toString());
        asset.setPIC(pic.getText().toString());
        asset.setPicEmail(pic_email.getText().toString());
        asset.setPicPhone(pic_phone.getText().toString());
        asset.setAssetType( asset_type.getSelectedItem().toString() );
        asset.setItemDescription(desc.getText().toString());

        asset.setLocalEdit(1);
        asset.setUploaded(0);

        asset.save();
        //Log.i("asset",asset.getIP());
        EventBus.getDefault().post(new AssetEvent("refresh"));
        EventBus.getDefault().post(new AssetEvent("syncAsset",asset));

        finish();

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_edit_asset, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        if(id == R.id.action_save_asset){
            saveAsset();
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if( resultCode == Activity.RESULT_OK ){
            if(requestCode == INTENT_REQUEST_GET_CODE){
                Bundle bundle = data.getExtras();
                sku.setText( bundle.getString("resultText"));
            }

            if(requestCode == INTENT_REQUEST_GET_IMAGES){
                Parcelable[] parcelableUris = data.getParcelableArrayExtra(ImagePickerActivity.EXTRA_IMAGE_URIS);

                if(parcelableUris == null) {
                    return;
                }

                Uri[] uris = new Uri[parcelableUris.length];
                System.arraycopy(parcelableUris, 0, uris, 0, parcelableUris.length);

                if(uris != null) {
                    for(Uri uri : uris){
                        Select select = Select.from(AssetImages.class)
                                .where(Condition.prop("uri").eq(uri.toString()))
                                .limit("1");

                        if(select.count() > 0){

                        }else{
                            AssetImages aim = new AssetImages();
                            aim.setExtId(extId);
                            aim.setUri( uri.toString());
                            aim.setIsLocal(1);
                            aim.setUploaded(0);
                            aim.save();

                        }

                    }

                    //EventBus.getDefault().post(new AssetEvent("refreshImage"));
                    refreshImage();

                }
            }

        }
    }

    public void refreshImage(){
        image_container.removeAllViews();

        Log.i("IMAGE","REFRESH");

        Select select = Select.from(AssetImages.class).where(Condition.prop("ext_id").eq(extId));

        Log.i( "IMG", String.valueOf(select.count()) );

        if(select.count() > 0){

            List<AssetImages> aim = select.list();
            for(int im = 0; im < select.count();im++ ){
                AssetImages am = aim.get(im);

                if( ( "".equalsIgnoreCase(am.getExtUrl() ) || am.getExtUrl() == null ) == false){

                    Log.i("IMAGE ITEM EXT", am.getExtUrl());
                    SquareImageView defpic = new SquareImageView(context);
                    image_container.addView(defpic);
                    Picasso.with(context)
                            .load(am.getExtUrl())
                            .fit()
                            .centerCrop()
                            .into(defpic);
                }else{
                    if( ("".equalsIgnoreCase( am.getUri() ) || am.getUri() == null ) == false){

                        Log.i("IMAGE ITEM INT", am.getUri());
                        SquareImageView defpic = new SquareImageView(context);
                        image_container.addView(defpic);
                        Picasso.with(context)
                                .load("file://" + am.getUri())
                                .fit()
                                .centerCrop()
                                .into(defpic);
                    }
                }
            }
        }
    }


    public MaterialEditText makeEditText(Context context,String label){
        MaterialEditText editText = new MaterialEditText(context);
        editText.setBaseColor(R.color.black);
        editText.setFloatingLabelText(label);
        editText.setFloatingLabel(MaterialEditText.FLOATING_LABEL_HIGHLIGHT);
        editText.setFloatingLabelAlwaysShown(true);
        return editText;
    }
}
