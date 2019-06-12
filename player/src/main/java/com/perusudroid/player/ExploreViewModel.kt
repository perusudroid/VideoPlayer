package com.perusudroid.player

import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.ViewModel;
import com.perusudroid.player.response.ExploreResponse
import com.perusudroid.player.response.video.VideoRequest
import com.perusudroid.player.retrofit.ApiClient
import com.perusudroid.player.retrofit.ApiInterface
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class ExploreViewModel : ViewModel() {

    private val exploreResponse = MutableLiveData<ExploreResponse>()

    fun getVideoList(videoRequest: VideoRequest): LiveData<ExploreResponse> {
        val apiInterface = ApiClient.getClient().create(ApiInterface::class.java)

        apiInterface.getVideoByCatId(videoRequest).enqueue(object :
            Callback<ExploreResponse> {

            override fun onFailure(call: Call<ExploreResponse>, t: Throwable) {
                handleError(t.localizedMessage)
            }

            override fun onResponse(call: Call<ExploreResponse>, response: Response<ExploreResponse>) {
                if (response.isSuccessful && response.code() == 200) {
                    handleSuccess(response.body()!!)
                } else {
                    handleError("Error " + response.code())
                }
            }

        })

    return exploreResponse
    }

    private fun handleSuccess(body: ExploreResponse) {
        if(body.status == 1){
            exploreResponse.postValue(body)
        }else{
            handleError("No Data")
        }
    }

    private fun handleError(localizedMessage: String?) {
        exploreResponse.postValue(ExploreResponse(0, localizedMessage))
    }
}
