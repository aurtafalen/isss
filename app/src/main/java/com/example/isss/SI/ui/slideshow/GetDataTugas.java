package com.example.isss.SI.ui.slideshow;

public class GetDataTugas {
    private String deskripsi;
    private String teamTugas;
    private String titleTugas;
    private String statusTugas;


    public GetDataTugas(String deskripsi, String teamTugas , String titleTugas, String statusTugas ) {

        this.deskripsi = deskripsi;
        this.teamTugas = teamTugas;
        this.titleTugas = titleTugas;
        this.statusTugas = statusTugas;


    }

    //Add this
    public GetDataTugas() {

    }

    public String getDeskripsi() {
        return deskripsi;
    }

    public void setDeskripsi(String deskripsi) {
        this.deskripsi = deskripsi;
    }

    public String getTeamTugas() {
        return teamTugas;
    }

    public void setTeamTugas(String teamTugas) {
        this.teamTugas = teamTugas;
    }

    public String getTitleTugas() {
        return titleTugas;
    }

    public void setTitleTugas(String titleTugas) {
        this.titleTugas = titleTugas;
    }

    public String getStatusTugas() {
        return statusTugas;
    }

    public void setStatusTugas(String statusTugas) {
        this.statusTugas = statusTugas;
    }
}
