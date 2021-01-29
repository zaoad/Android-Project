package com.teamd.quixx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tagmanager.Container;
import com.teamd.quixx.R;
import com.teamd.quixx.activity.ShowDetailsHistory;
import com.teamd.quixx.activity.UpdateStatus;
import com.teamd.quixx.domain.DeliveryInfo;
import com.teamd.quixx.domain.DeliveryMan;
import com.teamd.quixx.domain.DeliveryManInfo;
import com.teamd.quixx.domain.HistoryInfo;
import com.teamd.quixx.utils.Constants;

import java.util.List;

public class HistoryAdapter  extends RecyclerView.Adapter<HistoryAdapter.ItemViewHolder>{

    private Context context;
    private List<HistoryInfo> historyInfoList;
    private DeliveryManInfo deliveryManInfo = null;

    public HistoryAdapter(Context context , List<HistoryInfo> historyInfoList, DeliveryManInfo deliveryManInfo) {
        this.context=context;
        this.historyInfoList=historyInfoList;
        this.deliveryManInfo = deliveryManInfo;
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.from(parent.getContext()).inflate(R.layout.history_card,parent,false);
        ItemViewHolder itemViewHolder=new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final HistoryInfo historyInfo = historyInfoList.get(position);
        try {
            holder.deliveryId.setText("Delivery Id: " + historyInfo.getDelivery_Id());
            holder.deliveryTime.setText("Delivered time : " + historyInfo.getDelivery_complete_date());
            holder.delivery.setText("Delivery : " + historyInfo.getReceiver_address());
        }
        catch (Exception e)
        {
            holder.deliveryId.setText("Delivery Id: ");
            holder.deliveryTime.setText("Delivered time : " );
            holder.delivery.setText("Delivery : " );
            Toast.makeText(context, "some delivery is not valid",Toast.LENGTH_SHORT).show();
            Log.e("HistoryAdapter",e.toString());
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ShowDetailsHistory.class);
                intent.putExtra(Constants.HISTORY_OBJECT, historyInfo);
                intent.putExtra(Constants.DELIVERY_MAN_OBJECT, deliveryManInfo);
                view.getContext().startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        if(historyInfoList!=null)
            return historyInfoList.size();
        else
            return 0;
    }

    public  static  class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView card;
        private TextView deliveryId,deliveryTime, delivery;
        public ItemViewHolder(View itemView) {
            super(itemView);
            deliveryId=itemView.findViewById(R.id.deliveryId);
            card = itemView.findViewById(R.id.cvItem);
            deliveryTime = itemView.findViewById(R.id.deliveryTime);
            delivery = itemView.findViewById(R.id.delivery);

        }
    }
}