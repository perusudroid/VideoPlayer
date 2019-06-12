package com.perusudroid.player.retrofit;

import com.perusudroid.player.response.ExploreResponse;
import com.perusudroid.player.response.catid.CategoryResponse;
import com.perusudroid.player.response.video.VideoRequest;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;

public interface ApiInterface {

    @GET("Video/vidCat/{hero_id}")
    Call<CategoryResponse> getVideoCatId(@Path("hero_id") Integer hero_id);


    @POST("Video/videoById")
    Call<ExploreResponse> getVideoByCatId(@Body VideoRequest videoRequest);




}

