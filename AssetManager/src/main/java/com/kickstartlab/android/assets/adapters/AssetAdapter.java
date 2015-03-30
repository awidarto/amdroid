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
import com.kickstartlab.android.assets.rest.models.Asset;
import com.kickstartlab.android.assets.ui.SquareImageView;
import com.squareup.picasso.Picasso;

import java.util.List;

import de.hdodenhof.circleimageview.CircleImageView;

/**
 * Created by awidarto on 12/3/14.
 */
public class AssetAdapter extends BaseAdapter {

    LayoutInflater layoutInflater ;
    private List<Asset> list;
    private Context mContext;
    ColorGenerator generator;

    public AssetAdapter(Context context) {
        mContext = context;
        layoutInflater = LayoutInflater.from(context);
        generator = ColorGenerator.MATERIAL;
    }

    public void setData(List<Asset> list) {
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
            convertView     = layoutInflater.inflate(R.layout.item_row, null);
            holder          = new Holder();
            holder.head   = (TextView) convertView.findViewById(R.id.head);
            holder.subhead     = (TextView) convertView.findViewById(R.id.subhead);
            holder.avatar = (CircleImageView) convertView.findViewById(R.id.avatarpic);

            convertView.setTag(holder);
        }else{
            holder = (Holder) convertView.getTag();
        }

        holder.head.setText(list.get(i).getSKU());
        holder.subhead.setText(list.get(i).getItemDescription() );

        String iconText;
        if(list.get(i).getSKU().length() > 0){
            iconText = list.get(i).getSKU().substring(0,1);
        }else{
            iconText = "-";
        }

        TextDrawable icon = TextDrawable.builder()
                .beginConfig()
                .width(50)  // width in px
                .height(50) // height in px
                .endConfig()
                .buildRound(iconText, generator.getColor(iconText));

        if("".equalsIgnoreCase(list.get(i).getPictureMediumUrl()) == false){
            Picasso.with(mContext)
                    .load(list.get(i).getPictureMediumUrl())
                    .fit()
                    .centerCrop()
                    .placeholder(icon)
                    .into(holder.avatar);
        }else{
            holder.avatar.setImageDrawable(icon);
        }

        return convertView;
    }

    static class Holder{
        TextView head;
        TextView subhead;
        CircleImageView avatar;
    }
}
