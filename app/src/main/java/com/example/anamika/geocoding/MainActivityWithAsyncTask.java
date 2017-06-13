package com.example.anamika.geocoding;

import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.TextView;

import java.io.IOException;
import java.util.List;
import java.util.Locale;

public class MainActivityWithAsyncTask extends AppCompatActivity {

    EditText addressEdit;
    ProgressBar progressBar;
    TextView textView_lat;
    TextView textView_lng;
    CheckBox checkBox;

    private static final String TAG = "MAIN_ACTIVITY_ASYNC";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        addressEdit = (EditText) findViewById(R.id.addressEdit);
        progressBar = (ProgressBar) findViewById(R.id.progressBar);
        textView_lat = (TextView) findViewById(R.id.infoText_lat);
        textView_lng = (TextView) findViewById(R.id.infoText_lng);
        checkBox = (CheckBox) findViewById(R.id.checkbox);

        addressEdit.setEnabled(true);
    }

    public void onButtonClicked(View view) {
        new GeocodeAsyncTask().execute();
    }

    class GeocodeAsyncTask extends AsyncTask<Void, Void, Address> {

        String errorMessage = "";

        @Override
        protected void onPreExecute() {
            textView_lat.setVisibility(View.VISIBLE);
            textView_lng.setVisibility(View.VISIBLE);
            progressBar.setVisibility(View.VISIBLE);
        }

        @Override
        protected Address doInBackground(Void ... none) {
            Geocoder geocoder = new Geocoder(MainActivityWithAsyncTask.this, Locale.getDefault());
            List<Address> addresses = null;

               String name = addressEdit.getText().toString();
                try {
                    addresses = geocoder.getFromLocationName(name, 1);
                } catch (IOException e) {
                    errorMessage = "Service not available";
                    Log.e(TAG, errorMessage, e);
                }

            if(addresses != null && addresses.size() > 0)
                return addresses.get(0);
            return null;
        }
        protected void onPostExecute(Address address) {
            if(address == null) {
                progressBar.setVisibility(View.INVISIBLE);
                //infoText.setVisibility(View.VISIBLE);
                textView_lng.setText(errorMessage);
            }
            else {
                String addressName = "";
                for(int i = 0; i < address.getMaxAddressLineIndex(); i++) {
                    addressName += " --- " + address.getAddressLine(i);
                }
                progressBar.setVisibility(View.INVISIBLE);
               // infoText.setVisibility(View.VISIBLE);
                textView_lat.setText("Latitude: " + address.getLatitude());
                textView_lng.setText("Longitude: " + address.getLongitude());
            }
        }
    }
}
