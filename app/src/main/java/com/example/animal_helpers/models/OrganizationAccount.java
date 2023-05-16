package com.example.animal_helpers.models;


public class OrganizationAccount {

    private String Uid;     // firebase Uid (고유 토큰정보)
    private String organizationName;
    private String address;
    private String tel;

    public OrganizationAccount(String uid, String organizationName, String address, String tel) {
        this.Uid = uid;
        this.organizationName = organizationName;
        this.address = address;
        this.tel = tel;
    }

    public OrganizationAccount() {}

    public String getUid() {return Uid;}

    public void setUid(String uid) {Uid = uid;}

    public String getOrganizationName() {return organizationName;}

    public void setOrganizationName(String organizationName) {this.organizationName = organizationName;}

    public String getAddress() {return address;}

    public void setAddress(String address) {this.address = address;}

    public String getTel() {return tel;}

    public void setTel(String tel) {this.tel = tel;}


}
