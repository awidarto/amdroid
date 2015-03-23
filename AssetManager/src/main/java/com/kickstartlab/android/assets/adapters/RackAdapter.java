package com.kickstartlab.android.assets.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.amulyakhare.textdrawable.TextDrawable;
import com.amulyakhare.textdrawable.util.ColorGenerator;
import com.kickstartlab.android.assets.R;
import com.kickstartlab.android.assets.rest.models.Rack;

import java.util.List;

/**
 * Created by awidarto on 12/3/14.
 */
public class RackAdapter extends BaseAdapter {

    LayoutInflater layoutInflater ;
    private List<Rack> list;
    ColorGenerator generator;

    public RackAdapter(Context context) {
        layoutInflater = LayoutInflater.from(context);
        generator = ColorGenerator.MATERIAL;
    }

    public void setData(List<Rack> list) {
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
        return list.get(i).getId();
    }

    @Override
    public View getView(int i, View convertView, ViewGroup viewGroup) {
        Holder holder;

        if(convertView == null){
            convertView     = layoutInflater.inflate(R.layout.texthead_item_row, null);
            holder          = new Holder();
            holder.head   = (TextView) convertView.findViewById(R.id.head);
            holder.subhead     = (TextView) convertView.findViewById(R.id.subhead);
            holder.icon = (ImageView) convertView.findViewById(R.id.avatarpic);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.head.setText(list.get(i).getSKU());
        holder.subhead.setText(list.get(i).getItemDescription() );

        String iconText = list.get(i).getSKU().substring(0,1);
        TextDrawable icon = TextDrawable.builder().buildRound(iconText, generator.getColor(iconText));

        holder.icon.setImageDrawable(icon);

        return convertView;
    }

    static class Holder{
        ImageView icon;
        TextView head;
        TextView subhead;
    }
}
