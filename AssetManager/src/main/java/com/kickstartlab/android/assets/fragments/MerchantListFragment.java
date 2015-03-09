package com.kickstartlab.android.assets.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.TextView;
import android.widget.Toast;

import com.kickstartlab.android.assets.adapters.MerchantAdapter;
import com.kickstartlab.android.assets.events.MerchantEvent;
import com.kickstartlab.android.assets.rest.models.Merchant;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.activities.MainActivity;
import com.kickstartlab.android.assets.adapters.MerchantAdapter;
import com.kickstartlab.android.assets.events.MerchantEvent;
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
public class MerchantListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Merchant> mData;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    //private ListAdapter mAdapter;

    private MerchantAdapter mAdapter;

    SearchView mSearchView;


    // TODO: Rename and change types of parameters
    public static MerchantListFragment newInstance(String param1, String param2) {
        MerchantListFragment fragment = new MerchantListFragment();
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
    public MerchantListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_merchant_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);

        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mAdapter = new MerchantAdapter(getActivity());

        mSearchView.setIconifiedByDefault(false);

        refreshList();
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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

    public void onEvent(MerchantEvent me){
        if("refresh".equalsIgnoreCase(me.getAction())){
            refreshList();
            //Toast.makeText(getActivity(), me.getAction(), Toast.LENGTH_LONG).show();
        }

    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        EventBus.getDefault().post(new MerchantEvent("select",mData.get(position)));
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
        mData =  Select.from(Merchant.class)
                .where( Condition.prop("merchantname").like( "%" + word + "%" ) )
                .orderBy("merchantname collate nocase").list();

        Log.i("SEARCH",word);

        //mAdapter = new ArrayAdapter<Merchant>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, mData);

        mAdapter = new MerchantAdapter(getActivity());
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

    public void refreshList(){

        //mData = Merchant.listAll(Merchant.class);
        mData =  Select.from(Merchant.class).orderBy("merchantname collate nocase").list();

        //mAdapter = new ArrayAdapter<Merchant>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, mData);

        mAdapter = new MerchantAdapter(getActivity());
        mAdapter.setData(mData);
        mListView.setAdapter(mAdapter);

    }

}
