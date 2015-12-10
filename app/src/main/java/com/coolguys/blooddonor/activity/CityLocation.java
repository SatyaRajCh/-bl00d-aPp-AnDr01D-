package com.coolguys.blooddonor.activity;

import android.app.AlertDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.AsyncTask;
import android.os.Bundle;
import android.provider.Settings;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;




import static android.Manifest.permission.ACCESS_COARSE_LOCATION;
import static android.Manifest.permission.ACCESS_FINE_LOCATION;
import static android.Manifest.permission.INTERNET;

import info.androidhive.materialdesign.R;

public class CityLocation extends AppCompatActivity {

    ProgressBar pb;
    public Boolean flag;

    private LocationManager locationManager = null;
    private LocationListener locationListener = null;
    private TextView mTvView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_city_location);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        pb = (ProgressBar) findViewById(R.id.progressBar1);
        mTvView = (TextView) findViewById(R.id.tv);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                            .setAction("Action", null).show();

                    flag = displayGpsStatus();
                    if (flag) {


                        pb.setVisibility(View.VISIBLE);
                        locationListener = new MyLocationListener();

                        locationManager.requestLocationUpdates(LocationManager
                                .NETWORK_PROVIDER, 5000, 10, locationListener);
                        //locationManager.getLastKnownLocation();
                    } else {
                        alertbox("Gps Status!!", "Your GPS is: OFF");
                    }
                } catch (SecurityException securityException) {
                    Toast.makeText(getApplicationContext(), "Failed to Update", Toast.LENGTH_LONG).show();
                }
            }
        });


    }

    /*----Method to Check GPS is enable or disable ----- */
    private Boolean displayGpsStatus() {
        ContentResolver contentResolver = getBaseContext()
                .getContentResolver();
        boolean gpsStatus = Settings.Secure
                .isLocationProviderEnabled(contentResolver,
                        LocationManager.GPS_PROVIDER);
        if (gpsStatus) {
            return true;

        } else {
            return false;
        }
    }

    /*----------Method to create an AlertBox ------------- */
    protected void alertbox(String title, String mymessage) {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setMessage("Your Device's GPS is Disable")
                .setCancelable(false)
                .setTitle("** Gps Status **")
                .setPositiveButton("Gps On",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // finish the current activity
                                // AlertBoxAdvance.this.finish();
                                Intent myIntent = new Intent(
                                        Settings.ACTION_LOCATION_SOURCE_SETTINGS);
                                startActivity(myIntent);
                                dialog.cancel();
                            }
                        })
                .setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                // cancel the dialog box
                                dialog.cancel();
                            }
                        });
        AlertDialog alert = builder.create();
        alert.show();
    }

    /*----------Listener class to get coordinates ------------- */
    private class MyLocationListener implements LocationListener {
        @Override
        public void onLocationChanged(Location loc) {

            pb.setVisibility(View.INVISIBLE);
            Toast.makeText(getBaseContext(), "Location changed : Lat: " +
                            loc.getLatitude() + " Lng: " + loc.getLongitude(),
                    Toast.LENGTH_SHORT).show();
            String longitude = "Longitude: " + loc.getLongitude();

            String latitude = "Latitude: " + loc.getLatitude();

    /*----------to get City-Name from coordinates ------------- */
            String cityName = null;


            Geocoder gcd = new Geocoder(getBaseContext(),
                    Locale.getDefault());
            List<Address> addresses;
            try {
                addresses = gcd.getFromLocation(loc.getLatitude(), loc
                        .getLongitude(), 1);
                if (addresses.size() > 0)
                    System.out.println(addresses.get(0).getLocality());

                cityName = addresses.get(0).getLocality();
            } catch (IOException e) {
                e.printStackTrace();
            }

            String s = longitude + "\n" + latitude +
                    "\n\nMy Currrent City is: " + cityName;
            Toast.makeText(getApplicationContext(), s, Toast.LENGTH_LONG).show();
            mTvView.setText(s);
            new ZipCode("","","","").execute();
        }

        @Override
        public void onProviderDisabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onProviderEnabled(String provider) {
            // TODO Auto-generated method stub
        }

        @Override
        public void onStatusChanged(String provider,
                                    int status, Bundle extras) {
            // TODO Auto-generated method stub
        }
    }


    /**
     * Represents an asynchronous login/registration task used to authenticate
     * the user.
     */
    public class ZipCode extends AsyncTask<Void, Void, String> {

        private final String mName;
        private final String mPhone;
        private final String mBloodGroup;
        private final String mZipCode;

        ZipCode(String mName, String mPhone, String mBloodGroup, String mZipCode) {
            this.mName = mName;
            this.mPhone = mPhone;
            this.mBloodGroup = mBloodGroup;
            this.mZipCode = mZipCode;
        }

        @Override
        protected String doInBackground(Void... params) {
            // TODO: attempt authentication against a network service.

            URL url;
            String response = "";
            HttpURLConnection urlConnection = null;
            try {
                url = new URL("http://maps.googleapis.com/maps/api/geocode/json?latlng=17.4407927,78.3884554&sensor=true/");

                urlConnection = (HttpURLConnection) url
                        .openConnection();

                InputStream in = urlConnection.getInputStream();

                InputStreamReader isw = new InputStreamReader(in);

                int data = isw.read();

                while (data != -1) {
                    char current = (char) data;
                    response = response + current;
                    data = isw.read();
                    System.out.print(current);
                }
            } catch (Exception e) {
                e.printStackTrace();
            } finally {
                try {
                    urlConnection.disconnect();
                } catch (Exception e) {
                    e.printStackTrace(); //If you want further info on failure...
                }
            }

            // TODO: register the new account here.
            return response;
        }

        @Override
        protected void onPostExecute(final String success) {

            Toast.makeText(getApplicationContext(), success, Toast.LENGTH_LONG).show();

        }

        @Override
        protected void onCancelled() {


        }
    }

    /*public String GET(String url, HashMap<String, String> hashMap){
        InputStream inputStream = null;
        HttpResponse response = null;
        String result = null;
        try {

            // 1. create HttpClient

            HttpClient client;
            HttpsTrustedManager httpsTrustManager = new HttpsTrustedManager();
            client = httpsTrustManager.getNewHttpClient();

            HttpGet request = new HttpGet();
            request.setURI(new URI(url));


            final String basicAuth = "Basic " + Base64.encodeToString("admin:1234".getBytes(), Base64.NO_WRAP);


            //5. Set some headers to inform server about the type of the content
            request.setHeader("Accept", "application/x-www-form-urlencoded");
            request.setHeader("Content-type", "application/x-www-form-urlencoded");

            //6. Set some headers to inform server parameters
            SharedPreferences user_details = activity.getSharedPreferences("user_details", Context.MODE_PRIVATE);
            request.setHeader("access-token", user_details.getString("access_token", ""));
            request.setHeader("Authorization", basicAuth);
            System.out.println(user_details.getString("access_token", ""));
            // 7. Set some headers to inform server about the type of the content
            //System.out.println("httppost"+httpPost);


            // httpPost.setEntity(new UrlEncodedFormEntity(nameValuePairs));

            response = client.execute(request);


            System.out.println("http gettttt"+response);

            // 9. receive response as inputStream
            inputStream = response.getEntity().getContent();
            System.out.println("input stream"+inputStream);

            // 10. convert inputstream to string
            if(inputStream != null)
            {
                result = Utils.convertInputStreamToString(inputStream);
                System.out.println("result"+result);
            }
            else
                result = "Did not work!";

        } catch (URISyntaxException e) {
            e.printStackTrace();
        } catch (ClientProtocolException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        } catch (IOException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        System.out.println("final result"+result);

        // 11. return result
        return result;
    }*/
}
