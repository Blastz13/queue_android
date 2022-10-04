package com.example.shop;

public class QueueRoomModel {
    Integer id;
    String title;
    String date_create;

    public QueueRoomModel(Integer id, String title, String date_create) {
        this.id = id;
        this.title = title;
        this.date_create = date_create;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDate_create() {
        return date_create;
    }

    public void setDate_create(String date_create) {
        this.date_create = date_create;
    }
}
