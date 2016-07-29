package com.tanmay.androidsupport.adapter;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.models.Document;

import java.util.ArrayList;

/**
 * Created by tanma on 5/23/2016.
 */
public class DocumentAdapter extends RecyclerView.Adapter<DocumentAdapter.ViewHolder> {

    Context context;
    ArrayList<Document> documents;

    public DocumentAdapter(Context context, ArrayList<Document> documents) {
        super();
        this.context = context;
        this.documents = documents;
    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup viewGroup, final int i) {
        View v = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.document_item, viewGroup, false);
        ViewHolder viewHolder = new ViewHolder(v);

        return viewHolder;

    }

    @Override
    public void onBindViewHolder(ViewHolder viewHolder, final int i) {
        viewHolder.imageView.setImageDrawable(Drawable.createFromPath(documents.get(i).getFileName().toString()));
        viewHolder.textView.setText(documents.get(i).getTitle());
    }

    @Override
    public int getItemCount() {
        return documents.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder {

        public ImageView imageView;
        public TextView textView;

        public ViewHolder(View itemView) {
            super(itemView);
            imageView = (ImageView) itemView.findViewById(R.id.di_doc);
            textView = (TextView) itemView.findViewById(R.id.di_doc_title);
        }
    }

}
