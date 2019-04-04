package com.hieugie.banthe.web.rest.dto;

public class CallBackResponse {
    private int status;
    private String description;

    public CallBackResponse(int status, String description) {
        this.status = status;
        this.description = description;
    }

    public CallBackResponse() {
    }

    public int getStatus() {
        return status;
    }

    public void setStatus(int status) {
        this.status = status;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "CallBackResponse{" +
            "status=" + status +
            ", description='" + description + '\'' +
            '}';
    }
}
