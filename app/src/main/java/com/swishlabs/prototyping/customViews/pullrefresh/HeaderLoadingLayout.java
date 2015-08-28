package com.swishlabs.prototyping.customViews.pullrefresh;

import android.content.Context;
import android.text.TextUtils;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.swishlabs.prototyping.R;


/**
 * This class is for pull up refresh layout
 */
public class HeaderLoadingLayout extends LoadingLayout {
    /**  */
    private static final int ROTATE_ANIM_DURATION = 150;
    /**Header Container*/
    private View mHeaderContainer;
    /***/
    private ImageView mArrowImageView;
    /**Progress bar*/
    private ProgressBar mProgressBar;
    /**Status hint TextView*/
    private TextView mHintTextView;
    /***/
    private TextView mHeaderTimeView;
    /***/
    private TextView mHeaderTimeViewTitle;
    /**up animation*/
    private Animation mRotateUpAnim;
    /**down animation*/
    private Animation mRotateDownAnim;
    
    /**
     * Construct
     * 
     * @param context context
     */
    public HeaderLoadingLayout(Context context) {
        super(context);
        init(context);
    }

    /**
     * Construct
     * 
     * @param context context
     * @param attrs attrs
     */
    public HeaderLoadingLayout(Context context, AttributeSet attrs) {
        super(context, attrs);
        init(context);
    }

    /**
     * Initialize
     * 
     * @param context context
     */
    private void init(Context context) {
        mHeaderContainer = findViewById(R.id.pull_to_refresh_header_content);
        mArrowImageView = (ImageView) findViewById(R.id.pull_to_refresh_header_arrow);
        mHintTextView = (TextView) findViewById(R.id.pull_to_refresh_header_hint_textview);
        mProgressBar = (ProgressBar) findViewById(R.id.pull_to_refresh_header_progressbar);
        mHeaderTimeView = (TextView) findViewById(R.id.pull_to_refresh_header_time);
        mHeaderTimeViewTitle = (TextView) findViewById(R.id.pull_to_refresh_last_update_time_text);
        
        float pivotValue = 0.5f;    // SUPPRESS CHECKSTYLE
        float toDegree = 180f;     // SUPPRESS CHECKSTYLE
        // initialize rotation animation
        mRotateUpAnim = new RotateAnimation(0.0f, toDegree, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateUpAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateUpAnim.setFillAfter(true);
        mRotateDownAnim = new RotateAnimation(toDegree, 360f, Animation.RELATIVE_TO_SELF, pivotValue,
                Animation.RELATIVE_TO_SELF, pivotValue);
        mRotateDownAnim.setDuration(ROTATE_ANIM_DURATION);
        mRotateDownAnim.setFillAfter(true);
    }

    @Override
    public void setLastUpdatedLabel(CharSequence label) {
        // if the text of the last updated time is empty, hide the title
		if (TextUtils.isEmpty(label)) {
			mHeaderTimeViewTitle.setVisibility(View.GONE);
			mHeaderTimeView.setVisibility(View.GONE);
		} else {
			mHeaderTimeViewTitle.setVisibility(View.VISIBLE);
			mHeaderTimeView.setVisibility(View.VISIBLE);
			mHeaderTimeView.setText(label);
		}
    	    	
    }

    @Override
    public int getContentSize() {
        if (null != mHeaderContainer) {
            return mHeaderContainer.getHeight();
        }
        
        return (int) (getResources().getDisplayMetrics().density * 60);
    }
    
    @Override
    protected View createLoadingView(Context context, AttributeSet attrs) {
        View container = LayoutInflater.from(context).inflate(R.layout.pull_to_refresh_header, null);
        return container;
    }
    
    @Override
    protected void onStateChanged(State curState, State oldState) {
        mArrowImageView.setVisibility(View.VISIBLE);
        mProgressBar.setVisibility(View.INVISIBLE);
        
        super.onStateChanged(curState, oldState);
    }

    @Override
    protected void onReset() {
        mArrowImageView.clearAnimation();
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onPullToRefresh() {
        if (State.RELEASE_TO_REFRESH == getPreState()) {
            mArrowImageView.startAnimation(mRotateDownAnim);
        }
        
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_normal);
    }

    @Override
    protected void onReleaseToRefresh() {
        mArrowImageView.startAnimation(mRotateUpAnim);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_ready);
    }

    @Override
    protected void onRefreshing() {
        mArrowImageView.clearAnimation();
        mArrowImageView.setVisibility(View.INVISIBLE);
        mProgressBar.setVisibility(View.VISIBLE);
        mHintTextView.setText(R.string.pull_to_refresh_header_hint_loading);
    }
}
