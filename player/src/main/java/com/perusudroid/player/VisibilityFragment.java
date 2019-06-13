package com.perusudroid.player;

import android.content.Context;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;
import com.perusudroid.player.visibility.calculator.DefaultSingleItemCalculatorCallback;
import com.perusudroid.player.visibility.calculator.ListItemsVisibilityCalculator;
import com.perusudroid.player.visibility.calculator.SingleListViewItemActiveCalculator;
import com.perusudroid.player.visibility.scroll_utils.ItemsPositionGetter;
import com.perusudroid.player.visibility.scroll_utils.RecyclerViewItemPositionGetter;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

/**
 * Created by danylo.volokh on 08.01.2016.
 */
public class VisibilityFragment extends Fragment implements VisibilityItem.ItemCallback {


    private RecyclerView mRecyclerView;
    private LinearLayoutManager mLayoutManager;
    private VisibilityUtilsCallback mVisibilityUtilsCallback;

    private List<VisibilityItem> mList = new ArrayList<>(Arrays.asList(
            new VisibilityItem("1", "zI-Pux4uaqM", "https://i.ytimg.com/vi/zI-Pux4uaqM/maxresdefault.jpg", this,1),
            new VisibilityItem("2", "8ZK_S-46KwE", "https://i.ytimg.com/vi/8ZK_S-46KwE/maxresdefault.jpg", this,1),
            new VisibilityItem("3", "8czMWUH7vW4", "https://i.ytimg.com/vi/8czMWUH7vW4/hqdefault.jpg", this,1),
            new VisibilityItem("4", "YrQVYEb6hcc", "https://i.ytimg.com/vi/YrQVYEb6hcc/maxresdefault.jpg", this,1),
            new VisibilityItem("1", "zI-Pux4uaqM", "https://i.ytimg.com/vi/zI-Pux4uaqM/maxresdefault.jpg", this,1),
            new VisibilityItem("2", "8ZK_S-46KwE", "https://i.ytimg.com/vi/8ZK_S-46KwE/maxresdefault.jpg", this,1),
            new VisibilityItem("3", "8czMWUH7vW4", "https://i.ytimg.com/vi/8czMWUH7vW4/hqdefault.jpg", this,1),
            new VisibilityItem("4", "YrQVYEb6hcc", "https://i.ytimg.com/vi/YrQVYEb6hcc/maxresdefault.jpg", this,1)
    ));
   /*         new VisibilityItem("5", this),
            new VisibilityItem("6", this),
            new VisibilityItem("7", this),
            new VisibilityItem("8", this),
            new VisibilityItem("9", this),
            new VisibilityItem("10", this)));*/

    private final ListItemsVisibilityCalculator mListItemVisibilityCalculator =
            new SingleListViewItemActiveCalculator(new DefaultSingleItemCalculatorCallback(), mList);

    private ItemsPositionGetter mItemsPositionGetter;

    private int mScrollState;
    private Toast mToast;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        mVisibilityUtilsCallback = (VisibilityUtilsCallback) context;
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mVisibilityUtilsCallback = null;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.visibility_utils_activity, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindViews(view);
        setAssets();
    }

    private void bindViews(View view) {
        mRecyclerView = view.findViewById(R.id.visibility_demo_recycler_view);
    }

    private void setAssets() {

        mRecyclerView.setHasFixedSize(true);

        mLayoutManager = new LinearLayoutManager(getActivity());
        mRecyclerView.setLayoutManager(mLayoutManager);


        if (!mList.isEmpty()) {
            // need to call this method from list view handler in order to have filled list

            mRecyclerView.post(new Runnable() {
                @Override
                public void run() {

                    mListItemVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());

                }
            });
        }

        VisibilityUtilsAdapter adapter = new VisibilityUtilsAdapter(mList);
        mRecyclerView.setAdapter(adapter);


        mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {

            @Override
            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int scrollState) {
                mScrollState = scrollState;

                if (scrollState == RecyclerView.SCROLL_STATE_IDLE && !mList.isEmpty()) {

                    mListItemVisibilityCalculator.onScrollStateIdle(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition());
                }
            }

            @Override
            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {




                if (!mList.isEmpty()) {
                    mListItemVisibilityCalculator.onScroll(
                            mItemsPositionGetter,
                            mLayoutManager.findFirstVisibleItemPosition(),
                            mLayoutManager.findLastVisibleItemPosition() - mLayoutManager.findFirstVisibleItemPosition() + 1,
                            mScrollState);
                }
            }
        });

        adapter.notifyDataSetChanged();


        mItemsPositionGetter = new RecyclerViewItemPositionGetter(mLayoutManager, mRecyclerView);
    }

    @Override
    public void makeToast(String text) {
        if (mToast != null) {
            mToast.cancel();
            mToast = Toast.makeText(getActivity(), text, Toast.LENGTH_SHORT);
            mToast.show();
        }
    }

    @Override
    public void onActiveViewChangedActive(View newActiveView, int activeViewPosition, int visibility) {
        mVisibilityUtilsCallback.setTitle("Active view at position " + visibility);
    }


    public interface VisibilityUtilsCallback {
        void setTitle(String title);
    }

}
