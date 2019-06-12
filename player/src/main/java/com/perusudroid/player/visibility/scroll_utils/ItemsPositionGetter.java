package com.perusudroid.player.visibility.scroll_utils;

import android.view.View;
import com.perusudroid.player.visibility.calculator.ListItemsVisibilityCalculator;

/**
 * This class is an API for {@link ListItemsVisibilityCalculator}
 * Using this class is can access all the data from RecyclerView / ListView
 *
 * There is two different implementations for ListView and for RecyclerView.
 * RecyclerView introduced LayoutManager that's why some of data moved there
 *
 * Created by danylo.volokh on 9/20/2015.
 */
public interface ItemsPositionGetter {
    View getChildAt(int position);

    int indexOfChild(View view);

    int getChildCount();

    int getLastVisiblePosition();

    int getFirstVisiblePosition();
}
