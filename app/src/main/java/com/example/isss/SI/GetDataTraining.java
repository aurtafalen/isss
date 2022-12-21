package com.example.isss.SI;

import android.text.format.DateFormat;

import com.google.firebase.Timestamp;

import java.util.Date;

public class GetDataTraining {
    private String trainer;
    private String kehadiran;
    private String lokasi;
    private String deskripsi;
    private Timestamp timeDate;


    public GetDataTraining(String trainer, String kehadiran , String lokasi, String deskripsi , Timestamp timeDate) {

        this.trainer = trainer;
        this.kehadiran = kehadiran;
        this.lokasi = lokasi;
        this.deskripsi = deskripsi;
        this.timeDate = timeDate;



    }

    //Add this
    public GetDataTraining() {

    }

    public String getTimeDate() {
        long miliseconds = timeDate.toDate().getTime();
        String dateString = DateFormat.format("MMMM d, yyyy HH:mm", new Date(miliseconds)).toString();
        return dateString;
    }

    public void setTimeDate(Timestamp timeDate) {
        this.timeDate = timeDate;
    }

    public String getTrainer() {
        return trainer;
    }

    public void setTrainer(String trainer) {
        this.trainer = trainer;
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

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }
}
