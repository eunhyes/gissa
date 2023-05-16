package com.example.animal_helpers.models;

import java.util.HashMap;
import java.util.Map;

public class JobPost {

    private String Uid;     // firebase Uid (고유 토큰정보)
    private String store;
    private String title;
    private String date;
    private String address;
    private String condition;
    private String work;
    private String body;
    private String tel;
    private String status;
    private String time;
    private int employees;

    public JobPost() {}



    public JobPost(String Uid, String store, String title, String date, int employees, String address, String condition, String work, String body, String tel, String time, String status) {
        this.Uid = Uid;
        this.store = store;
        this.title = title;
        this.date = date;
        this.employees = employees;
        this.address = address;
        this.condition = condition;
        this.work = work;
        this.body = body;
        this.tel = tel;
        this.time = time;
        this.status = status;
    }



    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getStore() {
        return store;
    }

    public void setStore(String store) {
        this.store = store;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public int getEmployees() {
        return employees;
    }

    public void setEmployees(int employees) {
        this.employees = employees;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getCondition() {
        return condition;
    }

    public void setCondition(String condition) {
        this.condition = condition;
    }

    public String getWork() {
        return work;
    }

    public void setWork(String work) {
        this.work = work;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public String getTime() { return time; }

    public void setTime(String time) { this.time = time; }

    public String getTel() {
        return tel;
    }

    public void setTel(String tel) {
        this.tel = tel;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", Uid);
        result.put("store", store);
        result.put("title", title);
        result.put("date", date);
        result.put("employees", employees);
        result.put("address", address);
        result.put("condition", condition);
        result.put("work", work);
        result.put("body", body);
        result.put("tel", tel);
        result.put("time", time);
        result.put("status", status);
        return result;
    }
}
