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
import com.kickstartlab.android.assets.activities.AddRackActivity;
import com.kickstartlab.android.assets.adapters.LocationAdapter;
import com.kickstartlab.android.assets.adapters.RackAdapter;
import com.kickstartlab.android.assets.events.LocationEvent;
import com.kickstartlab.android.assets.events.RackEvent;
import com.kickstartlab.android.assets.events.ScannerEvent;
import com.kickstartlab.android.assets.rest.models.Location;
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
public class RackListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Rack> mData;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private RackAdapter mAdapter;

    //private MerchantAdapter mAdapter;

    SearchView mSearchView;

    FloatingActionButton mFabScan, mFabAddRack;

    // TODO: Rename and change types of parameters
    public static RackListFragment newInstance(String param1, String param2) {
        RackListFragment fragment = new RackListFragment();
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
    public RackListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_rack_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mAdapter = new RackAdapter(getActivity());

        mSearchView.setIconifiedByDefault(false);

        EventBus.getDefault().post(new RackEvent("refreshById", mParam1));

        //refreshList();
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        mFabScan = (FloatingActionButton) view.findViewById(R.id.fab_scan_rack);
        mFabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ScannerEvent("scan", "rack" ));
            }
        });

        mFabAddRack = (FloatingActionButton) view.findViewById(R.id.fab_add_rack);

        mFabAddRack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getActivity(), AddRackActivity.class);
                i.putExtra("locationId",mParam1);
                i.putExtra("locationName",mParam2);
                startActivity(i);
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

        menuItem = menu.add(Menu.NONE, R.id.action_scan_rack, 0, R.string.action_scan).setIcon(R.drawable.ic_action_qr);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh_rack, 0, R.string.action_refresh).setIcon(R.drawable.ic_sync_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        /*
        try {
            mListener = (OnMerchantInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnLocationInteractionListener");
        }*/
    }

    @Override
    public void onDetach() {
        EventBus.getDefault().unregister(this);
        super.onDetach();
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
        if("select".equalsIgnoreCase(le.getAction())){
            refreshList();
            //Toast.makeText(getActivity(), me.getAction(), Toast.LENGTH_LONG).show();
        }
    }

    public void onEvent(RackEvent le){
        if("refresh".equalsIgnoreCase(le.getAction())){
            Log.i("RACK EVENT","REFRESH LIST");
            refreshList();
        }
    }

    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        EventBus.getDefault().post(new RackEvent("select",mData.get(position)));

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

        String ql = "SELECT * FROM RACK WHERE location_id = ? AND deleted != 1 AND ( SKU LIKE ? OR item_description LIKE ? ) ORDER BY SKU desc";
        String q = "%" + s + "%";
        String mid = mParam1;

        mData = Rack.findWithQuery(Rack.class, ql, mid ,q, q);

        mAdapter = new RackAdapter(getActivity());

        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

    public void refreshList(){

        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(Rack.class)
                .where(Condition.prop("location_id").eq(mParam1), Condition.prop("deleted").notEq(1) )
                .orderBy("SKU collate nocase").list();
        /*

        Log.i("LOC ID",mParam1);
        Log.i("RACK COUNT", String.valueOf(mData.size())  );

        for(int i= 0; i < mData.size();i++){
            Log.i("RACK ITEM", mData.get(i).toString() + String.valueOf(mData.get(i).getDeleted())  );
        }

        mAdapter = new ArrayAdapter<Location>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mData);
        */
        mAdapter = new RackAdapter(getActivity());
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

}
