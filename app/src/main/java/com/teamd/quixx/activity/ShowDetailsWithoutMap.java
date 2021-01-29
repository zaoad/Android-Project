package com.teamd.quixx.activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.teamd.quixx.R;
import com.teamd.quixx.domain.DeliveryInfo;
import com.teamd.quixx.domain.DeliveryManInfo;
import com.teamd.quixx.domain.DeliveryStatus;
import com.teamd.quixx.retrofit.Api;
import com.teamd.quixx.retrofit.RetrofitInstance;
import com.teamd.quixx.utils.Commons;
import com.teamd.quixx.utils.Constants;
import com.teamd.quixx.utils.CurrentLocation;
import com.teamd.quixx.utils.SharedPrefHelper;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowDetailsWithoutMap extends AppCompatActivity {
    private Api api = null;
    private DeliveryInfo deliveryInfo = null;
    private TextView deliveryId, deliveryName, pickupTime,
            pickupAddress, deliveryAddress, currentStatus, deliveryType, deliveryNote, paymentMethod, senderNumber, deliveryNumber, totalCost;
    private Button deliveryStatusUpdateButton, deliveryStatusUpdateButton2,deliveredStatusUpdateButton,holdStatusButton,cancelStatusButton,returnStatusButton,callSenderButton,callReceiverButton;
    private LinearLayout forCustomerType;
    private RelativeLayout buttonLayout1, buttonLayout2;

    private DeliveryManInfo deliveryManInfo = null;
    private SharedPrefHelper sharedPrefHelper;
    private String userMailPassword = null;
    private UpdateStatus.SendEmailTask sendEmailTask;
    private String statusString;
    private int totalDeliveryCost,quantity,totalCostWithDeliveryCharge,productCostAmout;
    private Toolbar toolbar;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.delivery_details_without_map);
        setSupportActionBar(toolbar);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        userMailPassword = sharedPrefHelper.getStringFromSharedPref(Constants.ENTER_YOUR_MAIL_AND_PASSWORD);
        /*if(userMailPassword == null){
            Intent intent = new Intent(getApplicationContext(), UserMailAndPass.class);
            startActivity(intent);
        }*/
        //sendEmailTask = new SendEmailTask();





        deliveryInfo = (DeliveryInfo) getIntent().getSerializableExtra(Constants.DELIVERY_OBJECT);
        deliveryManInfo = (DeliveryManInfo) getIntent().getSerializableExtra(Constants.DELIVERY_MAN_OBJECT);

        api= RetrofitInstance.getRetrofitInstance().create(Api.class);
        deliveryId = findViewById(R.id.deliveryId);
        deliveryName = findViewById(R.id.deliveryName);
        pickupTime = findViewById(R.id.pickupTimeDetails);
        pickupAddress = findViewById(R.id.pickupAddress);
        deliveryAddress = findViewById(R.id.deliveryAddress);
        currentStatus = findViewById(R.id.currentStatus);
        deliveryType = findViewById(R.id.deliveryType);
        paymentMethod=findViewById(R.id.paymentMethod);
        senderNumber=findViewById(R.id.senderNumber);
        deliveryNumber=findViewById(R.id.deliveryNumber);
        totalCost=findViewById(R.id.deliveryCostTotal);
        deliveryStatusUpdateButton = findViewById(R.id.deliveryUpdateButton);
        deliveryStatusUpdateButton2 = findViewById(R.id.deliveryUpdateButton2);
        deliveredStatusUpdateButton = findViewById(R.id.deliveryUpdateButtonlayout2);
        holdStatusButton = findViewById(R.id.holdButtonlayout2);
        cancelStatusButton = findViewById(R.id.cancelButtonlayout2);
        returnStatusButton=findViewById(R.id.returnButtonlayout2);
        deliveryNote = findViewById(R.id.deliveryNote);
        forCustomerType = findViewById(R.id.forCustomerType);
        forCustomerType.setVisibility(View.INVISIBLE);
        buttonLayout1 =findViewById(R.id.button_layout1);
        buttonLayout2 = findViewById(R.id.button_layout2);
        callSenderButton=findViewById(R.id.callSenderButton);
        callReceiverButton=findViewById(R.id.callRecieverButton);
        deliveryStatusUpdateButton.setBackground(getResources().getDrawable(R.drawable.button_round));
        deliveryStatusUpdateButton.setClickable(true);
        deliveredStatusUpdateButton.setClickable(true);
        holdStatusButton.setClickable(true);
        cancelStatusButton.setClickable(true);
        returnStatusButton.setClickable(true);
        if(deliveryInfo != null){
            try {
                getDeliveryStatusForDeliveryMan();
                deliveryId.setText(deliveryId.getText() + String.valueOf(deliveryInfo.getDelivery_Id()));

                deliveryName.setText(deliveryName.getText() + deliveryInfo.getReceiver_name());
                pickupTime.setText(pickupTime.getText() + deliveryInfo.getPickup_time());
                deliveryType.setText(deliveryType.getText() + deliveryInfo.getDelivery_type());
                pickupAddress.setText(pickupAddress.getText() + deliveryInfo.getSender_address());
                deliveryAddress.setText(deliveryAddress.getText() + deliveryInfo.getReceiver_address());
                currentStatus.setText(deliveryInfo.getDelivery_status());
                paymentMethod.setText(paymentMethod.getText() + deliveryInfo.getPayment_method());
                deliveryNumber.setText(deliveryNumber.getText() + deliveryInfo.getReceiver_phone_number());
                quantity=Integer.parseInt(deliveryInfo.getProduct_qty());
                productCostAmout=Integer.parseInt(deliveryInfo.getProduct_cost());
                totalDeliveryCost=quantity*productCostAmout;
                senderNumber.setText(senderNumber.getText() + String.valueOf(deliveryInfo.getSender_phone_number()));
                totalCostWithDeliveryCharge=Integer.parseInt(deliveryInfo.getDelivery_charge())+totalDeliveryCost;
                totalCost.setText(totalCost.getText()+String.valueOf(totalCostWithDeliveryCharge));
                if(deliveryInfo.getDelivery_note()==null)
                {
                    Log.e("UpdateStatus","Delivery note is null");
                    deliveryNote.setVisibility(View.INVISIBLE);
                }
                else if (deliveryInfo.getDelivery_note().length() > 3) {
                    deliveryNote.setText(deliveryNote.getText() + deliveryInfo.getDelivery_note());
                } else {
                    deliveryNote.setVisibility(View.INVISIBLE);
                }
                statusString=deliveryInfo.getDelivery_status();

            }
            catch (Exception e){
                Toast.makeText(this,"delivery information is invalid",Toast.LENGTH_SHORT).show();
                Log.e("UpdateStatus",e.toString());
                finish();
            }
        }

        deliveryStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                deliveryStatusUpdateButton.setBackground(getResources().getDrawable(R.drawable.button_round_yellow));
                deliveryStatusUpdateButton.setClickable(false);
                updateDeliveryStatus(String.valueOf(deliveryInfo.getDelivery_Id()), deliveryStatusUpdateButton.getText().toString());

            }
        });
        deliveredStatusUpdateButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                deliveredStatusUpdateButton.setBackground(getResources().getDrawable(R.drawable.button_round_yellow));
                deliveredStatusUpdateButton.setClickable(false);
                holdStatusButton.setClickable(false);
                cancelStatusButton.setClickable(false);
                returnStatusButton.setClickable(false);
                updateDeliveryStatus(String.valueOf(deliveryInfo.getDelivery_Id()), deliveredStatusUpdateButton.getText().toString());
            }
        });
        holdStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holdStatusButton.setBackground(getResources().getDrawable(R.drawable.button_round_yellow));
                deliveredStatusUpdateButton.setClickable(false);
                holdStatusButton.setClickable(false);
                cancelStatusButton.setClickable(false);
                returnStatusButton.setClickable(false);
                updateDeliveryStatus(String.valueOf(deliveryInfo.getDelivery_Id()), holdStatusButton.getText().toString());
            }
        });
        cancelStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                cancelStatusButton.setBackground(getResources().getDrawable(R.drawable.button_round_yellow));
                deliveredStatusUpdateButton.setClickable(false);
                holdStatusButton.setClickable(false);
                cancelStatusButton.setClickable(false);
                returnStatusButton.setClickable(false);
                updateDeliveryStatus(String.valueOf(deliveryInfo.getDelivery_Id()), cancelStatusButton.getText().toString());
            }
        });
        returnStatusButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                returnStatusButton.setBackground(getResources().getDrawable(R.drawable.button_round_yellow));
                deliveredStatusUpdateButton.setClickable(false);
                holdStatusButton.setClickable(false);
                cancelStatusButton.setClickable(false);
                returnStatusButton.setClickable(false);
                updateDeliveryStatus(String.valueOf(deliveryInfo.getDelivery_Id()), returnStatusButton.getText().toString());
            }
        });
        callSenderButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + deliveryInfo.getSender_phone_number()));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid Sender Phone Number");
                    Log.e("UpdateStatus call",e.toString());
                }
            }
        });
        callReceiverButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    if(isPermissionGranted()) {
                        Intent callIntent = new Intent(Intent.ACTION_CALL);
                        callIntent.setData(Uri.parse("tel:" + deliveryInfo.getReceiver_phone_number()));
                        startActivity(callIntent);
                    }
                }
                catch (Exception e)
                {
                    Commons.showToast(getApplicationContext(),"Invalid Sender Phone Number");
                    Log.e("UpdateStatus call",e.toString());
                }
            }
        });
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater =getMenuInflater();
        inflater.inflate(R.menu.menu,menu);
        return super.onCreateOptionsMenu(menu);
    }

    public void getDeliveryStatusForDeliveryMan(){
        Call<DeliveryStatus> call=api.getStatusFroDeliveryMan(String.valueOf(deliveryInfo.getDelivery_Id()));
        call.enqueue(new Callback<DeliveryStatus>() {

            @Override
            public void onResponse(Call<DeliveryStatus> call, Response<DeliveryStatus> response) {
                if(response.isSuccessful()){
                    DeliveryStatus deliveryStatus = response.body();
                    String status = deliveryStatus.getData();
                    Log.e("UpdateStatus",status);
                    if(status.contains("@"))
                    {
                        String statusParts[]=status.split("@");
                        if(statusParts.length>=3)
                        {
                            buttonLayout1.setVisibility(View.INVISIBLE);
                            buttonLayout2.setVisibility(View.VISIBLE);
                            deliveredStatusUpdateButton.setText(statusParts[0]);
                            holdStatusButton.setText(statusParts[1]);
                            cancelStatusButton.setText(statusParts[2]);
                            returnStatusButton.setText(statusParts[3]);
                        }
                    }
                    else {
                        buttonLayout1.setVisibility(View.VISIBLE);
                        buttonLayout2.setVisibility(View.INVISIBLE);
                        String parts[] = status.split(",");
                        if (parts.length == 2) {
                            forCustomerType.setVisibility(View.VISIBLE);
                            deliveryStatusUpdateButton.setText(parts[0]);
                            deliveryStatusUpdateButton2.setText(parts[1]);
                        } else {
                            deliveryStatusUpdateButton.setText(parts[0]);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<DeliveryStatus> call, Throwable t) {
                Toast.makeText(ShowDetailsWithoutMap.this,t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

    public void updateDeliveryStatus(String deliveryId, final String status){
        Log.e("UpdateStatusCall",""+deliveryId+" "+status);
        Call<DeliveryStatus> call=api.updateDeliveryStatus(deliveryId, status);
        Log.e("UpdateStatus", String.valueOf(call.toString()));
        call.enqueue(new Callback<DeliveryStatus>() {
            @Override
            public void onResponse(Call<DeliveryStatus> call, Response<DeliveryStatus> response) {
                if(response.isSuccessful()){
                    Toast.makeText(ShowDetailsWithoutMap.this,"successfully updated",Toast.LENGTH_SHORT).show();
                    Log.e("UpdateStatusCall","delivery update");
                    sendMailAndFinishThisActivity(status);
                }
                else
                {
                    Toast.makeText(ShowDetailsWithoutMap.this,"unsuccessful response",Toast.LENGTH_SHORT).show();
                }
            }
            @Override
            public void onFailure(Call<DeliveryStatus> call, Throwable t) {
                Log.e("UpdateStatuscall","fail");
                Toast.makeText(ShowDetailsWithoutMap.this,t.getMessage().toString(),Toast.LENGTH_LONG).show();
                deliveredStatusUpdateButton.setClickable(true);
                deliveredStatusUpdateButton.setClickable(true);
                holdStatusButton.setClickable(true);
                cancelStatusButton.setClickable(true);
                returnStatusButton.setClickable(true);
            }
        });
    }



    public void sendMailAndFinishThisActivity(String status){
        //sendEmailTask.execute();
        sharedPrefHelper.saveDataToSharedPref(Constants.DELIVERY_STATUS_FOR_MAIL, status);
        //Toast.makeText(UpdateStatus.this,"finish",Toast.LENGTH_SHORT).show();
        finish();
    }


    public class SendEmailTask extends AsyncTask<Void, Void, Void> {

        private String ownEmail, ownPassword, toMail, subject, message, status;

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }

        @Override
        protected Void doInBackground(Void... params) {
            try {
                /*status = sharedPrefHelper.getStringFromSharedPref(Constants.DELIVERY_STATUS_FOR_MAIL);
                ownEmail = sharedPrefHelper.getStringFromSharedPref(Constants.DELIVERY_MAN_EMAIL);
                ownPassword = sharedPrefHelper.getStringFromSharedPref(Constants.DELIVERY_MAN_EMAIL_PASSWORD);
                toMail = deliveryManInfo.getReporting_boss_email();
                if(status.equals(Constants.DELIVERY_STATUS_DELIVERED)){
                    subject = "Delivery ID : "+String.valueOf(deliveryInfo.getDelivery_id())+"\\( "+" COMPLETE "+" \\)";
                }
                else if(status.equals(Constants.DELIVERY_STATUS_RETURNED)){
                    subject = "\\( "+" ALERT "+" \\)"+"Delivery ID : "+String.valueOf(deliveryInfo.getDelivery_id())+"\\( "+status+" \\)";
                }else{
                    subject = "Delivery ID : "+String.valueOf(deliveryInfo.getDelivery_id());
                }

                message = "Delivery man ID : "+String.valueOf(deliveryManInfo.getDelivery_man_id())+"\r\n"+
                        "Delivery ID : "+String.valueOf(deliveryInfo.getDelivery_id())+"\r\n"+
                        "Current Status : "+status;
                MailService mailService = new MailService(ownEmail, ownPassword);
                mailService.sendMail(subject, message, ownEmail, toMail);*/
            } catch (Exception e) {
                //Commons.showToast(getApplicationContext(), "Can not send the mail");
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Void result) {
            super.onPostExecute(result);
            Commons.showToast(getApplicationContext(), "Email sent");
        }


    }
    public  boolean isPermissionGranted() {
        if (Build.VERSION.SDK_INT >= 23) {
            if (checkSelfPermission(android.Manifest.permission.CALL_PHONE)
                    == PackageManager.PERMISSION_GRANTED) {
                Log.v("TAG","Permission is granted");
                return true;
            } else {

                Log.v("TAG","Permission is revoked");
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.CALL_PHONE}, 1);
                return false;
            }
        }
        else { //permission is automatically granted on sdk<23 upon installation
            Log.v("TAG","Permission is granted");
            return true;
        }
    }


    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {

            case 1: {

                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(getApplicationContext(), "Permission granted", Toast.LENGTH_SHORT).show();
                    //call_action();
                } else {
                    Toast.makeText(getApplicationContext(), "Permission denied", Toast.LENGTH_SHORT).show();
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }
    }


}
