package com.dasu.recyclerlibrary.listener;

public interface ICustomScrollListener {
    void scrollRefreshState(int state);
    void scrollLoadMoreState(int state);
    void refresh();
    void loadMore();
}
