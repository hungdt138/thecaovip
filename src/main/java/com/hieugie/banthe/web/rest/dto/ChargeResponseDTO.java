package com.hieugie.banthe.web.rest.dto;

public class ChargeResponseDTO {
    private int status;
    private String description;
    private String requestId;

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

    public String getRequestId() {
        return requestId;
    }

    public void setRequestId(String requestId) {
        this.requestId = requestId;
    }

    public ChargeResponseDTO() {
    }
}
