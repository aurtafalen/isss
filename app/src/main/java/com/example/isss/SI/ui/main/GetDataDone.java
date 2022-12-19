package com.example.isss.SI.ui.main;

public class GetDataDone {
    private String templateTeam;
    private String status;
    private String templateAddress;
    private String templateGroup;
    private String templateTitle;



    public GetDataDone(String templateTeam, String status , String templateAddress, String templateGroup,String templateTitle ) {

        this.templateTeam = templateTeam;
        this.status = status;
        this.templateAddress = templateAddress;
        this.templateGroup = templateGroup;
        this.templateTitle = templateTitle;


    }

    //Add this
    public GetDataDone() {

    }

    public String getTemplateTeam() {
        return templateTeam;
    }

    public void setTemplateTeam(String templateTeam) {
        this.templateTeam = templateTeam;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplateAddress() {
        return templateAddress;
    }

    public void setTemplateAddress(String templateAddress) {
        this.templateAddress = templateAddress;
    }

    public String getTemplateGroup() {
        return templateGroup;
    }

    public void setTemplateGroup(String templateGroup) {
        this.templateGroup = templateGroup;
    }

    public String getTemplateTitle() {
        return templateTitle;
    }

    public void setTemplateTitle(String templateTitle) {
        this.templateTitle = templateTitle;
    }
}
