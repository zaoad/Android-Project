package com.teamd.quixx.activity;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.teamd.quixx.R;
import com.teamd.quixx.domain.DeliveryManInfo;
import com.teamd.quixx.domain.HistoryInfo;
import com.teamd.quixx.utils.Commons;
import com.teamd.quixx.utils.Constants;

public class ShowDetailsHistory extends AppCompatActivity {
    private HistoryInfo historyInfo = null;
    private DeliveryManInfo deliveryManInfo = null;
    private TextView deliveryId, deliveryName, deliveryTime,
            pickupAddress, deliveryAddress,  deliveryType, deliveryNote,senderNumber, paymentMethod, recieverNumber,currentStatus,totalCost;
    private int totalDeliveryCost,quantity,totalCostWithDeliveryCharge,productCostAmout;
    Button callSenderButton,callReceiverButton;
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        historyInfo = (HistoryInfo) getIntent().getSerializableExtra(Constants.HISTORY_OBJECT);
        deliveryManInfo = (DeliveryManInfo) getIntent().getSerializableExtra(Constants.DELIVERY_MAN_OBJECT);
        setContentView(R.layout.details_history);
        deliveryId = findViewById(R.id.deliveryId);
        deliveryName = findViewById(R.id.deliveryName);
        deliveryTime = findViewById(R.id.pickupTimeDetails);
        pickupAddress = findViewById(R.id.pickupAddress);
        deliveryAddress = findViewById(R.id.deliveryAddress);
        deliveryType = findViewById(R.id.deliveryType);
        deliveryNote = findViewById(R.id.deliveryNote);
        paymentMethod=findViewById(R.id.paymentMethod);
        totalCost=findViewById(R.id.deliveryCostTotal);
        recieverNumber =findViewById(R.id.deliveryNumber);
        senderNumber=findViewById(R.id.senderNumber);
        callSenderButton=findViewById(R.id.callSenderButton);
        callReceiverButton=findViewById(R.id.callRecieverButton);
        currentStatus=findViewById(R.id.currentStatus);
        currentStatus.setVisibility(View.INVISIBLE);
        callSenderButton.setVisibility(View.INVISIBLE);
        callReceiverButton.setVisibility(View.INVISIBLE);
        if(historyInfo != null){
            try {
                deliveryId.setText(deliveryId.getText() + String.valueOf(historyInfo.getDelivery_Id()));

                deliveryName.setText(deliveryName.getText() + historyInfo.getReceiver_name());
                Log.e("History Info", "history delivey " + historyInfo.getDelivery_complete_date());
                deliveryTime.setText("Delivery Time: " + historyInfo.getDelivery_complete_date());
                deliveryType.setText(deliveryType.getText() + historyInfo.getDelivery_type());
                pickupAddress.setText(pickupAddress.getText() + historyInfo.getSender_address());
                deliveryAddress.setText(deliveryAddress.getText() + historyInfo.getReceiver_address());
                paymentMethod.setText(paymentMethod.getText() + historyInfo.getPayment_method());
                senderNumber.setText(senderNumber.getText() + historyInfo.getSender_phone_number());
                quantity=Integer.parseInt(historyInfo.getProduct_qty());
                productCostAmout=Integer.parseInt(historyInfo.getProduct_cost());
                totalDeliveryCost=quantity*productCostAmout;
                recieverNumber.setText(recieverNumber.getText() + historyInfo.getReceiver_phone_number());
                totalCostWithDeliveryCharge=Integer.parseInt(historyInfo.getDelivery_charge())+totalDeliveryCost;
                totalCost.setText(totalCost.getText()+String.valueOf(totalCostWithDeliveryCharge));
                if (historyInfo.getDelivery_note().length() > 3) {
                    deliveryNote.setText(deliveryNote.getText() + historyInfo.getDelivery_note());
                } else {
                    deliveryNote.setVisibility(View.INVISIBLE);
                }
            }
            catch (Exception e)
            {
                Toast.makeText(this,"delivery history error",Toast.LENGTH_SHORT).show();
                Log.e("ShowDetailsHistory",e.toString());
                finish();
            }

        }

    }
}
