package com.dasu.customrecyclerview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.view.Gravity;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dasu.recyclerlibrary.CustomAdapter;
import com.dasu.recyclerlibrary.CustomRecyclerView;
import com.dasu.recyclerlibrary.ICustomClickListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private CustomRecyclerView mCustomRv;
    private List<String> mData;
    private CustomRecyclerView mcustomrv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        this.mData = new ArrayList<>();
        this.mCustomRv = findViewById(R.id.m_customrv);
        mCustomRv.setLayoutManager(new LinearLayoutManager(this));
        RvAdapter rvAdapter = new RvAdapter(mData, this);
        mCustomRv.addHeadView(getTextView("头部9527", Color.parseColor("#CA66F0")));
        mCustomRv.addHeadView(getTextView("头部17131", Color.parseColor("#A6A5F1")));
        mCustomRv.addFootView(getTextView("尾部9527", Color.parseColor("#05F066")));
        mCustomRv.addFootView(getTextView("尾部17131", Color.parseColor("#90C56F")));
        mCustomRv.addFootView(getTextView("尾部17131", Color.parseColor("#90C56F")));
        mCustomRv.addFootView(getTextView("尾部17131", Color.parseColor("#856fc2")));
        mCustomRv.addFootView(getTextView("尾部17131", Color.parseColor("#2cf0bc")));
        mCustomRv.setAdapter(rvAdapter);
        mCustomRv.setCustomClickListener(new ICustomClickListener() {
            @Override
            public void onClick(View view, int position, int type, Object... data) {
            }

            @Override
            public void onLongClick(View view, int position, int type, Object... data) {

            }
        });
        /**
         * 添加数据源
         */
        for (int i = 0; i < 50; i++) {
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
        tv.setPadding(0, 20, 0, 20);
        return tv;
    }
}
