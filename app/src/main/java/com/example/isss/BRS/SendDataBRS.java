package com.example.isss.BRS;


import com.google.firebase.Timestamp;

import java.util.ArrayList;
import java.util.Map;

public class SendDataBRS {
    private String description;
    private ArrayList image_url;
    private Timestamp time_reported;
    private Map team;
    private String lokasi;
    private Double lat;
    private Double lng;
    private String kategori;
    private String subKategori;
    private ArrayList effects;
    private Timestamp time_incident;

    public SendDataBRS(String description, ArrayList image_url, Timestamp time_reported, Map team, String lokasi, Double lat, Double lng, String kategori, String subKategori, ArrayList effects, Timestamp time_incident) {
        this.description = description;
        this.image_url = image_url;
        this.time_reported = time_reported;
        this.team = team;
        this.lokasi = lokasi;
        this.lat = lat;
        this.lng = lng;
        this.kategori = kategori;
        this.subKategori = subKategori;
        this.effects = effects;
        this.time_incident = time_incident;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public ArrayList getImage_url() {
        return image_url;
    }

    public void setImage_url(ArrayList image_url) {
        this.image_url = image_url;
    }

    public Timestamp getTime_reported() {
        return time_reported;
    }

    public void setTime_reported(Timestamp time_reported) {
        this.time_reported = time_reported;
    }

    public Map getTeam() {
        return team;
    }

    public void setTeam(Map team) {
        this.team = team;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public Double getLat() {
        return lat;
    }

    public void setLat(Double lat) {
        this.lat = lat;
    }

    public Double getLng() {
        return lng;
    }

    public void setLng(Double lng) {
        this.lng = lng;
    }

    public String getKategori() {
        return kategori;
    }

    public void setKategori(String kategori) {
        this.kategori = kategori;
    }

    public String getSubKategori() {
        return subKategori;
    }

    public void setSubKategori(String subKategori) {
        this.subKategori = subKategori;
    }

    public ArrayList getEffects() {
        return effects;
    }

    public void setEffects(ArrayList effects) {
        this.effects = effects;
    }

    public Timestamp getTime_incident() {
        return time_incident;
    }

    public void setTime_incident(Timestamp time_incident) {
        this.time_incident = time_incident;
    }
}
