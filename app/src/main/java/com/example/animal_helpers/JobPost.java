package com.example.animal_helpers;

import java.util.HashMap;
import java.util.Map;

public class JobPost {

    private String Uid;     // firebase Uid (고유 토큰정보)
    private String store;
    private String title;
    private String date;
    private int employees;
    private String location;
    private String condition;
    private String work;
    private String address;
    private String body;
    private int time;

    public JobPost() {
    }

    public JobPost(String Uid, String store, String title, String date, int employees, String location, String condition, String work, String address, String body, int time, int status) {
        this.Uid = Uid;
        this.store = store;
        this.title = title;
        this.date = date;
        this.employees = employees;
        this.location = location;
        this.condition = condition;
        this.work = work;
        this.address = address;
        this.body = body;
        this.time = time;
        this.status = status;
    }


    private int status;

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

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
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

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public int getTime() {
        return time;
    }

    public void setTime(int time) {
        this.time = time;
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", Uid);
        result.put("store", store);
        result.put("title", title);
        result.put("date", date);
        result.put("employees", employees);
        result.put("location", location);
        result.put("condition", condition);
        result.put("work", work);
        result.put("address", address);
        result.put("body", body);
        result.put("time", time);
        result.put("status", status);
        return result;
    }
}
