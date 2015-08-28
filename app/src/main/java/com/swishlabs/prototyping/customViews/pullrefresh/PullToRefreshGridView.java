package com.swishlabs.prototyping.customViews.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AbsListView.OnScrollListener;
import android.widget.Adapter;
import android.widget.GridView;
import android.widget.ListView;




/**
 *
 * 
 * @author
 * @since
 */
public class PullToRefreshGridView extends PullToRefreshBase<GridView> implements OnScrollListener {

	/** GridView */
	private GridView mGridView;	
	/**  */
	private OnScrollListener mScrollListener;

	/**
	 *
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshGridView(Context context) {
		this(context, null);
	}

	/**
	 * 构造方法
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshGridView(Context context, AttributeSet attrs) {
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
	protected GridView createRefreshableView(Context context, AttributeSet attrs) {
		GridView gridView = new GridView(context, attrs);
		mGridView = gridView;
		gridView.setOnScrollListener(this);
		return gridView;
	}

	/**
	 *
	 * 
	 * @param hasMoreData
	 *
	 */
	public void setHasMoreData(boolean hasMoreData) {
		super.setHasMoreData(hasMoreData);		
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
	}

	@Override
	public void onPullUpRefreshComplete() {
		super.onPullUpRefreshComplete();
	}

	@Override
	public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
		if (isScrollLoadEnabled() == scrollLoadEnabled) {
			return;
		}
		super.setScrollLoadEnabled(scrollLoadEnabled);

	}

	@Override
	public LoadingLayout getFooterLoadingLayout() {		
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
		return super.hasMoreData();
	}

	/**
	 *
	 * 
	 * @return
	 */
	private boolean isFirstItemVisible() {
		final Adapter adapter = mGridView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		int mostTop = (mGridView.getChildCount() > 0) ? mGridView.getChildAt(0).getTop() : 0;
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
		final Adapter adapter = mGridView.getAdapter();

		if (null == adapter || adapter.isEmpty()) {
			return true;
		}

		final int lastItemPosition = adapter.getCount() - 1;
		final int lastVisiblePosition = mGridView.getLastVisiblePosition();

		/**
		 * This check should really just be: lastVisiblePosition ==
		 * lastItemPosition, but ListView internally uses a FooterView which
		 * messes the positions up. For me we'll just subtract one to account
		 * for it and rely on the inner condition which checks getBottom().
		 */
		if (lastVisiblePosition >= lastItemPosition) {
			final int childIndex = lastVisiblePosition - mGridView.getFirstVisiblePosition();
			final int childCount = mGridView.getChildCount();
			final int index = Math.min(childIndex, childCount - 1);
			final View lastVisibleChild = mGridView.getChildAt(index);
			if (lastVisibleChild != null) {
				return lastVisibleChild.getBottom() <= mGridView.getBottom();
			}
		}

		return false;
	}
}
