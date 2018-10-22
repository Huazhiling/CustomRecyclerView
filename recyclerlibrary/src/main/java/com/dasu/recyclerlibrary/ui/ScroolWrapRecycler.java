package com.dasu.recyclerlibrary.ui;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import com.dasu.recyclerlibrary.CustomAdapter;
import com.dasu.recyclerlibrary.listener.ICustomClickListener;

public class ScroolWrapRecycler extends LinearLayout {
    private CustomRecyclerView mRecyclerView;
    private View mRefreshView;
    private View mLoadMoreView;
    private int foorIndex; //每次添加foot都要赋值  防止下一次找不到
    private int start_X, start_Y = 0;

    public ScroolWrapRecycler(Context context) {
        this(context, null);
    }

    public ScroolWrapRecycler(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScroolWrapRecycler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mRecyclerView = new CustomRecyclerView(context);
        mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        addView(mRecyclerView);
    }

    /**
     * 添加HeadView的方法
     *
     * @param view
     */
    public void addHeadView(View view) {
        this.mRecyclerView.addHeadView(view);
    }

    /**
     * 添加HeadView的方法 并添加是否添加缓存的标识
     *
     * @param view
     * @param isCache
     */
    public void addHeadView(View view, boolean isCache) {
        this.mRecyclerView.addHeadView(view, isCache);
    }

    /**
     * 添加FootView的方法 并添加是否添加缓存的标识
     *
     * @param view
     */
    public void addFootView(View view) {
        this.mRecyclerView.addFootView(view);
    }

    /**
     * 设置Adapter
     *
     * @param adapter
     */
    public void setAdapter(RecyclerView.Adapter adapter) {
        this.mRecyclerView.setAdapter(adapter);
    }

    /**
     * 返回Adapter
     *
     * @return
     */
    public CustomAdapter getAdapter() {
        return this.mRecyclerView.getHeadAndFootAdapter();
    }

    /**
     * 设置点击事件
     *
     * @param customClickListener
     */
    public void setCustomClickListener(ICustomClickListener customClickListener) {
        this.mRecyclerView.setCustomClickListener(customClickListener);
    }

    /**
     * 设置是否刷新
     *
     * @param isRefresh
     */
    public void setRefresh(boolean isRefresh) {
        this.mRecyclerView.setRefresh(isRefresh);
    }

    /**
     * 设置是否加载
     *
     * @param isLoadMore
     */
    public void setLoadMore(boolean isLoadMore) {
        this.mRecyclerView.setLoadMore(isLoadMore);
    }

    /**
     * 设置LayoutManager
     *
     * @param manager
     */
    public void setLayoutManager(RecyclerView.LayoutManager manager) {
        this.mRecyclerView.setLayoutManager(manager);
    }

    /**
     * 设置LayoutParmas
     *
     * @param pramas
     */
    public void setRLayoutPramas(RecyclerView.LayoutParams pramas) {
        this.mRecyclerView.setLayoutParams(pramas);
    }

    /**
     * 添加刷新的View
     *
     * @param mRefreshView
     */
    public void addRefreshView(View mRefreshView) {
        Log.e("ScroolWrapRecycler", "mRefreshView:" + mRefreshView);
        if (this.mRefreshView != null) {
            this.mRecyclerView.removeFirstHeadView();
        }
        this.mRefreshView = mRefreshView;
        addHeadView(this.mRefreshView);
    }

    /**
     * 添加加载的View
     *
     * @param mLoadMoreView
     */
    public void addLoadMoreView(View mLoadMoreView) {
        if (this.mLoadMoreView != null) {
            this.mRecyclerView.removeLastFootView(foorIndex);
        }
        this.mLoadMoreView = mLoadMoreView;
        this.foorIndex = this.mRecyclerView.getmFootCouListInfo().size(); //不需要-1  应为这个时候新的foot还没有添加进去
        addFootView(this.mLoadMoreView);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                break;
            case MotionEvent.ACTION_MOVE:
                break;
            case MotionEvent.ACTION_CANCEL:
                break;
            case MotionEvent.ACTION_UP:
                break;
        }
        return mRecyclerView.dispatchTouchEvent(ev);
    }
}
