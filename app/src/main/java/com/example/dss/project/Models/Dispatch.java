package com.example.dss.project.Models;

public class Dispatch {
    private String id;
    private String time;
    private String place;
    private String type;
    private long salary;

    public Dispatch() {
        this.id = "this.id";
        this.time = "this.time";
        this.place = "this.place";
        this.type = "this.type";
        this.salary = 1;
    }

    public Dispatch(String id, String time, String place, String type, long salary) {
        this.id = id;
        this.time = time;
        this.place = place;
        this.type = type;
        this.salary = salary;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public String getPlace() {
        return place;
    }

    public void setPlace(String place) {
        this.place = place;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public long getSalary() {
        return salary;
    }

    public void setSalary(long salary) {
        this.salary = salary;
    }
}
