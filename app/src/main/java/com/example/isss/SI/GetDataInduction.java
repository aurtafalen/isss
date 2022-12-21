package com.example.isss.SI;

import android.text.format.DateFormat;

import com.google.firebase.Timestamp;

import java.util.Date;

public class GetDataInduction {
    private String nama;
    private String prusahaan;
    private String jumlahPeserta;
    private String status;
    private Timestamp timeDate;


    public GetDataInduction(String nama, String prusahaan , String jumlahPeserta, String status ,Timestamp timeDate ) {

        this.nama = nama;
        this.prusahaan = prusahaan;
        this.jumlahPeserta = jumlahPeserta;
        this.status = status;
        this.timeDate = timeDate;


    }

    //Add this
    public GetDataInduction() {

    }

    public String getTimeDate() {
        long miliseconds = timeDate.toDate().getTime();
        String dateString = DateFormat.format("MMMM d, yyyy HH:mm", new Date(miliseconds)).toString();
        return dateString;
    }

    public void setTimeDate(Timestamp timeDate) {
        this.timeDate = timeDate;
    }

    public String getNama() {
        return nama;
    }

    public void setNama(String nama) {
        this.nama = nama;
    }

    public String getPrusahaan() {
        return prusahaan;
    }

    public void setPrusahaan(String prusahaan) {
        this.prusahaan = prusahaan;
    }

    public String getJumlahPeserta() {
        return jumlahPeserta;
    }

    public void setJumlahPeserta(String jumlahPeserta) {
        this.jumlahPeserta = jumlahPeserta;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
