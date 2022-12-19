package com.example.isss.SI.ui.main;


public class GetDataTodo {
    private String author;
    private String status;
    private String templateDescription;
    private String templateGroup;
    private String templateTitle;



    public GetDataTodo(String author, String status , String templateDescription, String templateGroup,String templateTitle ) {

        this.author = author;
        this.status = status;
        this.templateDescription = templateDescription;
        this.templateGroup = templateGroup;
        this.templateTitle = templateTitle;

    }

    //Add this
    public GetDataTodo() {

    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getTemplateDescription() {
        return templateDescription;
    }

    public void setTemplateDescription(String templateDescription) {
        this.templateDescription = templateDescription;
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
