package com.kickstartlab.android.assets.fragments;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.SearchView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.activities.AddAssetActivity;
import com.kickstartlab.android.assets.activities.EditAssetActivity;
import com.kickstartlab.android.assets.adapters.AssetAdapter;
import com.kickstartlab.android.assets.adapters.RackAdapter;
import com.kickstartlab.android.assets.events.AssetEvent;
import com.kickstartlab.android.assets.events.LocationEvent;
import com.kickstartlab.android.assets.events.RackEvent;
import com.kickstartlab.android.assets.events.ScannerEvent;
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.rest.models.Rack;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.util.List;

import de.greenrobot.event.EventBus;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class AssetListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Asset> mData;

    FloatingActionButton mFabAddAsset;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private AssetAdapter mAdapter;

    //private MerchantAdapter mAdapter;

    SearchView mSearchView;


    // TODO: Rename and change types of parameters
    public static AssetListFragment newInstance(String param1, String param2) {
        AssetListFragment fragment = new AssetListFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AssetListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mAdapter = new AssetAdapter(getActivity());

        mSearchView.setIconifiedByDefault(false);

        //EventBus.getDefault().post(new AssetEvent("refreshById", mParam1));
        //refreshList();
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mFabAddAsset = (FloatingActionButton) view.findViewById(R.id.fab_add_asset);

        mFabAddAsset.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddAssetActivity.class);
                i.putExtra("rackId",mParam1);
                i.putExtra("rackName",mParam2);
                startActivity(i);
            }
        });

        FloatingActionButton mFabScan = (FloatingActionButton) view.findViewById(R.id.fab_scan_asset);
        mFabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ScannerEvent("scan", "asset" ));
            }
        });

        mSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String s) {
                searchList(s);
                return false;
            }

            @Override
            public boolean onQueryTextChange(String s) {
                searchList(s);
                return false;
            }
        });

        return view;
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

        menuItem = menu.add(Menu.NONE, R.id.action_scan_asset, 0, R.string.action_scan).setIcon(R.drawable.ic_action_qr);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh_asset, 0, R.string.action_refresh).setIcon(R.drawable.ic_sync_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_rack_detail, 0, R.string.acction_rack_detail).setIcon(R.drawable.ic_info_outline_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
    }

    @Override
    public void onResume() {
        refreshList();
        super.onResume();
    }

    @Override
    public void onPause() {
        EventBus.getDefault().unregister(this);
        super.onPause();
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    /* Events */

    public void onEvent(LocationEvent le){
        if("refresh".equalsIgnoreCase(le.getAction())){
            refreshList();
            //Toast.makeText(getActivity(), me.getAction(), Toast.LENGTH_LONG).show();
        }

    }

    public void onEvent(AssetEvent le){
        if("refresh".equalsIgnoreCase(le.getAction())){
            Log.i("ASSET LIST", "REFRESH");
            refreshList();
            //Toast.makeText(getActivity(), me.getAction(), Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        EventBus.getDefault().post(new AssetEvent("select",mData.get(position)));

    }

    /**
     * The default content for this Fragment has a TextView that is shown when
     * the list is empty. If you would like to change the text, call this method
     * to supply the text it should use.
     */
    public void setEmptyText(CharSequence emptyText) {
        View emptyView = mListView.getEmptyView();

        if (emptyText instanceof TextView) {
            ((TextView) emptyView).setText(emptyText);
        }
    }

    public void searchList(String s){

        //mData = Merchant.listAll(Merchant.class);
        /*
        mData =  Select.from(Rack.class)
                .where(Condition.prop("location_id").eq(mParam1))
                .where( Condition.prop("SKU").like( "%" + word + "%" ) )
                .orderBy("SKU collate nocase").list();
        */

        String ql = "SELECT * FROM ASSET WHERE rack_id = ? AND deleted = 0  AND ( SKU LIKE ? OR item_description LIKE ? ) ORDER BY last_update desc, SKU desc";
        String q = "%" + s + "%";
        String mid = mParam1;

        mData = Asset.findWithQuery(Asset.class, ql, mid ,q, q);

        mAdapter = new AssetAdapter(getActivity());

        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

    public void refreshList(){

        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(Asset.class)
                .where(Condition.prop("rack_id").eq(mParam1), Condition.prop("deleted").notEq(1) )
                .orderBy("last_update desc")
                .orderBy("SKU collate nocase").list();
        /*
        mAdapter = new ArrayAdapter<Location>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mData);
        */
        mAdapter = new AssetAdapter(getActivity());
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

}
