package com.hieugie.banthe.web.rest.dto;

public class TwoFa {

    private String url;
    private String privateKey;

    public TwoFa(String url, String privateKey) {
        this.url = url;
        this.privateKey = privateKey;
    }

    public TwoFa() {
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getPrivateKey() {
        return privateKey;
    }

    public void setPrivateKey(String privateKey) {
        this.privateKey = privateKey;
    }
}
