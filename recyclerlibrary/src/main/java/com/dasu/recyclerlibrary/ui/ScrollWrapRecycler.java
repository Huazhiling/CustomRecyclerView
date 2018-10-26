package com.dasu.recyclerlibrary.ui;

import android.animation.PropertyValuesHolder;
import android.animation.ValueAnimator;
import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Scroller;
import android.widget.TextView;

import com.dasu.recyclerlibrary.CustomAdapter;
import com.dasu.recyclerlibrary.R;
import com.dasu.recyclerlibrary.listener.ICustomClickListener;
import com.dasu.recyclerlibrary.listener.ICustomScrollListener;
import com.dasu.recyclerlibrary.listener.IScrollListener;

public class ScrollWrapRecycler extends LinearLayout {
    public static final int SCROLL_NOTSLIPPING = 0;
    public static final int SCROLL_NOTMET = 1;
    public static final int SCROLL_RELEASH = 2;
    public static final int SCROLL_LOADING = 3;
    public static final int SCROLL_REFRESH_SUCCESS = 4;
    public static final int SCROLL_REFRESH_FAILD = 5;
    private int scrollStatus;

    private CustomRecyclerView mRecyclerView;
    private View mRefreshView;
    private View mLoadMoreView;
    private int foorIndex; //每次添加foot都要赋值  防止下一次找不到
    private float start_X, start_Y = 0;
    private Scroller mScroll;
    private boolean isUseSelfRefresh = false;
    private boolean isUseSelfLoadMore = false;
    private ICustomScrollListener mCustomScrollListener;
    private IScrollListener mIScrollListener;
    private TextView mRefreshHint;
    private ValueAnimator animator;

    public ScrollWrapRecycler(Context context) {
        this(context, null);
    }

    private Context mContext;

    public ScrollWrapRecycler(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ScrollWrapRecycler(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context);
    }

    private void init(Context context) {
        this.mRecyclerView = new CustomRecyclerView(context);
        this.mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.mScroll = new Scroller(context);
        this.mContext = context;
        addView(mRecyclerView);
        animator = new ValueAnimator();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                Log.e("ScroolWrapRecycler", "animatedValue:" + animatedValue);
                Log.e("ScroolWrapRecycler", "-mRefreshView.getMeasuredHeight():" + -mRefreshView.getMeasuredHeight());
                if (animatedValue < -mRefreshView.getMeasuredHeight()) {
                    animatedValue = mRefreshView.getMeasuredHeight();
                }
                getMarginParams().topMargin = (int) animatedValue;
                mRecyclerView.setLayoutParams(getMarginParams());
            }
        });
    }

    public void setmCustomScrollListener(ICustomScrollListener mCustomScrollListener) {
        if (mIScrollListener != null) {
            throw new RuntimeException("不能同时设置两个监听器，请移除IScrollListener");
        }
        this.mCustomScrollListener = mCustomScrollListener;
    }

    public void setmIScrollListener(IScrollListener mIScrollListener) {
        if (mCustomScrollListener != null) {
            throw new RuntimeException("不能同时设置两个监听器，请移除ICustomScrollListener");
        }
        this.mIScrollListener = mIScrollListener;
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
        View refreshView = null;
        if (mRefreshView == null) {
            refreshView = LayoutInflater.from(mContext).inflate(R.layout.item_defalut_refresh_view, null);
            mRefreshHint = (TextView) refreshView.findViewById(R.id.m_refresh_hint);
        }
        addRefreshView(refreshView);
        isUseSelfRefresh = false;
        RecyclerView.MarginLayoutParams marginParams = getMarginParams();
        mRefreshView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        marginParams.topMargin = -mRefreshView.getMeasuredHeight();
        setRLayoutPramas(marginParams);
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
    public void setRLayoutPramas(ViewGroup.LayoutParams pramas) {
        this.mRecyclerView.setLayoutParams(pramas);
    }

    /**
     * 添加刷新的View
     *
     * @param mRefreshView
     */
    protected void addRefreshView(View mRefreshView) {
        this.isUseSelfRefresh = true;
        if (this.mRefreshView != null) {
            this.mRecyclerView.removeFirstHeadView();
        }
        this.mRefreshView = mRefreshView;
        this.mRecyclerView.addRefreshView(this.mRefreshView);
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

    public void setRefreshStatus(int status) {
        scrollState(status);
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setScrollAnimation();
            }
        }, 1500);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getAction()) {
            case MotionEvent.ACTION_DOWN:
                start_X = (int) ev.getX();
                start_Y = (int) ev.getY();
                break;
            case MotionEvent.ACTION_MOVE:
                float move_X = ev.getX();
                float move_Y = ev.getY();
                LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
                if (mRefreshView != null && getMarginParams().topMargin <= -mRefreshView.getMeasuredHeight() && layoutManager.findFirstVisibleItemPosition() != 0 && move_Y - start_Y > 0) {
                    return super.dispatchTouchEvent(ev);
                    //下拉刷新的判断
                } else if (mRefreshView != null && getMarginParams().topMargin >= -mRefreshView.getMeasuredHeight() && layoutManager.findFirstVisibleItemPosition() == 0 && move_Y - start_Y > 0) {
                    float phaseDiff = move_Y - start_Y;
                    updateHead((float) (phaseDiff / 1.5));
                    start_Y = move_Y;
                    return true;
                    //如果getMarginParams().topMargin > -refresh的高度  证明下拉了 需要先还原  还原下拉刷新的margin
                } else if (mRefreshView != null && getMarginParams().topMargin > -mRefreshView.getMeasuredHeight() && layoutManager.findFirstVisibleItemPosition() < 1 && move_Y - start_Y < 0) {
                    float phaseDiff = move_Y - start_Y;
                    updateHead((float) (phaseDiff * 1.5));
                    start_Y = move_Y;
                    return true;
                } else if (mRefreshView != null && getMarginParams().topMargin <= -mRefreshView.getMeasuredHeight() && layoutManager.findFirstVisibleItemPosition() != 0 && move_Y - start_Y < 0) {
                    getMarginParams().topMargin = -mRefreshView.getMeasuredHeight();
                    return super.dispatchTouchEvent(ev);
                } else {
                    return super.dispatchTouchEvent(ev);
                }
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_UP:
                if (mRefreshView != null) {
                    if (scrollStatus == SCROLL_RELEASH) {
                        refresh();
                        scrollState(SCROLL_LOADING);
                    } else {
                        setScrollAnimation();
                    }
                }

                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    private void refresh() {
        if (mCustomScrollListener != null) {
            mCustomScrollListener.refresh();
        } else if (mIScrollListener != null) {
            mIScrollListener.refresh();
        } else {
            Log.w(getClass().getName(), "请设置回调监听器");
        }
    }

    private void updateHead(float phaseDiff) {
        RecyclerView.MarginLayoutParams marginParams = getMarginParams();
        int scrollMax = marginParams.topMargin + (int) (phaseDiff);
        if (phaseDiff > 0) {
            scrollMax = scrollMax > mRefreshView.getMeasuredHeight() * 3 ? mRefreshView.getMeasuredHeight() * 3 : scrollMax;
        } else {
            scrollMax = scrollMax < -mRefreshView.getMeasuredHeight() ? -mRefreshView.getMeasuredHeight() : scrollMax;
        }
        Log.e("ScroolWrapRecycler", "scrollMax:" + scrollMax);
        marginParams.topMargin = scrollMax;
        setRLayoutPramas(marginParams);
        if (isUseSelfRefresh) {
            if (mCustomScrollListener != null) {
                if (scrollMax < mRefreshView.getMeasuredHeight() * 1.5) {
                    mCustomScrollListener.scrollState(SCROLL_NOTMET);
                } else if (scrollMax > mRefreshView.getMeasuredHeight() * 1.5) {
                    mCustomScrollListener.scrollState(SCROLL_RELEASH);
                }
//                mCustomScrollListener.scrollState(scrollMax > mRefreshView.getMeasuredHeight());
            }
        } else {
            if (scrollMax < mRefreshView.getMeasuredHeight() * 1.5) {
                this.scrollState(SCROLL_NOTMET);
            } else if (scrollMax > mRefreshView.getMeasuredHeight() * 1.5) {
                this.scrollState(SCROLL_RELEASH);
            }
        }
    }

    /**
     * 更改头部状态
     *
     * @param scrollReleash
     */
    public void scrollState(int scrollReleash) {
        switch (scrollReleash) {
            case SCROLL_NOTSLIPPING:
                mRefreshHint.setText("等待刷新");
                break;
            case SCROLL_NOTMET:
                mRefreshHint.setText("下拉刷新");
                break;
            case SCROLL_RELEASH:
                mRefreshHint.setText("松开刷新");
                break;
            case SCROLL_LOADING:
                mRefreshHint.setText("正在刷新...");
                break;
            case SCROLL_REFRESH_SUCCESS:
                mRefreshHint.setText("刷新成功");
                break;
            case SCROLL_REFRESH_FAILD:
                mRefreshHint.setText("刷新失败");
                break;
        }
        this.scrollStatus = scrollReleash;
    }

    private RecyclerView.MarginLayoutParams getMarginParams() {
        return (MarginLayoutParams) mRecyclerView.getLayoutParams();
    }

    public void setScrollAnimation() {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("", getMarginParams().topMargin, -mRefreshView.getMeasuredHeight());
        animator.setValues(valuesHolder);
        animator.setDuration(300);
        animator.start();
    }

    /**
     * ====================================================================================
     * 以下提供的方法均调用的是CustomAdapter的方法
     * ====================================================================================
     */
    public void notifyDataSetChanged() {
        this.getAdapter().notifyDataSetChanged();
    }

    public void notifyItemChanged(int position) {
        this.getAdapter().notifyItemChanged(position);
    }

    public void notifyItemChanged(int position, Object payload) {
        this.getAdapter().notifyItemChanged(position, payload);
    }

    public void notifyItemInserted(int position) {
        this.getAdapter().notifyItemInserted(position);
    }

    public void notifyItemMoved(int fromPosition, int toPosition) {
        this.getAdapter().notifyItemMoved(fromPosition, toPosition);
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount) {
        this.getAdapter().notifyItemRangeChanged(positionStart, itemCount);
    }

    public void notifyItemRangeChanged(int positionStart, int itemCount, Object payload) {
        this.getAdapter().notifyItemRangeChanged(positionStart, itemCount, payload);
    }

    public void notifyItemRangeInserted(int positionStart, int itemCount) {
        this.getAdapter().notifyItemRangeInserted(positionStart, itemCount);
    }

    public void notifyItemRemoved(int positionStart) {
        this.getAdapter().notifyItemRemoved(positionStart);
    }
}
