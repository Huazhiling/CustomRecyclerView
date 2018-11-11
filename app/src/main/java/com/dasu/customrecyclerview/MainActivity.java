package com.dasu.customrecyclerview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.util.DiffUtil;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;


import com.dasu.recyclerlibrary.listener.ICustomScrollListener;
import com.dasu.recyclerlibrary.listener.IScrollListener;
import com.dasu.recyclerlibrary.ui.ScrollWrapRecycler;

import java.util.ArrayList;
import java.util.List;

import static com.dasu.recyclerlibrary.ui.ScrollWrapRecycler.SCROLL_RL_FAILD;
import static com.dasu.recyclerlibrary.ui.ScrollWrapRecycler.SCROLL_RL_LOADING;
import static com.dasu.recyclerlibrary.ui.ScrollWrapRecycler.SCROLL_RL_NOTMET;
import static com.dasu.recyclerlibrary.ui.ScrollWrapRecycler.SCROLL_RL_NOTSLIPPING;
import static com.dasu.recyclerlibrary.ui.ScrollWrapRecycler.SCROLL_RL_REFRESH;
import static com.dasu.recyclerlibrary.ui.ScrollWrapRecycler.SCROLL_RL_SUCCESS;


public class MainActivity extends AppCompatActivity {
    private ScrollWrapRecycler mCustomRv;
    private RecyclerView mChildRv;
    private List<UserInfo> mData;
    private TextView refresh;
    private TextView load;
    private Button mChange;
    private RvAdapter rvAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mData = new ArrayList<>();
        this.mCustomRv = findViewById(R.id.m_customrv);
        this.mChildRv = findViewById(R.id.child_rv);
        this.mChange = findViewById(R.id.change);
        mCustomRv.setLayoutManager(new LinearLayoutManager(this));
        rvAdapter = new RvAdapter(mData, this);
//        mCustomRv.addRefreshView(getTextView("刷新1", Color.parseColor("#CCCCCC")));
//        mCustomRv.addRefreshView(getTextView("刷新2", Color.parseColor("#CCCCCC")));
        mCustomRv.addHeadView(getTextView("头部1", Color.parseColor("#CA66F0")));
        mCustomRv.addHeadView(getTextView("头部2", Color.parseColor("#90C56F")));
        mCustomRv.addHeadView(getTextView("头部4", Color.parseColor("#2cf0bc")));
        mCustomRv.addFootView(getTextView("尾部1", Color.parseColor("#90C56F")));
        mCustomRv.addHeadView(mChildRv,false);
        mCustomRv.addFootView(getTextView("尾部2", Color.parseColor("#90C56F")));
        load = getTextView("加载", Color.parseColor("#CCCCCC"));
        mCustomRv.addLoadMoreView(load);
        mCustomRv.addFootView(getTextView("尾部3", Color.parseColor("#856fc2")));
        mCustomRv.addFootView(getTextView("尾部4", Color.parseColor("#2cf0bc")));
        mCustomRv.addFootView(getTextView("尾部7", Color.parseColor("#856fc2")));
//        mCustomRv.addLoadMoreView(getTextView("加载", Color.parseColor("#CCCCCC")));
        mCustomRv.setAdapter(rvAdapter);
        mChildRv.setAdapter(rvAdapter);
//        refresh = getTextView("刷新3", Color.parseColor("#CCCCCC"));
//        mCustomRv.addRefreshView(refresh);
        mCustomRv.setmIScrollListener(new IScrollListener() {
            @Override
            public void loadMore() {
                for (int i = 100; i < 105; i++) {
                    mData.add(new UserInfo(i, "数据源头--->" + i, i * 15));
                }
                rvAdapter.notifyDataSetChanged();
                //模拟加载成功
                mCustomRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomRv.setLoadMoreStatus(SCROLL_RL_SUCCESS);
                    }
                }, 2000);
            }

            @Override
            public void refresh() {
                //模拟刷新成功
                mCustomRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomRv.setRefreshStatus(SCROLL_RL_SUCCESS);
                    }
                }, 2000);
            }
        });
        mCustomRv.setmCustomScrollListener(new ICustomScrollListener() {
            @Override
            public void scrollRefreshState(int state) {
                switch (state) {
                    case SCROLL_RL_NOTSLIPPING:
                        refresh.setText("等待刷新");
                        break;
                    case SCROLL_RL_NOTMET:
                        refresh.setText("下拉刷新");
                        break;
                    case SCROLL_RL_REFRESH:
                        refresh.setText("松开刷新");
                        break;
                    case SCROLL_RL_LOADING:
                        refresh.setText("正在刷新...");
                        break;
                    case SCROLL_RL_SUCCESS:
                        refresh.setText("刷新成功");
                        break;
                    case SCROLL_RL_FAILD:
                        refresh.setText("刷新失败");
                        break;
                }
            }

            @Override
            public void scrollLoadMoreState(int state) {
                switch (state) {
                    case SCROLL_RL_NOTSLIPPING:
                        load.setText("加载更多");
                        break;
                    case SCROLL_RL_NOTMET:
                        load.setText("上拉加载");
                        break;
                    case SCROLL_RL_REFRESH:
                        load.setText("松开立即加载");
                        break;
                    case SCROLL_RL_LOADING:
                        load.setText("正在加载...");
                        break;
                    case SCROLL_RL_SUCCESS:
                        load.setText("加载成功");
                        break;
                    case SCROLL_RL_FAILD:
                        load.setText("加载失败");
                        break;
                }
            }

            @Override
            public void refresh() {
                //模拟刷新成功
                mCustomRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomRv.setRefreshStatus(SCROLL_RL_SUCCESS);
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {
                for (int i = 100; i < 105; i++) {
                    mData.add(new UserInfo(i, "数据源头--->" + i, i * 15));
                }
                rvAdapter.notifyDataSetChanged();
                //模拟加载成功
                mCustomRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomRv.setLoadMoreStatus(SCROLL_RL_SUCCESS);
                    }
                }, 2000);
            }
        });
        /**
         * 添加数据源
         */
        for (int i = 0; i < 30; i++) {
            UserInfo userInfo = new UserInfo(i, "数据源--->" + i, i * 18);
            mData.add(userInfo);
        }
        rvAdapter.notifyDataSetChanged();
        mCustomRv.setRefresh();
    }

    public TextView getTextView(String content, int color) {
        TextView tv = new TextView(this);
        ViewGroup.LayoutParams layoutParams = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        tv.setGravity(Gravity.CENTER);
        tv.setBackgroundColor(color);
        tv.setText(content);
        tv.setTextSize(15);
        tv.setLayoutParams(layoutParams);
        tv.setPadding(0, 30, 0, 30);
        tv.setId(R.id.m_refresh_hint);
        return tv;
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.change:
                //利用Diff刷新例子，实际项目中可以根据需要更改数据源
                List<UserInfo> mData = new ArrayList<>(this.mData);
                for (int i = 0; i < 15; i++) {
                    this.mData.set(i, new UserInfo(i, "数据--->" + i, i * 15));
                }
                DiffUtil.calculateDiff(new DiffUtils(mData, this.mData)).dispatchUpdatesTo(rvAdapter);
                break;
        }

    }
}
