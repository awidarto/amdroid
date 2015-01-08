package com.kickstartlab.android.jwh.fragments;

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

import com.google.zxing.BarcodeFormat;
import com.google.zxing.Result;
import com.kickstartlab.android.jwh.R;
import com.kickstartlab.android.jwh.events.OrderEvent;
import com.kickstartlab.android.jwh.events.ScannerEvent;
import com.kickstartlab.android.jwh.rest.models.Asset;
import com.kickstartlab.android.jwh.rest.models.OrderItem;
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

        Boolean is_trx = false;

        String scresult = rawResult.getText();

        if(scresult.contains("|") && scresult.indexOf("|") > 0 ){
            String[] sparts = scresult.split("\\|");
            scresult = sparts[1];
            if(scresult.contains("TRX_") ){
                is_trx = true;
            }
        }

        String lastsix = "";
        // work around for bukukita, should be omitted after uniform labeling
        if(scresult.length() > 6){
            lastsix = scresult.substring( scresult.length() - 6 , scresult.length() );
        }else{
            lastsix = scresult;
        }

        Log.i("last six", lastsix);

        Log.i("captured trx id", scresult);

        String scresultquery;

        if (scresult.length() != 0) {
            scresultquery = "%" + scresult + "%";
        }else{
            scresultquery = scresult;
        }

        String lastsixquery;

        if (lastsix.length() != 0) {
            lastsixquery = "%" + scresult + "%";
        }else{
            lastsixquery = scresult;
        }

        List<OrderItem> ord = OrderItem.find(OrderItem.class, "( MERCHANT_TRANS_ID = ? OR MERCHANT_TRANS_ID LIKE ? OR MERCHANT_TRANS_ID = ? OR MERCHANT_TRANS_ID LIKE ? OR DELIVERY_ID = ? OR DELIVERY_ID LIKE ? ) ", scresult ,scresultquery, lastsix, lastsixquery, scresult ,scresultquery  );

        if(ord.size() > 0){

            OrderItem o = ord.get(0);
            /*
            o.setWarehouseStatus("");
            o.save();


            String strx =  Base64.encodeToString(scresult.getBytes(), Base64.NO_WRAP);

            Log.i("base64 encoded", strx);

            String identifier,invoice,deliverytype;
            if(is_trx){
                identifier = "Delivery ID: " + o.getDeliveryId();
            }else{
                String did = ("".equalsIgnoreCase(o.getDeliveryId()))?"":"Delivery ID: " + o.getDeliveryId();
                identifier = new StringBuilder(did).append(" ").append( "Invoice: " ).append(o.getMerchantTransId()).toString();
                invoice = "Invoice: " + o.getMerchantTransId();
                identifier = "Delivery ID: " + o.getDeliveryId();

            }

            deliverytype = o.getDeliveryType();
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

            showMessageDialog(message);
            */


            EventBus.getDefault().post(new OrderEvent("select",o));

        }else{
            String message = new StringBuilder("Scan Result : ")
                    .append(System.lineSeparator())
                    .append(rawResult.getText())
                    .toString();

            showAlienDialog(message );
        }



        try {
            Uri notification = RingtoneManager.getDefaultUri(RingtoneManager.TYPE_NOTIFICATION);
            Ringtone r = RingtoneManager.getRingtone(getActivity().getApplicationContext(), notification);
            r.play();
        } catch (Exception e) {}

    }

    public void showAlienDialog(String message) {
        DialogFragment fragment = AlienDialogFragment.newInstance("Tidak Dikenal", message, this);
        fragment.show(getActivity().getSupportFragmentManager(), "alien_results");
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
