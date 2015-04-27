package com.kickstartlab.android.assets.fragments;

import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.MenuItemCompat;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.afollestad.materialdialogs.MaterialDialog;
import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.kickstartlab.android.assets.activities.MainActivity;
import com.kickstartlab.android.assets.events.AssetEvent;
import com.kickstartlab.android.assets.events.LocationEvent;
import com.kickstartlab.android.assets.events.OrderEvent;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.events.OrderEvent;
import com.kickstartlab.android.assets.events.RackEvent;
import com.kickstartlab.android.assets.events.ScannerEvent;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.Location;
import com.kickstartlab.android.assets.rest.models.OrderItem;
import com.kickstartlab.android.assets.rest.models.Rack;
import com.kickstartlab.android.assets.utils.SLog;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.ArrayList;
import java.util.List;

import de.greenrobot.event.EventBus;
import me.dm7.barcodescanner.zxing.ZXingScannerView;

public class ScannerFragment extends Fragment implements
        ZXingScannerView.ResultHandler,
        MessageDialogFragment.MessageDialogListener,
        FormatSelectorDialogFragment.FormatSelectorDialogListener,
        AlienDialogFragment.AlienDialogListener {
    private static final String FLASH_STATE = "FLASH_STATE";
    private static final String AUTO_FOCUS_STATE = "AUTO_FOCUS_STATE";
    private static final String SELECTED_FORMATS = "SELECTED_FORMATS";
    private ZXingScannerView mScannerView;
    private boolean mFlash;
    private boolean mAutoFocus;
    private ArrayList<Integer> mSelectedIndices;

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;


    // TODO: Rename and change types of parameters
    public static ScannerFragment newInstance(String param1, String param2) {
        ScannerFragment fragment = new ScannerFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public ScannerFragment(){
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle state) {
        mScannerView = new ZXingScannerView(getActivity());
        if(state != null) {
            mFlash = state.getBoolean(FLASH_STATE, false);
            mAutoFocus = state.getBoolean(AUTO_FOCUS_STATE, true);
            mSelectedIndices = state.getIntegerArrayList(SELECTED_FORMATS);
        } else {
            mFlash = false;
            mAutoFocus = true;
            mSelectedIndices = null;
        }
        setupFormats();
        return mScannerView;
    }

    @Override
    public void onCreate(Bundle state) {
        super.onCreate(state);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        EventBus.getDefault().register(this);
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(menu.getItem(0).isVisible()){
            menu.getItem(0).setVisible(false);
        }

        if(mFlash) {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_flash, 0, R.string.flash_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


        if(mAutoFocus) {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_on);
        } else {
            menuItem = menu.add(Menu.NONE, R.id.menu_auto_focus, 0, R.string.auto_focus_off);
        }
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.menu_formats, 0, R.string.formats);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle presses on the action bar items
        switch (item.getItemId()) {
            case R.id.menu_flash:
                mFlash = !mFlash;
                if(mFlash) {
                    item.setTitle(R.string.flash_on);
                } else {
                    item.setTitle(R.string.flash_off);
                }
                mScannerView.setFlash(mFlash);
                return true;
            case R.id.menu_auto_focus:
                mAutoFocus = !mAutoFocus;
                if(mAutoFocus) {
                    item.setTitle(R.string.auto_focus_on);
                } else {
                    item.setTitle(R.string.auto_focus_off);
                }
                mScannerView.setAutoFocus(mAutoFocus);
                return true;
            case R.id.menu_formats:
                DialogFragment fragment = FormatSelectorDialogFragment.newInstance(this, mSelectedIndices);
                fragment.show(getActivity().getSupportFragmentManager(), "format_selector");
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mScannerView.setResultHandler(this);
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
        Log.i("SCAN MODE", mParam2);
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    public void onEvent(ScannerEvent se){
        Log.i("scanner event",se.getCommand());
        if(se.getCommand() == "resume"){
            mScannerView.startCamera();
            mScannerView.setFlash(mFlash);
            mScannerView.setAutoFocus(mAutoFocus);
        }
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putBoolean(FLASH_STATE, mFlash);
        outState.putBoolean(AUTO_FOCUS_STATE, mAutoFocus);
        outState.putIntegerArrayList(SELECTED_FORMATS, mSelectedIndices);
    }

    @Override
    public void handleResult(Result rawResult) {

        String scresult = rawResult.getText();

        if("location".equalsIgnoreCase(mParam2)){
            try{
                Select select = Select.from(Location.class).where(Condition.prop("ext_id").eq(scresult))
                        .limit("1");
                if (select.count() > 0) {
                    Location loc = (Location) select.first();
                    SLog.s(scresult, "checked", "location", loc.getExtId());
                    EventBus.getDefault().post(new LocationEvent("select", loc));
                }else{
                    String message = new StringBuilder("Scan Result : ")
                            .append(System.lineSeparator())
                            .append(rawResult.getText())
                            .toString();
                    SLog.e( scresult, "unregistered", "location" );
                    showUnregisteredDialog(getResources().getString(R.string.dialog_unregistered_location_title), message, "location");
                }

            }catch(Exception e){
                Log.e("location scan exception", e.toString());
                String message = new StringBuilder("Scan Result : ")
                        .append(System.lineSeparator())
                        .append(rawResult.getText())
                        .toString();
                SLog.e( scresult, "unregistered", "location" );
                showUnregisteredDialog(getResources().getString(R.string.dialog_unregistered_location_title), message, "location");
            }

        }else if("rack".equalsIgnoreCase(mParam2)){
            try{
                Select select = Select.from(Rack.class).where(Condition.prop("SKU").eq(scresult))
                        .limit("1");
                if (select.count() > 0) {
                    Rack rack = (Rack) select.first();
                    SLog.s(scresult, "checked", "rack", rack.getExtId());
                    EventBus.getDefault().post(new RackEvent("select", rack));
                }else{
                    String message = new StringBuilder("Scan Result : ")
                            .append(System.lineSeparator())
                            .append(rawResult.getText())
                            .toString();
                    SLog.e( scresult, "unregistered", "rack" );
                    showUnregisteredDialog(getResources().getString(R.string.dialog_unregistered_rack_title),message, "rack" );
                }

            }catch (Exception e){
                Log.e("rack scan exception", e.toString());
                String message = new StringBuilder("Scan Result : ")
                        .append(System.lineSeparator())
                        .append(rawResult.getText())
                        .toString();
                SLog.e( scresult, "unregistered", "rack" );
                showUnregisteredDialog(getResources().getString(R.string.dialog_unregistered_rack_title),message, "rack" );
            }

        }else if("asset".equalsIgnoreCase(mParam2)){
            Select select = Select.from(Asset.class).where(Condition.prop("SKU").eq(scresult))
                    .limit("1");

            try{
                if (select.count() > 0) {
                    Asset asset = (Asset) select.first();
                    SLog.s(scresult, "checked", "asset", asset.getExtId());

                    if( !mParam1.equalsIgnoreCase(asset.getRackId()) ){
                        String message = new StringBuilder(getResources().getString(R.string.misplaced_rack))
                                .append(System.lineSeparator())
                                .append(getResources().getString(R.string.ask_move))
                                .toString();
                        SLog.s(scresult, "displacement", "asset", "asset " + asset.getExtId() + " in rack " + asset.getRackId()  );
                        showDisplacedDialog(message, asset, mParam1);
                    }else{
                        EventBus.getDefault().post(new AssetEvent("select", asset));
                    }

                }else{
                    String message = new StringBuilder("Scan Result : ")
                            .append(System.lineSeparator())
                            .append(rawResult.getText())
                            .toString();
                    SLog.e(scresult, "alien", "asset");
                    showAlienDialog(getResources().getString(R.string.dialog_alien_asset_title), message);
                }

            }catch (Exception e){
                Log.e("asset scan exception", e.toString());
                String message = new StringBuilder("Scan Result : ")
                        .append(System.lineSeparator())
                        .append(rawResult.getText())
                        .toString();
                SLog.e(scresult, "alien", "asset");
                showAlienDialog(getResources().getString(R.string.dialog_alien_asset_title), message);
            }


        }else if("getcode".equalsIgnoreCase(mParam2)){

            try{
                Select select = Select.from(Asset.class).where(Condition.prop("SKU").eq(scresult))
                        .limit("1");

                if (select.count() > 0) {
                    Asset asset = (Asset) select.first();
                    String message = new StringBuilder("Scan Result : ")
                            .append(System.lineSeparator())
                            .append(rawResult.getText())
                            .toString();
                    SLog.s(scresult, "asset code exists", "asset", asset.getExtId());
                    showDupeDialog(message);
                }else{
                    SLog.e(scresult, "new asset code", "asset");
                    EventBus.getDefault().post( new ScannerEvent("sendCode", rawResult ));
                }
            }catch (Exception e){
                Log.e("getcode scan exception", e.toString());
            }

        }

        MediaPlayer m = new MediaPlayer();
        try{
            AssetFileDescriptor descriptor = getActivity().getAssets().openFd("scanbeep.mp3");
            m.setDataSource(descriptor.getFileDescriptor(), descriptor.getStartOffset(), descriptor.getLength() );
            descriptor.close();
            m.prepare();
            m.start();
        } catch(Exception e){
            // handle error here..
        }
        /*
        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}
        */
    }

    public void showDupeDialog(String message) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_dupe_asset_title)
                .content(message)
                .positiveText(R.string.ok_button)
                .positiveColor(R.color.green)
                .negativeText(R.string.cancel_button)
                .negativeColor(R.color.red)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  //super.onPositive(dialog);
                                  SLog.e("OK","asset duplicate","asset");
                                  EventBus.getDefault().post(new ScannerEvent("resume"));
                              }

                              @Override
                              public void onNegative(MaterialDialog dialog) {
                                  SLog.e("Ignore","asset duplicate","asset");
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

        //DialogFragment fragment = AlienDialogFragment.newInstance("Tidak Dikenal", message, this);
        //fragment.show(getActivity().getSupportFragmentManager(), "alien_results");
    }

    public void showAlienDialog(String title, String message) {
        new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(message)
                .neutralText(R.string.action_create_asset)
                .neutralColor(R.color.green)
                .positiveText(R.string.ok_button)
                .positiveColor(R.color.green)
                .negativeText(R.string.cancel_button)
                .negativeColor(R.color.red)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  //super.onPositive(dialog);
                                  SLog.e("OK","alien asset","asset");
                                  EventBus.getDefault().post(new ScannerEvent("resume"));
                              }

                              @Override
                              public void onNegative(MaterialDialog dialog) {
                                  SLog.e("Cancel","alien asset","asset");
                                  EventBus.getDefault().post(new ScannerEvent("resume"));
                                  super.onNegative(dialog);
                              }

                              @Override
                              public void onNeutral(MaterialDialog dialog) {
                                  SLog.e("Create","alien asset","asset");
                                  EventBus.getDefault().post(new AssetEvent("createAsset"));
                                  super.onNeutral(dialog);
                              }
                          }

                )
                .show();

        //DialogFragment fragment = AlienDialogFragment.newInstance("Tidak Dikenal", message, this);
        //fragment.show(getActivity().getSupportFragmentManager(), "alien_results");
    }

    public void showUnregisteredDialog(String title, String message, final String mode) {

        new MaterialDialog.Builder(getActivity())
                .title(title)
                .content(message)
                .neutralText(R.string.action_create_asset)
                .neutralColor(R.color.green)
                .positiveText(R.string.ok_button)
                .positiveColor(R.color.green)
                .negativeText(R.string.cancel_button)
                .negativeColor(R.color.red)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  //super.onPositive(dialog);
                                  SLog.e("Create","unregistered " + mode,mode);
                                  EventBus.getDefault().post(new ScannerEvent("resume"));
                              }

                              @Override
                              public void onNegative(MaterialDialog dialog) {
                                  SLog.e("Cancel","unregistered " + mode,mode);
                                  EventBus.getDefault().post(new ScannerEvent("resume"));
                                  super.onNegative(dialog);
                              }

                              @Override
                              public void onNeutral(MaterialDialog dialog) {
                                  if ("rack".equalsIgnoreCase(mode)) {
                                      SLog.e("Create","unregistered " + mode,mode);
                                      EventBus.getDefault().post(new RackEvent("createRack"));
                                  }
                                  super.onNeutral(dialog);
                              }
                          }

                )
                .show();

        //DialogFragment fragment = AlienDialogFragment.newInstance("Tidak Dikenal", message, this);
        //fragment.show(getActivity().getSupportFragmentManager(), "alien_results");
    }

    public void showDisplacedDialog(String message, final Asset asset ,final String rackId) {
        new MaterialDialog.Builder(getActivity())
                .title(R.string.dialog_displaced_asset_title)
                .content(message)
                .positiveText(R.string.action_move_asset)
                .positiveColor(R.color.green)
                .negativeText(R.string.ignore_button)
                .negativeColor(R.color.red)
                .callback(new MaterialDialog.ButtonCallback() {
                              @Override
                              public void onPositive(MaterialDialog dialog) {
                                  //super.onPositive(dialog);
                                    Log.i("CURRENT RACK", rackId);
                                    Log.i("ASSET RACK", asset.getRackId());
                                  SLog.s("Move", "displacement", "asset", "asset " + asset.getExtId() + " to rack " + rackId  );
                                  EventBus.getDefault().post(new AssetEvent("moveRack", asset, rackId ));
                                  EventBus.getDefault().post(new ScannerEvent("resume"));
                              }

                              @Override
                              public void onNegative(MaterialDialog dialog) {
                                  SLog.s("Ignore", "displacement", "asset", "asset " + asset.getExtId() + " to rack " + rackId  );
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

        //DialogFragment fragment = AlienDialogFragment.newInstance("Tidak Dikenal", message, this);
        //fragment.show(getActivity().getSupportFragmentManager(), "alien_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeFormatsDialog() {
        closeDialog("format_selector");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog, String deliveryId) {
        // Resume the camera
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onDialogPositiveClick(DialogFragment dialog) {
        // Resume the camera
        mScannerView.startCamera();
        mScannerView.setFlash(mFlash);
        mScannerView.setAutoFocus(mAutoFocus);
    }

    @Override
    public void onFormatsSaved(ArrayList<Integer> selectedIndices) {
        mSelectedIndices = selectedIndices;
        setupFormats();
    }

    public void setupFormats() {
        List<BarcodeFormat> formats = new ArrayList<BarcodeFormat>();
        if(mSelectedIndices == null || mSelectedIndices.isEmpty()) {
            mSelectedIndices = new ArrayList<Integer>();
            for(int i = 0; i < ZXingScannerView.ALL_FORMATS.size(); i++) {
                mSelectedIndices.add(i);
            }
        }

        for(int index : mSelectedIndices) {
            formats.add(ZXingScannerView.ALL_FORMATS.get(index));
        }
        if(mScannerView != null) {
            mScannerView.setFormats(formats);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        mScannerView.stopCamera();
        closeMessageDialog();
        closeFormatsDialog();
        EventBus.getDefault().unregister(this);
    }
}
