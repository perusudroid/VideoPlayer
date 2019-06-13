package com.perusudroid.player.loadmore;

import android.arch.lifecycle.MutableLiveData;
import android.arch.paging.DataSource;
import android.arch.paging.PageKeyedDataSource;
import com.perusudroid.player.VisibilityItem;
import com.perusudroid.player.response.Data;

public class ItemDataSourceFactory extends DataSource.Factory {

    private MutableLiveData<PageKeyedDataSource<Integer, VisibilityItem>> itemLiveDataSource = new MutableLiveData<>();


    @Override
    public DataSource create() {
        ItemDataSource itemDataSource = new ItemDataSource();
        itemLiveDataSource.postValue(itemDataSource);
        return itemDataSource;
    }

    public MutableLiveData<PageKeyedDataSource<Integer, VisibilityItem>> getItemLiveDataSource() {
        return itemLiveDataSource;
    }
}
