package com.teamd.quixx.activity;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;

import com.teamd.quixx.R;
import com.teamd.quixx.adapter.DeliveryAdapter;
import com.teamd.quixx.adapter.HistoryAdapter;
import com.teamd.quixx.domain.Delivery;
import com.teamd.quixx.domain.DeliveryInfo;
import com.teamd.quixx.domain.DeliveryManInfo;
import com.teamd.quixx.domain.History;
import com.teamd.quixx.domain.HistoryInfo;
import com.teamd.quixx.retrofit.Api;
import com.teamd.quixx.retrofit.RetrofitInstance;
import com.teamd.quixx.utils.Commons;
import com.teamd.quixx.utils.Constants;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ShowHisoty extends AppCompatActivity {
    private Api api = null;
    private String deliveryManPhoneNumber = null;
    private RecyclerView recyclerView;
    private LinearLayoutManager manager;
    private HistoryAdapter historyAdapter;
    private DeliveryManInfo deliveryManInfo = null;
    protected void onCreate(Bundle savedInstanceState){

        super.onCreate(savedInstanceState);
        setContentView(R.layout.show_history);

        deliveryManPhoneNumber = getIntent().getStringExtra(Constants.DELIVERY_MAN_PHONE_NUMBER);

        recyclerView = findViewById(R.id.recyclerViewItemHistory);
        manager = new LinearLayoutManager(this);
        recyclerView.setLayoutManager(manager);
        api= RetrofitInstance.getRetrofitInstance().create(Api.class);

        getHistories(Constants.DELIVERY_MAN_ID);

    }
    public void getHistories(String deliveryManId){
        Call<History> call=api.getAllHistoryByDeliveryManId(deliveryManId);
        call.enqueue(new Callback<History>() {
            @Override
            public void onResponse(Call<History> call, Response<History> response) {
                if(response.isSuccessful()){
                    History history= response.body();
                    List<HistoryInfo> histories = history.getData();
                    historyAdapter = new HistoryAdapter(getApplicationContext(),histories, deliveryManInfo);
                    recyclerView.setAdapter(historyAdapter);
                }
            }

            @Override
            public void onFailure(Call<History> call, Throwable t) {
                Commons.showToast(getApplicationContext(), t.getMessage());
            }
        });
    }
}
