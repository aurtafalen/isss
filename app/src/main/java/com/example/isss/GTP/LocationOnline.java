package com.example.isss.GTP;

public class LocationOnline {
    private String email,uid,status,id;
    private Double latitude,longitude;

    public LocationOnline(){

    }

    public LocationOnline(String email, String uid , Double latitude, Double longitude, String id, String status){
        this.email = email;
        this.uid = uid;
        this.latitude = latitude;
        this.longitude = longitude;
        this.id = id;
        this.status = status;
    }

    public String getEmail(){
        return email;
    }
    public void setEmail (String email){
        this.email = email;

    }
    public String getUid(){
        return uid;
    }
    public void setUid (String uid){
        this.uid = uid;

    }

    public Double getLatitude() {
        return latitude;
    }

    public void setLatitude(Double latitude) {
        this.latitude = latitude;
    }

    public Double getLongitude() {
        return longitude;
    }

    public void setLongitude(Double longitude) {
        this.longitude = longitude;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


}
