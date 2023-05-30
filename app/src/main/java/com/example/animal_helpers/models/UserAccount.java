package com.example.animal_helpers.models;


public class UserAccount {

    private String Uid;
    private String email;     // 이메일
    private String password;
    private String name;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public UserAccount() {
    }

    public UserAccount(String uid, String email, String password, String name) {
        this.Uid = uid;
        this.email = email;
        this.password = password;
        this.name = name;
    }

    public String getEmail() {return email;}

    public void setEmail(String email) {this.email = email;}

    public String getPassword() {return password;}

    public void setPassword(String password) {this.password = password;}

    public String getUid() {return Uid;}

    public void setUid(String uid) {Uid = uid;}
}
