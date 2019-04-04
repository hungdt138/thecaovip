package com.hieugie.banthe.web.rest.dto;

public class DemandRequestDTO {
    private Integer cardValue;
    private Integer quantity;

    public DemandRequestDTO() {
    }

    public DemandRequestDTO(Integer cardValue, Integer quantity) {
        this.cardValue = cardValue;
        this.quantity = quantity;
    }

    public Integer getCardValue() {
        return cardValue;
    }

    public void setCardValue(Integer cardValue) {
        this.cardValue = cardValue;
    }

    public Integer getQuantity() {
        return quantity;
    }

    public void setQuantity(Integer quantity) {
        this.quantity = quantity;
    }
}
