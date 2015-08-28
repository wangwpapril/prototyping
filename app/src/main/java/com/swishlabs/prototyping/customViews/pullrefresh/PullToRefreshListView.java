package com.swishlabs.prototyping.customViews.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.ListView;

import com.swishlabs.prototyping.customViews.pullrefresh.ILoadingLayout.State;



/**
 * for ListView
 * 
 * @author
 * @since
 */
public class PullToRefreshListView extends PullToRefreshBase<ListView> implements OnScrollListener {

	/** ListView */
	private ListView mListView;
	/** Footer */
	private LoadingLayout mLoadMoreFooterLayout;
	/**  */
	private OnScrollListener mScrollListener;

	/**
	 *
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshListView(Context context) {
		this(context, null);
	}

	/**
	 *
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshListView(Context context, AttributeSet attrs) {
		// this(context, attrs, 0);
		super(context, attrs);
	}

	/**
	 *
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 * @param defStyle
	 *            defStyle
	 */
	/*
	 * public PullToRefreshListView(Context context, AttributeSet attrs, int
	 * defStyle) { super(context, attrs, defStyle);
	 * 
	 * setPullLoadEnabled(false); }
	 */

	@Override
	protected ListView createRefreshableView(Context context, AttributeSet attrs) {
		ListView listView = new ListView(context, attrs);
		listView.setId(View.NO_ID);
		mListView = listView;
		listView.setOnScrollListener(this);
		return listView;
	}

	/**
	 *
	 * 
	 * @param hasMoreData
	 *
	 */
	public void setHasMoreData(boolean hasMoreData) {
		super.setHasMoreData(hasMoreData);
		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(hasMoreData ? State.HAS_MORE_DATA : State.NO_MORE_DATA);
		}		
	}

	/**
	 *
	 * 
	 * @param l
	 *
	 */
	public void setOnScrollListener(OnScrollListener l) {
		mScrollListener = l;
	}

	@Override
	protected boolean isReadyForPullUp() {
		return isLastItemVisible();
	}

	@Override
	protected boolean isReadyForPullDown() {
		return isFirstItemVisible();
	}

	@Override
	protected void startLoading() {
		super.startLoading();

		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.REFRESHING);
		}
	}

	@Override
	public void onPullUpRefreshComplete() {
		super.onPullUpRefreshComplete();

		if (null != mLoadMoreFooterLayout) {
			mLoadMoreFooterLayout.setState(State.RESET);
		}
	}

	@Override
	public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
		if (isScrollLoadEnabled() == scrollLoadEnabled) {
			return;
		}
		super.setScrollLoadEnabled(scrollLoadEnabled);

		if (scrollLoadEnabled) {
			// set Footer
			if (null == mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout = new FooterLoadingLayout(getContext());
				mListView.addFooterView(mLoadMoreFooterLayout, null, false);
			}

			mLoadMoreFooterLayout.show(true);
		} else {
			if (null != mLoadMoreFooterLayout) {
				mLoadMoreFooterLayout.show(false);
			}
		}
	}

	@Override
	public LoadingLayout getFooterLoadingLayout() {
		if (isScrollLoadEnabled()) {
			return mLoadMoreFooterLayout;
		}

		return super.getFooterLoadingLayout();
	}

	@Override
	public void onScrollStateChanged(AbsListView view, int scrollState) {
		if (isScrollLoadEnabled() && hasMoreData()) {
			if (scrollState == OnScrollListener.SCROLL_STATE_IDLE || scrollState == OnScrollListener.SCROLL_STATE_FLING) {
				if (isReadyForPullUp()) {
					startLoading();
				}
			}
		}

		if (null != mScrollListener) {
			mScrollListener.onScrollStateChanged(view, scrollState);
		}
	}

	@Override
	public void onScroll(AbsListView view, int firstVisibleItem, int visibleItemCount, int totalItemCount) {
		if (null != mScrollListener) {
			mScrollListener.onScroll(view, firstVisibleItem, visibleItemCount, totalItemCount);
		}
	}

//	@Override
//	protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
//		return new RotateLoadingLayout(context);
//	}

	/**
	 *
	 * 
	 * @return
	 */
	@Override
	protected boolean hasMoreData() {
		if (null != mLoadMoreFooterLayout){
			return mLoadMoreFooterLayout.getState() != State.NO_MORE_DATA;	
		}		
		return super.hasMoreData();
	}

	/**
	 *
	 * 
	 * @return
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = mListView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		int mostTop = (mListView.getChildCount() > 0) ? mListView.getChildAt(0).getTop() : 0;
		if (mostTop >= 0) {
			return true;
		}

		return false;
	}

	/**
	 *
	 * 
	 * @return
	 */
	private boolean isLastItemVisible() {
		final Adapter adapter = mListView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		final int lastItemPosition = adapter.getCount() - 1;
		final int lastVisiblePosition = mListView.getLastVisiblePosition();

		/**
		 * This check should really just be: lastVisiblePosition ==
		 * lastItemPosition, but ListView internally uses a FooterView which
		 * messes the positions up. For me we'll just subtract one to account
		 * for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition >= lastItemPosition) {
			final int childIndex = lastVisiblePosition - mListView.getFirstVisiblePosition();
			final int childCount = mListView.getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = mListView.getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= mListView.getBottom();
			}
		}

		return false;
	}
	
	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (mOnTouchLisener != null) {
			mOnTouchLisener.onTouch(this, event);
		}
		return super.onInterceptTouchEvent(event);
	}
	
	private OnTouchListener mOnTouchLisener;
	
	@Override
	public void setOnTouchListener(OnTouchListener l) {
		mOnTouchLisener = l;
		super.setOnTouchListener(l);
	}
}
