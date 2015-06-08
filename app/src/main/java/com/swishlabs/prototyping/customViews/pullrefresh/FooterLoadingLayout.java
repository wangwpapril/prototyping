package com.swishlabs.prototyping.customViews.pullrefresh;


import android.content.Context;
import android.graphics.Bitmap;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.Interpolator;
import android.view.animation.LinearInterpolator;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swishlabs.prototyping.R;


/**
 * 这个类封装了下拉刷新的布局
 * 
 */
public class FooterLoadingLayout extends LoadingLayout {
    /**旋转动画的时间*/
    static final int ROTATION_ANIMATION_DURATION = 800;
    /**动画插值*/
    static final Interpolator ANIMATION_INTERPOLATOR = new LinearInterpolator();
    /**Header的容器*/
    private View mHeaderContainer;
    /**箭头图片*/
    private ImageView mArrowImageView;
    /**状态提示TextView*/
    private TextView mHintTextView;
    /**最后更新时间的TextView*/
    private TextView mHeaderTimeView;
    /**最后更新时间的标题*/
    private TextView mHeaderTimeViewTitle;
    
    private TextView mNomoreDataContent;
    
    private View mProgressContainer;
    
    private ProgressBar mProgressbar;    
    
    private Animation mPullArrowUpAnimation;
    private Animation mPullArrowDownAnimation;
        
    
    Bitmap rotateBitmap;
    
    boolean isOnRefresh = false;
    
    boolean isHasMoreData = true;
    
    /**
     * 构造方法
     * 
     * @param context context
     */
    public FooterLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * 构造方法
     * 
     * @param context context
     * @param attrs attrs
     */
    public FooterLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * 初始化
     * 
     * @param context context
     */
    private void init(Context context) {
    	mProgressbar = (ProgressBar) findViewById(R.id.pull_to_refresh_header_progressbar);
        mHeaderContainer = findViewById(R.id.pull_to_refresh_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
        mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
        mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);
        mNomoreDataContent = (TextView)findViewById(R.id.pull_to_refresh_header_nomoredata);
        mProgressContainer = findViewById(R.id.pull_to_refresh_header_progress_layout);
        
        mPullArrowUpAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pull_arrow_up);
        mPullArrowUpAnimation.setFillAfter(true);
        mPullArrowDownAnimation = AnimationUtils.loadAnimation(getContext(), R.anim.pull_arrow_down);
        mPullArrowDownAnimation.setFillAfter(true);
       
    }
    
    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_footer, null);
        return container;
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        // 如果最后更新的时间的文本是空的话，隐藏前面的标题
    	mHeaderTimeViewTitle.setVisibility(View.INVISIBLE);
//        mHeaderTimeViewTitle.setVisibility(TextUtils.isEmpty(label) ? View.INVISIBLE : View.VISIBLE);
        mHeaderTimeView.setText(label);
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }
        
        return (int) (getResources().getDisplayMetrics().density * 60);
    }
    
    @Override
    protected void onStateChanged(State curState, State oldState) {
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
    	Log.i("load", "onReset");
        resetRotation();
        mArrowImageView.setVisibility(View.VISIBLE);
        mProgressbar.setVisibility(View.GONE);
        mHintTextView.setText(R.string.pull_to_refresh_footer_hint_normal2);
    }

    @Override
    protected void onReleaseToRefresh() {
    	Log.i("load", "onReleaseToRefresh");
    	isOnRefresh = true;
		if (isHasMoreData) {
			mArrowImageView.startAnimation(mPullArrowUpAnimation);
			mHintTextView.setText(R.string.pushmsg_center_pull_up_text);
		}
    }
    
    @Override
    protected void onPullToRefresh() {
    	Log.i("load", "onPullToRefresh");
    	if(isHasMoreData){
    		if (State.RELEASE_TO_REFRESH == getPreState()) {
                mArrowImageView.startAnimation(mPullArrowDownAnimation);
            }
        mHintTextView.setText(R.string.pull_to_refresh_footer_hint_normal2);
    	}
    }
    
	@Override
	protected void onRefreshing() {
		if (isHasMoreData) {
			mArrowImageView.clearAnimation();
			mArrowImageView.setVisibility(View.GONE);				
			mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
			mProgressbar.setVisibility(View.VISIBLE);
		}
	}
    
    
    @Override
    protected void onNoMoreData() {
    	super.onNoMoreData();
    	isHasMoreData =false;
    	mNomoreDataContent.setVisibility(View.VISIBLE);
    	mHeaderContainer.setVisibility(View.GONE);
		mProgressContainer.setVisibility(View.GONE);
    }
    
    @Override
    protected void onHasMoreData() {
    	super.onHasMoreData();
    	isHasMoreData = true;
    	mNomoreDataContent.setVisibility(View.GONE);
    	mHeaderContainer.setVisibility(View.VISIBLE);
    	mProgressContainer.setVisibility(View.VISIBLE);
    }
    
    /**
     * 重置动画
     */
    private void resetRotation() {
    	Log.i("load", "resetRotation");
    	isOnRefresh = false;
        mArrowImageView.clearAnimation();
        mArrowImageView.setImageResource(R.drawable.refresh);
//        mArrowImageView.setRotation(0);
    }
}
