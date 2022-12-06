package com.example.isss.GTP;


import com.google.firebase.Timestamp;

import java.util.ArrayList;

public class SendDataKegiatan {
    private String deskripsi;
    private ArrayList listImage;
    private Timestamp time;
    private String team;
    private String lokasi;
    private Double CurrentLat;
    private Double CurrentLng;

    public SendDataKegiatan(String deskripsi, ArrayList listImage, Timestamp time, String team, String lokasi, Double currentLat, Double currentLng) {
        this.deskripsi = deskripsi;
        this.listImage = listImage;
        this.time = time;
        this.team = team;
        this.lokasi = lokasi;
        CurrentLat = currentLat;
        CurrentLng = currentLng;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public ArrayList getListImage() {
        return listImage;
    }

    public void setListImage(ArrayList listImage) {
        this.listImage = listImage;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public Double getCurrentLat() {
        return CurrentLat;
    }

    public void setCurrentLat(Double currentLat) {
        CurrentLat = currentLat;
    }

    public Double getCurrentLng() {
        return CurrentLng;
    }

    public void setCurrentLng(Double currentLng) {
        CurrentLng = currentLng;
    }
}
