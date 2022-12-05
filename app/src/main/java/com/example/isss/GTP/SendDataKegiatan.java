package com.example.isss.GTP;


import com.google.firebase.Timestamp;

public class SendDataKegiatan {
    private String deskripsi;
    private String txtfoto;
    private Timestamp time;
    private String team;
    private String lokasi;
    private Double CurrentLat;
    private Double CurrentLng;



    public SendDataKegiatan(String deskripsi, String txtfoto, Timestamp time, String team, String lokasi, Double CurrentLat, Double CurrentLng) {
        this.deskripsi = deskripsi;
        this.txtfoto = txtfoto;
        this.time = time;
        this.team = team;
        this.lokasi = lokasi;
        this.CurrentLat = CurrentLat;
        this.CurrentLng = CurrentLng;
    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTxtfoto() {
        return txtfoto;
    }

    public void setTxtfoto(String txtfoto) {
        this.txtfoto = txtfoto;
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
