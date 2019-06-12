package com.perusudroid.player.response.catid;

/**
 * Awesome Pojo Generator
 */
public class Data {
    private String mc_name;
    private String mc_pic;
    private Integer count;
    private Integer mc_id;
    private Integer total_count;

    public Integer getTotal_count() {
        return total_count;
    }

    public void setTotal_count(Integer total_count) {
        this.total_count = total_count;
    }

    public void setMc_name(String mc_name) {
        this.mc_name = mc_name;
    }

    public String getMc_name() {
        return mc_name;
    }

    public void setMc_pic(String mc_pic) {
        this.mc_pic = mc_pic;
    }

    public String getMc_pic() {
        return mc_pic;
    }

    public void setCount(Integer count) {
        this.count = count;
    }

    public Integer getCount() {
        return count;
    }

    public void setMc_id(Integer mc_id) {
        this.mc_id = mc_id;
    }

    public Integer getMc_id() {
        return mc_id;
    }
}