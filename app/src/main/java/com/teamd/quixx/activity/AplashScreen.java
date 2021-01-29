package com.teamd.quixx.activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.teamd.quixx.MainActivity;
import com.teamd.quixx.R;
import com.teamd.quixx.utils.Commons;
import com.teamd.quixx.utils.Constants;
import com.teamd.quixx.utils.SharedPrefHelper;

import java.util.Collections;

public class AplashScreen extends AppCompatActivity {
    private String is_log_in="0",is_get_icon="0",icon_string="0",org_name_string="0";
    private SharedPrefHelper sharedPrefHelper;
    private ImageView orgIcon;
    private TextView orgName, orgNameCenter;
    private String deliveryManPhoneNumber = null;
    private static int SPLASH_TIME = 1500;//?
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        is_log_in=sharedPrefHelper.getStringFromSharedPref(Constants.IS_LOG_IN);
        is_get_icon=sharedPrefHelper.getStringFromSharedPref(Constants.IS_GET_ICON);
        icon_string=sharedPrefHelper.getStringFromSharedPref(Constants.ICON_STRING);
        org_name_string=sharedPrefHelper.getStringFromSharedPref(Constants.ORG_NAME_STRING);
        //is_log_in="1";
        deliveryManPhoneNumber=sharedPrefHelper.getStringFromSharedPref(Constants.LOG_IN_DELIVERY_NUMBER);
        Log.d("check value log in","value: "+is_log_in);
        setContentView(R.layout.activity_aplash_screen);
        orgIcon=findViewById(R.id.orglogo);
        orgName=findViewById(R.id.orgName);
        orgNameCenter=findViewById(R.id.orgNameCenter);
        if(is_get_icon==null || is_get_icon.equals("0"))
        {
            orgName.setVisibility(View.INVISIBLE);
            orgIcon.setVisibility(View.INVISIBLE);
            orgNameCenter.setVisibility(View.VISIBLE);
        }
        else
        {
            orgName.setVisibility(View.VISIBLE);
            orgNameCenter.setVisibility(View.INVISIBLE);
            byte[] imageData = Base64.decode(icon_string, Base64.DEFAULT);
            Bitmap bitmap= BitmapFactory.decodeByteArray(imageData,0,imageData.length);
            orgIcon.setImageBitmap(bitmap);
            orgIcon.setVisibility(View.VISIBLE);
            orgName.setText(org_name_string);
        }
        if(is_log_in==null || is_log_in.equals("0"))
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent mySuperIntent = new Intent(getApplicationContext(), MainActivity.class);
                    startActivity(mySuperIntent);
                    finish();
                }
            }, SPLASH_TIME);
        }
        else
        {
            new Handler().postDelayed(new Runnable() {
                @Override
                public void run() {
                    Intent intent = new Intent(getApplicationContext(), UserProfile.class);
                    intent.putExtra(Constants.DELIVERY_MAN_PHONE_NUMBER, deliveryManPhoneNumber);
                    startActivity(intent);
                    finish();
                }
            }, SPLASH_TIME);

        }

    }
}
