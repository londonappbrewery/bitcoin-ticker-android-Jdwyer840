package com.londonappbrewery.bitcointicker;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.Toast;


import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONException;
import org.json.JSONObject;

import cz.msebera.android.httpclient.Header;

//BEFORE LOOKING AT THIS AND GETTING CONFUSED NOTE THAT THERE IS ALSO A WEATHER APP WE MADE
//LOOK AT THE DIFFERENCES BETWEEN THE TWO.
//IN WEATHER APP WE HAD A WHOLE CLASS TO HANDLE JSON STUFF AND IN THIS CLASSS WE CREATED A SPECIALL FROM JSON CONSTRUCTOR... BASICALLY AND WE USED THE OTHER CLASS
// IE THE WEATHER CONTROLLER APP
public class MainActivity extends AppCompatActivity {

    // Constants:
    // TODO: Create the base URL
    private final String BASE_URL = "https://apiv2.bitcoinaverage.com/indices/global/ticker/BTC";

    // Member Variables:
    TextView mPriceTextView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        //**BELOW IS AN EXCELLENT EXAMPLE OF HOW TO CRANK OUT A DROP DOWN MENU TYPE THING OR
        //IE THE SPINNER (MAY BE OTHER DROP DOWN THINGS... NOT TOO SURE
        mPriceTextView = (TextView) findViewById(R.id.priceLabel);
        Spinner spinner = (Spinner) findViewById(R.id.currency_spinner);

        // Create an ArrayAdapter using the String array and a spinner layout
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this,
                R.array.currency_array, R.layout.spinner_item);

        // Specify the layout to use when the list of choices appears
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item);

        // Apply the adapter to the spinner
        spinner.setAdapter(adapter);

        // TODO: Set an OnItemSelected listener on the spinner
        spinner.setOnItemSelectedListener(new OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {//parent, view, position, rowID?
                adapterView.getItemAtPosition(i); // see the google documentation https://developer.android.com/guide/topics/ui/controls/spinner.html
                                                    //AdapterView is the parent... all i can say now...
                Log.d("Bitcoin", "" + adapterView.getItemAtPosition(i));

                letsDoSomeNetworking(BASE_URL+adapterView.getItemAtPosition(i));
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {
                Log.d("Bitcoin", "Nothing Selected");
            }
        });
    }

    // TODO: complete the letsDoSomeNetworking() method
    private void letsDoSomeNetworking(String url) {

        AsyncHttpClient client = new AsyncHttpClient();

        client.get(url, new JsonHttpResponseHandler(){

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // called when response HTTP status is "200 OK"
                Log.d("Bitcoin", "JSON: " + response.toString());

                //we need try catch for json. response gives us all the crap we got from bitcoin json object!

                try {
                    String ask = response.getString("ask");

                    mPriceTextView.setText(ask);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onFailure(int statusCode, Header[] headers, Throwable e, JSONObject response) {
                // called when response HTTP status is "4XX" (eg. 401, 403, 404)
                Log.d("Bitcoin", "Request fail! Status code: " + statusCode);
                Log.d("Bitcoin", "Fail response: " + response);
                Log.e("ERROR", e.toString());

            }
        });


    }




}
