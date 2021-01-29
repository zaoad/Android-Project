package com.teamd.quixx.domain;

import java.io.Serializable;

public class HistoryInfo implements Serializable {


    private String sender_phone_number;
    private String delivery_created_date;
    private String sender_name;
    private String receiver_lat;
    private String delivery_note;
    private String receiver_phone_number;
    private String sender_lat;
    private String receiver_longi;
    private String sender_address;
    private String sender_longi;
    private String delivery_charge;
    private String delivery_type;
    private int creator_id;
    private String receiver_address;
    private String receiver_name;
    private String pickup_time;
    private String payment_method;
    private String delivery_status;
    private String collection_name;

    public String getProduct_qty() {
        return product_qty;
    }

    public void setDelivery_created_date(String delivery_created_date) {
        this.delivery_created_date = delivery_created_date;
    }

    public void setCreator_id(int creator_id) {
        this.creator_id = creator_id;
    }

    public void setProduct_qty(String product_qty) {
        this.product_qty = product_qty;
    }

    private String product_qty;

    public void setDelivery_complete_date(String delivery_complete_date) {
        this.delivery_complete_date = delivery_complete_date;
    }

    private String delivery_complete_date;
    private int delivery_Id;
    private String assign_delivery_man_name;
    private String assign_delivery_man_phone;
    private String product_cost;
    public void setProduct_cost(String product_cost) {
        this.product_cost = product_cost;
    }
    public String getProduct_cost() {
        return product_cost;
    }


    public int getDelivery_Id() {
        return delivery_Id;
    }

    public void setDelivery_Id(int delivery_Id) {
        this.delivery_Id = delivery_Id;
    }

    public String getSender_phone_number() {
        return sender_phone_number;
    }

    public void setSender_phone_number(String sender_phone_number) {
        this.sender_phone_number = sender_phone_number;
    }

    public String getSender_name() {
        return sender_name;
    }

    public void setSender_name(String sender_name) {
        this.sender_name = sender_name;
    }

    public String getReceiver_lat() {
        return receiver_lat;
    }

    public void setReceiver_lat(String receiver_lat) {
        this.receiver_lat = receiver_lat;
    }

    public String getDelivery_note() {
        return delivery_note;
    }

    public void setDelivery_note(String delivery_note) {
        this.delivery_note = delivery_note;
    }

    public String getReceiver_phone_number() {
        return receiver_phone_number;
    }

    public void setReceiver_phone_number(String receiver_phone_number) {
        this.receiver_phone_number = receiver_phone_number;
    }

    public String getSender_lat() {
        return sender_lat;
    }

    public void setSender_lat(String sender_lat) {
        this.sender_lat = sender_lat;
    }

    public String getReceiver_longi() {
        return receiver_longi;
    }

    public void setReceiver_longi(String receiver_longi) {
        this.receiver_longi = receiver_longi;
    }

    public String getSender_address() {
        return sender_address;
    }

    public String getDelivery_created_date() {
        return delivery_created_date;
    }

    public int getCreator_id() {
        return creator_id;
    }

    public String getDelivery_complete_date() {
        return delivery_complete_date;
    }

    public void setSender_address(String sender_address) {
        this.sender_address = sender_address;
    }

    public String getSender_longi() {
        return sender_longi;
    }

    public void setSender_longi(String sender_longi) {
        this.sender_longi = sender_longi;
    }

    public String getDelivery_charge() {
        return delivery_charge;
    }

    public void setDelivery_charge(String delivery_charge) {
        this.delivery_charge = delivery_charge;
    }

    public String getDelivery_type() {
        return delivery_type;
    }

    public void setDelivery_type(String delivery_type) {
        this.delivery_type = delivery_type;
    }

    public String getReceiver_address() {
        return receiver_address;
    }

    public void setReceiver_address(String receiver_address) {
        this.receiver_address = receiver_address;
    }

    public String getReceiver_name() {
        return receiver_name;
    }

    public void setReceiver_name(String receiver_name) {
        this.receiver_name = receiver_name;
    }

    public String getPickup_time() {
        return pickup_time;
    }

    public void setPickup_time(String pickup_time) {
        this.pickup_time = pickup_time;
    }

    public String getDelivery_status() {
        return delivery_status;
    }

    public void setDelivery_status(String delivery_status) {
        this.delivery_status = delivery_status;
    }

    public String getPayment_method() {
        return payment_method;
    }

    public void setPayment_method(String payment_method) {
        this.payment_method = payment_method;
    }

    public String getCollection_name() {
        return collection_name;
    }

    public void setCollection_name(String collection_name) {
        this.collection_name = collection_name;
    }

    public String getAssign_delivery_man_name() {
        return assign_delivery_man_name;
    }

    public void setAssign_delivery_man_name(String assign_delivery_man_name) {
        this.assign_delivery_man_name = assign_delivery_man_name;
    }

    public String getAssign_delivery_man_phone() {
        return assign_delivery_man_phone;
    }

    public void setAssign_delivery_man_phone(String assign_delivery_man_phone) {
        this.assign_delivery_man_phone = assign_delivery_man_phone;
    }


}
