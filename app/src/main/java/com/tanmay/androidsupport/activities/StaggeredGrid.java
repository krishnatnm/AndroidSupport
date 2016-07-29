package com.tanmay.androidsupport.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.Toolbar;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.utils.ConstantClass;
import com.tanmay.androidsupport.adapter.StaggeredGridAdapter;

import java.util.ArrayList;

public class StaggeredGrid extends AppCompatActivity {

    public static String TAG = "StaggeredGrid ==>";

    Context context;

    Toolbar toolbar;
    RecyclerView mGridView;

    ArrayList<String> imagesForGrid;
    ArrayList<Integer> heightOfImages;
    ArrayList<Integer> widthOfImages;

    StaggeredGridAdapter staggeredGridAdapter;
    StaggeredGridLayoutManager staggeredGridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_staggered_grid);

        context = this;
        initView();
        setSupportActionBar(toolbar);

        imagesForGrid = addImagesToList();
        heightOfImages = addDimenToList("height");
        widthOfImages = addDimenToList("width");
        staggeredGridAdapter = new StaggeredGridAdapter(this, addImagesToList(), heightOfImages, widthOfImages);
        staggeredGridLayoutManager = new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL);
        mGridView.setLayoutManager(staggeredGridLayoutManager);
        mGridView.setAdapter(staggeredGridAdapter);

    }

    public void initView() {
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        mGridView = (RecyclerView) findViewById(R.id.csd_grid_view);
    }

    public ArrayList<String> addImagesToList() {
        ArrayList<String> images = new ArrayList<String>();
        for (int i = 0; i < ConstantClass.GRID_IMAGES.length; i++) {
            images.add(ConstantClass.GRID_IMAGES[i]);
        }
        return images;
    }

    public ArrayList<Integer> addDimenToList(String type) {
        ArrayList<Integer> dimen = new ArrayList<Integer>();
        for (int i = 0; i < ConstantClass.GRID_IMAGE_HEIGHT.length; i++) {
            if (type.equals("height")) {
                dimen.add(ConstantClass.GRID_IMAGE_HEIGHT[i]);
            } else if (type.equals("width")) {
                dimen.add(ConstantClass.GRID_IMAGES_WIDTH[i]);
            }
        }
        return dimen;
    }

}
