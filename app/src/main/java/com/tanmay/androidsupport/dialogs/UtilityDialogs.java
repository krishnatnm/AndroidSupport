package com.tanmay.androidsupport.dialogs;

import android.app.AlertDialog;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.location.GeocoderAPI;
import com.tanmay.androidsupport.location.PlacesAutoCompleteAdapter;
import com.tanmay.androidsupport.interfaces.OnUtilityDialogButtonClickListener;

import java.io.File;

/**
 * Created by TaNMay on 3/5/2016.
 */
public class UtilityDialogs {

    Context context;
    LayoutInflater li;
    View custom;
    AlertDialog.Builder alertDialogBuilder;
    AlertDialog alertDialog;
    OnUtilityDialogButtonClickListener listener;
    GeocoderAPI forLocation;
    String descOrigin, descDest;
    Boolean isOrigin, isDestination;

    public UtilityDialogs(Context context, OnUtilityDialogButtonClickListener listener) {
        this.context = context;
        this.listener = listener;
    }

    public void toDrawRoute() {
        li = LayoutInflater.from(context);
        custom = li.inflate(R.layout.dialog_get_route, null);
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(custom);
        alertDialogBuilder.setCancelable(true);

        // create alert dialog
        alertDialog = alertDialogBuilder.create();
        //////////////////// Your Stuff ////////////////////
        ImageView closeButton = (ImageView) custom.findViewById(R.id.close_button);
        final AutoCompleteTextView origin = (AutoCompleteTextView) custom.findViewById(R.id.pickup);
        final AutoCompleteTextView destination = (AutoCompleteTextView) custom.findViewById(R.id.destination);
        Button getRoute = (Button) custom.findViewById(R.id.getRoute);

        isOrigin = false;
        isDestination = false;

        forLocation = new GeocoderAPI();
        origin.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.autocomplete_list_item));
        origin.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isOrigin = true;
                descOrigin = (String) parent.getItemAtPosition(position);
            }
        });
        destination.setAdapter(new PlacesAutoCompleteAdapter(context, R.layout.autocomplete_list_item));
        destination.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                isDestination = true;
                descDest = (String) parent.getItemAtPosition(position);
            }
        });

        getRoute.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (origin.getText().toString().length() == 0 || !isOrigin) {
                    Toast.makeText(context, "Please select a origin from the options!", Toast.LENGTH_SHORT).show();
                } else if (destination.getText().toString().length() == 0 || !isDestination) {
                    Toast.makeText(context, "Please select a destinationn from the options!", Toast.LENGTH_SHORT).show();
                } else {
                    alertDialog.dismiss();
                    listener.onAcceptClick(descOrigin, descDest);
                }
            }
        });
        closeButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                alertDialog.dismiss();
            }
        });
        ////////////////////////////////////////////////////
        alertDialog.show();
    }

    public void viewSignature(File file) {
        li = LayoutInflater.from(context);
        custom = li.inflate(R.layout.dialog_view_signature, null);
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(custom);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();

        ImageView imageView = (ImageView) custom.findViewById(R.id.dvs_signature);
        imageView.setImageDrawable(Drawable.createFromPath(file.toString()));

        alertDialog.show();
    }

    public void inputText(final File file) {
        li = LayoutInflater.from(context);
        custom = li.inflate(R.layout.text_input_dialog, null);
        alertDialogBuilder = new AlertDialog.Builder(context);
        alertDialogBuilder.setView(custom);
        alertDialogBuilder.setCancelable(true);
        alertDialog = alertDialogBuilder.create();

        final TextView title = (TextView) custom.findViewById(R.id.tid_title);
        final EditText input = (EditText) custom.findViewById(R.id.tid_input);
        ImageView accept = (ImageView) custom.findViewById(R.id.tid_accept);

        title.setText("Enter a title -");
        accept.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String titleText;
                alertDialog.dismiss();
                if (input.getText().toString().isEmpty()) {
                    titleText = "NA";
                } else {
                    titleText = input.getText().toString();
                }
                listener.onAcceptClick(titleText, file);
            }
        });

        alertDialog.show();
    }
}
