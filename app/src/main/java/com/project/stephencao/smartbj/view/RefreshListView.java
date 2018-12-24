package com.project.stephencao.smartbj.view;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.widget.*;
import com.project.stephencao.smartbj.R;
import com.project.stephencao.smartbj.utils.ConstantValues;

import java.text.SimpleDateFormat;

public class RefreshListView extends ListView {
    private static final int STATE_PULL_TO_REFRESH = 0;
    private static final int STATE_RELEASE_TO_REFRESH = 1;
    private static final int STATE_REFRESHING = 2;

    private int mCurrentState = STATE_PULL_TO_REFRESH;
    private ProgressBar mProgressBar;
    private View mHeaderView;
    private View mFooterView;
    private OnRefreshListener mListener;
    private TextView mDateTextView;
    private int startY = -1;
    private int endY = 0;
    private ImageView mArrowIconImageView;
    private TextView mTitleTextView;
    private Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1: {
                    restoreToPull();
                    mCurrentState = STATE_PULL_TO_REFRESH;
                    mHeaderView.setPadding(0, -70, 0, 0);
                    startY = -1;

                }
            }
        }
    };


    public RefreshListView(Context context) {
        this(context, null);
    }

    public RefreshListView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public RefreshListView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initHeaderView();
        initFooterView();
    }

    private void initFooterView() {
        mFooterView = LayoutInflater.from(getContext()).inflate(R.layout.view_for_refresh_footer, null);
        addFooterView(mFooterView);
        mFooterView.setPadding(0, -70, 0, 0);
        setOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(AbsListView view, int scrollState) {
                switch (scrollState) {
                    case SCROLL_STATE_IDLE: {
                        int lastVisiblePosition = getLastVisiblePosition();
                        if (lastVisiblePosition == getCount() - 1) {
                            mFooterView.setPadding(0, 0, 0, 0);
                            setSelection(getCount() - 1);
                            if (mListener != null) {
                                int moreData = mListener.getMoreData();
                                if(moreData == ConstantValues.LIST_VIEW_NO_MORE_DATA){
                                    mFooterView.setPadding(0, -70, 0, 0);
                                }
                            }
                        }
                        break;
                    }
                }
            }

            @Override
            public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {

            }
        });
    }

    private void initHeaderView() {
        mHeaderView = LayoutInflater.from(getContext()).inflate(R.layout.view_for_refresh_header, null);
        mArrowIconImageView = mHeaderView.findViewById(R.id.iv_news_header_refresh_icon);
        mTitleTextView = mHeaderView.findViewById(R.id.tv_news_header_refresh_title);
        mProgressBar = mHeaderView.findViewById(R.id.pb_news_header_refresh_progress_bar);
        mDateTextView = mHeaderView.findViewById(R.id.tv_news_header_refresh_date);
        addHeaderView(mHeaderView);

        mHeaderView.setPadding(0, -70, 0, 0);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        refreshTime();
        if (mCurrentState != STATE_REFRESHING) {
            switch (ev.getAction()) {
                case MotionEvent.ACTION_DOWN: {
                    startY = (int) ev.getY();
                    break;
                }
                case MotionEvent.ACTION_UP: {
                    if (mCurrentState == STATE_RELEASE_TO_REFRESH) {
                        mCurrentState = STATE_REFRESHING;
                        refreshing();
                        handler.sendEmptyMessageDelayed(1, 2000);
                    }
                    break;
                }
                case MotionEvent.ACTION_MOVE: {
                    if (startY == -1) {
                        startY = (int) ev.getY();
                    }
                    endY = (int) ev.getY();
                    int distanceY = endY - startY;
                    int firstVisiblePosition = this.getFirstVisiblePosition();
                    if (distanceY > 0 && firstVisiblePosition == 0) {
                        int paddingValue = -70;
                        if (paddingValue + distanceY > 70 && mCurrentState != STATE_RELEASE_TO_REFRESH) {
                            mCurrentState = STATE_RELEASE_TO_REFRESH;
                            releaseToRefresh();
                        }
                        mHeaderView.setPadding(0, paddingValue + distanceY, 0, 0);
                        return true;
                    }
                    break;
                }
            }
        }
        return super.onTouchEvent(ev);
    }

    private void refreshing() {
        mHeaderView.setPadding(0, 0, 0, 0);
        mProgressBar.setVisibility(VISIBLE);
        mArrowIconImageView.setVisibility(INVISIBLE);
        mTitleTextView.setText("刷新中...");
        if (mListener != null) {
            mListener.doRefresh();
        }
    }

    private void releaseToRefresh() {
        mTitleTextView.setText("松开刷新");
        mArrowIconImageView.setBackgroundResource(R.drawable.arrow_refreshing);
    }

    private void restoreToPull() {
        mTitleTextView.setText("下拉刷新");
        mArrowIconImageView.setBackgroundResource(R.drawable.arrow_refresh);
        mProgressBar.setVisibility(INVISIBLE);
        mArrowIconImageView.setVisibility(VISIBLE);
    }

    private void refreshTime() {
        long timeMillis = System.currentTimeMillis();
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyy-MM-dd HH:mm:ss");
        String formatTime = simpleDateFormat.format(timeMillis);
        mDateTextView.setText(formatTime);
    }

    public interface OnRefreshListener {
        public void doRefresh();

        public int getMoreData();
    }

    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        mListener = onRefreshListener;
    }
}
