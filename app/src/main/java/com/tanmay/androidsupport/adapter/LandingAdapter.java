package com.tanmay.androidsupport.adapter;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.interfaces.OnLandingItemClickListener;

import java.util.ArrayList;

/**
 * Created by TaNMay on 3/4/2016.
 */
public class LandingAdapter extends RecyclerView.Adapter<LandingAdapter.ViewHolder> {

    Context context;
    ArrayList<String> listItems;
    public static OnLandingItemClickListener click;

    public LandingAdapter(Context context, ArrayList<String> listItems) {
        super();
        this.context = context;
        this.listItems = listItems;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext())
                .inflate(R.layout.landing_list_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        v.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                click.onItemClick(v);
            }
        });

        return viewHolder;
    }

    @Override
    public void onBindViewHolder(final ViewHolder viewHolder, final int position) {
        viewHolder.textView.setText(listItems.get(position));
    }

    @Override
    public int getItemCount() {
        return listItems.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            textView = (TextView) itemView.findViewById(R.id.text_view);
        }
    }
}