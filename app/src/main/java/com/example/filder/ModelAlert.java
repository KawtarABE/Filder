package com.example.filder;

public class ModelAlert {

    private String context;
    private String field;
    private String sender;
    private String text;
    private String id;
    private String date;
    private Boolean open;

    public String getContext() {
        return context;
    }

    public void setContext(String context) {
        this.context = context;
    }

    public String getField() {
        return field;
    }

    public void setField(String field) {
        this.field = field;
    }

    public String getSender() {
        return sender;
    }

    public void setSender(String sender) {
        this.sender = sender;
    }

    public String getText() {
        return text;
    }

    public void setText(String text) {
        this.text = text;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public Boolean getOpen() {
        return open;
    }

    public void setOPen(Boolean open) {
        this.open = open;
    }
}
