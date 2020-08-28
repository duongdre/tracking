package com.example.dss.project.Models;

public class Mess {
    private String fromWho;
    private String toWho;
    private String type;
    private String value;

    public Mess(String fromWho, String toWho, String type, String value) {
        this.fromWho = fromWho;
        this.toWho = toWho;
        this.type = type;
        this.value = value;
    }

    public Mess() {
    }

    public String getFromWho() {
        return fromWho;
    }

    public void setFromWho(String fromWho) {
        this.fromWho = fromWho;
    }

    public String getToWho() {
        return toWho;
    }

    public void setToWho(String toWho) {
        this.toWho = toWho;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
