package com.example.dss.project.Models;

public class Status {
    private String id;
    private String name;
    private String time;
    private int progress;
    private double lat;
    private double log;
    private String type;
    private long salary;
    private String shipper;
    private String customer;

    public Status() {
        this.id = "id";
        this.name = "name";
        this.time = "time";
        this.lat = 20.0;
        this.log = 105.0;
        this.type = "type";
        this.salary = 1;
        this.shipper = "shipper";
        this.customer = "customer";
    }

    public Status(String name, String time, int progress) {
        this.id = "id";
        this.name = name;
        this.time = time;
        this.progress = progress;
        this.lat = 20.0;
        this.log = 105.0;
        this.type = "type";
        this.salary = 1;
        this.shipper = "shipper";
        this.customer = "customer";
    }

    public Status(String id, String name, String time, int progress, double lat, double log, String type, long salary, String shipper, String customer) {
        this.id = id;
        this.name = name;
        this.time = time;
        this.progress = progress;
        this.lat = lat;
        this.log = log;
        this.type = type;
        this.salary = salary;
        this.shipper = shipper;
        this.customer = customer;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public int getProgress() {
        return progress;
    }

    public void setProgress(int progress) {
        this.progress = progress;
    }

    public double getLat() {
        return lat;
    }

    public void setLat(double lat) {
        this.lat = lat;
    }

    public double getLog() {
        return log;
    }

    public void setLog(double log) {
        this.log = log;
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

    public String getShipper() {
        return shipper;
    }

    public void setShipper(String shipper) {
        this.shipper = shipper;
    }

    public String getCustomer() {
        return customer;
    }

    public void setCustomer(String customer) {
        this.customer = customer;
    }
}
