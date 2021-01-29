package com.teamd.quixx.service;

import android.Manifest;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.IBinder;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.NotificationCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.content.PermissionChecker;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.firebase.database.DatabaseReference;
import com.teamd.quixx.MainActivity;
import com.teamd.quixx.R;
import com.teamd.quixx.activity.UpdateStatus;
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

public class LocationUpdateService extends Service {
    public static final String CHANNEL_ID = "ForegroundServiceChannel";
    private FusedLocationProviderClient mFusedLocationClient;
    private LocationCallback locationCallback;
    private LocationRequest locationRequest;
    private Api api = null;
    private String is_online="0";
    SharedPrefHelper sharedPrefHelper;
    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        api= RetrofitInstance.getRetrofitInstance().create(Api.class);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        is_online=sharedPrefHelper.getStringFromSharedPref(Constants.IS_ONLINE);
        if (Build.VERSION.SDK_INT >= 26) {
            createNotification(intent);
        }
        if(intent.getAction().equals(Constants.START_FORGROUND_ACTION)) {
            if(mFusedLocationClient==null) {
                requestLocationUpdate();
                Log.i("LocationUpadateService", "Recieved start foreground intent");
            }
        }
        else if(intent.getAction().equals(Constants.STOP_FORGROUND_ACTION)){
            if(mFusedLocationClient != null)
            {
                mFusedLocationClient.removeLocationUpdates(locationCallback);
            }
            stopForeground(true);
            stopSelf();
            Log.i("LocationUpadateService","Recieved stop foreground intent");
        }
        return START_NOT_STICKY;
    }

    private void createNotification(Intent intent) {
        String input = intent.getStringExtra(Constants.NOTIFICATION_TITLE);
        createNotificationChannel();
        Intent notificationIntent = new Intent(getApplicationContext(), MainActivity.class);

        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_CANCEL_CURRENT);

        Notification notification = new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("User location updating")
                .setContentText(input)
                .setSmallIcon(R.drawable.ic_android)
                .setContentIntent(pendingIntent)
                .build();

        startForeground(1, notification);
    }


    public void requestLocationUpdate() {

        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getApplicationContext());
        locationRequest = new LocationRequest();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setFastestInterval(Constants.FASTEST_INTERVAL_TIME);
        locationRequest.setInterval(Constants.INTERVAL_TIME);
        locationCallback=new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);

                String lat = ""+locationResult.getLastLocation().getLatitude();
                String longi = ""+locationResult.getLastLocation().getLongitude();

                CurrentLocation.CURR_LAT= lat;
                CurrentLocation.CURR_LONGI = longi;

                Commons.showToast(getApplicationContext(), "updating location lat : "+lat+" longi : "+longi);
                updateLocationToServer(Constants.DELIVERY_MAN_ID, lat, longi);
                Log.e("LocationUpdateService","updating");
                //Toast.makeText(getApplicationContext(), "locationUpdating", Toast.LENGTH_SHORT).show();

            }
        };

        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED
        &&ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_BACKGROUND_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            System.out.println("testing");
            return;
        }
        mFusedLocationClient.requestLocationUpdates(locationRequest, locationCallback , getMainLooper());
    }
    private void createNotificationChannel() {
        if(Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID, "Foreground Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );
            NotificationManager manager = getSystemService(NotificationManager.class);
            manager.createNotificationChannel(serviceChannel);
        }
    }

    public void updateLocationToServer(String deliveryManId, String lat, String longi){
        Call<DeliveryStatus> call=api.updateDeliveryManLocation(String.valueOf(deliveryManId), lat, longi);
        call.enqueue(new Callback<DeliveryStatus>() {
            @Override
            public void onResponse(Call<DeliveryStatus> call, Response<DeliveryStatus> response) {
                if(response.isSuccessful()){
                    Log.i("LocationUpadateService","location updated");
                }
            }

            @Override
            public void onFailure(Call<DeliveryStatus> call, Throwable t) {
                //Toast.makeText(getApplicationContext(),t.getMessage().toString(),Toast.LENGTH_LONG).show();
            }
        });
    }

}
