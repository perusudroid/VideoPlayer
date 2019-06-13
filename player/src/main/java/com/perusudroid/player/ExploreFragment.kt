package com.perusudroid.player

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.arch.paging.PagedList
import android.content.Context
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import com.perusudroid.player.response.Data
import com.perusudroid.player.response.ExploreResponse
import com.perusudroid.player.response.video.VideoRequest
import com.perusudroid.player.visibility.calculator.DefaultSingleItemCalculatorCallback
import com.perusudroid.player.visibility.calculator.SingleListViewItemActiveCalculator
import com.perusudroid.player.visibility.scroll_utils.ItemsPositionGetter
import com.perusudroid.player.visibility.scroll_utils.RecyclerViewItemPositionGetter
import java.util.ArrayList


class ExploreFragment : Fragment(), VisibilityItem.ItemCallback {


    private lateinit var mRecyclerView: RecyclerView
    private lateinit var viewModel: ExploreViewModel
    private lateinit var linearLayoutManager: LinearLayoutManager
    private var lastVisibleItem: Int? = 0
    private lateinit var itemsList: ArrayList<VisibilityItem>
    private var mItemsPositionGetter: ItemsPositionGetter? = null
    private var mListItemVisibilityCalculator: SingleListViewItemActiveCalculator? = null
    private var mVisibilityUtilsCallback: VisibilityUtilsCallback? = null
    private var mScrollState: Int = 0
    private var mToast: Toast? = null
    private var adapter: VisibilityUtilsAdapter? = null


    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.explore_fragment, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        bindViews(view)
        setAssets()
    }


    private fun bindViews(view: View) {
        mRecyclerView = view.findViewById(R.id.recyclerView)
    }

    private fun setAssets() {
        itemsList = ArrayList()
        linearLayoutManager = LinearLayoutManager(context)
    }


    override fun onActivityCreated(savedInstanceState: Bundle?) {
        super.onActivityCreated(savedInstanceState)
        viewModel = ViewModelProviders.of(this).get(ExploreViewModel::class.java)
        doFetchApiData()
        setRecyclerListener()
    }

    private fun setRecyclerListener() {

        mRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {

            override fun onScrollStateChanged(recyclerView: RecyclerView, scrollState: Int) {
                mScrollState = scrollState
                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !itemsList.isEmpty()) {

                    mListItemVisibilityCalculator!!.onScrollStateIdle(
                        mItemsPositionGetter,
                        linearLayoutManager.findFirstVisibleItemPosition(),
                        linearLayoutManager.findLastVisibleItemPosition()
                    )
                }
            }

            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {

                lastVisibleItem = linearLayoutManager.findLastVisibleItemPosition()

                //reached bottom of the recycler view
                if (itemsList.isNotEmpty()) {
                    mListItemVisibilityCalculator!!.onScroll(
                        mItemsPositionGetter,
                        linearLayoutManager.findFirstVisibleItemPosition(),
                        linearLayoutManager.findLastVisibleItemPosition() - linearLayoutManager.findFirstVisibleItemPosition() + 1,
                        mScrollState
                    )
                }
            }
        })


    }

    private fun makeLog(s: String) {
        Log.d("Explore", "" + s)
    }

    private fun doFetchApiData() {

        Log.d("Explore ", "doFetchApiData")


        viewModel.getVideoList(VideoRequest(0,8,1)).observe(this, Observer {


            it?.data?.forEach {
                itemsList.add(VisibilityItem(it.md_title,it.md_vid_id,it.md_thumb_medium,this,1))
            }

            mListItemVisibilityCalculator = SingleListViewItemActiveCalculator(
                DefaultSingleItemCalculatorCallback(), itemsList
            )

            mRecyclerView.layoutManager = linearLayoutManager

            if(adapter == null){
                adapter = VisibilityUtilsAdapter(itemsList)
                mRecyclerView.adapter = adapter
            }else{
                adapter?.notifyDataSetChanged()
            }


            if (itemsList.isNotEmpty()) {
                // need to call this method from list view handler in order to have filled list

                mRecyclerView.post {
                    mListItemVisibilityCalculator!!.onScrollStateIdle(
                        mItemsPositionGetter,
                        linearLayoutManager.findFirstVisibleItemPosition(),
                        linearLayoutManager.findLastVisibleItemPosition()
                    )
                }
            }


            mItemsPositionGetter = RecyclerViewItemPositionGetter(linearLayoutManager, mRecyclerView)

        })



    }


    override fun makeToast(text: String?) {
        if (mToast != null) {
            mToast?.cancel()
            mToast = Toast.makeText(activity, text, Toast.LENGTH_SHORT)
            mToast?.show()
        }
    }


    override fun onActiveViewChangedActive(
        newActiveView: View,
        activeViewPosition: Int,
        visibility: Int
    ) {

        Log.d("Explore", "Visibility $visibility")

        mVisibilityUtilsCallback?.setTitle("Active view at position $activeViewPosition")
    }


    override fun onAttach(context: Context?) {
        super.onAttach(context)
        mVisibilityUtilsCallback = context as VisibilityUtilsCallback
    }

    override fun onDetach() {
        super.onDetach()
        mVisibilityUtilsCallback = null
    }

}
