package com.kickstartlab.android.assets.fragments;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
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
import com.kickstartlab.android.assets.activities.EditRackActivity;
import com.kickstartlab.android.assets.events.AssetEvent;
import com.kickstartlab.android.assets.events.ImageEvent;
import com.kickstartlab.android.assets.events.RackEvent;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.AssetImages;
import com.kickstartlab.android.assets.rest.models.Rack;
import com.kickstartlab.android.assets.ui.LabeledTextView;
import com.kickstartlab.android.assets.ui.SquareImageView;
import com.kickstartlab.android.assets.utils.RandomStringGenerator;
import com.orm.query.Condition;
import com.orm.query.Select;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.greenrobot.event.EventBus;
import nl.changer.polypicker.ImagePickerActivity;

/**
 * A simple {@link android.support.v4.app.Fragment} subclass.
 * Use the {@link com.kickstartlab.android.assets.fragments.RackDetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class RackDetailFragment extends Fragment {
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    private int INTENT_REQUEST_GET_IMAGES = 1667;

    Rack rack;

    String yes, no;

    FloatingActionButton btEdit, btAddImage;

    LabeledTextView sku, desc;

    LabeledTextView rackStatus;

    LinearLayout image_container;

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment AssetDetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static RackDetailFragment newInstance(String param1, String param2) {
        RackDetailFragment fragment = new RackDetailFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    public RackDetailFragment() {
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
                Intent i = new Intent(getActivity(), EditRackActivity.class);
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

        image_container = (LinearLayout) mview.findViewById(R.id.image_container);

        Select select = Select.from(Rack.class).where(Condition.prop("ext_id").eq(mParam1))
                .limit("1");

        Log.i("RACK COUNT", String.valueOf(select.count()));
        if (select.count() > 0) {
            rack = (Rack) select.first();

            Log.i("RACK SKU", rack.getSKU()) ;

            sku = makeText(getActivity(),"Serial Number / Rack Code",rack.getSKU());
            desc = makeText(getActivity(),"Description",rack.getItemDescription());

            yes = getResources().getString(R.string.label_active);
            no = getResources().getString(R.string.label_inactive);

            rackStatus = makeText(getActivity(), "Status", ( "active".equalsIgnoreCase(rack.getStatus())) ? yes : no);

            detail_container.addView(sku);
            detail_container.addView(rackStatus);
            detail_container.addView(desc);

        }

        refreshImage();

        return mview;
    }


    @Override
    public void onResume() {
        refreshImage();
        super.onResume();
    }

    public void onEvent(RackEvent re){
        if(re.getAction() == "refreshDetailView"){
            Rack a = Select.from(Rack.class).where(Condition.prop("ext_id").eq(mParam1))
                    .limit("1").first();

            sku.setBody(a.getSKU());
            desc.setBody(a.getItemDescription());

            rackStatus.setBody( ( "active".equalsIgnoreCase(a.getStatus()))?yes:no );

            refreshImage();

            EventBus.getDefault().post(new ImageEvent("upsync", a.getExtId(), "rack"));

        }

        if(re.getAction() == "refreshDetail"){
            Rack a = re.getRack();

            sku.setBody(a.getSKU());
            desc.setBody(a.getItemDescription());

            rackStatus.setBody( ( "active".equalsIgnoreCase(a.getStatus()))?yes:no );

            refreshImage();
        }

        if(re.getAction() == "editRack"){
            Intent i = new Intent(getActivity(), EditRackActivity.class);
            i.putExtra("ext_id", mParam1);
            startActivity(i);
        }

    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(menu.getItem(0).isVisible()){
            menu.getItem(0).setVisible(false);
        }

        menuItem = menu.add(Menu.NONE, R.id.action_edit_rack, 0, R.string.action_edit_asset).setIcon(R.drawable.ic_mode_edit_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh_rack_detail, 0, R.string.action_refresh).setIcon(R.drawable.ic_sync_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item){
        int id = item.getItemId();

        if (id == R.id.action_refresh_rack) {
            EventBus.getDefault().post(new RackEvent("upsyncRack",rack));
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if( resultCode == Activity.RESULT_OK ){
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
                            aim.setParentId(mParam1);
                            aim.setFileId(file_id);
                            aim.setExtId(upload_id);
                            aim.setUri(uri.toString());
                            aim.setIsLocal(1);
                            aim.setUploaded(0);
                            aim.setDeleted(0);
                            aim.save();

                        }

                    }

                    refreshImage();
                }
            }
        }
    }

    public void refreshImage(){
        image_container.removeAllViews();

        Log.i("IMAGE","REFRESH " + mParam1);

        Select select = Select.from(AssetImages.class).where(Condition.prop("parent_id").eq(mParam1),
                Condition.prop("parent_class").eq("rack"),
                Condition.prop("deleted").eq(0));

        Log.i( "IMG COUNT", String.valueOf(select.count()) );

        if(select.count() > 0){

            List<AssetImages> aim = select.list();

            for(int im = 0; im < select.count();im++ ){
                AssetImages am = aim.get(im);

                SquareImageView defpic = makeImage(am);
                image_container.addView(defpic);

                if( am.getUrl() == null || "".equalsIgnoreCase(am.getUrl()) ){
                    Picasso.with(getActivity())
                            .load("file://" + am.getUri())
                            .fit()
                            .centerCrop()
                            .into(defpic);

                }else{
                    if( "".equalsIgnoreCase( am.getUrl() ) == false && am.getUrl() != null ) {
                       Picasso.with(getActivity())
                                .load(am.getUrl())
                                .fit()
                                .centerCrop()
                                .into(defpic);
                    }
                }
            }
        }
    }

    public LabeledTextView makeText(Context context,String label, String content){
        LabeledTextView lText = new LabeledTextView(context);
        lText.setLabel(label);
        lText.setBody(content);

        return lText;
    }

    public SquareImageView makeImage(AssetImages am){

        SquareImageView defpic = new SquareImageView(getActivity());
        defpic.setPadding(0,8,0,8);

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
