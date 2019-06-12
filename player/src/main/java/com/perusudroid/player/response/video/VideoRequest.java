package com.perusudroid.player.response.video;

/**
 * Awesome Pojo Generator
 */
public class VideoRequest {
    private Integer offset;
    private Integer media_type;
    private Integer hero_id;

    public VideoRequest(Integer offset, Integer media_type, Integer hero_id) {
        this.offset = offset;
        this.media_type = media_type;
        this.hero_id = hero_id;
    }

    public void setOffset(Integer offset) {
        this.offset = offset;
    }

    public Integer getOffset() {
        return offset;
    }

    public void setMedia_type(Integer media_type) {
        this.media_type = media_type;
    }

    public Integer getMedia_type() {
        return media_type;
    }

    public void setHero_id(Integer hero_id) {
        this.hero_id = hero_id;
    }

    public Integer getHero_id() {
        return hero_id;
    }
}