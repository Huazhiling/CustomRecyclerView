package com.dasu.recyclerlibrary;

import android.content.Context;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;


import java.util.ArrayList;
import java.util.List;

public class CustomAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder>{
    private List<ViewConfig> headConfig;
    private List<ViewConfig> footConfig;
    private ArrayList<ViewConfig> EMPTY_LIST = new ArrayList<>();
    private LayoutInflater inflater;
    private RecyclerView.Adapter mAdapter;
    private int headcount = 0;
    private int footcount = 0;
    private Context mContext;
    private ICustomClickListener customClickListener;

    public CustomAdapter(List<ViewConfig> headConfig, List<ViewConfig> footConfig, RecyclerView.Adapter mAdapter, Context mContext) {
        this.mAdapter = mAdapter;
        this.inflater = LayoutInflater.from(mContext);
        this.mContext = mContext;
        if (headConfig == null) {
            this.headConfig = EMPTY_LIST;
        } else {
            this.headConfig = headConfig;
        }
        if (footConfig == null) {
            this.footConfig = EMPTY_LIST;
        } else {
            this.footConfig = footConfig;
        }
    }

    /**
     * 设置监听事件
     *
     * @param customClickListener
     */
    public void setCustomClickListener(ICustomClickListener customClickListener) {
        this.customClickListener = customClickListener;
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    @NonNull
    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int index) {
        if (index == ViewConfig.HEADVIEW_TYPE) {
            FrameLayout contentView = (FrameLayout) inflater.inflate(R.layout.item_head_foot_parent, viewGroup, false);
            View cView = headConfig.get(headcount++).getContentView();
            ViewGroup vg = (ViewGroup) cView.getParent();
            if (vg != null) {
                vg.removeView(cView);
            }
            contentView.addView(cView);
            return new CustomViewHolder(contentView);
        } else if (index == ViewConfig.FOOTVIEW_TYPE) {
            FrameLayout contentView = (FrameLayout) inflater.inflate(R.layout.item_head_foot_parent, viewGroup, false);
            View cView = footConfig.get(footcount++).getContentView();
            ViewGroup vg = (ViewGroup) cView.getParent();
            if (vg != null) {
                vg.removeView(cView);
            }
            contentView.addView(cView);
            return new CustomViewHolder(contentView);
        } else {
            return mAdapter.onCreateViewHolder(viewGroup, index);
        }
    }

    @Override
    public void onBindViewHolder(@NonNull RecyclerView.ViewHolder viewHolder, final int position) {
        CustomViewHolder customViewHolder;
        if (viewHolder instanceof CustomViewHolder) {
            customViewHolder = (CustomViewHolder) viewHolder;
            View mParent = customViewHolder.mParent;
            //设置view的点击事件
            if (mParent instanceof ViewGroup) {
                ViewGroup parentGroup = (ViewGroup) mParent;
                int childCount = parentGroup.getChildCount();
                for (int i = 0; i < childCount; i++) {
                    parentGroup.getChildAt(i).setOnClickListener(new View.OnClickListener() {
                        @Override
                        public void onClick(View v) {
                            if (customClickListener != null) {
                                if (position < getHeadSize()) {
                                    customClickListener.onClick(v, position, 0);
                                } else {
                                    customClickListener.onClick(v, position - getHeadSize() - mAdapter.getItemCount(), 1);
                                }
                            }
                        }
                    });
                }
            }
            mParent.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (position < getHeadSize()) {
                        customClickListener.onClick(v, position, 0);
                    } else {
                        customClickListener.onClick(v, position - getHeadSize() - mAdapter.getItemCount(), 1);
                    }
                }
            });
        } else {
            mAdapter.onBindViewHolder(viewHolder, position - getHeadSize());
        }
    }

    @Override
    public int getItemCount() {
        return mAdapter != null
                ? headConfig.size() + mAdapter.getItemCount() + footConfig.size()
                : headConfig.size() + footConfig.size();
    }

    public class CustomViewHolder extends RecyclerView.ViewHolder {
        View mParent;

        public CustomViewHolder(@NonNull View itemView) {
            super(itemView);
            mParent = itemView;
        }
    }

    @Override
    public int getItemViewType(int position) {
        int headSize = getHeadSize();
        int adapterCount = getItemCount();//获取实际的个数
        if (position < headSize) {
            return ViewConfig.HEADVIEW_TYPE;
        } else if (position >= headSize + mAdapter.getItemCount() && position < adapterCount) {
            return ViewConfig.FOOTVIEW_TYPE;
        }
        return -1;
    }

    public int getHeadSize() {
        return headConfig.size();
    }

    public int getFootSize() {
        return footConfig.size();
    }
}