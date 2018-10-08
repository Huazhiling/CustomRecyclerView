package com.dasu.recyclerlibrary;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
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

    /**
     * 添加HeadView的方法
     *
     * @param view
     */
    public void addHeadView(View view) {
        this.addHeadView(view, 0);
    }

    public void addHeadView(View view, int index) {
        headCount++;
        setHeadViewConfig(view, ViewConfig.HEADVIEW, headCount, 100000);
        if (mAdapter != null) {
            if (!(mAdapter instanceof CustomAdapter)) {
                wrapHeadAdapter();
            }
        }
    }

    public void addFootView(View view) {
        this.addFootView(view, 0);
    }

    public void addFootView(View view, int index) {
        footCount++;
        setFootViewConfig(view, ViewConfig.FOOTVIEW, footCount, 100000);
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
        mAdapter = new CustomAdapter(mHeadCouListInfo, mFootCouListInfo, mAdapter, mContext);
    }

    @Override
    public void setAdapter(@Nullable Adapter adapter) {
        if (mHeadCouListInfo.size() > 0 || mFootCouListInfo.size() > 0) {
            mAdapter = new CustomAdapter(mHeadCouListInfo, mFootCouListInfo, adapter, mContext);
        } else {
            mAdapter = adapter;
        }
        super.setAdapter(mAdapter);
    }

    /**
     * 配置头部view的信息
     *
     * @param view
     * @param type
     * @param count
     * @param headCount
     */
    private void setHeadViewConfig(View view, String type, int count, int headCount) {
        ViewConfig viewConfig = new ViewConfig();
        viewConfig.setTag(view.getClass() + type + count);
        viewConfig.setType(headCount);
        viewConfig.setView(R.layout.item_head_foot_parent);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        viewConfig.setContentView(view);
        mHeadCouListInfo.add(viewConfig);
    }

    /**
     * 配置尾部view的信息
     *
     * @param view
     * @param type
     * @param count
     * @param headCount
     */
    private void setFootViewConfig(View view, String type, int count, int headCount) {
        ViewConfig viewConfig = new ViewConfig();
        viewConfig.setTag(view.getClass() + type + count);
        viewConfig.setType(headCount);
        viewConfig.setView(R.layout.item_head_foot_parent);
        ViewGroup parent = (ViewGroup) view.getParent();
        if (parent != null) {
            parent.removeView(view);
        }
        viewConfig.setContentView(view);
        mFootCouListInfo.add(viewConfig);
    }

    public CustomAdapter getHeadAndFootAdapter() {
        return (CustomAdapter) mAdapter;
    }

    public void setCustomClickListener(ICustomClickListener customClickListener) {
        getHeadAndFootAdapter().setCustomClickListener(customClickListener);
    }
}
