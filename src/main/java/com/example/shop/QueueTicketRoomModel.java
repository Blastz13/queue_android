package com.example.shop;

public class QueueTicketRoomModel {
    int queueId;
    String status;
    String username;

    public QueueTicketRoomModel(int queueId, String status, String username) {
        this.queueId = queueId;
        this.status = status;
        this.username = username;
    }

    public int getQueueId() {
        return queueId;
    }

    public void setQueueId(int queueId) {
        this.queueId = queueId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
