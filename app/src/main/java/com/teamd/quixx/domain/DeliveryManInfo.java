package com.teamd.quixx.domain;

import java.io.Serializable;

public class DeliveryManInfo  implements Serializable {

    private int delivery_man_id;
    private String name;
    private String phone_number;
    private String email;
    private String reporting_boss_email;

    public DeliveryManInfo() {
    }

    public int getDelivery_man_id() {
        return delivery_man_id;
    }

    public void setDelivery_man_id(int delivery_man_id) {
        this.delivery_man_id = delivery_man_id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getPhone_number() {
        return phone_number;
    }

    public void setPhone_number(String phone_number) {
        this.phone_number = phone_number;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getReporting_boss_email() {
        return reporting_boss_email;
    }

    public void setReporting_boss_email(String reporting_boss_email) {
        this.reporting_boss_email = reporting_boss_email;
    }
}
