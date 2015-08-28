package com.swishlabs.prototyping.customViews.pullrefresh;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.util.AttributeSet;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;

/**
 * define common functions for Header and Footer
 * 
 * @author
 * @since
 */
public abstract class LoadingLayout extends FrameLayout implements ILoadingLayout {

	/**  */
	private View mContainer;
	/** current state */
	private State mCurState = State.NONE;
	/** previous state */
	private State mPreState = State.NONE;

	/**
	 * construct
	 * 
	 * @param context
	 *            context
	 */
	public LoadingLayout(Context context) {
		this(context, null);
	}

	/**
	 * construct
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public LoadingLayout(Context context, AttributeSet attrs) {
		this(context, attrs, 0);
	}

	/**
	 * construct
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	public LoadingLayout(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);

		init(context, attrs);
	}

	/**
	 * initialization
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	protected void init(Context context, AttributeSet attrs) {
		mContainer = createLoadingView(context, attrs);
		if (null == mContainer) {
			throw new NullPointerException("Loading view can not be null.");
		}
		addView(mContainer);
	}

	/**
	 * show/hide this layout
	 * 
	 * @param show
	 *            flag
	 */
	public void show(boolean show) {
		// If is showing, do nothing.
		if (show == (View.VISIBLE == getVisibility())) {
			return;
		}

		ViewGroup.LayoutParams params = mContainer.getLayoutParams();
		if (null != params) {
			if (show) {
				params.height = ViewGroup.LayoutParams.WRAP_CONTENT;
			} else {
				params.height = 0;
			}

			requestLayout();
			setVisibility(show ? View.VISIBLE : View.INVISIBLE);
		}
	}

	/**
	 * set last updated text
	 * 
	 * @param label
	 *
	 */
	public void setLastUpdatedLabel(CharSequence label) {

	}

	/**
	 * set loading immage
	 * 
	 * @param drawable
	 *
	 */
	public void setLoadingDrawable(Drawable drawable) {

	}

	/**
	 * set pull prompt text”
	 * 
	 * @param pullLabel
	 *
	 */
	public void setPullLabel(CharSequence pullLabel) {

	}

	/**
	 * set " refreshing"
	 * 
	 * @param refreshingLabel
	 *
	 */
	public void setRefreshingLabel(CharSequence refreshingLabel) {

	}

	/**
	 * set "release to refresh"
	 * 
	 * @param releaseLabel
	 *
	 */
	public void setReleaseLabel(CharSequence releaseLabel) {

	}

	@Override
	public void setState(State state) {
		if (mCurState != state) {
			mPreState = mCurState;
			mCurState = state;
			onStateChanged(state, mPreState);
		}
	}

	@Override
	public State getState() {
		return mCurState;
	}

	@Override
	public void onPull(float scale) {

	}

	/**
	 *
	 * 
	 * @return
	 */
	protected State getPreState() {
		return mPreState;
	}

	/**
	 *
	 * 
	 * @param curState
	 *
	 * @param oldState
	 *
	 */
	protected void onStateChanged(State curState, State oldState) {
		switch (curState) {
		case RESET:
			onReset();
			break;
		case RELEASE_TO_REFRESH:
			onReleaseToRefresh();
			break;
		case PULL_TO_REFRESH:
			onPullToRefresh();
			break;
		case REFRESHING:
			onRefreshing();
			break;
		case NO_MORE_DATA:
			onNoMoreData();
			break;
		case HAS_MORE_DATA:
			onHasMoreData();
			break;
		default:
			break;
		}
	}

	/**
	 * {@link State#RESET}
	 */
	protected void onReset() {

	}

	/**
	 * {@link State#PULL_TO_REFRESH}
	 */
	protected void onPullToRefresh() {

	}

	/**
	 * {@link State#RELEASE_TO_REFRESH}
	 */
	protected void onReleaseToRefresh() {

	}

	/**
	 * {@link State#REFRESHING}
	 */
	protected void onRefreshing() {

	}

	/**
	 * {@link State#NO_MORE_DATA}
	 */
	protected void onNoMoreData() {

	}

	protected void onHasMoreData() {

	}

	/**
	 * get the height of the current layout
	 * 
	 * @return height
	 */
	public abstract int getContentSize();

	/**
	 * create Loading view
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @return Loading View
	 */
	protected abstract View createLoadingView(Context context, AttributeSet attrs);
}
