package com.coolguys.blooddonor.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.media.Image;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import info.androidhive.materialdesign.R;


public class ContactDonor extends AppCompatActivity {

    String donorName = null;
    String donorNumber = null;
    String message = "It's an emergency. Please donate your blood. Thank you.";
    TextView mDonorProfileNameView, mDonorNumberView;
    RelativeLayout mPhoneCallLayoutView;
    ImageView mPhoneCallView, mSendMessageView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_donor);
        Intent intent = getIntent();

        donorName = intent.getStringExtra("donorName");
        donorNumber = intent.getStringExtra("donorNumber");

        mDonorProfileNameView = (TextView) findViewById(R.id.donor_profile_name);
        mDonorNumberView = (TextView) findViewById(R.id.donorNumber);

        mPhoneCallView = (ImageView) findViewById(R.id.phoneCall);
        mSendMessageView = (ImageView) findViewById(R.id.sendMessage);
        mPhoneCallLayoutView = (RelativeLayout) findViewById(R.id.phoneCallLayout);

        View.OnClickListener callListener = new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                try{
                    Intent callIntent = new Intent(Intent.ACTION_CALL);
                    callIntent.setData(Uri.parse("tel:"+donorNumber));
                    startActivity(callIntent);
                } catch (SecurityException ex){
                    Toast.makeText(getApplicationContext(), "Unable to make a call", Toast.LENGTH_SHORT).show();
                }

            }
        };

        mPhoneCallLayoutView.setOnClickListener(callListener);
        mPhoneCallView.setOnClickListener(callListener);

        mDonorProfileNameView.setText(donorName);
        mDonorNumberView.setText(donorNumber);

        View.OnClickListener messageListener = new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    SmsManager smsManager = SmsManager.getDefault();
                    smsManager.sendTextMessage(donorNumber, null, message, null, null);
                    Toast.makeText(getApplicationContext(), "SMS sent.", Toast.LENGTH_LONG).show();
                }

                catch (SecurityException e) {
                    Toast.makeText(getApplicationContext(), "SMS faild, please try again.", Toast.LENGTH_LONG).show();
                }
            }
        };

        mSendMessageView.setOnClickListener(messageListener);
    }
}
