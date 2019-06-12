package com.perusudroid.player.response;

import java.util.ArrayList;

/**
 * Awesome Pojo Generator
 */
public class ExploreResponse {
    private Integer offset;
    private ArrayList<Data> data;
    private Integer limit_per_request;
    private Integer status;
    private String message = "Success";

    public ExploreResponse(Integer status, String message) {
        this.status = status;
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setData(ArrayList<Data> data) {
        this.data = data;
    }

    public ArrayList<Data> getData() {
        return data;
    }

    public void setLimit_per_request(Integer limit_per_request) {
        this.limit_per_request = limit_per_request;
    }

    public Integer getLimit_per_request() {
        return limit_per_request;
    }

    public void setStatus(Integer status) {
        this.status = status;
    }

    public Integer getStatus() {
        return status;
    }
}