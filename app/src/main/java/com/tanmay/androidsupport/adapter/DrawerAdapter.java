package com.tanmay.androidsupport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.squareup.picasso.Picasso;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.customui.RoundedTransformation;
import com.tanmay.androidsupport.interfaces.OnDrawerItemClickListener;

/**
 * Created by TaNMay on 3/4/2016.
 */
public class DrawerAdapter extends RecyclerView.Adapter<DrawerAdapter.ViewHolder> {

    private static final int TYPE_HEADER = 0;
    private static final int TYPE_ITEM = 1;
    public static OnDrawerItemClickListener click;
    Context context;
    int[] icons;
    private String[] mNavTitles;

    public DrawerAdapter(Context context, String titles[], int[] icons) {
        mNavTitles = titles;
        this.context = context;
        this.icons = icons;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == TYPE_ITEM) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_item, parent, false);
            ViewHolder vhItem = new ViewHolder(v, viewType);
            return vhItem;
        } else if (viewType == TYPE_HEADER) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.drawer_header, parent, false);
            ViewHolder vhHeader = new ViewHolder(v, viewType);
            return vhHeader;
        }
        return null;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        if (holder.Holderid == 1) {
            holder.item.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    click.onDrawerItemClick(position - 1);
                }
            });
            holder.drawerItem.setText(mNavTitles[position - 1]);
            Picasso.with(context).load(icons[position - 1])
                    .placeholder(icons[position - 1])
                    .error(icons[position - 1])
                    .resizeDimen(R.dimen.drawer_item_icon, R.dimen.drawer_item_icon)
                    .centerCrop()
                    .into(holder.itemIcon);
        } else {
            Picasso.with(context).load(R.drawable.android)
                    .placeholder(R.drawable.android)
                    .error(R.drawable.android)
                    .transform(new RoundedTransformation(50, 0))
                    .resizeDimen(R.dimen.drawer_header_icon, R.dimen.drawer_header_icon)
                    .centerCrop()
                    .into(holder.profilePic);
        }
    }

    @Override
    public int getItemCount() {
        return mNavTitles.length + 1;
    }

    @Override
    public int getItemViewType(int position) {
        if (isPositionHeader(position))
            return TYPE_HEADER;

        return TYPE_ITEM;
    }

    private boolean isPositionHeader(int position) {
        return position == 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        int Holderid;
        RelativeLayout item;
        TextView drawerItem;
        ImageView itemIcon, profilePic;

        public ViewHolder(View itemView, int ViewType) {
            super(itemView);
            if (ViewType == TYPE_ITEM) {
                item = (RelativeLayout) itemView.findViewById(R.id.item);
                drawerItem = (TextView) itemView.findViewById(R.id.drawer_option);
                itemIcon = (ImageView) itemView.findViewById(R.id.item_icon);
                Holderid = 1;
            } else {
                Holderid = 0;
                profilePic = (ImageView) itemView.findViewById(R.id.profile_pic);
            }
        }
    }
}