package com.teamd.quixx.activity;

import android.Manifest;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;


import com.teamd.quixx.domain.Organization;
import com.teamd.quixx.domain.OrganizationInfo;
import com.teamd.quixx.service.LocationUpdateService;
import com.teamd.quixx.MainActivity;
import com.teamd.quixx.R;
import com.teamd.quixx.adapter.DeliveryAdapter;
import com.teamd.quixx.domain.SimpleApiResponse;
import com.teamd.quixx.domain.DeliveryInfo;
import com.teamd.quixx.domain.Delivery;
import com.teamd.quixx.domain.DeliveryManInfo;
import com.teamd.quixx.domain.DeliveryMan;
import com.teamd.quixx.retrofit.Api;
import com.teamd.quixx.retrofit.RetrofitInstance;
import com.teamd.quixx.utils.Commons;
import com.teamd.quixx.utils.Constants;
import com.teamd.quixx.utils.SharedPrefHelper;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class UserProfile extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    private String is_online="0";
    private Api api = null;
//    private TextView deliveryManName, deliveryManId, deliveryManReportingBoss;
    private TextView nav_deliveryManName, nav_deliveryManId, nav_deliveryManReportingBoss;
    private String deliveryManPhoneNumber = null;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private DeliveryAdapter deliveryAdapter;
    private DeliveryManInfo deliveryManInfo = null;
    private Toolbar toolbar;
    private DrawerLayout drawerLayout;
    private SharedPrefHelper sharedPrefHelper;
    private ToggleButton toggleButton;
    private SwipeRefreshLayout swipeContainer;
    NavigationView navigationView;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        sharedPrefHelper = new SharedPrefHelper(getApplicationContext());
        setContentView(R.layout.activity_user_profile);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        //for toolbar
        setSupportActionBar(toolbar);
        getSupportActionBar().setTitle("");
        //initialize drawer
        drawerLayout =findViewById(R.id.drawer_layout);
        navigationView=findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);
        ActionBarDrawerToggle toggle= new ActionBarDrawerToggle(this, drawerLayout, toolbar,
                R.string.navigation_drawer_open,R.string.navigation_drawer_close);
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();
        //create header view
        View headeview=navigationView.getHeaderView(0);
        nav_deliveryManName=(TextView) headeview.findViewById(R.id.nav_deliveryManName);
        nav_deliveryManId=(TextView) headeview.findViewById(R.id.nav_deliveryManId);
        nav_deliveryManReportingBoss=(TextView) headeview.findViewById(R.id.nav_deliveryManReportingBossEmail);
        /*--------*/
        Log.d("first " ,"navigation clear");
        deliveryManPhoneNumber = getIntent().getStringExtra(Constants.DELIVERY_MAN_PHONE_NUMBER);
        sharedPrefHelper.saveDataToSharedPref(Constants.LOG_IN_DELIVERY_NUMBER,deliveryManPhoneNumber);
        sharedPrefHelper.saveDataToSharedPref(Constants.IS_LOG_IN,"1");
        is_online=sharedPrefHelper.getStringFromSharedPref(Constants.IS_ONLINE);
//        if(is_online==null || is_online.equals("0") )
//        {
//            Intent intent = new Intent(getApplicationContext(), LocationUpdateService.class);
//            intent.putExtra(Constants.NOTIFICATION_TITLE, Constants.QUIXX);
//            intent.setAction(Constants.START_FORGROUND_ACTION);
//            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
//                UserProfile.this.startForegroundService(intent);
//            }else{
//                startService(intent);
//            }
//            sharedPrefHelper.saveDataToSharedPref(Constants.IS_ONLINE,"1");
//        }

        // = findViewById(R.id.deliveryManName);
        //deliveryManId = findViewById(R.id.deliveryManId);
        //deliveryManReportingBoss = findViewById(R.id.deliveryManReportingBossEmail);
        recyclerView = findViewById(R.id.recyclerViewItem);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        swipeContainer=findViewById(R.id.swipeContainer);
        swipeContainer.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                Toast.makeText(getBaseContext(),"reloading",Toast.LENGTH_SHORT).show();
                getDeliveries(String.valueOf(deliveryManInfo.getDelivery_man_id()));
                swipeContainer.setRefreshing(false);
            }
        });

        swipeContainer.setColorSchemeResources(android.R.color.holo_blue_bright,

                android.R.color.holo_green_light,

                android.R.color.holo_orange_light,

                android.R.color.holo_red_light);
        api= RetrofitInstance.getRetrofitInstance().create(Api.class);
        if(deliveryManPhoneNumber != null) {
            getUserId();
        }
        Menu navMenu=navigationView.getMenu();
        MenuItem item=navMenu.findItem(R.id.nav_on_duty);
        if(is_online==null || is_online.equals("0"))
        {
            Log.e("LocationUpdate","inactive");
            locationServiceStop();
            item.setChecked(false);
            item.setTitle("Active Status:  Off");
        }
        else
        {
            item.setChecked(true);
            item.setTitle("Active Status:  On");
            Log.e("LocationUpdate","active");
            locationServiceOn();
        }


    }
    //Drawer fragment click listener
    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        switch (menuItem.getItemId())
        {
            case R.id.nav_on_duty:
                if(menuItem.isChecked()) {
                    menuItem.setTitle("Active Status:  Off");
                    menuItem.setChecked(false);
                    locationServiceStop();
                    sharedPrefHelper.saveDataToSharedPref(Constants.IS_ONLINE,"0");
                    Toast.makeText(this, "Inactive", Toast.LENGTH_SHORT).show();

                }
                else {
                    sharedPrefHelper.saveDataToSharedPref(Constants.IS_ONLINE,"1");
                    menuItem.setTitle("Active Status:  On");
                    Commons.showToast(getApplicationContext(),"please turn on your GPS");
                    locationServiceOn();
                    menuItem.setChecked(true);
                    Toast.makeText(this,"Active",Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.nav_notification:
                Toast.makeText(this,"notification feature is not available now",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_history:
                //Toast.makeText(this,"click history",Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), ShowHisoty.class);
                intent.putExtra(Constants.DELIVERY_MAN_PHONE_NUMBER, deliveryManPhoneNumber);
                startActivity(intent);
                break;
            case R.id.nav_help:
                Toast.makeText(this,"help feature is not available now",Toast.LENGTH_SHORT).show();
                break;
            case R.id.nav_log_out:
                sharedPrefHelper.saveDataToSharedPref(Constants.IS_LOG_IN,"0");
                sharedPrefHelper.saveDataToSharedPref(Constants.IS_GET_ICON,"0");
                //Toast.makeText(this,"click log out",Toast.LENGTH_SHORT).show();
                Intent main_activity_intent = new Intent(getApplicationContext(), MainActivity.class);
                startActivity(main_activity_intent);
                finish();
                break;
        }
        drawerLayout.closeDrawer(GravityCompat.START);
        return true;
    }
    //navigation override method

    @Override
    public void onBackPressed() {
        if(drawerLayout.isDrawerOpen(GravityCompat.START))
        {
            drawerLayout.closeDrawer(GravityCompat.START);
        }
        else
        {
            super.onBackPressed();
        }
    }

    public void getUserId(){
        Call<SimpleApiResponse> call=api.getUserId(deliveryManPhoneNumber);
        call.enqueue(new Callback<SimpleApiResponse>() {

            @Override
            public void onResponse(Call<SimpleApiResponse> call, Response<SimpleApiResponse> response) {
                if(response.isSuccessful()){
                    SimpleApiResponse apiResponse = response.body();
                    Constants.DELIVERY_MAN_ID = apiResponse.getData();
                    if(Constants.DELIVERY_MAN_ID != ""){
                        chekPermission(Manifest.permission.ACCESS_FINE_LOCATION, Constants.PERMISSION_CODE_FINE_LOCATION);
                        chekPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Constants.PERMISSION_CODE_COARSE_LOCATION);
                        chekPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Constants.PERMISSION_BACKGROUND_LOCATION);

                    }
                    //deliveryManId.setText("ID : "+apiResponse.getData());
                    nav_deliveryManId.setText("ID : "+apiResponse.getData());
                    getUserInfo(apiResponse.getData());
                    getOrgInfo(apiResponse.getData());
                }
            }

            @Override
            public void onFailure(Call<SimpleApiResponse> call, Throwable t) {
                Commons.showToast(getApplicationContext(), t.getMessage());
            }
        });
    }

    public void getUserInfo(final String deliveryManId){
        Call<DeliveryMan> call=api.getUserDetailsById(deliveryManId);
        call.enqueue(new Callback<DeliveryMan>() {

            @Override
            public void onResponse(Call<DeliveryMan> call, Response<DeliveryMan> response) {
                if(response.isSuccessful()){
                    DeliveryMan deliveryMan = response.body();
                    if(deliveryMan != null) {
                        deliveryManInfo = deliveryMan.getData();
                        if(deliveryManInfo != null) {
                            //deliveryManName.setText("Name : " + deliveryManInfo.getName());
                            //deliveryManReportingBoss.setText("Reporting Boss : " + deliveryManInfo.getReporting_boss_email());
                            getDeliveries(String.valueOf(deliveryManInfo.getDelivery_man_id()));
                            nav_deliveryManName.setText("Name : " + deliveryManInfo.getName());
                            nav_deliveryManReportingBoss.setText("Reporting Boss : " + deliveryManInfo.getReporting_boss_email());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<DeliveryMan> call, Throwable t) {
                Commons.showToast(getApplicationContext(), t.getMessage());
            }
        });
    }

    public void getDeliveries(String deliveryManId){
        Call<Delivery> call=api.getAllDeliveryByDeliveryManId(deliveryManId);
        call.enqueue(new Callback<Delivery>() {
            @Override
            public void onResponse(Call<Delivery> call, Response<Delivery> response) {
                if(response.isSuccessful()){
                    Delivery delivery = response.body();
                    //Log.e("User Profle", String.valueOf(delivery.getData().get(0)));
                    List<DeliveryInfo> deliveries = delivery.getData();
                    deliveryAdapter = new DeliveryAdapter(getApplicationContext(),deliveries, deliveryManInfo);
                    recyclerView.setAdapter(deliveryAdapter);
                }
            }

            @Override
            public void onFailure(Call<Delivery> call, Throwable t) {
                Commons.showToast(getApplicationContext(), t.getMessage());
            }
        });
    }

    public void chekPermission(String permission, int reuestCode){

        if(ContextCompat.checkSelfPermission(this, permission) != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UserProfile.this, new String[]{permission}, reuestCode);

        }else{
            //Commons.showToast(getApplicationContext(), permission+ "already granted");
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode){
            case Constants.PERMISSION_CODE_FINE_LOCATION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    System.out.println("permission : fine location granted");
                }else{
                    System.out.println("permission : fine location not granted");
                    chekPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Constants.PERMISSION_CODE_FINE_LOCATION);
                }
            case Constants.PERMISSION_CODE_COARSE_LOCATION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    System.out.println("permission : fine coars granted");
                }else {
                    chekPermission(Manifest.permission.ACCESS_COARSE_LOCATION, Constants.PERMISSION_CODE_COARSE_LOCATION);
                }
            case Constants.PERMISSION_BACKGROUND_LOCATION:
                if(grantResults.length>0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
                    System.out.println("permission : fine background service granted");
                }else {
                    chekPermission(Manifest.permission.ACCESS_BACKGROUND_LOCATION, Constants.PERMISSION_BACKGROUND_LOCATION);
                }
            default:
                //do nothing

        }
    }

    @Override
    protected void onPause() {
        super.onPause();
    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Intent intent = getIntent();
        finish();
        startActivity(intent);
    }
    public void locationServiceOn()
    {
        Intent intent = new Intent(getApplicationContext(), LocationUpdateService.class);
        intent.putExtra(Constants.NOTIFICATION_TITLE, Constants.QUIXX);
        intent.setAction(Constants.START_FORGROUND_ACTION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            UserProfile.this.startForegroundService(intent);
        }else{
            startService(intent);
        }
    }
    public void locationServiceStop()
    {
        Intent intent = new Intent(getApplicationContext(), LocationUpdateService.class);
        intent.putExtra(Constants.NOTIFICATION_TITLE, Constants.QUIXX);
        intent.setAction(Constants.STOP_FORGROUND_ACTION);
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O){
            UserProfile.this.startForegroundService(intent);
        }else{
            startService(intent);
        }
    }
    public void getOrgInfo(String deliveryManId){
        Call<Organization> call=api.getOrganizationInfo(deliveryManId);
        call.enqueue(new Callback<Organization>() {

            @Override
            public void onResponse(Call<Organization> call, Response<Organization> response) {
                if(response.isSuccessful()){
                    Organization organization = response.body();

                    if(organization != null) {
                        OrganizationInfo organizationInfo = organization.getData();
                        if(organizationInfo != null) {
                            sharedPrefHelper.saveDataToSharedPref(Constants.IS_GET_ICON,"1");
                            String image_string=organizationInfo.getImage_str();
                            String[] main_image=image_string.split(",");
                            if(main_image.length>=2) {
                                sharedPrefHelper.saveDataToSharedPref(Constants.ICON_STRING, main_image[1]);
                            }
                            else
                            {
                                sharedPrefHelper.saveDataToSharedPref(Constants.ICON_STRING,"");
                            }
                            sharedPrefHelper.saveDataToSharedPref(Constants.ORG_NAME_STRING,organizationInfo.getOrg_name());
                            Log.e("UserProfile",main_image[1]+" \n"+organizationInfo.getOrg_name());
                        }
                    }
                }
            }
            @Override
            public void onFailure(Call<Organization> call, Throwable t) {
                Commons.showToast(getApplicationContext(), t.getMessage());
            }
        });
    }


}
