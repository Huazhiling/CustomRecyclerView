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
import com.dasu.recyclerlibrary.listener.IScrollListener;
import com.dasu.recyclerlibrary.ui.ScrollWrapRecycler;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private ScrollWrapRecycler mCustomRv;
    private List<String> mData;

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
//        mCustomRv.addRefreshView(getTextView("刷新3", Color.parseColor("#CCCCCC")));
        mCustomRv.addHeadView(getTextView("头部1", Color.parseColor("#CA66F0")));
        mCustomRv.addHeadView(getTextView("头部2", Color.parseColor("#90C56F")));
        mCustomRv.addHeadView(getTextView("头部3", Color.parseColor("#856fc2")));
        mCustomRv.addHeadView(getTextView("头部4", Color.parseColor("#2cf0bc")));
        mCustomRv.addFootView(getTextView("尾部1", Color.parseColor("#90C56F")));
        mCustomRv.addFootView(getTextView("尾部2", Color.parseColor("#90C56F")));
        final TextView load = getTextView("加载", Color.parseColor("#CCCCCC"));
        mCustomRv.addLoadMoreView(load);
        mCustomRv.addFootView(getTextView("尾部3", Color.parseColor("#856fc2")));
        mCustomRv.addFootView(getTextView("尾部4", Color.parseColor("#2cf0bc")));
        mCustomRv.addFootView(getTextView("尾部5", Color.parseColor("#856fc2")));
        mCustomRv.addFootView(getTextView("尾部6", Color.parseColor("#2cf0bc")));
        mCustomRv.addFootView(getTextView("尾部7", Color.parseColor("#856fc2")));
        mCustomRv.addLoadMoreView(getTextView("加载", Color.parseColor("#CCCCCC")));
        mCustomRv.setAdapter(rvAdapter);
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
            public void loadmore() {

            }

            @Override
            public void refresh() {
                mCustomRv.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        mCustomRv.setRefreshStatus(ScrollWrapRecycler.SCROLL_REFRESH_SUCCESS);
                    }
                }, 2000);
            }
        });
        /**
         * 添加数据源
         */
        for (int i = 0; i < 20; i++) {
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
        return tv;
    }
}
