package com.dasu.customrecyclerview;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.List;

public class RvAdapter extends RecyclerView.Adapter<RvAdapter.RvViewHolder> {
    private List<String> mData;
    private Context context;
    private LayoutInflater inflater;

    public RvAdapter(List<String> mData, Context context) {
        this.mData = mData;
        this.context = context;
        this.inflater = LayoutInflater.from(context);
    }

    @NonNull
    @Override
    public RvViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        return new RvViewHolder(inflater.inflate(R.layout.item_tv, viewGroup,false));
    }

    @Override
    public void onBindViewHolder(@NonNull RvViewHolder rvViewHolder, int i) {
        rvViewHolder.tv.setText(mData.get(i));
    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    public class RvViewHolder extends RecyclerView.ViewHolder {
        TextView tv;
        public RvViewHolder(@NonNull View itemView) {
            super(itemView);
            tv = (TextView) itemView;
        }
    }

}
