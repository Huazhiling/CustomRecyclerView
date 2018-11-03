package com.dasu.recyclerlibrary.ui;

import android.animation.Animator;
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
import android.widget.TextView;

import com.dasu.recyclerlibrary.R;
import com.dasu.recyclerlibrary.listener.ICustomClickListener;
import com.dasu.recyclerlibrary.listener.ICustomScrollListener;
import com.dasu.recyclerlibrary.listener.IScrollListener;

public class ScrollWrapRecycler extends LinearLayout {
    /**
     * 没有滑动
     */
    public static final int SCROLL_RL_NOTSLIPPING = 0;
    /**
     * 正在滑动 但是还没有到可以刷新的指定距离
     */
    public static final int SCROLL_RL_NOTMET = 1;
    /**
     * 松开后刷新
     */
    public static final int SCROLL_RL_REFRESH = 2;
    /**
     * 正在刷新
     */
    public static final int SCROLL_RL_LOADING = 3;
    /**
     * 刷新成功
     */
    public static final int SCROLL_RL_SUCCESS = 4;
    /**
     * 刷新失败
     */
    public static final int SCROLL_RL_FAILD = 5;
    /**
     * 刷新状态
     */
    private int refreshScrollStatus;
    private int loadMoreScrollStatus;
    private int foorIndex; //每次添加foot都要赋值  防止下一次找不到
    private int scroolRemaining = 0;

    private float start_X, start_Y = 0;

    private boolean isUseSelfRefresh = false;
    private boolean isUseSelfLoadMore = false;
    //是否正在刷新或者加载
    private boolean isRefreshing = false;
    private boolean isLoadMored = false;
    //记录当前的刷新滑动状态  如果isRefreshStatus==true，那就是刷新， 如果isLoadMoreStatus==true,那就是加载
    private boolean isRefreshStatus = false;
    private boolean isLoadMoreStatus = false;
    //刷新加载的标志  为true的时候才添加  默认都添加
    private boolean isRefresh = true;
    private boolean isLoadMore = true;
    private boolean isFootViewVisibility = false;
    private boolean isUpdateFoot = false;

    private View mRefreshView;
    private View mLoadMoreView;
    private TextView mRefreshHint;
    private TextView mLoadMoreHint;
    private CustomRecyclerView mRecyclerView;
    private ICustomScrollListener mCustomScrollListener;
    private IScrollListener mIScrollListener;

    private ValueAnimator refreshAnimator;
    private ValueAnimator loadmoreAnimator;

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

    private void init(Context context) {
        setOrientation(VERTICAL);
        this.mRecyclerView = new CustomRecyclerView(context);
        this.mRecyclerView.setLayoutParams(new RecyclerView.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.mContext = context;
        refreshAnimator = new ValueAnimator();
        loadmoreAnimator = new ValueAnimator();
        refreshAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (animatedValue < -mRefreshView.getMeasuredHeight()) {
                    animatedValue = mRefreshView.getMeasuredHeight();
                }
                getMarginParams(mRefreshView).topMargin = (int) animatedValue;
                mRecyclerView.setLayoutParams(getMarginParams(mRefreshView));
            }
        });
        loadmoreAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float animatedValue = (float) animation.getAnimatedValue();
                if (animatedValue < -mLoadMoreView.getMeasuredHeight()) {
                    animatedValue = mLoadMoreView.getMeasuredHeight();
                }
                mLoadMoreView.setVisibility(VISIBLE);
                getMarginParams(mLoadMoreView).bottomMargin = (int) animatedValue;
                mRecyclerView.setLayoutParams(getMarginParams(mLoadMoreView));
            }
        });
        loadmoreAnimator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                Log.e("ScrollWrapRecycler", "动画结束");
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        this.mRecyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                if (!isUpdateFoot) {
                    scroolRemaining += dy;
                }
                Log.e("ScrollWrapRecycler", "scroolRemaining:" + scroolRemaining);
                if (!isFootViewVisibility) {
                    int lastVisibleItemPosition = ((LinearLayoutManager) mRecyclerView.getLayoutManager()).findLastVisibleItemPosition();
                    if (getAdapter() != null) {
                        if (getFullSize() <= lastVisibleItemPosition) {
                            mLoadMoreView.setVisibility(GONE);
                            isFootViewVisibility = false;
                        } else {
                            isFootViewVisibility = true;
                            mLoadMoreView.setVisibility(VISIBLE);
                        }
                    }
                }
            }

            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                Log.e("ScrollWrapRecycler", "newState:" + newState);
//                if (newState == 2) {
//                    mLoadMoreView.setVisibility(GONE);
//                    isFootViewVisibility = false;
//                }
            }
        });
    }

    public void setmCustomScrollListener(ICustomScrollListener mCustomScrollListener) {
        if (mIScrollListener != null) {
            mIScrollListener = null;
        }
        this.mCustomScrollListener = mCustomScrollListener;
    }

    /**
     * 获取RecyclerView实际大小，返回值为HeadSize+content+FootSize-Refresh-Loadmore
     * 不包含刷新和加载的个数返回，如果需要返回全部，调用{#getAdapter().getItemCount()}
     *
     * @return
     */
    public int getFullSize() {
        return getAdapter().getItemCount() - (mRefreshView == null ? 0 : 1) - (mLoadMoreView == null ? 0 : 1);
    }

    public void setmIScrollListener(IScrollListener mIScrollListener) {
        if (mCustomScrollListener != null) {
            mCustomScrollListener = null;
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
        View refreshView;
        View loadmoreView;
        //初始化的时候根据是否需要刷新加载给一个默认
        if (mRefreshView == null && isRefresh) {
            refreshView = LayoutInflater.from(mContext).inflate(R.layout.item_defalut_refresh_view, null);
            mRefreshHint = (TextView) refreshView.findViewById(R.id.m_refresh_hint);
            addRefreshView(refreshView);
        }
        if (mLoadMoreView == null && isLoadMore) {
            loadmoreView = LayoutInflater.from(mContext).inflate(R.layout.item_defalut_loadmore_view, null);
            mLoadMoreHint = (TextView) loadmoreView.findViewById(R.id.m_loadmore_hint);
            addLoadMoreView(loadmoreView);
        }
        isUseSelfRefresh = false;
        isUseSelfLoadMore = false;
        //计算高度并且将刷新的view设置负margin隐藏
//        RecyclerView.MarginLayoutParams marginParams = getMarginParams();
//        mRefreshView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//        mLoadMoreView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
//        marginParams.topMargin = -mRefreshView.getMeasuredHeight();
//        setRLayoutPramas(marginParams);
        addView(mRefreshView);
        addView(mRecyclerView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT, 1));
        addView(mLoadMoreView, new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.mRecyclerView.setAdapter(adapter);
        viewLayout();
    }

    /**
     * 改变View的位置
     */
    private void viewLayout() {
        mRefreshView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        mLoadMoreView.measure(MeasureSpec.UNSPECIFIED, MeasureSpec.UNSPECIFIED);
        getMarginParams(mRefreshView).topMargin = -mRefreshView.getMeasuredHeight();
        getMarginParams(mLoadMoreView).bottomMargin = -mLoadMoreView.getMeasuredHeight();
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
    public void addRefreshView(View mRefreshView) {
        this.isUseSelfRefresh = true;
        this.mIScrollListener = null;
        this.mRefreshView = mRefreshView;
    }

    /**
     * 添加加载的View
     *
     * @param mLoadMoreView
     */
    public void addLoadMoreView(View mLoadMoreView) {
        this.isUseSelfLoadMore = true;
        this.mIScrollListener = null;
        this.mLoadMoreView = mLoadMoreView;
    }

    /**
     * 用来回调是否刷新成功
     *
     * @param status
     */
    public void setRefreshStatus(int status) {
        this.refreshScrollStatus = status;
        if (isUseSelfRefresh) {
            mCustomScrollListener.scrollRefreshState(status);
        } else {
            scrollRefreshState(status);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshScrollAnimation(-mRefreshView.getMeasuredHeight());
                isRefreshing = false;
            }
        }, 500);
    }

    /**
     * 用来回调是否刷新成功
     *
     * @param status
     */
    public void setLoadMoreStatus(int status) {
        this.loadMoreScrollStatus = status;
        if (isUseSelfLoadMore) {
            mCustomScrollListener.scrollRefreshState(status);
        } else {
            scrollRefreshState(status);
        }
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setRefreshScrollAnimation(-mLoadMoreView.getMeasuredHeight());
                isLoadMored = false;
            }
        }, 500);
    }

//    @Override
//    public boolean dispatchTouchEvent(MotionEvent ev) {
//        if (!isRefreshing && !isLoadMored) {
//            switch (ev.getAction()) {
//                case MotionEvent.ACTION_DOWN:
//                    start_X = (int) ev.getX();
//                    start_Y = (int) ev.getY();
//                    break;
//                case MotionEvent.ACTION_MOVE:
//                    float move_X = ev.getX();
//                    float move_Y = ev.getY();
//                    isUpdateFoot = false;
//                    /*
//                     * 如果没有刷新的View  或者刷新的View <= view的高度负值  证明刷新的view已经还原  这时候执行recycler的滑动
//                     */
////                    View childAt = layoutManager.getChildAt(1);
//                    /*
//                     * 每次进来时先全部置为false  且isRefreshStatus与isLoadMoreStatus为互斥状态  isLoadMoreStatus为true，则且isRefreshStatus为false，反之一样
//                     */
//                    LinearLayoutManager layoutManager = (LinearLayoutManager) mRecyclerView.getLayoutManager();
//                    Log.e("ScrollWrapRecycler", "getFullSize() + 1:" + (getFullSize() + 1));
//                    Log.e("ScrollWrapRecycler", "layoutManager.findLastCompletelyVisibleItemPosition():" + layoutManager.findLastCompletelyVisibleItemPosition());
//                    int lastVisibleItemPosition = layoutManager.findLastVisibleItemPosition();
//                    if ((mRefreshView != null && getMarginParams(mRefreshView).topMargin <= -mRefreshView.getMeasuredHeight()
//                            && scroolRemaining > 0 && move_Y - start_Y > 0 && (!isFootViewVisibility || getFullSize() + 1 != lastVisibleItemPosition))
//                            || mRefreshView == null) {
//                        start_Y = move_Y;
//                        isRefreshStatus = false;
//                        isLoadMoreStatus = false;
//                        return super.dispatchTouchEvent(ev);
//                    } else if (mRefreshView != null && getMarginParams(mRefreshView).topMargin >= -mRefreshView.getMeasuredHeight()
//                            && scroolRemaining <= 0 && move_Y - start_Y > 0 && (!isFootViewVisibility || getFullSize() + 1 != lastVisibleItemPosition)) {
//                        float phaseDiff = move_Y - start_Y;
//                        updateHead((float) (phaseDiff / 1.5));
//                        start_Y = move_Y;
//                        return true;
//                        //如果getMarginParams().topMargin > -refresh的高度  证明下拉了 需要先还原  还原下拉刷新的margin
//                    } else if (mRefreshView != null && getMarginParams(mRefreshView).topMargin > -mRefreshView.getMeasuredHeight()
//                            && scroolRemaining <= 0 && move_Y - start_Y < 0 && (!isFootViewVisibility || getFullSize() + 1 != lastVisibleItemPosition)) {
//                        float phaseDiff = move_Y - start_Y;
//                        updateHead((float) (phaseDiff * 1.5));
//                        start_Y = move_Y;
//                        return true;
//                    } else if (isFootViewVisibility && getMarginParams(mRefreshView).topMargin <= -mRefreshView.getMeasuredHeight()
//                            && scroolRemaining > 0
//                            && getFullSize() + 1 == layoutManager.findLastCompletelyVisibleItemPosition()/* && move_Y - start_Y < 0*/) {
//                        mLoadMoreView.setVisibility(VISIBLE);
//                        if (move_Y - start_Y > 0) {
//                            float phaseDiff = move_Y - start_Y;
//                            updateFoot((float) (phaseDiff / 1.5));
//                        } else {
//                            float phaseDiff = move_Y - start_Y;
//                            updateFoot((float) (phaseDiff / 1.5));
//                        }
//                        start_Y = move_Y;
//                        return true;
//                    } else {
//                        start_Y = move_Y;
////                        getMarginParams().topMargin = -mRefreshView.getMeasuredHeight();
////                        getMarginParams().bottomMargin = 0;
////                        setRLayoutPramas(getMarginParams());
//                        return super.dispatchTouchEvent(ev);
//                    }
//                case MotionEvent.ACTION_CANCEL:
//                case MotionEvent.ACTION_UP:
//                    if (isRefreshStatus) {
//                        if (mRefreshView != null) {
//                            if (refreshScrollStatus == SCROLL_RL_REFRESH) {
//                                isRefreshing = true;
//                                refresh();
//                                if (mCustomScrollListener != null && isUseSelfRefresh) {
//                                    mCustomScrollListener.scrollRefreshState(SCROLL_RL_LOADING);
//                                } else {
//                                    scrollRefreshState(SCROLL_RL_LOADING);
//                                }
//                                setRefreshScrollAnimation((int) (mRefreshView.getMeasuredHeight() * 1.2));
//                            } else {
//                                setRefreshScrollAnimation(-mRefreshView.getMeasuredHeight());
//                            }
//                        }
//                    } else if (isLoadMoreStatus) {
//                        if (mLoadMoreView != null) {
//                            if (loadMoreScrollStatus == SCROLL_RL_REFRESH) {
//                                isLoadMored = true;
//                                loadMore();
//                                if (mCustomScrollListener != null && isUseSelfLoadMore) {
//                                    mCustomScrollListener.scrollLoadMoreState(SCROLL_RL_LOADING);
//                                } else {
//                                    scrollLoadMoreState(SCROLL_RL_LOADING);
//                                }
//                                setLoadMoreScrollAnimation(mLoadMoreView.getMeasuredHeight());
//                            } else {
//                                setLoadMoreScrollAnimation(-mLoadMoreView.getMeasuredHeight() - 1);
//                            }
//                        }
//                    }
//                    isLoadMoreStatus = false;
//                    isRefreshStatus = false;
//                    break;
//            }
//        } else {
//            return true;
//        }
//        return super.dispatchTouchEvent(ev);
//    }

    private void refresh() {
        if (mCustomScrollListener != null) {
            mCustomScrollListener.refresh();
        } else if (mIScrollListener != null) {
            mIScrollListener.refresh();
        } else {
            Log.w(getClass().getName(), "请设置回调监听器");
        }
    }

    private void loadMore() {
        if (mCustomScrollListener != null) {
            mCustomScrollListener.loadMore();
        } else if (mIScrollListener != null) {
            mIScrollListener.loadMore();
        } else {
            Log.w(getClass().getName(), "请设置回调监听器");
        }
    }

    /**
     * 更新加载的View
     *
     * @param phaseDiff
     */
    private void updateFoot(float phaseDiff) {
        isUpdateFoot = true;
        isLoadMoreStatus = true;
        isRefreshStatus = false;
        RecyclerView.MarginLayoutParams marginParams = getMarginParams(mLoadMoreView);
        int scrollMax = marginParams.bottomMargin - (int) (phaseDiff);
        if (phaseDiff < 0) {
            scrollMax = scrollMax > mLoadMoreView.getMeasuredHeight() * 2.4 ? (int) (mLoadMoreView.getMeasuredHeight() * 2.4) : scrollMax;
        } else {
            scrollMax = scrollMax < -mLoadMoreView.getMeasuredHeight() ? -mLoadMoreView.getMeasuredHeight() : scrollMax;
        }
        marginParams.bottomMargin = scrollMax;
        setRLayoutPramas(marginParams);
        if (isUseSelfLoadMore) {
            if (mCustomScrollListener != null) {
                if (scrollMax < mLoadMoreView.getMeasuredHeight() * 1.2) {
                    this.loadMoreScrollStatus = SCROLL_RL_NOTMET;
                    mCustomScrollListener.scrollRefreshState(SCROLL_RL_NOTMET);
                } else if (scrollMax > mLoadMoreView.getMeasuredHeight() * 1.2) {
                    this.loadMoreScrollStatus = SCROLL_RL_REFRESH;
                    mCustomScrollListener.scrollRefreshState(SCROLL_RL_REFRESH);
                }
//                mCustomScrollListener.scrollRefreshState(scrollMax > mRefreshView.getMeasuredHeight());
            }
        } else {
            if (scrollMax < mLoadMoreView.getMeasuredHeight() * 1.2) {
                this.scrollRefreshState(SCROLL_RL_NOTMET);
            } else if (scrollMax > mLoadMoreView.getMeasuredHeight() * 1.2) {
                this.scrollRefreshState(SCROLL_RL_REFRESH);
            }
        }
    }

    /**
     * 更新头部的刷新
     *
     * @param phaseDiff
     */
    private void updateHead(float phaseDiff) {
        isRefreshStatus = true;
        isLoadMoreStatus = false;
        RecyclerView.MarginLayoutParams marginParams = getMarginParams(mRefreshView);
        int scrollMax = marginParams.topMargin + (int) (phaseDiff);
        if (phaseDiff > 0) {
            scrollMax = scrollMax > mRefreshView.getMeasuredHeight() * 2.4 ? (int) (mRefreshView.getMeasuredHeight() * 2.4) : scrollMax;
        } else {
            scrollMax = scrollMax < -mRefreshView.getMeasuredHeight() ? -mRefreshView.getMeasuredHeight() : scrollMax;
        }
        marginParams.topMargin = scrollMax;
        marginParams.bottomMargin = 0;
        setRLayoutPramas(marginParams);
        if (isUseSelfRefresh) {
            if (mCustomScrollListener != null) {
                if (scrollMax < mRefreshView.getMeasuredHeight() * 1.2) {
                    this.refreshScrollStatus = SCROLL_RL_NOTMET;
                    mCustomScrollListener.scrollRefreshState(SCROLL_RL_NOTMET);
                } else if (scrollMax > mRefreshView.getMeasuredHeight() * 1.2) {
                    this.refreshScrollStatus = SCROLL_RL_REFRESH;
                    mCustomScrollListener.scrollRefreshState(SCROLL_RL_REFRESH);
                }
//                mCustomScrollListener.scrollRefreshState(scrollMax > mRefreshView.getMeasuredHeight());
            }
        } else {
            if (scrollMax < mRefreshView.getMeasuredHeight() * 1.2) {
                this.scrollRefreshState(SCROLL_RL_NOTMET);
            } else if (scrollMax > mRefreshView.getMeasuredHeight() * 1.2) {
                this.scrollRefreshState(SCROLL_RL_REFRESH);
            }
        }
    }

    /**
     * 更改头部状态
     *
     * @param scrollReleash
     */
    public void scrollRefreshState(int scrollReleash) {
        switch (scrollReleash) {
            case SCROLL_RL_NOTSLIPPING:
                mRefreshHint.setText("等待刷新");
                break;
            case SCROLL_RL_NOTMET:
                mRefreshHint.setText("下拉刷新");
                break;
            case SCROLL_RL_REFRESH:
                mRefreshHint.setText("松开刷新");
                break;
            case SCROLL_RL_LOADING:
                mRefreshHint.setText("正在刷新...");
                break;
            case SCROLL_RL_SUCCESS:
                mRefreshHint.setText("刷新成功");
                break;
            case SCROLL_RL_FAILD:
                mRefreshHint.setText("刷新失败");
                break;
        }
        this.refreshScrollStatus = scrollReleash;
    }

    /**
     * 更改底部加载状态
     *
     * @param scrollReleash
     */
    public void scrollLoadMoreState(int scrollReleash) {
        switch (scrollReleash) {
            case SCROLL_RL_NOTSLIPPING:
                mLoadMoreHint.setText("加载更多");
                break;
            case SCROLL_RL_NOTMET:
                mLoadMoreHint.setText("上拉加载");
                break;
            case SCROLL_RL_REFRESH:
                mLoadMoreHint.setText("松开立即加载");
                break;
            case SCROLL_RL_LOADING:
                mLoadMoreHint.setText("正在加载...");
                break;
            case SCROLL_RL_SUCCESS:
                mLoadMoreHint.setText("加载成功");
                break;
            case SCROLL_RL_FAILD:
                mLoadMoreHint.setText("加载失败");
                break;
        }
        this.loadMoreScrollStatus = scrollReleash;
    }

    private RecyclerView.MarginLayoutParams getMarginParams(View view) {
        return (MarginLayoutParams) view.getLayoutParams();
    }

    public void setRefreshScrollAnimation(int height) {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("", getMarginParams(mRefreshView).topMargin, height);
        refreshAnimator.setValues(valuesHolder);
        refreshAnimator.setDuration(300);
        refreshAnimator.start();
    }

    public void setLoadMoreScrollAnimation(int height) {
        PropertyValuesHolder valuesHolder = PropertyValuesHolder.ofFloat("", getMarginParams(mLoadMoreView).bottomMargin, height);
        loadmoreAnimator.setValues(valuesHolder);
        loadmoreAnimator.setDuration(300);
        loadmoreAnimator.start();
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
