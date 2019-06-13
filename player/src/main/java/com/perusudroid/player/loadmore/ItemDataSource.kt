package com.perusudroid.player.loadmore

import android.arch.paging.PageKeyedDataSource
import com.perusudroid.player.VisibilityItem
import com.perusudroid.player.response.Data
import com.perusudroid.player.response.ExploreResponse
import com.perusudroid.player.response.video.VideoRequest
import com.perusudroid.player.retrofit.ApiClient
import com.perusudroid.player.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ItemDataSource : PageKeyedDataSource<Int, VisibilityItem>() {


    companion object {
        val PAGE_SIZE = 50
        private val FIRST_PAGE = 1
        private val SITE_NAME = "stackoverflow"
        var offsetCount = 0
    }

    override fun loadInitial(
        params: PageKeyedDataSource.LoadInitialParams<Int>,
        callback: PageKeyedDataSource.LoadInitialCallback<Int, VisibilityItem>
    ) {

        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        apiInterface.getVideoByCatId(VideoRequest(offsetCount, 8, 1))
            .enqueue(object : Callback<ExploreResponse> {
                override fun onResponse(call: Call<ExploreResponse>, response: Response<ExploreResponse>) {

                    if (response.body() != null) {

                        val tmp = ArrayList<VisibilityItem>()

                        response.body()!!.data.forEach {
                            tmp.add(VisibilityItem(it.md_title, it.md_vid_id, it.md_thumb_medium, null, 1))
                        }

                        callback.onResult(tmp, null, FIRST_PAGE + 1)

                    }

                }

                override fun onFailure(call: Call<ExploreResponse>, t: Throwable) {

                }
            })

    }

    override fun loadBefore(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, VisibilityItem>
    ) {

        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        apiInterface.getVideoByCatId(VideoRequest(offsetCount, 8, 1))
            .enqueue(object : Callback<ExploreResponse> {
                override fun onResponse(call: Call<ExploreResponse>, response: Response<ExploreResponse>) {

                    if (response.body() != null) {
                        val key = if (params.key > 1) params.key - 1 else null

                        val tmp = ArrayList<VisibilityItem>()

                        response.body()!!.data.forEach {
                            tmp.add(VisibilityItem(it.md_title, it.md_vid_id, it.md_thumb_medium, null, 1))
                        }

                        callback.onResult(tmp, key)
                    }
                }

                override fun onFailure(call: Call<ExploreResponse>, t: Throwable) {

                }
            })

    }

    override fun loadAfter(
        params: PageKeyedDataSource.LoadParams<Int>,
        callback: PageKeyedDataSource.LoadCallback<Int, VisibilityItem>
    ) {

        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        apiInterface
            .getVideoByCatId(VideoRequest(offsetCount, 8, 1))
            .enqueue(object : Callback<ExploreResponse> {
                override fun onResponse(call: Call<ExploreResponse>, response: Response<ExploreResponse>) {

                    if (response.body() != null) {
                        if (response.body()!!.status == 1) {
                            offsetCount = response.body()!!.offset
                            val key = if (response.body()!!.offset < 5) params.key + 1 else null

                            val tmp = ArrayList<VisibilityItem>()

                            response.body()!!.data.forEach {
                                tmp.add(VisibilityItem(it.md_title, it.md_vid_id, it.md_thumb_medium, null, 1))
                            }

                            callback.onResult(tmp, key)
                        } else {
                            //no data
                        }

                    }

                }

                override fun onFailure(call: Call<ExploreResponse>, t: Throwable) {

                }
            })


    }


}
