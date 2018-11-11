package com.dasu.customrecyclerview;

import android.support.v7.util.DiffUtil;

import java.util.ArrayList;
import java.util.List;

public class DiffUtils extends DiffUtil.Callback {
    private List<UserInfo> mOldData;
    private List<UserInfo> mNewsData;

    public DiffUtils(List<UserInfo> mOldData, List<UserInfo> mNewsData) {
        this.mOldData = mOldData;
        this.mNewsData = mNewsData;
    }

    @Override
    public int getOldListSize() {
        return mOldData.size();
    }

    @Override
    public int getNewListSize() {
        return mNewsData.size();
    }

    @Override
    public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldData.get(oldItemPosition).getId() == mNewsData.get(newItemPosition).getId();
    }

    @Override
    public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
        return mOldData.get(oldItemPosition).getName().equals(mNewsData.get(newItemPosition).getName());
    }
}
