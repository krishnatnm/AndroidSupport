package com.tanmay.androidsupport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.squareup.picasso.LruCache;
import com.squareup.picasso.Picasso;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.customui.DynamicHeightImageView;
import com.tanmay.androidsupport.customui.PlaceHolderDrawableHelper;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by TaNMay on 6/8/2016.
 */
public class StaggeredGridAdapter extends RecyclerView.Adapter {

    Context context;
    List<String> imageUrls = new ArrayList<>();
    List<Integer> imageHeight = new ArrayList<Integer>();
    List<Integer> imageWidth = new ArrayList<Integer>();

    public StaggeredGridAdapter(Context context, List<String> imageUrls, List<Integer> imageHeight, List<Integer> imageWidth) {
        this.context = context;
        this.imageUrls = imageUrls;
        this.imageHeight = imageHeight;
        this.imageWidth = imageWidth;
        Picasso picasso = new Picasso.Builder(context)
                .memoryCache(new LruCache(24000))
                .build();
        picasso.setIndicatorsEnabled(true);
        picasso.setLoggingEnabled(true);
//        Picasso.setSingletonInstance(picasso);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup viewGroup, int position) {
        View itemView = LayoutInflater.from(context).inflate(R.layout.resizable_grid_item, null);
        MyViewHolder holder = new MyViewHolder(itemView);
        holder.imageView = (DynamicHeightImageView) itemView.findViewById(R.id.rgi_image);
        holder.positionTextView = (TextView) itemView.findViewById(R.id.rgi_text);
        itemView.setTag(holder);
        return holder;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder viewHolder, int position) {
        MyViewHolder vh = (MyViewHolder) viewHolder;
        vh.positionTextView.setText("Position: " + position);

        int width = imageWidth.get(position);
        int height = imageHeight.get(position);
        float ratio = (float) height / (float) width;

        Log.d("DIMENS ==>", "(" + width + ", " + height + ")");

        vh.imageView.setRatio(ratio);
        Picasso.with(context)
                .load(imageUrls.get(position))
                .placeholder(PlaceHolderDrawableHelper.getBackgroundDrawable(position))
                .into(vh.imageView);
    }

    @Override
    public int getItemCount() {
        return imageUrls.size();
    }

    public class MyViewHolder extends RecyclerView.ViewHolder {
        public DynamicHeightImageView imageView;
        public TextView positionTextView;

        public MyViewHolder(View itemView) {
            super(itemView);
        }
    }
}