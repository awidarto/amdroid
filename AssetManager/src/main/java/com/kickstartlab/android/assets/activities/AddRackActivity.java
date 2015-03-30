package com.kickstartlab.android.assets.activities;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import com.kickstartlab.android.assets.events.ImageEvent;
import com.kickstartlab.android.assets.events.RackEvent;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.AssetImages;
import com.kickstartlab.android.assets.rest.models.DeviceType;
import com.kickstartlab.android.assets.rest.models.Rack;
import com.kickstartlab.android.assets.ui.LabeledSwitchView;
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

public class AddRackActivity extends ActionBarActivity {

    private static final int INTENT_REQUEST_GET_CODE = 4554;
    private int INTENT_REQUEST_GET_IMAGES = 1667;

    Toolbar mToolbar;
    SmoothProgressBar mProgressBar;
    TextView mProgressIndicator;

    MaterialEditText sku,desc;

    LabeledSwitchView rackStatus;

    Spinner asset_type;

    Rack rack;

    String locationId, locationName , extId, yes, no;

    LinearLayout image_container;

    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        context = this;

        setContentView(R.layout.activity_edit_asset);

        mToolbar = (Toolbar) findViewById(R.id.toolbar);

        mProgressBar = (SmoothProgressBar) findViewById(R.id.loadProgressBar);
        mProgressIndicator = (TextView) findViewById(R.id.progressMessage);

        mProgressBar.setVisibility(View.GONE);
        mProgressIndicator.setVisibility(View.GONE);

        if (mToolbar != null) {
            setSupportActionBar(mToolbar);
        }

        Bundle extra = getIntent().getExtras();

        locationId = extra.getString("locationId");
        locationName = extra.getString("locationName");

        //create local extId
        extId = RandomStringGenerator.generateRandomString(24, RandomStringGenerator.Mode.HEX);

        extId = extId.toLowerCase();

        this.getSupportActionBar().setTitle("Add Rack");

        this.getSupportActionBar().setHomeButtonEnabled(true);

        LinearLayout detail_container = (LinearLayout) findViewById(R.id.detail_container);
        image_container = (LinearLayout) findViewById(R.id.image_container);

        sku = makeEditText(this,"Serial Number / Rack Code");
        desc = makeEditText(this,"Description");

        yes = getResources().getString(R.string.label_active);
        no = getResources().getString(R.string.label_inactive);

        rackStatus = makeSwitch(this, getResources().getString(R.string.rack_status) , 0, yes, no );

        detail_container.addView(sku);
        detail_container.addView(rackStatus);
        detail_container.addView(desc);

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fab_save_item);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                saveRack();
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

    public void saveRack(){
        rack = new Rack();

        String createdDate = DbDateUtil.getDateTime();
        String lastUpdate = DbDateUtil.getDateTime();


        rack.setStatus((rackStatus.isChecked()) ? yes : no);

        rack.setLocalEdit(1);
        rack.setUploaded(0);

        rack.setLocationId(locationId);
        rack.setLocationName(locationName);
        rack.setCreatedDate(createdDate);
        rack.setLastUpdate(lastUpdate);
        rack.setExtId(extId);
        rack.setSKU(sku.getText().toString());
        rack.setItemDescription(desc.getText().toString());
        rack.setStatus((rackStatus.isChecked() ? yes : no));

        rack.setLocalEdit(1);
        rack.setUploaded(0);

        rack.save();

        EventBus.getDefault().postSticky(new RackEvent("refreshById", locationId));
        EventBus.getDefault().post(new RackEvent("syncRack",rack));

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
            saveRack();
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

                            String file_id = RandomStringGenerator.generateRandomString(15, RandomStringGenerator.Mode.HEX).toLowerCase();
                            String upload_id = RandomStringGenerator.generateRandomString(24, RandomStringGenerator.Mode.HEX).toLowerCase();

                            aim.setNs("rackpic");
                            aim.setParentClass("rack");
                            aim.setParentId(extId);
                            aim.setFileId(file_id);
                            aim.setExtId(upload_id);
                            aim.setUri(uri.toString());
                            aim.setIsLocal(1);
                            aim.setUploaded(0);
                            aim.setDeleted(0);
                            aim.save();

                        }

                    }

                    //EventBus.getDefault().post(new AssetEvent("refreshImage"));
                    refreshImage();

                }
            }

        }
    }

    public void onEvent(ImageEvent im){

        if( im.getAction() == "refreshImageView" ){
            refreshImage();
        }

    }

    public void refreshImage(){
        image_container.removeAllViews();

        Log.i("IMAGE","REFRESH");

        //Select select = Select.from(AssetImages.class).where(Condition.prop("ext_id").eq(extId));
        Select select = Select.from(AssetImages.class).where(Condition.prop("parent_id").eq(extId),
                Condition.prop("parent_class").eq("asset"),
                Condition.prop("deleted").eq(0));

        Log.i( "IMG", String.valueOf(select.count()) );

        if(select.count() > 0){

            List<AssetImages> aim = select.list();
            for(int im = 0; im < select.count();im++ ){
                AssetImages am = aim.get(im);
                SquareImageView defpic = makeImage(am);
                image_container.addView(defpic);

                if( am.getUrl() == null || "".equalsIgnoreCase(am.getUrl()) ){
                    Picasso.with(context)
                            .load("file://" + am.getUri())
                            .fit()
                            .centerCrop()
                            .into(defpic);

                }else{
                    if( "".equalsIgnoreCase( am.getUrl() ) == false && am.getUrl() != null ) {
                        Picasso.with(context)
                                .load(am.getUrl())
                                .fit()
                                .centerCrop()
                                .into(defpic);
                    }
                }

            }
        }
    }

    private void setProgressIndicator(String message){
        String defaultmessage = getResources().getString(R.string.loading_default);
        if("".equalsIgnoreCase(message) ){
            mProgressIndicator.setText(defaultmessage);
            mProgressIndicator.setVisibility(View.VISIBLE);
        }else{
            mProgressIndicator.setText(message);
            mProgressIndicator.setVisibility(View.VISIBLE);
        }
    }

    private void unsetProgressIndicator(){
        String defaultmessage = getResources().getString(R.string.loading_default);
        if("".equalsIgnoreCase(defaultmessage) ){
            mProgressIndicator.setText(defaultmessage);
            mProgressIndicator.setVisibility(View.GONE);
        }else{
            mProgressIndicator.setVisibility(View.GONE);
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

    public LabeledSwitchView makeSwitch(Context context, String label, int checked, String positive, String negative){
        LabeledSwitchView sw = new LabeledSwitchView(context);
        sw.setLabel(label);
        sw.setPositive(positive);
        sw.setNegative(negative);
        sw.setChecked( (checked == 1)?true:false );
        return sw;
    }

    public SquareImageView makeImage(AssetImages am){

        SquareImageView defpic = new SquareImageView(context);
        defpic.setPadding(0, 8, 0, 8);


        if(am.getUploaded() == 0){
            defpic.setAlpha(0.75f);

            final String file_id = am.getFileId();

            defpic.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Log.i("IMAGE CLICK", file_id);
                    EventBus.getDefault().post(new ImageEvent("upload", file_id , "rack"));
                }
            });
        }

        return defpic;
    }

}
