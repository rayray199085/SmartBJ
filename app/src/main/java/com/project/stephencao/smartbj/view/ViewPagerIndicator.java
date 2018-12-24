package com.project.stephencao.smartbj.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.*;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import com.project.stephencao.smartbj.R;

import java.util.List;

public class ViewPagerIndicator extends LinearLayout {
    private Paint mPaint;
    private Path mPath;
    private int mTriangleWidth;
    private int mTriangleHeight;
    private static final float RADIO_TRIANGLE_WIDTH = 1/6f;
    private int mInitOffsetX;
    private int mOffsetX;
    private List<String> mTitleList;
    private ViewPager mViewPager;
    public PageOnChangeListener mPageOnChangeListener;

    private int tabVisibleNum;
    private static final int DEFAULT_NUM = 4;

    public ViewPagerIndicator(Context context) {
        this(context,null);
    }

    public ViewPagerIndicator(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        TypedArray array = context.obtainStyledAttributes(attrs, R.styleable.ViewPagerIndicator);
        tabVisibleNum = array.getInt(R.styleable.ViewPagerIndicator_visible_tab_num,DEFAULT_NUM);
        if(tabVisibleNum<0){
            tabVisibleNum = DEFAULT_NUM;
        }
        array.recycle();

        mPaint = new Paint();
        mPaint.setAntiAlias(true);
        mPaint.setColor(Color.WHITE);
        mPaint.setStyle(Paint.Style.FILL);
        mPaint.setPathEffect(new CornerPathEffect(3));// radius
    }

    @Override
    protected void onFinishInflate() {
        // This callback method will be executed after all layout contents are initialized
        super.onFinishInflate();

        int childNum = getChildCount();
        if(childNum==0){
            return;
        }
        else{
            for(int i=0;i<childNum;i++){
                View view =getChildAt(i);
                LayoutParams layoutParams = (LayoutParams) view.getLayoutParams();
                layoutParams.weight = 0;
                layoutParams.width = getScreenWidth()/tabVisibleNum;
                view.setLayoutParams(layoutParams);

            }
            setItemClickEvent();
        }
    }

    private int getScreenWidth() {
        WindowManager manager = (WindowManager) getContext().getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics metrics = new DisplayMetrics();
        manager.getDefaultDisplay().getMetrics(metrics);
        return metrics.widthPixels;
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        canvas.save();
        canvas.translate(mInitOffsetX + mOffsetX, getHeight());
        canvas.drawPath(mPath,mPaint);

        canvas.restore();
        super.dispatchDraw(canvas);

    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        mTriangleWidth = (int) (w/tabVisibleNum * RADIO_TRIANGLE_WIDTH);
        mInitOffsetX = w/tabVisibleNum/2 -  mTriangleWidth/2;

        initTriangle();
    }

    private void initTriangle() {
        mTriangleHeight = mTriangleWidth/2;
        mPath = new Path();
        mPath.moveTo(0,0);
        mPath.lineTo(mTriangleWidth,0);
        mPath.lineTo(mTriangleWidth/2,-mTriangleHeight);
        mPath.close();
    }

    public void scroll(int i, float v) {
        int tabWidth = getWidth()/tabVisibleNum;
        mOffsetX = (int) (tabWidth * v + i * tabWidth); //

        if((i >= tabVisibleNum -2) && v > 0 && getChildCount() > tabVisibleNum){
            if(tabVisibleNum!=1) {
                this.scrollTo((i - (tabVisibleNum - 2)) * tabWidth + (int) (tabWidth * v), 0);
            }
            else{
                this.scrollTo(tabWidth*i + (int)(tabWidth*v),0);
            }
        }

        invalidate();//refresh
    }

    public void setTabItemTitles(List<String> titles){
        if(titles != null && titles.size() > 0){
            this.removeAllViews();
            mTitleList = titles;
            for(String title: mTitleList){
                addView(generateTextView(title));
            }
            setItemClickEvent();

        }    }

    private View generateTextView(String title) {
        TextView textView = new TextView(getContext());
        LayoutParams layoutParams =
                new LayoutParams(LayoutParams.MATCH_PARENT,LayoutParams.MATCH_PARENT);
        layoutParams.width = getScreenWidth()/tabVisibleNum;
        textView.setText(title);
        textView.setGravity(Gravity.CENTER);
        textView.setTextSize(TypedValue.COMPLEX_UNIT_SP,16);
        textView.setTextColor(Color.WHITE);
        textView.setLayoutParams(layoutParams);
        return textView;

    }

    public void setTabVisibleNum(int num){
        tabVisibleNum = num;
    }

    public void setViewPager(ViewPager viewPager, int pos){
        this.mViewPager = viewPager;
        this.mViewPager.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int i, float v, int i1) {
                scroll(i,v);
                if(mPageOnChangeListener != null){
                    mPageOnChangeListener.onPageScrolled(i,v,i1);
                }
            }

            @Override
            public void onPageSelected(int i) {
                if(mPageOnChangeListener != null){
                    mPageOnChangeListener.onPageSelected(i);
                }
                highlightTextView(i);

            }

            @Override
            public void onPageScrollStateChanged(int i) {
                if(mPageOnChangeListener != null){
                    mPageOnChangeListener.onPageScrollStateChanged(i);
                }
            }
        });
        this.mViewPager.setCurrentItem(pos);
        highlightTextView(pos);
    }

    public interface PageOnChangeListener{
        public void onPageScrolled(int i, float v, int i1);

        public void onPageSelected(int i) ;

        public void onPageScrollStateChanged(int i);
    }

    public void setOnPageChangeListener(PageOnChangeListener pageChangeListener){
        this.mPageOnChangeListener = pageChangeListener;
    }

    public void highlightTextView(int pos){
        resetTextViewColor();
        View view = getChildAt(pos);
        if(view instanceof TextView){
            ((TextView) view).setTextColor(Color.parseColor("#FF02FF20"));
        }
    }

    public void resetTextViewColor(){
        for(int i=0;i<getChildCount();i++){
            View view = getChildAt(i);
            if(view instanceof TextView){
                ((TextView) view).setTextColor(Color.WHITE);
            }
        }
    }

    public void setItemClickEvent(){
        int childCount = getChildCount();
        for(int i=0;i<childCount;i++){
            View view = getChildAt(i);
            final int j = i;
            view.setOnClickListener(new OnClickListener() {
                @Override
                public void onClick(View v) {
                    mViewPager.setCurrentItem(j);
                }
            });
        }
    }
}
