package com.kickstartlab.android.assets.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;

import com.getbase.floatingactionbutton.FloatingActionButton;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.adapters.LocationAdapter;
import com.kickstartlab.android.assets.adapters.MerchantAdapter;
import com.kickstartlab.android.assets.events.LocationEvent;
import com.kickstartlab.android.assets.events.MerchantEvent;
import com.kickstartlab.android.assets.events.ScannerEvent;
import com.kickstartlab.android.assets.rest.models.Location;
import com.kickstartlab.android.assets.rest.models.Merchant;
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
public class LocationListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Location> mData;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;
    private SwipeRefreshLayout swipeRefreshLayout;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private LocationAdapter mAdapter;

    //private MerchantAdapter mAdapter;

    SearchView mSearchView;


    // TODO: Rename and change types of parameters
    public static LocationListFragment newInstance(String param1, String param2) {
        LocationListFragment fragment = new LocationListFragment();
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
    public LocationListFragment() {
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
        View view = inflater.inflate(R.layout.fragment_location_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mAdapter = new LocationAdapter(getActivity());

        mSearchView.setIconifiedByDefault(false);

        refreshList();
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        FloatingActionButton mFabScan = (FloatingActionButton) view.findViewById(R.id.fab_location);
        mFabScan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EventBus.getDefault().post(new ScannerEvent("scan", "location"));
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

        menuItem = menu.add(Menu.NONE, R.id.action_scan_location, 0, R.string.action_scan).setIcon(R.drawable.ic_action_qr);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh_location, 0, R.string.action_refresh).setIcon(R.drawable.ic_sync_white_24dp);
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


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventBus.getDefault().post(new LocationEvent("select",mData.get(position)));
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

    public void searchList(String word){

        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(Location.class)
                .where( Condition.prop("name").like( "%" + word + "%" ), Condition.prop("deleted").eq(0) )
                .orderBy("name collate nocase").list();

        Log.i("SEARCH",word);

        mAdapter = new LocationAdapter(getActivity());

        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

    public void refreshList(){

        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(Location.class).where(Condition.prop("deleted").eq(0)).orderBy("name collate nocase").list();
        /*
        mAdapter = new ArrayAdapter<Location>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mData);
        */
        mAdapter = new LocationAdapter(getActivity());
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

}
