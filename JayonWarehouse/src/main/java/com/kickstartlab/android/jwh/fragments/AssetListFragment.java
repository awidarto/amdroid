package com.kickstartlab.android.jwh.fragments;

import android.app.Activity;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListAdapter;
import android.widget.TextView;
import com.kickstartlab.android.jwh.R;

import com.kickstartlab.android.jwh.rest.models.Asset;

import java.util.List;

/**
 * A fragment representing a list of Items.
 * <p />
 * Large screen devices (such as tablets) are supported by replacing the ListView
 * with a GridView.
 * <p />
 * Activities containing this fragment MUST implement the {@link Callbacks}
 * interface.
 */
public class AssetListFragment extends Fragment implements AbsListView.OnItemClickListener, MessageDialogFragment.MessageDialogListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    List<Asset> mData;

    private OnAssetInteractionListener mListener;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private ListAdapter mAdapter;

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

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_asset, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        ((AdapterView<ListAdapter>) mListView).setAdapter(mAdapter);

        refreshList();

        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

        return view;
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        try {
            mListener = (OnAssetInteractionListener) activity;
        } catch (ClassCastException e) {
            throw new ClassCastException(activity.toString()
                + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
        if (null != mListener) {
            // Notify the active callbacks interface (the activity, if the
            // fragment is attached to one) that an item has been selected.
            //mListener.onAssetInteraction(DummyContent.ITEMS.get(position).id);

            String assetId = mData.get(position).getExtId();

            Asset asset = mData.get(position);

            String message = new StringBuilder("ID : ")
                    .append(asset.getSKU())
                    .append(System.lineSeparator())
                    .append("Type : ").append(asset.getAssetType())
                    .append(System.lineSeparator())
                    .append("IP : ").append(asset.getIP())
                    .append(System.lineSeparator())
                    .append("Host : ").append(asset.getHostName())
                    .append(System.lineSeparator())
                    .append("Desc : ")
                    .append(System.lineSeparator())
                    .append(asset.getItemDescription())
                    .toString();

            showMessageDialog(message);

        }
    }

    @Override
    public void onDialogPositiveClick(MessageDialogFragment dialog, String deliveryId) {

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

    public void refreshList(){

        Log.i("location id", mParam1);

        //mData = Select.from(Rack.class).where(Condition.prop("location_id").eq(mParam1)).list();
        mData = Asset.find(Asset.class, "rack_id = ?", mParam1);
        //mData = Rack.listAll(Rack.class);

        mAdapter = new ArrayAdapter<Asset>(getActivity(),
                android.R.layout.simple_list_item_1, android.R.id.text1, mData);
        mListView.setAdapter(mAdapter);

    }

    public void showMessageDialog(String message) {
        //DialogFragment fragment = MessageDialogFragment.newInstance("Asset Info", message, this);
        //fragment.show(getActivity().getSupportFragmentManager(), "scan_results");
    }

    public void closeMessageDialog() {
        closeDialog("scan_results");
    }

    public void closeDialog(String dialogName) {
        FragmentManager fragmentManager = getActivity().getSupportFragmentManager();
        DialogFragment fragment = (DialogFragment) fragmentManager.findFragmentByTag(dialogName);
        if(fragment != null) {
            fragment.dismiss();
        }
    }

    /**
    * This interface must be implemented by activities that contain this
    * fragment to allow an interaction in this fragment to be communicated
    * to the activity and potentially other fragments contained in that
    * activity.
    * <p>
    * See the Android Training lesson <a href=
    * "http://developer.android.com/training/basics/fragments/communicating.html"
    * >Communicating with Other Fragments</a> for more information.
    */
    public interface OnAssetInteractionListener {
        // TODO: Update argument type and name
        public void onAssetInteraction(String id);
    }

}
