package com.example.shop;

public class QueueTicketRoomModel {
    int queueId;
    int userId;
    String status;
    String username;

    public QueueTicketRoomModel(int queueId, int userId, String status, String username) {
        this.queueId = queueId;
        this.userId = userId;
        this.status = status;
        this.username = username;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
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
