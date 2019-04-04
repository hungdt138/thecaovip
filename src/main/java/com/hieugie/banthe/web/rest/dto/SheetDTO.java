package com.hieugie.banthe.web.rest.dto;

public class SheetDTO {
    private Long id;
    private String name;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public SheetDTO(Long id, String name) {
        this.id = id;
        this.name = name;
    }

    public SheetDTO() {
    }
}
