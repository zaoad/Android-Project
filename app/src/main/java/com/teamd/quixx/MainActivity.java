package com.teamd.quixx;

import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.teamd.quixx.Drawer.MessageFragment;
import com.teamd.quixx.activity.PhoneAuth;
import com.teamd.quixx.activity.UserProfile;
import com.teamd.quixx.domain.SimpleApiResponse;
import com.teamd.quixx.retrofit.Api;
import com.teamd.quixx.retrofit.RetrofitInstance;
import com.teamd.quixx.utils.Commons;
import com.teamd.quixx.utils.Constants;
import com.teamd.quixx.utils.Message;
import com.teamd.quixx.utils.SharedPrefHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MainActivity extends AppCompatActivity {
    private String is_log_in="0";
    private SharedPrefHelper sharedPrefHelper;
    Api api = null;
    private EditText phoneNumber;
    private Button okPhoneNumberButton;

    private String deliveryManPhoneNumber;
    private ProgressBar progressBar;
    private TextView waitText;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        is_log_in=sharedPrefHelper.getStringFromSharedPref(Constants.IS_LOG_IN);
        if(is_log_in==null)
        {
            is_log_in="0";
        }
        else if(is_log_in.equals("1"))
        {
            deliveryManPhoneNumber=sharedPrefHelper.getStringFromSharedPref(Constants.LOG_IN_DELIVERY_NUMBER);
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            intent.putExtra(Constants.DELIVERY_MAN_PHONE_NUMBER, deliveryManPhoneNumber);
            startActivity(intent);
            finish();
        }
        setContentView(R.layout.activity_main);
//        getSupportActionBar().hide();
        progressBar = findViewById(R.id.vCodeProgressBar);
        waitText=findViewById(R.id.waittext);
        api= RetrofitInstance.getRetrofitInstance().create(Api.class);

        phoneNumber = findViewById(R.id.phoneNumber);
        okPhoneNumberButton = findViewById(R.id.okPhoneNumber);

        /*
        //for zaoad
        Intent intent = new Intent(getApplicationContext(), PhoneAuth.class);
        intent.putExtra(Constants.DELIVERY_MAN_PHONE_NUMBER, "01687997516");
        startActivity(intent);
        */

        okPhoneNumberButton.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                progressBar.setVisibility(View.VISIBLE);
                waitText.setVisibility(View.VISIBLE);
                deliveryManPhoneNumber = phoneNumber.getText().toString();
                if(deliveryManPhoneNumber != null && deliveryManPhoneNumber != ""){
                    //okPhoneNumberButton.setBackground(getResources().getDrawable(R.drawable.button_round_yellow));
                    getVerificationCone();
                    //okPhoneNumberButton.setBackground(getResources().getDrawable(R.drawable.button_round));

                }
            }
        });
    }



    //this is for toolbar menu
//
//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        MenuInflater menuInflater=getMenuInflater();
//        menuInflater.inflate(R.menu.menu,menu);
//        return super.onCreateOptionsMenu(menu);
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId())
//        {
//            case R.id.logout:
//                Toast.makeText(this,"logout selected",Toast.LENGTH_SHORT);
//                return true;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private boolean isNetworkConnected() {
        ConnectivityManager cm = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        return cm.getActiveNetworkInfo() != null;
    }


    /*@Override
    protected void onStart(){
        super.onStart();
        if(FirebaseAuth.getInstance().getCurrentUser() != null){
            Intent intent = new Intent(getApplicationContext(), UserProfile.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
        }
    }*/


    //for zaoad
    public void getVerificationCone(){

        Call<SimpleApiResponse> call=api.getVerificationCode(deliveryManPhoneNumber);
        call.enqueue(new Callback<SimpleApiResponse>() {

            @Override
            public void onResponse(Call<SimpleApiResponse> call, Response<SimpleApiResponse> response) {
                if(response.isSuccessful()){
                    SimpleApiResponse simpleApiResponse = response.body();
                    if(simpleApiResponse.getData().equals("true")){
                        Intent intent = new Intent(getApplicationContext(), PhoneAuth.class);
                        intent.putExtra(Constants.DELIVERY_MAN_PHONE_NUMBER, deliveryManPhoneNumber);
                        startActivity(intent);
                        progressBar.setVisibility(View.INVISIBLE);
                        waitText.setVisibility(View.INVISIBLE);
                        Commons.showToast(getApplicationContext(), "A verification code has been sent to your phone number");
                        finish();
                    }else{
                        Commons.showToast(getApplicationContext(), "Phone number is not valid");
                        progressBar.setVisibility(View.INVISIBLE);
                        waitText.setVisibility(View.INVISIBLE);
                    }
                }
                else
                {
                    Commons.showToast(getApplicationContext(), "Phone number is not valid");
                    progressBar.setVisibility(View.INVISIBLE);
                    waitText.setVisibility(View.INVISIBLE);
                }
            }

            @Override
            public void onFailure(Call<SimpleApiResponse> call, Throwable t) {
                Commons.showToast(getApplicationContext(), t.getMessage());
                progressBar.setVisibility(View.INVISIBLE);
                waitText.setVisibility(View.INVISIBLE);

            }
        });

    }

}
