package com.kickstartlab.android.assets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.rest.models.Merchant;
import com.kickstartlab.android.assets.rest.models.OrderItem;

import java.util.List;

/**
 * Created by awidarto on 12/3/14.
 */
public class OrderItemAdapter extends BaseAdapter {

    LayoutInflater layoutInflater ;
    private List<OrderItem> list;

    Context ctx;
    String wh_status, deliverytype;

    public OrderItemAdapter(Context context) {
        ctx = context;
        wh_status = context.getResources().getString(R.string.wh_status_long_not_wh);
        layoutInflater = LayoutInflater.from(context);
    }

    public void setData(List<OrderItem> list) {
        this.list = list;
    }

    @Override
    public int getCount() {
        return list.size();
    }

    @Override
    public Object getItem(int i) {
        return list.get(i);
    }

    @Override
    public long getItemId(int i) {
        return list.get(i).getExtId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder;

        if(convertView == null){
            convertView     = layoutInflater.inflate(R.layout.order_item_row, null);
            holder          = new Holder();
            holder.head   = (TextView) convertView.findViewById(R.id.head);
            holder.subhead     = (TextView) convertView.findViewById(R.id.subhead);
            holder.subheadtwo     = (TextView) convertView.findViewById(R.id.subheadtwo);
            holder.subheadthree     = (TextView) convertView.findViewById(R.id.subheadthree);
            //holder.subheadfour     = (TextView) convertView.findViewById(R.id.subheadfour);
            holder.whstatus = (TextView) convertView.findViewById(R.id.wh_status);
            holder.deliverytype = (TextView) convertView.findViewById(R.id.delivery_type);

            convertView.setTag(holder);

        }else{
            holder = (Holder) convertView.getTag();
        }


        holder.head.setText(list.get(i).getBuyerName() + " | " + list.get(i).getRecipientName() );
        String asdate;
        try{
            asdate = list.get(i).getAssignmentDate().substring(0,10);
        }catch(NullPointerException e){

        }finally {
            asdate = list.get(i).getAssignmentDate();
        }
        String dtype =("Delivery Only".equalsIgnoreCase(list.get(i).getDeliveryType()))?"DO":list.get(i).getDeliveryType();
        String zone = list.get(i).getBuyerdeliveryzone();
        holder.subhead.setText( asdate + " " + zone );
        holder.subheadtwo.setText("-");
        holder.subheadthree.setText(list.get(i).getDeliveryId() + " | " + list.get(i).getMerchantTransId() );
        //holder.subheadfour.setText(list.get(i).getMerchantTransId() );
        holder.whstatus.setText(list.get(i).getWarehouseStatus());
        holder.deliverytype.setText(dtype);

        if(wh_status.equalsIgnoreCase(list.get(i).getWarehouseStatus())){
            holder.whstatus.setBackgroundColor( ctx.getResources().getColor(R.color.orange) );
            holder.whstatus.setTextColor( ctx.getResources().getColor(R.color.darkgrey) );
        }else{
            holder.whstatus.setBackgroundColor( ctx.getResources().getColor(R.color.green) );
            holder.whstatus.setTextColor(ctx.getResources().getColor(R.color.white));
        }

        if("COD".equalsIgnoreCase(list.get(i).getDeliveryType()) || "CCOD".equalsIgnoreCase(list.get(i).getDeliveryType()) ){
            holder.deliverytype.setBackgroundColor( ctx.getResources().getColor(R.color.red) );
            holder.deliverytype.setTextColor( ctx.getResources().getColor(R.color.white) );
        }else{
            holder.deliverytype.setBackgroundColor( ctx.getResources().getColor(R.color.green) );
            holder.deliverytype.setTextColor(ctx.getResources().getColor(R.color.white));
        }

        return convertView;
    }

    static class Holder{
        TextView head;
        TextView subhead;
        TextView subheadtwo;
        TextView subheadthree;
        //TextView subheadfour;
        TextView whstatus;
        TextView deliverytype;
    }
}
