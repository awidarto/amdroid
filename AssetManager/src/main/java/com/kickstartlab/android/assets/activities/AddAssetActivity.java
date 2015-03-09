package com.kickstartlab.android.assets.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.ActionBarActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.events.AssetEvent;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.utils.DbDateUtil;
import com.kickstartlab.android.assets.utils.RandomStringGenerator;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.rengwuxian.materialedittext.MaterialEditText;

import de.greenrobot.event.EventBus;
import fr.castorflex.android.smoothprogressbar.SmoothProgressBar;

public class AddAssetActivity extends ActionBarActivity {

    Toolbar mToolbar;
    SmoothProgressBar mProgressBar;

    MaterialEditText sku,desc,ip,host,os,pic,pic_email,pic_phone,contract,asset_type,owner;

    Asset asset;

    String rackId, rackName;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

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

        this.getSupportActionBar().setTitle("Add Asset");

        this.getSupportActionBar().setHomeButtonEnabled(true);

        LinearLayout detail_container = (LinearLayout) findViewById(R.id.detail_container);

        sku = makeEditText(this,"Serial Number / Asset Code");
        desc = makeEditText(this,"Description");
        ip = makeEditText(this, "IP Address");
        host = makeEditText(this, "Host");
        os = makeEditText(this, "OS");
        pic = makeEditText(this, "PIC");
        pic_email = makeEditText(this, "PIC Email");
        pic_phone = makeEditText(this, "PIC Phone");
        contract = makeEditText(this, "Contract Number");
        asset_type = makeEditText(this,"Asset Type");
        owner = makeEditText(this,"Owner");
        //MaterialEditText type = makeEditText(this,"Type");

        detail_container.addView(sku);
        detail_container.addView(ip);
        detail_container.addView(host);
        detail_container.addView(os);
        detail_container.addView(contract);
        detail_container.addView(owner);
        detail_container.addView(pic);
        detail_container.addView(pic_email);
        detail_container.addView(pic_phone);
        detail_container.addView(asset_type);
        //detail_container.addView(type);
        detail_container.addView(desc);

        FloatingActionButton fabSave = (FloatingActionButton) findViewById(R.id.fab_save_item);

        fabSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                asset = new Asset();

                String extId = RandomStringGenerator.generateRandomString(24, RandomStringGenerator.Mode.ALPHANUMERIC);

                String createdDate = DbDateUtil.getDateTime();
                String lastUpdate = DbDateUtil.getDateTime();

                asset.setRackId(rackId);
                asset.setCreatedDate(createdDate);
                asset.setLastUpdate(lastUpdate);
                asset.setExtId(extId.toLowerCase());
                asset.setSKU(sku.getText().toString());
                asset.setIP(ip.getText().toString());
                asset.setHostName(host.getText().toString());
                asset.setOS(os.getText().toString());
                asset.setContractNumber(contract.getText().toString());
                asset.setOwner(owner.getText().toString());
                asset.setPIC(pic.getText().toString());
                asset.setPicEmail(pic_email.getText().toString());
                asset.setPicPhone(pic_phone.getText().toString());
                asset.setAssetType(asset_type.getText().toString());
                asset.setItemDescription(desc.getText().toString());

                asset.setLocalEdit(1);
                asset.setUploaded(0);

                asset.save();
                //Log.i("asset",asset.getIP());
                EventBus.getDefault().post(new AssetEvent("refresh"));
                finish();
            }
        });




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

        return super.onOptionsItemSelected(item);
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
