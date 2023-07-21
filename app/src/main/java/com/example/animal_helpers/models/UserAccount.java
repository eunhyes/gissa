package com.example.animal_helpers.models;


public class UserAccount {

    private String Uid;
    private String email;     // 이메일
    private String name;

    private String nickname;
    private String address;
    private String tel;

    public UserAccount(String uid, String email, String name, String nickname, String address, String tel) {
        Uid = uid;
        this.email = email;
        this.name = name;
        this.nickname = nickname;
        this.address = address;
        this.tel = tel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserAccount() {
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getUid() {return Uid;}

    public void setUid(String uid) {Uid = uid;}
}
