package com.example.isss.GTP;


import com.google.firebase.Timestamp;

public class SendDataScan {
    private String idCheckpoint;
    private String titikKoordinat;
    private String lokasi;
    private String area;
    private String team;
    private Timestamp time;
    private Double currentLat;
    private Double currentLng;
    private String ttdText;

    public SendDataScan(String idCheckpoint, String titikKoordinat, String lokasi, String area, String team, Timestamp time, Double currentLat, Double currentLng, String ttdText) {
        this.idCheckpoint = idCheckpoint;
        this.titikKoordinat = titikKoordinat;
        this.lokasi = lokasi;
        this.area = area;
        this.team = team;
        this.time = time;
        this.currentLat = currentLat;
        this.currentLng = currentLng;
        this.ttdText = ttdText;
    }

    public String getIdCheckpoint() {
        return idCheckpoint;
    }

    public void setIdCheckpoint(String idCheckpoint) {
        this.idCheckpoint = idCheckpoint;
    }

    public String getTitikKoordinat() {
        return titikKoordinat;
    }

    public void setTitikKoordinat(String titikKoordinat) {
        this.titikKoordinat = titikKoordinat;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getArea() {
        return area;
    }

    public void setArea(String area) {
        this.area = area;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public Timestamp getTime() {
        return time;
    }

    public void setTime(Timestamp time) {
        this.time = time;
    }

    public Double getCurrentLat() {
        return currentLat;
    }

    public void setCurrentLat(Double currentLat) {
        this.currentLat = currentLat;
    }

    public Double getCurrentLng() {
        return currentLng;
    }

    public void setCurrentLng(Double currentLng) {
        this.currentLng = currentLng;
    }

    public String getTtdText() {
        return ttdText;
    }

    public void setTtdText(String ttdText) {
        this.ttdText = ttdText;
    }
}
