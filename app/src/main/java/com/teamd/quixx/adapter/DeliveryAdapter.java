package com.teamd.quixx.adapter;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.CardView;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.teamd.quixx.R;
import com.teamd.quixx.activity.ShowDetailsWithoutMap;
import com.teamd.quixx.activity.UpdateStatus;
import com.teamd.quixx.domain.DeliveryInfo;
import com.teamd.quixx.domain.DeliveryManInfo;
import com.teamd.quixx.utils.Constants;
import com.teamd.quixx.utils.SharedPrefHelper;

import java.util.List;

public class DeliveryAdapter  extends RecyclerView.Adapter<DeliveryAdapter.ItemViewHolder>{
    private String is_online;
    private Context context;
    private List<DeliveryInfo> deliveryInfoList;
    private DeliveryManInfo deliveryManInfo = null;
    private SharedPrefHelper sharedPrefHelper;

    public DeliveryAdapter(Context context , List<DeliveryInfo> deliveryInfoList, DeliveryManInfo deliveryManInfo) {
        this.context=context;
        this.deliveryInfoList=deliveryInfoList;
        this.deliveryManInfo = deliveryManInfo;
        sharedPrefHelper = new SharedPrefHelper(context);
    }
    @Override
    public ItemViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        LayoutInflater inflater=LayoutInflater.from(parent.getContext());
        View view= inflater.from(parent.getContext()).inflate(R.layout.delivery_card,parent,false);
        ItemViewHolder itemViewHolder=new ItemViewHolder(view);
        return itemViewHolder;
    }

    @Override
    public void onBindViewHolder(final ItemViewHolder holder, int position) {
        final DeliveryInfo deliveryInfo = deliveryInfoList.get(position);
        if(deliveryInfo.getDelivery_status().equals(Constants.JUST_CREATED))
        {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.cardviewcreated));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.card_view_just_created));
        }
        else if(deliveryInfo.getDelivery_status().equals(Constants.ENROUTE_TO_PICKUP))
        {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.cardviewenroutepickup));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.card_view_enroute_to_picked_up));
        }
        else if(deliveryInfo.getDelivery_status().equals(Constants.PICKED_UP))
        {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.cardviewpickedup));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.card_view_picked_up));
        }
        else if(deliveryInfo.getDelivery_status().equals(Constants.ENROUTE_TO_DELIVERY))
        {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.cardviewenroutedelivey));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.card_view_enroute_to_delivey));
        }
        else
        {
            holder.card.setCardBackgroundColor(context.getResources().getColor(R.color.cardviewcolor));
            holder.relativeLayout.setBackground(context.getResources().getDrawable(R.drawable.cardviewbg));
        }
        try {
            holder.pickupTime.setText("Pickup : " + deliveryInfo.getPickup_time());
            holder.delivery.setText("Delivery : " + deliveryInfo.getReceiver_address());
            holder.status.setText(deliveryInfo.getDelivery_status());
            holder.id.setText("id : "+deliveryInfo.getDelivery_Id());
        }
        catch (Exception e)
        {
            holder.pickupTime.setText("Pickup : " );
            holder.delivery.setText("Delivery : " );
            holder.status.setText("");
            holder.id.setText("id : ");
            Toast.makeText(context, "some delivery is not valid",Toast.LENGTH_SHORT).show();
            Log.e("DeliveryAdapter",e.toString());
        }
        holder.card.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                is_online=sharedPrefHelper.getStringFromSharedPref(Constants.IS_ONLINE);
                if(is_online==null || is_online.equals("0"))
                {
                    Toast.makeText(context,"You're offline. Please, Turn on active status",Toast.LENGTH_SHORT).show();
                }
                else if(is_online.equals("1"))
                {
                    if(deliveryInfo.getSender_lat()==null||deliveryInfo.getSender_longi()==null||deliveryInfo.getReceiver_lat()==null||deliveryInfo.getReceiver_longi()==null)
                    {
                        try {
                            Intent intent = new Intent(view.getContext(), ShowDetailsWithoutMap.class);
                            intent.putExtra(Constants.DELIVERY_OBJECT, deliveryInfo);
                            intent.putExtra(Constants.DELIVERY_MAN_OBJECT, deliveryManInfo);
                            //Log.e("Delivery Addres:" , "gece");
                            view.getContext().startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context,"Delivery error",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else if(deliveryInfo.getSender_lat().equals("")||deliveryInfo.getSender_longi().equals("")||deliveryInfo.getReceiver_lat().equals("")||deliveryInfo.getReceiver_longi().equals(""))
                    {
                        try {
                            Intent intent = new Intent(view.getContext(), ShowDetailsWithoutMap.class);
                            intent.putExtra(Constants.DELIVERY_OBJECT, deliveryInfo);
                            intent.putExtra(Constants.DELIVERY_MAN_OBJECT, deliveryManInfo);
                            //Log.e("Delivery Addres:" , "gece");
                            view.getContext().startActivity(intent);
                        }
                        catch (Exception e)
                        {
                            Toast.makeText(context,"Delivery error",Toast.LENGTH_SHORT).show();
                        }
                    }
                    else {
                        try {
                            Intent intent = new Intent(view.getContext(), UpdateStatus.class);
                            intent.putExtra(Constants.DELIVERY_OBJECT, deliveryInfo);
                            intent.putExtra(Constants.DELIVERY_MAN_OBJECT, deliveryManInfo);
                            //Log.e("Delivery Addres:" , "gece");
                            view.getContext().startActivity(intent);
                        } catch (Exception e) {
                            Toast.makeText(context, "Delivery error", Toast.LENGTH_SHORT).show();
                        }
                    }
                }


            }
        });
    }

    @Override
    public int getItemCount() {
        if(deliveryInfoList!=null)
            return deliveryInfoList.size();
        else
            return 0;
    }

    public  static  class ItemViewHolder extends RecyclerView.ViewHolder {
        private CardView card;
        private TextView pickupTime, delivery, status,id;
        private RelativeLayout relativeLayout;
        public ItemViewHolder(View itemView) {
            super(itemView);
            card = itemView.findViewById(R.id.cvItem);
            pickupTime = itemView.findViewById(R.id.pickUpTime);
            delivery = itemView.findViewById(R.id.delivery);
            status = itemView.findViewById(R.id.deliveryStatus);
            relativeLayout=itemView.findViewById(R.id.cardlayout);
            id=itemView.findViewById(R.id.id);
        }
    }
    public void clear() {

        deliveryInfoList.clear();

        notifyDataSetChanged();

    }



// Add a list of items -- change to type used

    public void addAll(List<DeliveryInfo> list) {

        deliveryInfoList.addAll(list);

        notifyDataSetChanged();

    }
}
