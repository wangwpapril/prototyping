package com.swishlabs.prototyping.customViews.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ScrollView;

import com.swishlabs.prototyping.R;


public class PullToRefreshScrollView extends PullToRefreshBase<ViewGroup> {

	private ScrollView mScrollView;

	public PullToRefreshScrollView(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	public PullToRefreshScrollView(Context context, AttributeSet attrs) {
		super(context, attrs);
		// TODO Auto-generated constructor stub
	}

	@Override
	protected ViewGroup createRefreshableView(Context context, AttributeSet attrs) {
		setPullLoadEnabled(false);
		mScrollView = new ScrollView(context);
		return mScrollView;
	}

	@Override
	protected boolean isReadyForPullDown() {
		// TODO Auto-generated method stub
		return mScrollView.getScrollY() == 0;

	}

	@Override
	protected boolean isReadyForPullUp() {
		View scrollViewChild = getChildAt(0);

		if (null != scrollViewChild) {
			return mScrollView.getScrollY() >= (scrollViewChild.getHeight() - getHeight());
		}
		return false;
	}

	@Override
	protected void onFinishInflate() {
		super.onFinishInflate();
		View view = findViewById(R.id.content);
		if (view != null) {
			removeView(view);
			mScrollView.addView(view);
		}
	}
}
