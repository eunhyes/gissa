package com.example.animal_helpers.models;

import java.util.HashMap;
import java.util.Map;

public class JobPost extends HashMap<String, String> {

    private String Uid;     // firebase Uid (고유 토큰정보)
    private String activityPlace; // x
    private String title;
    private String writingDate;
    private String address;
    private String condition;
    private String work; // x
    private String body;
    private String tel;
    private String status; // x
    private String startDate;
    private String endDate;
    private String startTime;
    private String endTime;
    private boolean favorite;



    private String employees;

    public JobPost() {}

    public JobPost(String uid, String activityPlace, String title, String writingDate, String address, String condition, String work, String body, String tel, String status, String startDate, String endDate, String startTime, String endTime, String employees, boolean Favorite) {
        this.Uid = uid;
        this.activityPlace = activityPlace;
        this.title = title;
        this.writingDate = writingDate;
        this.address = address;
        this.condition = condition;
        this.work = work;
        this.body = body;
        this.tel = tel;
        this.status = status;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employees = employees;
        this.favorite = Favorite;
    }
    public JobPost(String uid, String body, String title, String address, String condition, String writingDate, String startDate, String endDate, String startTime, String endTime, String employees){
        this.Uid = uid;
        this.body = body;
        this.title = title;
        this.address = address;
        this.condition = condition;
        this.writingDate = writingDate;
        this.startDate = startDate;
        this.endDate = endDate;
        this.startTime = startTime;
        this.endTime = endTime;
        this.employees = employees;
    }

    public JobPost(String Uid, String title, String address, String writingDate, boolean favorite){
        this.Uid = Uid;
        this.title = title;
        this.address = address;
        this.writingDate = writingDate;
        this.favorite = favorite;
    }

    public boolean isFavorite() {
        return favorite;
    }

    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }


    //    public boolean GetFavorite() { return Favorite; }
//
//    public void setFavorite(boolean favorite) { Favorite = favorite; }

    public String getUid() {
        return Uid;
    }

    public void setUid(String uid) {
        Uid = uid;
    }

    public String getActivityPlace() {
        return activityPlace;
    }

    public void setActivityPlace(String activityPlace) {
        this.activityPlace = activityPlace;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getWritingDate() {
        return writingDate;
    }

    public void setWritingDate(String writingDate) {
        this.writingDate = writingDate;
    }

    public String getEmployees() { return employees; }

    public void setEmployees(String employees) {
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

    public String getStartDate() {
        return startDate;
    }

    public void setStartDate(String startDate) {
        this.startDate = startDate;
    }

    public String getEndDate() {
        return endDate;
    }

    public void setEndDate(String endDate) {
        this.endDate = endDate;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    public Map<String, Object> toMap() {
        HashMap<String, Object> result = new HashMap<>();
        result.put("uid", Uid);
        result.put("body", body);
        result.put("title", title);
        result.put("address", address);
        result.put("condition", condition);
        result.put("writingDate", writingDate);
        result.put("startDate", startDate);
        result.put("endDate", endDate);
        result.put("startTime", startTime);
        result.put("endTime", endTime);
        result.put("employees", employees);
//        result.put("activityPlace", activityPlace);
//        result.put("work", work);
//        result.put("tel", tel);
//        result.put("status", status);
//        result.put("Favorite", Favorite);
        return result;
    }
}
