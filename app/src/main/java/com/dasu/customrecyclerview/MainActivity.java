package com.dasu.customrecyclerview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;


import com.dasu.recyclerlibrary.listener.ICustomClickListener;
import com.dasu.recyclerlibrary.listener.ICustomScrollListener;
import com.dasu.recyclerlibrary.listener.IScrollListener;
import com.dasu.recyclerlibrary.ui.ScrollWrapRecycler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ScrollWrapRecycler mCustomRv;
    private List<String> mData;
    private TextView refresh;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mData = new ArrayList<>();
        this.mCustomRv = findViewById(R.id.m_customrv);
        mCustomRv.setLayoutManager(new LinearLayoutManager(this));
        RvAdapter rvAdapter = new RvAdapter(mData, this);
//        mCustomRv.addRefreshView(getTextView("刷新1", Color.parseColor("#CCCCCC")));
//        mCustomRv.addRefreshView(getTextView("刷新2", Color.parseColor("#CCCCCC")));
        mCustomRv.addHeadView(getTextView("头部1", Color.parseColor("#CA66F0")));
        mCustomRv.addHeadView(getTextView("头部2", Color.parseColor("#90C56F")));
        mCustomRv.addHeadView(getTextView("头部4", Color.parseColor("#2cf0bc")));
        mCustomRv.addFootView(getTextView("尾部1", Color.parseColor("#90C56F")));
        mCustomRv.addFootView(getTextView("尾部2", Color.parseColor("#90C56F")));
        final TextView load = getTextView("加载", Color.parseColor("#CCCCCC"));
        mCustomRv.addLoadMoreView(load);
        mCustomRv.addFootView(getTextView("尾部3", Color.parseColor("#856fc2")));
        mCustomRv.addFootView(getTextView("尾部4", Color.parseColor("#2cf0bc")));
        mCustomRv.addFootView(getTextView("尾部7", Color.parseColor("#856fc2")));
//        mCustomRv.addLoadMoreView(getTextView("加载", Color.parseColor("#CCCCCC")));
        mCustomRv.setAdapter(rvAdapter);
//        refresh = getTextView("刷新3", Color.parseColor("#CCCCCC"));
//        mCustomRv.addRefreshView(refresh);
        mCustomRv.setCustomClickListener(new ICustomClickListener() {
            @Override
            public void onClick(View view, int position, int type, Object... data) {
                Log.e("CustomAdapter", "position:" + position);
            }

            @Override
            public void onLongClick(View view, int position, int type, Object... data) {

            }
        });
        mCustomRv.setmIScrollListener(new IScrollListener() {
            @Override
            public void loadMore() {

            }

            @Override
            public void refresh() {
                //模拟刷新成功
                mCustomRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomRv.setRefreshStatus(ScrollWrapRecycler.SCROLL_RL_SUCCESS);
                    }
                }, 2000);
            }
        });
        mCustomRv.setmCustomScrollListener(new ICustomScrollListener() {
            @Override
            public void scrollRefreshState(int state) {
                switch (state) {
                    case ScrollWrapRecycler.SCROLL_RL_NOTSLIPPING:
                        refresh.setText("等待刷新");
                        break;
                    case ScrollWrapRecycler.SCROLL_RL_NOTMET:
                        refresh.setText("下拉刷新");
                        break;
                    case ScrollWrapRecycler.SCROLL_RL_REFRESH:
                        refresh.setText("松开刷新");
                        break;
                    case ScrollWrapRecycler.SCROLL_RL_LOADING:
                        refresh.setText("正在刷新...");
                        break;
                    case ScrollWrapRecycler.SCROLL_RL_SUCCESS:
                        refresh.setText("刷新成功");
                        break;
                    case ScrollWrapRecycler.SCROLL_RL_FAILD:
                        refresh.setText("刷新失败");
                        break;
                }
            }

            @Override
            public void scrollLoadMoreState(int state) {

            }

            @Override
            public void refresh() {
                Log.e("MainActivity", "模拟刷新");
                //模拟刷新成功
                mCustomRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomRv.setRefreshStatus(ScrollWrapRecycler.SCROLL_RL_SUCCESS);
                    }
                }, 2000);
            }

            @Override
            public void loadMore() {

            }
        });
        /**
         * 添加数据源
         */
        for (int i = 0; i < 30; i++) {
            mData.add("mData --- " + i);
        }
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
}
