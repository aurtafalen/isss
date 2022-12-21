package com.example.isss.SI;

import android.text.format.DateFormat;

import com.google.firebase.Timestamp;

import java.util.Date;

public class GetDataToolBox {
    private String input;
    private String kehadiran;
    private String lokasi;
    private String status;
    private Timestamp timeDate;
    private String tanggapan;
    private String targetWaktu;
    private String topik;

    public GetDataToolBox(String input, String kehadiran , String lokasi, String status , Timestamp timeDate, String tanggapan , String targetWaktu, String topik ) {

        this.input = input;
        this.kehadiran = kehadiran;
        this.lokasi = lokasi;
        this.status = status;
        this.timeDate = timeDate;
        this.tanggapan = tanggapan;
        this.targetWaktu = targetWaktu;
        this.topik = topik;


    }

    //Add this
    public GetDataToolBox() {

    }

    public String getTimeDate() {
        long miliseconds = timeDate.toDate().getTime();
        String dateString = DateFormat.format("MMMM d, yyyy HH:mm", new Date(miliseconds)).toString();
        return dateString;
    }

    public void setTimeDate(Timestamp timeDate) {
        this.timeDate = timeDate;
    }

    public String getInput() {
        return input;
    }

    public void setInput(String input) {
        this.input = input;
    }

    public String getKehadiran() {
        return kehadiran;
    }

    public void setKehadiran(String kehadiran) {
        this.kehadiran = kehadiran;
    }

    public String getLokasi() {
        return lokasi;
    }

    public void setLokasi(String lokasi) {
        this.lokasi = lokasi;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTanggapan() {
        return tanggapan;
    }

    public void setTanggapan(String tanggapan) {
        this.tanggapan = tanggapan;
    }

    public String getTargetWaktu() {
        return targetWaktu;
    }

    public void setTargetWaktu(String targetWaktu) {
        this.targetWaktu = targetWaktu;
    }

    public String getTopik() {
        return topik;
    }

    public void setTopik(String topik) {
        this.topik = topik;
    }
}
