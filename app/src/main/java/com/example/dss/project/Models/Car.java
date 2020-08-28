package com.example.dss.project.Models;

import java.util.Date;

public class Car {
    private String signNumber;
    private Date date;
    private double latitude;
    private double longitude;
    private String status;
    private String business;
    private float direction;
    private double speed;
    private double kmPerDay;
    private double fuel;
    private double maxFuel;


    public Car(String signNumber, Date date, double latitude, double longitude, String status, String business, float direction, double speed, double kmPerDay, double fuel, double maxFuel) {
        this.signNumber = signNumber;
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = status;
        this.business = business;
        this.direction = direction;
        this.speed = speed;
        this.kmPerDay = kmPerDay;
        this.fuel = fuel;
        this.maxFuel = maxFuel;
    }

    public Car() {
        this.signNumber = "signNumber";
        this.date = null;
        this.latitude = 21.0;
        this.longitude = 105.0;
        this.status = "status";
        this.business = "business";
        this.direction = 360f;
        this.speed = 40.5;
        this.kmPerDay = 100;
        this.fuel = 11.9;
        this.maxFuel = 20;
    }

    public Car(double latitude, double longitude) {
        this.signNumber = "signNumber";
        this.date = null;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = "status";
        this.business = "business";
        this.direction = 360f;
        this.speed = 40.5;
        this.kmPerDay = 100;
        this.fuel = 11.9;
        this.maxFuel = 20;
    }

    public Car(double latitude, double longitude, float direction) {
        this.signNumber = "signNumber";
        this.date = null;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = "status";
        this.business = "business";
        this.direction = direction;
        this.speed = 40.5;
        this.kmPerDay = 100;
        this.fuel = 11.9;
        this.maxFuel = 20;
    }

    public Car(double latitude, double longitude, float direction, Date date) {
        this.signNumber = "signNumber";
        this.date = date;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = "status";
        this.business = "business";
        this.direction = direction;
        this.speed = 40.5;
        this.kmPerDay = 100;
        this.fuel = 11.9;
        this.maxFuel = 20;
    }

    public Car(double latitude, double longitude, float direction, Date datetime, double speed) {
        this.signNumber = "signNumber";
        this.date = datetime;
        this.latitude = latitude;
        this.longitude = longitude;
        this.status = "status";
        this.business = "business";
        this.direction = direction;
        this.speed = speed;
        this.kmPerDay = 100;
        this.fuel = 11.9;
        this.maxFuel = 20;
    }

    public String getSignNumber() {
        return signNumber;
    }

    public void setSignNumber(String signNumber) {
        this.signNumber = signNumber;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public double getLongitude() {
        return longitude;
    }

    public void setLongitude(double longitude) {
        this.longitude = longitude;
    }

    public double getLatitude() {
        return latitude;
    }

    public void setLatitude(double latitude) {
        this.latitude = latitude;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getBusiness() {
        return business;
    }

    public void setBusiness(String business) {
        this.business = business;
    }

    public float getDirection() {
        return direction;
    }

    public void setDirection(float direction) {
        this.direction = direction;
    }

    public double getSpeed() {
        return speed;
    }

    public void setSpeed(double speed) {
        this.speed = speed;
    }

    public double getKmPerDay() {
        return kmPerDay;
    }

    public void setKmPerDay(double kmPerDay) {
        this.kmPerDay = kmPerDay;
    }

    public double getFuel() {
        return fuel;
    }

    public void setFuel(double fuel) {
        this.fuel = fuel;
    }

    public double getMaxFuel() {
        return maxFuel;
    }

    public void setMaxFuel(double maxFuel) {
        this.maxFuel = maxFuel;
    }
}
