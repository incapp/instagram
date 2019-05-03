package com.incapp.instagram.models;

public class PostModel implements Comparable<PostModel> {

    public PostModel() {
    }

    public PostModel(String dateTime, String fileName, String caption, String downloadUrl, String userName) {
        this.dateTime = dateTime;
        this.fileName = fileName;
        this.caption = caption;
        this.downloadUrl = downloadUrl;
        this.userName = userName;
    }

    private String dateTime;
    private String fileName;
    private String caption;
    private String downloadUrl;
    private String userName;

    public String getDateTime() {
        return dateTime;
    }

    public void setDateTime(String dateTime) {
        this.dateTime = dateTime;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getCaption() {
        return caption;
    }

    public void setCaption(String caption) {
        this.caption = caption;
    }

    public String getDownloadUrl() {
        return downloadUrl;
    }

    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    @Override
    public int compareTo(PostModel o) {
        return o.dateTime.compareTo(this.dateTime);
    }
}
