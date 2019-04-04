package com.hieugie.banthe.web.rest.dto;

public class TrumTheResponse {

    /**
     * 1 - Số điện thoại sai định dạng
     * 2 - Thẻ cào không hợp lệ hoặc đã được sử dụng.
     * 2 - Nạp sai quá số lần qui định trong ngày.
     */
    private Integer error;
    private String msg;

    public TrumTheResponse() {
    }

    public TrumTheResponse(Integer error, String msg) {
        this.error = error;
        this.msg = msg;
    }

    public Integer getError() {
        return error;
    }

    public void setError(Integer error) {
        this.error = error;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    @Override
    public String toString() {
        return "TrumTheResponse{" +
            "error=" + error +
            ", msg='" + msg + '\'' +
            '}';
    }
}
