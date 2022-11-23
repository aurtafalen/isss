package com.example.isss.GTP;


import com.google.firebase.Timestamp;

public class SendDataPatroli {

    private Timestamp waktuMasuk;
    private String team;
    private String judul;



    public SendDataPatroli(Timestamp waktuMasuk, String team , String judul) {

        this.waktuMasuk = waktuMasuk;
        this.team = team;
        this.judul = judul;


    }

    public Timestamp getWaktuMasuk() {
        return waktuMasuk;
    }

    public void setWaktuMasuk(Timestamp waktuMasuk) {
        this.waktuMasuk = waktuMasuk;
    }

    public String getTeam() {
        return team;
    }

    public void setTeam(String team) {
        this.team = team;
    }

    public String getJudul() {
        return judul;
    }

    public void setJudul(String judul) {
        this.judul = judul;
    }
}
