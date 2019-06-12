package com.perusudroid.videoplay

import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.perusudroid.player.ExploreFragment
import com.perusudroid.player.VisibilityUtilsCallback

class SampleActivity : AppCompatActivity() , VisibilityUtilsCallback {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sample)

        supportFragmentManager.beginTransaction()
            .replace(R.id.mainContainer, ExploreFragment())
            .commitNow()
    }

    override fun setTitle(title: String) {
        Log.d("Sample","title $title")
    }
}
