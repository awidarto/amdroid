package com.kickstartlab.android.assets.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.activities.EditAssetActivity;
import com.kickstartlab.android.assets.events.AssetEvent;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.ui.LabeledTextView;
import com.kickstartlab.android.assets.ui.SquareImageView;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import de.greenrobot.event.EventBus;
import nl.changer.polypicker.ImagePickerActivity;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link AssetDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class AssetDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int INTENT_REQUEST_GET_IMAGES = 1667;

    FloatingActionButton btEdit, btAddImage;

    LabeledTextView sku, desc,ip,os,pic,pic_email,pic_phone,contract,asset_type,owner,type,host;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static AssetDetailFragment newInstance(String param1, String param2) {
        AssetDetailFragment fragment = new AssetDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public AssetDetailFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);
        EventBus.getDefault().register(this);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View mview = inflater.inflate(R.layout.fragment_asset_detail, container, false);

        btEdit = (FloatingActionButton) mview.findViewById(R.id.fab_edit_item);
        btAddImage = (FloatingActionButton) mview.findViewById(R.id.fab_add_image);

        btEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), EditAssetActivity.class);
                i.putExtra("ext_id", mParam1);
                startActivity(i);
            }
        });

        btAddImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(), ImagePickerActivity.class);
                intent.putExtra(ImagePickerActivity.EXTRA_SELECTION_LIMIT, 3);  // allow only upto 3 images to be selected.
                startActivityForResult(intent, INTENT_REQUEST_GET_IMAGES);
            }
        });

        LinearLayout detail_container = (LinearLayout) mview.findViewById(R.id.detail_container);

        Select select = Select.from(Asset.class).where(Condition.prop("ext_id").eq(mParam1))
                .limit("1");

        Log.i("ASSET COUNT", String.valueOf(select.count()));
        if (select.count() > 0) {
            Asset asset = (Asset) select.first();

            Log.i("ASSET SKU", asset.getSKU() ) ;

            /*
                private String IP;
                private String OS;
                private String PIC;
                private String PicEmail;
                private String PicPhone;
                private String contractNumber;
                private String SKU;
                private String assetType;
                private String brc1;
                private String brc2;
                private String brc3;
                private String brchead;
                private String createdDate;
                private String defaultpic;
                private String hostName;
                private String itemDescription;
                private String lastUpdate;
                private String locationId;
                private String owner;
                private String rackId;
                private String status;
                private String tags;
                private String type;
                private String updatedAt;
                private String extId;
                private String pictureThumbnailUrl;
                private String pictureLargeUrl;
                private String pictureMediumUrl;
                private String pictureFullUrl;
                private String pictureBrchead;
                private String pictureBrc1;
                private String pictureBrc2;
                private String pictureBrc3;
            *
            *
            * */

            sku = makeText(getActivity(),"Serial Number / Asset Code",asset.getSKU());
            desc = makeText(getActivity(),"Description",asset.getItemDescription());
            ip = makeText(getActivity(),"IP Address",asset.getIP());
            os = makeText(getActivity(),"OS",asset.getOS());
            pic = makeText(getActivity(),"PIC",asset.getPIC());
            pic_email = makeText(getActivity(),"PIC Email",asset.getPicEmail());
            pic_phone = makeText(getActivity(),"PIC Phone",asset.getPicPhone());
            contract = makeText(getActivity(),"Contract Number",asset.getContractNumber());
            asset_type = makeText(getActivity(),"Asset Type",asset.getAssetType());
            owner = makeText(getActivity(),"Owner",asset.getOwner());
            type = makeText(getActivity(),"Type",asset.getType());
            host = makeText(getActivity(),"Host",asset.getHostName());

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

            if("".equalsIgnoreCase(asset.getPictureMediumUrl()) == false){
                SquareImageView defpic = new SquareImageView(getActivity());
                detail_container.addView(defpic);
                Picasso.with(getActivity())
                        .load(asset.getPictureMediumUrl())
                        .fit()
                        .centerCrop()
                        .placeholder(R.drawable.ic_cloud_download_grey600_24dp)
                        .into(defpic);
            }

        }



        return mview;
    }

    public void onEvent(AssetEvent ae){
        if(ae.getAction() == "refreshDetail"){
           Asset a = ae.getAsset();
            Log.i("asset evt",a.getIP());

            sku.setBody(a.getSKU());
            desc.setBody(a.getItemDescription());
            ip.setBody(a.getIP());
            os.setBody(a.getOS());
            pic.setBody(a.getPIC());
            pic_email.setBody(a.getPicEmail());
            pic_phone.setBody(a.getPicPhone());
            contract.setBody(a.getContractNumber());
            asset_type.setBody(a.getAssetType());
            owner.setBody(a.getOwner());
            host.setBody(a.getHostName());

        }
    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(menu.getItem(0).isVisible()){
            menu.getItem(0).setVisible(false);
        }

        menuItem = menu.add(Menu.NONE, R.id.action_edit_asset, 0, R.string.action_edit_asset).setIcon(R.drawable.ic_mode_edit_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh_asset, 0, R.string.action_refresh).setIcon(R.drawable.ic_cloud_download_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == Activity.RESULT_OK ){
            if(requestCode == INTENT_REQUEST_GET_IMAGES){

            }
        }
    }

    public LabeledTextView makeText(Context context,String label, String content){
        LabeledTextView lText = new LabeledTextView(context);
        lText.setLabel(label);
        lText.setBody(content);

        return lText;
    }
}
