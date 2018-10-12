package com.dasu.recyclerlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Adapter;

import java.util.ArrayList;

public class CustomRecyclerView extends RecyclerView {
    private ArrayList<ViewConfig> mHeadCouListInfo; //保存头部的view
    private ArrayList<ViewConfig> mFootCouListInfo; //保存尾部的view
    private int headCount;  //记录head的个数
    private int footCount;  //记录foot的个数
    private Adapter mAdapter; //adapter，可能是customadapter， 可能是自定义adapter
    private Context mContext;
    private boolean isRefresh;
    private boolean isLoadMore;
    private View mRefreshView;
    private View mLoadMoreView;

    public CustomRecyclerView(@NonNull Context context) {
        this(context, null);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public CustomRecyclerView(@NonNull Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init(context);
    }

    private void init(Context context) {
        mHeadCouListInfo = new ArrayList<>();
        mFootCouListInfo = new ArrayList<>();
        mContext = context;
    }


    public void addRefreshView(View mRefreshView) {
        if (this.mRefreshView != null) {
            return;
        }
        this.mRefreshView = mRefreshView;
        addHeadView(this.mRefreshView);
    }

    public void addLoadMoreView(View mLoadMoreView) {
        if (this.mLoadMoreView != null) {
            return;
        }
        this.mLoadMoreView = mLoadMoreView;
        addFootView(this.mLoadMoreView);
    }

    /**
     * 添加HeadView的方法
     *
     * @param view
     */

    public void addHeadView(View view) {
        headCount++;
        int index = 0; //默认添加位置为0
        if (mHeadCouListInfo.size() != 0) {
//            View contentView = mHeadCouListInfo.get(0).getContentView();
            if (view.equals(this.mRefreshView)) {
                index = 0;
            } else {
                index = headCount - 1;
            }
        }
        setHeadViewConfig(view, ViewConfig.HEADVIEW, headCount, 100000, index);
        if (mAdapter != null) {
            if (!(mAdapter instanceof CustomAdapter)) {
                wrapHeadAdapter();
            }
        }
    }

    public void addFootView(View view) {
        footCount++;
        int index = 0;
        if (mFootCouListInfo.size() != 0) {
            View contentView = mFootCouListInfo.get(mFootCouListInfo.size() - 1).getContentView();
            if (contentView.equals(this.mLoadMoreView)) {
                index = mFootCouListInfo.size() - 1;
            } else {
                index = mFootCouListInfo.size();
            }
        }
        setFootViewConfig(view, ViewConfig.FOOTVIEW, footCount, 100000, index);
        if (mAdapter != null) {
            if (!(mAdapter instanceof CustomAdapter)) {
                wrapHeadAdapter();
            }
        }
    }

    /**
     * 将adapter构建为customadapter用来填充头部尾部布局
     */
    private void wrapHeadAdapter() {
        mAdapter = new CustomAdapter(mHeadCouListInfo, mFootCouListInfo, mAdapter, mContext, this);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (mHeadCouListInfo.size() > 0 || mFootCouListInfo.size() > 0) {
            mAdapter = new CustomAdapter(mHeadCouListInfo, mFootCouListInfo, adapter, mContext, this);
        } else {
            mAdapter = adapter;
        }
        /**
         * 设置头尾的两个缓存为size  变相解决复用问题
         */
        getRecycledViewPool().setMaxRecycledViews(ViewConfig.FOOTVIEW_TYPE,mFootCouListInfo.size());
        getRecycledViewPool().setMaxRecycledViews(ViewConfig.HEADVIEW_TYPE,mHeadCouListInfo.size());
        super.setAdapter(mAdapter);
    }

    /**
     * 配置头部view的信息
     *
     * @param view
     * @param type
     * @param count
     * @param headCount
     * @param index
     */
    private void setHeadViewConfig(View view, String type, int count, int headCount, int index) {
        ViewConfig viewConfig = new ViewConfig();
        viewConfig.setTag(view.getClass() + type + count);
        viewConfig.setType(headCount);
        viewConfig.setView(R.layout.item_head_foot_parent);
        ViewGroup mHeadParent = (ViewGroup) view.getParent();
        if (mHeadParent != null) {
            mHeadParent.removeView(view);
        }
        viewConfig.setContentView(view);
        mHeadCouListInfo.add(index, viewConfig);
    }

    /**
     * 配置尾部view的信息
     *
     * @param view
     * @param type
     * @param count
     * @param headCount
     */
    private void setFootViewConfig(View view, String type, int count, int headCount, int index) {
        ViewConfig viewConfig = new ViewConfig();
        viewConfig.setTag(view.getClass() + type + count);
        viewConfig.setType(headCount);
        viewConfig.setView(R.layout.item_head_foot_parent);
        ViewGroup mFootParent = (ViewGroup) view.getParent();
        if (mFootParent != null) {
            mFootParent.removeView(view);
        }
        viewConfig.setContentView(view);
        mFootCouListInfo.add(index, viewConfig);
    }

    public CustomAdapter getHeadAndFootAdapter() {
        return (CustomAdapter) mAdapter;
    }

    public void setCustomClickListener(ICustomClickListener customClickListener) {
        getHeadAndFootAdapter().setCustomClickListener(customClickListener);
    }

    /**
     * 设置是否允许刷新
     *
     * @param refresh
     */
    public void setRefresh(boolean refresh) {
        this.isRefresh = refresh;
    }

    /**
     * 设置是否允许加载
     *
     * @param loadMore
     */
    public void setLoadMore(boolean loadMore) {
        this.isLoadMore = loadMore;
    }

    /**
     * 同时设置
     *
     * @param isRefALoad
     */
    public void setRefreshAndLoadMore(boolean isRefALoad) {
        this.isRefresh = isRefALoad;
        this.isLoadMore = isRefALoad;
    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {
        return super.onTouchEvent(e);
    }
}
