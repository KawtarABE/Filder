package com.example.filder;

public class ModelField {
    private String numero;

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    private String id;
    private String surface;
    private String location;
    private String owner;
    private String Temperature;
    private String UV;
    private String Humidity;

    public String getNumero() {
        return numero;
    }

    public void setNumero(String number) {
        this.numero = number;
    }

    public String getSurface() {
        return surface;
    }

    public void setSurface(String surface) {
        this.surface = surface;
    }

    public String getLocation() {
        return location;
    }

    public void setLocation(String location) {
        this.location = location;
    }

    public String getOwner() {
        return owner;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public String getTemperature() {
        return Temperature;
    }

    public void setTemperature(String temperature) {
        Temperature = temperature;
    }

    public String getUV() {
        return UV;
    }

    public void setUV(String UV) {
        this.UV = UV;
    }

    public String getHumidity() {
        return Humidity;
    }

    public void setHumidity(String humidity) {
        Humidity = humidity;
    }
}
