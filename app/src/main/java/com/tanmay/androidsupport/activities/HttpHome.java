package com.tanmay.androidsupport.activities;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.Patterns;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.Toast;
import android.widget.ToggleButton;

import com.android.volley.Request;
import com.tanmay.androidsupport.R;
import com.tanmay.androidsupport.datahandlers.CustomRequestDataHandler;
import com.tanmay.androidsupport.datahandlers.JsonArrayDataHandler;
import com.tanmay.androidsupport.datahandlers.JsonObjectDataHandler;
import com.tanmay.androidsupport.interfaces.ResponseListener;

import org.json.JSONArray;
import org.json.JSONObject;

public class HttpHome extends AppCompatActivity implements AdapterView.OnItemSelectedListener, ResponseListener {

    public static String TAG = "HttpHome ==>";
    Context context;

    FloatingActionButton fab;
    Toolbar toolbar;
    EditText tokenInput, urlInput, dataParamsInput;
    TextInputLayout tokenLayout, dataParamsLayout;
    RadioGroup responseTypeSec;
    RadioButton selectedResponseType;
    RelativeLayout httpContent;
    ToggleButton tokenSwitch;
    Spinner requestType;

    ArrayAdapter<CharSequence> requestAdapter;
    String requestTypeSelected, url, dataParams, token, selectedResponse;
    int selectedResponseId, selectedRequestMethod;
    Boolean isToken, isGet, isDataParams;

    JsonObjectDataHandler jsonObjectDataHandler;
    JsonArrayDataHandler jsonArrayDataHandler;
    CustomRequestDataHandler customRequestDataHandler;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_http_home);
        initView();

        context = this;
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("HTTP Service");

        Toast.makeText(context, "This activity is under development.\nProceed with caution!", Toast.LENGTH_LONG).show();

        isGet = false;
        requestAdapter = ArrayAdapter.createFromResource(this, R.array.request_types, R.layout.support_simple_spinner_dropdown_item);
        requestAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        requestType.setAdapter(requestAdapter);
        requestType.setOnItemSelectedListener(this);

        isToken = false;
        tokenSwitch.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                isToken = isChecked;
                if (isChecked) {
                    tokenLayout.setVisibility(View.VISIBLE);
                } else {
                    tokenLayout.setVisibility(View.GONE);
                }
            }
        });

        jsonObjectDataHandler = new JsonObjectDataHandler(this);
        jsonArrayDataHandler = new JsonArrayDataHandler(this);
        customRequestDataHandler = new CustomRequestDataHandler(this);
        isDataParams = false;
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                url = urlInput.getText().toString();
                selectedResponseId = responseTypeSec.getCheckedRadioButtonId();
                selectedResponseType = (RadioButton) findViewById(selectedResponseId);
                selectedResponse = selectedResponseType.getText().toString();

                if (isToken) {
                    if (token.length() == 0)
                        Snackbar.make(httpContent, "Please enter a valid Token.", Snackbar.LENGTH_SHORT).show();
                    else
                        token = tokenInput.getText().toString();
                } else {
                    token = null;
                }

                dataParams = dataParamsInput.getText().toString();
                if (dataParams.length() > 0) {
                    isDataParams = true;
                } else {
                    isDataParams = false;
                }

                if (url.length() == 0) {
                    Log.d(TAG, "Empty URL");
                    Snackbar.make(httpContent, "Please enter a URL.", Snackbar.LENGTH_SHORT).show();
                } else if (!Patterns.WEB_URL.matcher(url).matches()) {
                    Log.d(TAG, "Invalid URL");
                    Snackbar.make(httpContent, "Please enter a valid URL.", Snackbar.LENGTH_SHORT).show();
                } else {
                    if (isDataParams) {
                        int dataParamLength = dataParams.length();
                        if (!dataParams.substring(0, 1).equals("{")
                                || !dataParams.substring(dataParamLength - 1, dataParamLength).equals("}")) {
                            Snackbar.make(httpContent, "Please enter valid Data Params.", Snackbar.LENGTH_SHORT).show();
                        } else {
                            if (selectedResponse.equals("JSON Object")) {
                                jsonObjectDataHandler.sendRequest(null, url, token, selectedRequestMethod);         ////////// To Do
                            } else {
                                customRequestDataHandler.sendRequest(null, url, token, selectedRequestMethod);      ////////// To Do
                            }
                        }
                    } else {
                        if (selectedResponse.equals("JSON Object")) {
                            jsonObjectDataHandler.sendRequest(null, url, token, selectedRequestMethod);
                        } else {
                            jsonArrayDataHandler.sendRequest(url, token, selectedRequestMethod);
                        }
                    }
                }
            }
        });
    }

    void initView() {
        fab = (FloatingActionButton) findViewById(R.id.fab);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        requestType = (Spinner) findViewById(R.id.request_type);
        tokenSwitch = (ToggleButton) findViewById(R.id.token_switch);
        tokenInput = (EditText) findViewById(R.id.input_token);
        tokenLayout = (TextInputLayout) findViewById(R.id.input_layout_token);
        urlInput = (EditText) findViewById(R.id.input_url);
        dataParamsInput = (EditText) findViewById(R.id.input_data_params);
        dataParamsLayout = (TextInputLayout) findViewById(R.id.input_layout_data_params);
        responseTypeSec = (RadioGroup) findViewById(R.id.response_type_sec);
        httpContent = (RelativeLayout) findViewById(R.id.http_content);
    }

    @Override
    public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
        requestTypeSelected = parent.getItemAtPosition(position).toString();
        if (requestTypeSelected.equals("GET")) {
            isGet = true;
            selectedRequestMethod = Request.Method.GET;
        } else if (requestTypeSelected.equals("POST")) {
            isGet = false;
            selectedRequestMethod = Request.Method.POST;
        } else {
            isGet = false;
            selectedRequestMethod = Request.Method.PATCH;
        }
    }

    @Override
    public void onNothingSelected(AdapterView<?> parent) {

    }

    @Override
    public void gotObjectResponse(JSONObject jObject) {

    }

    @Override
    public void gotArrayResponse(JSONArray jArray) {

    }

    @Override
    public void errorResponse() {

    }
}
