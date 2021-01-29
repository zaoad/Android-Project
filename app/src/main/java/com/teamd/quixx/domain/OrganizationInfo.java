package com.teamd.quixx.domain;

public class OrganizationInfo {
    private String user_id;
    private String image_str;
    private String org_name;

    public OrganizationInfo(String user_id, String image_str, String org_name) {
        this.user_id = user_id;
        this.image_str = image_str;
        this.org_name = org_name;
    }

    public String getUser_id() {
        return user_id;
    }

    public String getImage_str() {
        return image_str;
    }

    public String getOrg_name() {
        return org_name;
    }

    public void setUser_id(String user_id) {
        this.user_id = user_id;
    }

    public void setImage_str(String image_str) {
        this.image_str = image_str;
    }

    public void setOrg_name(String org_name) {
        this.org_name = org_name;
    }
}
