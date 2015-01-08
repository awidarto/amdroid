package com.kickstartlab.android.jwh.fragments;

import android.app.Activity;
import android.app.DatePickerDialog;
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
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.ListAdapter;
import android.widget.SearchView;
import android.widget.Spinner;
import android.widget.TextView;

import com.kickstartlab.android.jwh.R;
import com.kickstartlab.android.jwh.adapters.OrderItemAdapter;
import com.kickstartlab.android.jwh.events.OrderEvent;
import com.kickstartlab.android.jwh.rest.models.OrderItem;
import com.orm.query.Condition;
import com.orm.query.Select;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
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
public class OrderListFragment extends Fragment implements AbsListView.OnItemClickListener {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private static final int DATE_FROM = 125;
    private static final int DATE_TO = 127;


    // TODO: Rename and change types of parameters
    private Integer mParam1;
    private String mParam2;

    List<OrderItem> mData;

    SearchView mSearchView;
    Spinner mDateFilter;

    Button mDateFrom,mDateTo;

    String today, warehouse_accepted;

    int mYear, mMonth, mDay;

    TextView mTotalPackage, mScannedCount;

    DatePickerDialog datePickerFromDialog, datePickerToDialog;

    /**
     * The fragment's ListView/GridView.
     */
    private AbsListView mListView;

    /**
     * The Adapter which will be used to populate the ListView/GridView with
     * Views.
     */
    private OrderItemAdapter mAdapter;


    // TODO: Rename and change types of parameters
    public static OrderListFragment newInstance(Integer param1, String param2) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public OrderListFragment() {
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if (getArguments() != null) {
            mParam1 = getArguments().getInt(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }

        today = new SimpleDateFormat("yyyy-MM-dd").format(new Date());
        warehouse_accepted = getResources().getString(R.string.wh_status_long_accept_wh);

        final Calendar c = Calendar.getInstance();
        mYear = c.get(Calendar.YEAR);
        mMonth = c.get(Calendar.MONTH);
        mDay = c.get(Calendar.DAY_OF_MONTH);

        setHasOptionsMenu(true);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order_list, container, false);

        // Set the adapter
        mListView = (AbsListView) view.findViewById(android.R.id.list);
        mSearchView = (SearchView) view.findViewById(R.id.searchView);
        mDateFilter = (Spinner) view.findViewById(R.id.filterSpinner);

        mDateFrom = (Button) view.findViewById(R.id.fromDate);
        mDateTo = (Button) view.findViewById(R.id.toDate);

        mTotalPackage = (TextView) view.findViewById(R.id.total_package);
        mScannedCount = (TextView) view.findViewById(R.id.scanned_count);

        mDateFrom.setText(today);
        mDateTo.setText(today);

        mSearchView.setIconifiedByDefault(false);

        mDateFilter.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                try{
                    String s = mSearchView.getQuery().toString();
                    searchList(s);
                }catch(NullPointerException e){
                    refreshList();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

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

        datePickerFromDialog = new DatePickerDialog(getActivity(),new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                mDateFrom.setText(i + "-" + padDigit((i2 + 1))  + "-" + padDigit(i3) );
                try{
                    String s = mSearchView.getQuery().toString();
                    searchList(s);
                }catch(NullPointerException e){
                    refreshList();
                }

            }
        }, mYear, mMonth, mDay);

        datePickerToDialog = new DatePickerDialog(getActivity(), new DatePickerDialog.OnDateSetListener() {
            @Override
            public void onDateSet(DatePicker datePicker, int i, int i2, int i3) {
                mDateTo.setText(i + "-" + padDigit((i2 + 1))  + "-" + padDigit(i3) );
                try{
                    String s = mSearchView.getQuery().toString();
                    searchList(s);
                }catch(NullPointerException e){
                    refreshList();
                }

            }
        }, mYear, mMonth, mDay);

        mDateFrom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerFromDialog.show();
            }
        });

        mDateTo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                datePickerToDialog.show();
            }
        });

        mAdapter = new OrderItemAdapter(getActivity());

        EventBus.getDefault().post(new OrderEvent("refreshById", String.valueOf(mParam1)));
        // Set OnItemClickListener so we can be notified on item clicks
        mListView.setOnItemClickListener(this);

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

    public void onEvent(OrderEvent me){
        if("refresh".equalsIgnoreCase(me.getAction())){
            refreshList();
            //Toast.makeText(getActivity(), me.getAction(), Toast.LENGTH_LONG).show();
        }

    }

    public void onCreateOptionsMenu (Menu menu, MenuInflater inflater) {
        super.onCreateOptionsMenu(menu, inflater);

        MenuItem menuItem;

        if(menu.getItem(0).isVisible()){
            menu.getItem(0).setVisible(false);
        }

        menuItem = menu.add(Menu.NONE, R.id.action_scan, 0, R.string.action_scan);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);

        menuItem = menu.add(Menu.NONE, R.id.action_refresh, 0, R.string.action_refresh).setIcon(R.drawable.ic_cloud_download_white_24dp);
        MenuItemCompat.setShowAsAction(menuItem, MenuItem.SHOW_AS_ACTION_ALWAYS);


    }



    @Override
    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

        EventBus.getDefault().post(new OrderEvent("select",mData.get(position)));

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

        String sel = mDateFilter.getSelectedItem().toString();

        String q = "%" + s + "%";
        String mid = String.valueOf(mParam1);
        String t = today + "%";

        String to = mDateFrom.getText().toString();
        String from = mDateTo.getText().toString();

        Long mScanned = 0L;

        if( "Today".equalsIgnoreCase(sel)){
            String ql = "SELECT * FROM ORDER_ITEM WHERE merchant_id = ?  AND ( buyerdeliverytime LIKE ? OR buyer_name LIKE ? OR recipient_name LIKE ? OR buyerdeliveryzone LIKE ? ) AND assignment_date LIKE ? ORDER BY assignment_date desc";
            mData = OrderItem.findWithQuery(OrderItem.class, ql, mid ,q, q, q, q, t);

            String cql = "merchant_id = ?  AND ( buyerdeliverytime LIKE ? OR buyer_name LIKE ? OR recipient_name LIKE ? OR buyerdeliveryzone LIKE ? ) AND assignment_date LIKE ? AND warehouse_status = ? ";

            mScanned = (Long) OrderItem.count(OrderItem.class, cql, new String[]{mid ,q, q, q, q, t, warehouse_accepted} );

        }else if("Range".equalsIgnoreCase(sel)){
            String ql = "SELECT * FROM ORDER_ITEM WHERE merchant_id = ?  AND ( buyerdeliverytime LIKE ? OR buyer_name LIKE ? OR recipient_name LIKE ? OR buyerdeliveryzone LIKE ? ) AND assignment_date BETWEEN ? AND ? ORDER BY assignment_date desc";
            mData = OrderItem.findWithQuery(OrderItem.class, ql, mid ,q, q, q, q, to, from);

            String cql = "merchant_id = ?  AND ( buyerdeliverytime LIKE ? OR buyer_name LIKE ? OR recipient_name LIKE ? OR buyerdeliveryzone LIKE ? ) AND ( assignment_date BETWEEN ? AND ? ) AND warehouse_status = ? ";

            mScanned = (Long) OrderItem.count(OrderItem.class, cql, new String[]{mid ,q, q, q, q, to, from, warehouse_accepted} );

        }else{
            String ql = "SELECT * FROM ORDER_ITEM WHERE merchant_id = ?  AND ( buyerdeliverytime LIKE ? OR buyer_name LIKE ? OR recipient_name LIKE ? OR buyerdeliveryzone LIKE ? ) ORDER BY assignment_date desc";
            mData = OrderItem.findWithQuery(OrderItem.class, ql, mid ,q, q, q, q);

            String cql = "merchant_id = ?  AND ( buyerdeliverytime LIKE ? OR buyer_name LIKE ? OR recipient_name LIKE ? OR buyerdeliveryzone LIKE ? ) AND warehouse_status = ? ";

            mScanned = (Long) OrderItem.count(OrderItem.class, cql, new String[]{mid ,q, q, q, q, warehouse_accepted} );

        }

        mTotalPackage.setText( String.valueOf( mData.size() ) );
        mScannedCount.setText(String.valueOf(mScanned));

        mAdapter = new OrderItemAdapter(getActivity());
        mAdapter.setData(mData);

        mListView.setAdapter(mAdapter);

    }

    public void refreshList(){

        String sel = mDateFilter.getSelectedItem().toString();

        String to = mDateFrom.getText().toString() + " 00:00:00";
        String from = mDateTo.getText().toString() + " 59:59:59";;

        long mScanned = 0L;


        if( "Today".equalsIgnoreCase(sel)){
            mData =  Select.from(OrderItem.class)
                    .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)),
                            Condition.prop("assignment_date").like(today + "%"))
                    .orderBy("assignment_date desc").list();

            mScanned =  Select.from(OrderItem.class)
                    .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)),
                            Condition.prop("warehouse_status").eq(warehouse_accepted),
                            Condition.prop("assignment_date").like(today + "%")).count();

        }else if("Range".equalsIgnoreCase(sel)){
            mData =  Select.from(OrderItem.class)
                    .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)),
                            Condition.prop("assignment_date").gt(from), Condition.prop("assignment_date").lt(to))
                    .orderBy("assignment_date desc").list();

            mScanned =  Select.from(OrderItem.class)
                    .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)),
                            Condition.prop("warehouse_status").eq(warehouse_accepted),
                            Condition.prop("assignment_date").gt(from), Condition.prop("assignment_date").lt(to))
                    .count();

        }else{
            mData =  Select.from(OrderItem.class)
                    .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)))
                    .orderBy("assignment_date desc").list();

            mScanned =  Select.from(OrderItem.class)
                    .where(Condition.prop("merchant_id").eq(String.valueOf(mParam1)),
                            Condition.prop("warehouse_status").eq(warehouse_accepted))
                    .count();

        }

        mTotalPackage.setText( String.valueOf( mData.size() ) );
        mScannedCount.setText(String.valueOf(mScanned));
        //mAdapter = new ArrayAdapter<OrderItem>(getActivity(),
        //        android.R.layout.simple_list_item_1, android.R.id.text1, mData);

        mAdapter = new OrderItemAdapter(getActivity());
        mAdapter.setData(mData);

        mListView.setAdapter(mAdapter);

    }

    public String padDigit(int number)
    {
        return number<=9?"0"+number:String.valueOf(number);
    }
}
