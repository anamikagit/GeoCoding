package com.example.anamika.geocoding;

import android.content.Intent;
import android.location.Address;
import android.os.Bundle;
import android.os.Handler;
import android.os.ResultReceiver;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {

    AddressResultReceiver mResultReceiver;

    EditText addressEdit;
    ProgressBar progressBar;
    TextView textView_lat;
    TextView textView_lng;
    CheckBox checkBox;

    private static final String TAG = "MAIN_ACTIVITY";

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


        mResultReceiver = new AddressResultReceiver(null);
    }

    public void onButtonClicked(View view) {
        Intent intent = new Intent(this, GeocodeAddressIntentService.class);
        intent.putExtra(Constants.RECEIVER, mResultReceiver);
            if(addressEdit.getText().length() == 0) {
                Toast.makeText(this, "Please enter an address name", Toast.LENGTH_LONG).show();
                return;
            }
            intent.putExtra(Constants.LOCATION_NAME_DATA_EXTRA, addressEdit.getText().toString());

        textView_lat.setVisibility(View.VISIBLE);
        textView_lng.setVisibility(View.VISIBLE);
        progressBar.setVisibility(View.VISIBLE);
        Log.e(TAG, "Starting Service");
        startService(intent);
    }

    class AddressResultReceiver extends ResultReceiver {
        public AddressResultReceiver(Handler handler) {
            super(handler);
        }

        @Override
        protected void onReceiveResult(int resultCode, final Bundle resultData) {
            if (resultCode == Constants.SUCCESS_RESULT) {
                final Address address = resultData.getParcelable(Constants.RESULT_ADDRESS);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        //infoText.setVisibility(View.VISIBLE);
                        textView_lat.setText("Latitude: " + address.getLatitude());
                        textView_lng.setText("Longitude: " + address.getLongitude());

                    }
                });
            }
            else {
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        progressBar.setVisibility(View.GONE);
                        //textView_lat.setVisibility(View.VISIBLE);
                        textView_lat.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                        textView_lng.setText(resultData.getString(Constants.RESULT_DATA_KEY));
                    }
                });
            }
        }
    }
}
