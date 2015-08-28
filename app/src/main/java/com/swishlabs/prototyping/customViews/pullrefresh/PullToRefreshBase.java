package com.swishlabs.prototyping.customViews.pullrefresh;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewConfiguration;
import android.view.ViewGroup;
import android.view.ViewTreeObserver.OnGlobalLayoutListener;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.Interpolator;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import com.swishlabs.prototyping.customViews.pullrefresh.ILoadingLayout.State;

/**
 *
 * 
 * @author
 * @since
 * @param <T>
 */
public abstract class PullToRefreshBase<T extends View> extends LinearLayout implements IPullToRefresh<T> {
	/**
	 *
	 * 
	 * @author
	 * @since
	 */
	public interface OnRefreshListener<V extends View> {

		/**
		 *
		 * 
		 * @param refreshView
		 *
		 */
		void onPullDownToRefresh(final PullToRefreshBase<V> refreshView);

		/**
		 *
		 * 
		 * @param refreshView
		 *
		 */
		void onPullUpToRefresh(final PullToRefreshBase<V> refreshView);
	}

	/** duration for scroll back  */
	private static final int SCROLL_DURATION = 150;
	/**  */
	private static final float OFFSET_RADIO = 2.5f;
	/**  */
	private float mLastMotionY = -1;
	/**  */
	private float mLastDownMotionY = -1;
	/**  */
	private OnRefreshListener<T> mRefreshListener;
	/**  */
	private LoadingLayout mHeaderLayout;
	/**  */
	private LoadingLayout mFooterLayout;
	/**  */
	private int mHeaderHeight;
	/**  */
	private int mFooterHeight;
	/**  */
	private boolean mPullRefreshEnabled = true;
	/**  */
	private boolean mPullLoadEnabled = true;
	/**  */
	private boolean mScrollLoadEnabled = false;
	/**  */
	private boolean mInterceptEventEnable = true;
	/** if consume the touch event, if yes, will not call the parent's onTouchEvent */
	private boolean mIsHandledTouchEvent = false;
	/**  */
	private int mTouchSlop;
	/**  */
	private ILoadingLayout.State mPullDownState = ILoadingLayout.State.NONE;
	/**  */
	private ILoadingLayout.State mPullUpState = ILoadingLayout.State.NONE;
	/**  */
	T mRefreshableView;
	/**  */
	private SmoothScrollRunnable mSmoothScrollRunnable;
	/**  */
	private FrameLayout mRefreshableViewWrapper;

	/**
	 *
	 * 
	 * @param context
	 *            context
	 */
	public PullToRefreshBase(Context context) {
		super(context);
		init(context, null);
	}

	/**
	 *
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *            attrs
	 */
	public PullToRefreshBase(Context context, AttributeSet attrs) {
		super(context, attrs);
		init(context, attrs);
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
	 * public PullToRefreshBase(Context context, AttributeSet attrs, int
	 * defStyle) { super(context, attrs, defStyle); init(context, attrs); }
	 */
	/**
	 *
	 * 
	 * @param context
	 *            context
	 */
	private void init(Context context, AttributeSet attrs) {
		setOrientation(LinearLayout.VERTICAL);

		mTouchSlop = ViewConfiguration.get(context).getScaledTouchSlop();

		mHeaderLayout = createHeaderLoadingLayout(context, attrs);
		mFooterLayout = createFooterLoadingLayout(context, attrs);
		mRefreshableView = createRefreshableView(context, attrs);

		if (null == mRefreshableView) {
			throw new NullPointerException("Refreshable view can not be null.");
		}

		addRefreshableView(context, mRefreshableView);
		addHeaderAndFooter(context);

		// get the height of the Header, it will return 0 in the onLayout()
		getViewTreeObserver().addOnGlobalLayoutListener(new OnGlobalLayoutListener() {
			@Override
			public void onGlobalLayout() {
				refreshLoadingViewsSize();
				getViewTreeObserver().removeGlobalOnLayoutListener(this);
			}
		});
	}

	/**
	 * initialize padding，set top padding and bottom padding according to the height of header and footer
	 */
	private void refreshLoadingViewsSize() {
		//
		int headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getContentSize() : 0;
		int footerHeight = (null != mFooterLayout) ? mFooterLayout.getContentSize() : 0;

		if (headerHeight < 0) {
			headerHeight = 0;
		}

		if (footerHeight < 0) {
			footerHeight = 0;
		}

		mHeaderHeight = headerHeight;
		mFooterHeight = footerHeight;

		//
		headerHeight = (null != mHeaderLayout) ? mHeaderLayout.getMeasuredHeight() : 0;
		footerHeight = (null != mFooterLayout) ? mFooterLayout.getMeasuredHeight() : 0;
		if (0 == footerHeight) {
			footerHeight = mFooterHeight;
		}

		int pLeft = getPaddingLeft();
		int pTop = getPaddingTop();
		int pRight = getPaddingRight();
		int pBottom = getPaddingBottom();

		pTop = -headerHeight;
		pBottom = -footerHeight;

		setPadding(pLeft, pTop, pRight, pBottom);
	}

	@Override
	protected final void onSizeChanged(int w, int h, int oldw, int oldh) {
		super.onSizeChanged(w, h, oldw, oldh);

		// We need to update the header/footer when our size changes
		refreshLoadingViewsSize();

		// set the size of the view
		refreshRefreshableViewSize(w, h);

		/**
		 * As we're currently in a Layout Pass, we need to schedule another one
		 * to layout any changes we've made here
		 */
		post(new Runnable() {
			@Override
			public void run() {
				requestLayout();
			}
		});
	}

	@Override
	public void setOrientation(int orientation) {
		if (LinearLayout.VERTICAL != orientation) {
			throw new IllegalArgumentException("This class only supports VERTICAL orientation.");
		}

		// Only support vertical orientation
		super.setOrientation(orientation);
	}

	@Override
	public boolean onInterceptTouchEvent(MotionEvent event) {
		if (!isInterceptTouchEventEnabled()) {
			return false;
		}

		if (!isPullLoadEnabled() && !isPullRefreshEnabled()) {
			return false;
		}

		final int action = event.getAction();
		if (action == MotionEvent.ACTION_CANCEL || action == MotionEvent.ACTION_UP) {
			mIsHandledTouchEvent = false;
			return false;
		}

		if (action != MotionEvent.ACTION_DOWN && mIsHandledTouchEvent) {
			return true;
		}

		switch (action) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = event.getY();
			mLastDownMotionY = event.getY();
			mIsHandledTouchEvent = false;
			break;

		case MotionEvent.ACTION_MOVE:
			final float deltaY = event.getY() - mLastMotionY;
			final float absDiff = Math.abs(deltaY);
			// 3 conditions：
			// 1，> mTouchSlop，prevent pull quickly
			// 2，isPullRefreshing()，if pull down now, permit scroll up and move Header
			// 3，isPullLoading()，same as no.2
			if (absDiff > mTouchSlop || isPullRefreshing() || isPullLoading()) {
				mLastMotionY = event.getY();
				// the 1st one displayed , Header already displayed
				if (isPullRefreshEnabled() && isReadyForPullDown()) {
					// 1，Math.abs(getScrollY()) >
					// 0：indicate that either HeaderView show up or invisible completely
					// 2，deltaY > 0.5f：
					mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY > 0.5f);
					//
					if (mIsHandledTouchEvent) {
						mRefreshableView.onTouchEvent(event);
					}
				} else if (isPullLoadEnabled() && isReadyForPullUp()) {
					//
					mIsHandledTouchEvent = (Math.abs(getScrollYValue()) > 0 || deltaY < -0.5f);
				}
			}
			break;

		default:
			break;
		}

		return mIsHandledTouchEvent;
	}

	@Override
	public boolean onTouchEvent(MotionEvent ev) {
		boolean handled = false;
		switch (ev.getAction()) {
		case MotionEvent.ACTION_DOWN:
			mLastMotionY = ev.getY();
			mIsHandledTouchEvent = false;
			break;

		case MotionEvent.ACTION_MOVE:
			final float deltaY = ev.getY() - mLastMotionY;
			mLastMotionY = ev.getY();
			if (isPullRefreshEnabled() && isReadyForPullDown()) {
				pullHeaderLayout(deltaY / OFFSET_RADIO);
				handled = true;
			} else if (isPullLoadEnabled() && isReadyForPullUp()) {
				pullFooterLayout(deltaY / OFFSET_RADIO);
				handled = true;
			} else {
				mIsHandledTouchEvent = false;
			}
			break;

		case MotionEvent.ACTION_CANCEL:
		case MotionEvent.ACTION_UP:
			if (mIsHandledTouchEvent) {
				mIsHandledTouchEvent = false;
				// when the first displayed
				if (isReadyForPullDown()) {
					// refresh
					if (mPullRefreshEnabled && (mPullDownState == ILoadingLayout.State.RELEASE_TO_REFRESH)) {
						startRefreshing();
						handled = true;
					}
					resetHeaderLayout();
				} else if (isReadyForPullUp()) {
					// load more
					if (isPullLoadEnabled() && (mPullUpState == ILoadingLayout.State.RELEASE_TO_REFRESH)) {
						startLoading();
						handled = true;
					}
					resetFooterLayout();
				}
			}
			break;

		default:
			break;
		}

		return handled;
	}

	@Override
	public void setPullRefreshEnabled(boolean pullRefreshEnabled) {
		mPullRefreshEnabled = pullRefreshEnabled;
	}

	@Override
	public void setPullLoadEnabled(boolean pullLoadEnabled) {
		mPullLoadEnabled = pullLoadEnabled;
	}

	@Override
	public void setScrollLoadEnabled(boolean scrollLoadEnabled) {
		mScrollLoadEnabled = scrollLoadEnabled;
	}

	@Override
	public boolean isPullRefreshEnabled() {
		return mPullRefreshEnabled && (null != mHeaderLayout);
	}

	@Override
	public boolean isPullLoadEnabled() {
		return mPullLoadEnabled && (null != mFooterLayout);
	}

	@Override
	public boolean isScrollLoadEnabled() {
		return mScrollLoadEnabled;
	}

	@Override
	public void setOnRefreshListener(OnRefreshListener<T> refreshListener) {
		mRefreshListener = refreshListener;
	}

	@Override
	public void onPullDownRefreshComplete() {
		if (isPullRefreshing()) {
			mPullDownState = State.RESET;
			onStateChanged(State.RESET, true);

			// scrolling back take some time, we will set the state to normal after it complete
			// before normal state, don't intercept touch event
			postDelayed(new Runnable() {
				@Override
				public void run() {
					setInterceptTouchEventEnabled(true);
					mHeaderLayout.setState(State.RESET);
				}
			}, getSmoothScrollDuration());

			resetHeaderLayout();
			setInterceptTouchEventEnabled(false);
		}
	}

	@Override
	public void onPullUpRefreshComplete() {
		if (isPullLoading()) {
			mPullUpState = ILoadingLayout.State.RESET;
			onStateChanged(ILoadingLayout.State.RESET, false);

			postDelayed(new Runnable() {
				@Override
				public void run() {
					setInterceptTouchEventEnabled(true);
					mFooterLayout.setState(ILoadingLayout.State.RESET);
				}
			}, getSmoothScrollDuration());

			resetFooterLayout();
			setInterceptTouchEventEnabled(false);
		}
	}

	@Override
	public T getRefreshableView() {
		return mRefreshableView;
	}

	@Override
	public LoadingLayout getHeaderLoadingLayout() {
		return mHeaderLayout;
	}

	@Override
	public LoadingLayout getFooterLoadingLayout() {
		return mFooterLayout;
	}

	@Override
	public void setLastUpdatedLabel(CharSequence label) {
		if (null != mHeaderLayout) {
			mHeaderLayout.setLastUpdatedLabel(label);
		}

		if (null != mFooterLayout) {
			mFooterLayout.setLastUpdatedLabel(label);
		}
	}

	public void doPullRefreshing(boolean smoothScroll) {
		doPullRefreshing(smoothScroll, 0);
	}

	/**
	 * not by pull , but typically when the first time enter the screen
	 * 
	 * @param smoothScroll
	 *            whether smooth scrolling
	 * @param delayMillis
	 *
	 */
	public void doPullRefreshing(final boolean smoothScroll, final long delayMillis) {
		postDelayed(new Runnable() {
			@Override
			public void run() {
				int newScrollValue = -mHeaderHeight;
				int duration = smoothScroll ? SCROLL_DURATION : 0;

				startRefreshing();
				smoothScrollTo(newScrollValue, duration, 0);
			}
		}, delayMillis);
	}

	/**
	 *
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *
	 * @return View
	 */
	protected abstract T createRefreshableView(Context context, AttributeSet attrs);

	/**
	 * check if the view is scrolled to the top
	 * 
	 * @return
	 */
	protected abstract boolean isReadyForPullDown();

	/**
	 * check if the view is scrolled to the bottom
	 * 
	 * @return
	 */
	protected abstract boolean isReadyForPullUp();

	/**
	 *
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *
	 * @return LoadingLayout
	 */
	protected LoadingLayout createHeaderLoadingLayout(Context context, AttributeSet attrs) {
		return new HeaderLoadingLayout(context);
	}

	/**
	 *
	 * 
	 * @param context
	 *            context
	 * @param attrs
	 *
	 * @return LoadingLayout
	 */
	protected LoadingLayout createFooterLoadingLayout(Context context, AttributeSet attrs) {
		return new FooterLoadingLayout(context);
	}

	/**
	 *
	 * 
	 * @return milli second
	 */
	protected long getSmoothScrollDuration() {
		return SCROLL_DURATION;
	}

	/**
	 * calculate the size of the view
	 * 
	 * @param width
	 *
	 * @param height
	 *
	 */
	protected void refreshRefreshableViewSize(int width, int height) {
		if (null != mRefreshableViewWrapper) {
			LayoutParams lp = (LayoutParams) mRefreshableViewWrapper.getLayoutParams();
			if (lp.height != height) {
				lp.height = height;
				mRefreshableViewWrapper.requestLayout();
			}
		}
	}

	/**
	 * add view to the container
	 * 
	 * @param context
	 *            context
	 * @param refreshableView
	 *
	 */
	protected void addRefreshableView(Context context, T refreshableView) {
		int width = ViewGroup.LayoutParams.MATCH_PARENT;
		int height = ViewGroup.LayoutParams.MATCH_PARENT;

		// create a wrap container
		mRefreshableViewWrapper = new FrameLayout(context);
		mRefreshableViewWrapper.addView(refreshableView, width, height);

		// set the height of Refresh view to a very small value，it will be set to MATCH_PARENT in onSizeChanged()
		// the reason is if at this time its height is MATCH_PARENT，then the height of footer wil be 0
		height = 10;
		addView(mRefreshableViewWrapper, new LayoutParams(width, height));
	}

	/**
	 *
	 * 
	 * @param context
	 *            context
	 */
	protected void addHeaderAndFooter(Context context) {
		LayoutParams params = new LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);

		final LoadingLayout headerLayout = mHeaderLayout;
		final LoadingLayout footerLayout = mFooterLayout;

		if (null != headerLayout) {
			if (this == headerLayout.getParent()) {
				removeView(headerLayout);
			}

			addView(headerLayout, 0, params);
		}

		if (null != footerLayout) {
			if (this == footerLayout.getParent()) {
				removeView(footerLayout);
			}

			addView(footerLayout, -1, params);
		}
	}

	/**
	 * Pull Header Layout
	 * 
	 * @param delta
	 *            distance moved
	 */
	protected void pullHeaderLayout(float delta) {
		// scroll up, no scroll when current scrollY is 0
		int oldScrollY = getScrollYValue();
		if (delta < 0 && (oldScrollY - delta) >= 0) {
			setScrollTo(0, 0);
			return;
		}

		// scroll down
		setScrollBy(0, -(int) delta);

		// if (delta < 0 && getScrollYValue() > 0) {
		// setScrollTo(0, 0);
		// }

		if (null != mHeaderLayout && 0 != mHeaderHeight) {
			float scale = Math.abs(getScrollYValue()) / (float) mHeaderHeight;
			mHeaderLayout.onPull(scale);
		}

		// not in refreshing state, update the arrow
		int scrollY = Math.abs(getScrollYValue());
		if (isPullRefreshEnabled() && !isPullRefreshing()) {
			if (scrollY > mHeaderHeight) {
				mPullDownState = State.RELEASE_TO_REFRESH;
			} else {
				mPullDownState = State.PULL_TO_REFRESH;
			}

			mHeaderLayout.setState(mPullDownState);
			onStateChanged(mPullDownState, true);
		}
	}

	/**
	 *
	 * 
	 * @param delta
	 *            distance moved
	 */
	protected void pullFooterLayout(float delta) {
		int oldScrollY = getScrollYValue();
		if (delta > 0 && (oldScrollY - delta) <= 0) {
			setScrollTo(0, 0);
			return;
		}

		setScrollBy(0, -(int) delta);

		// if (delta > 0 && getScrollYValue() < 0) {
		// setScrollTo(0, 0);
		// }

		if (null != mFooterLayout && 0 != mFooterHeight) {
			float scale = Math.abs(getScrollYValue()) / (float) mFooterHeight;
			mFooterLayout.onPull(scale);
		}

		int scrollY = Math.abs(getScrollYValue());
		if (isPullLoadEnabled() && !isPullLoading()) {
			if (scrollY > mFooterHeight) {
				mPullUpState = ILoadingLayout.State.RELEASE_TO_REFRESH;
			} else {
				mPullUpState = ILoadingLayout.State.PULL_TO_REFRESH;
			}

			mFooterLayout.setState(mPullUpState);
			onStateChanged(mPullUpState, false);
		}
	}

	/**
	 *
	 */
	protected void resetHeaderLayout() {
		final int scrollY = Math.abs(getScrollYValue());
		final boolean refreshing = isPullRefreshing();

		if (refreshing && scrollY <= mHeaderHeight) {
			smoothScrollTo(0);
			return;
		}

		if (refreshing) {
			smoothScrollTo(-mHeaderHeight);
		} else {
			smoothScrollTo(0);
		}
	}

	/**
	 *
	 */
	protected void resetFooterLayout() {
		int scrollY = Math.abs(getScrollYValue());
		boolean isPullLoading = isPullLoading();

		if (isPullLoading && scrollY <= mFooterHeight) {
			smoothScrollTo(0);
			return;
		}

		if (isPullLoading) {
			smoothScrollTo(mFooterHeight);
		} else {
			smoothScrollTo(0);
		}
	}

	/**
	 *
	 * 
	 * @return
	 */
	protected boolean isPullRefreshing() {
		return (mPullDownState == State.REFRESHING);
	}

	/**
	 *
	 * 
	 * @return
	 */
	protected boolean isPullLoading() {
		return (mPullUpState == State.REFRESHING);
	}

	/**
	 *
	 */
	protected void startRefreshing() {
		//
		if (isPullRefreshing()) {
			return;
		}

		mPullDownState = State.REFRESHING;
		onStateChanged(State.REFRESHING, true);

		if (null != mHeaderLayout) {
			mHeaderLayout.setState(State.REFRESHING);
		}

		if (null != mRefreshListener) {
			// because it will take 200 to scroll back to the original location, so we need to wait until the complete
			postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefreshListener.onPullDownToRefresh(PullToRefreshBase.this);
				}
			}, getSmoothScrollDuration());
		}
	}

	/**
	 *
	 */
	protected void startLoading() {
		//
		if (isPullLoading()) {
			return;
		}

		mPullUpState = State.REFRESHING;
		onStateChanged(State.REFRESHING, false);

		if (null != mFooterLayout) {
			mFooterLayout.setState(State.REFRESHING);
		}

		if (null != mRefreshListener) {
			// because it will take 200 to scroll back to the original location, so we need to wait until the complete
			postDelayed(new Runnable() {
				@Override
				public void run() {
					mRefreshListener.onPullUpToRefresh(PullToRefreshBase.this);
				}
			}, getSmoothScrollDuration());
		}
	}

	/**
	 *
	 * 
	 * @param state
	 *
	 * @param isPullDown
	 *
	 */
	protected void onStateChanged(ILoadingLayout.State state, boolean isPullDown) {

	}

	/**
	 *
	 * 
	 * @param x
	 *
	 * @param y
	 *
	 */
	private void setScrollTo(int x, int y) {
		scrollTo(x, y);
	}

	/**
	 *
	 * 
	 * @param x
	 *
	 * @param y
	 *
	 */
	private void setScrollBy(int x, int y) {
		scrollBy(x, y);
	}

	/**
	 *
	 * 
	 * @return
	 */
	private int getScrollYValue() {
		return getScrollY();
	}

	/**
	 *
	 * 
	 * @param newScrollValue
	 *
	 */
	private void smoothScrollTo(int newScrollValue) {
		smoothScrollTo(newScrollValue, getSmoothScrollDuration(), 0);
	}

	/**
	 *
	 * 
	 * @param newScrollValue
	 *
	 * @param duration
	 *
	 * @param delayMillis
	 *
	 */
	private void smoothScrollTo(int newScrollValue, long duration, long delayMillis) {
		if (null != mSmoothScrollRunnable) {
			mSmoothScrollRunnable.stop();
		}

		int oldScrollValue = this.getScrollYValue();
		boolean post = (oldScrollValue != newScrollValue);
		if (post) {
			mSmoothScrollRunnable = new SmoothScrollRunnable(oldScrollValue, newScrollValue, duration);
		}

		if (post) {
			if (delayMillis > 0) {
				postDelayed(mSmoothScrollRunnable, delayMillis);
			} else {
				post(mSmoothScrollRunnable);
			}
		}
	}

	/**
	 *
	 * 
	 * @param enabled
	 *            true for intercept，false no
	 */
	private void setInterceptTouchEventEnabled(boolean enabled) {
		mInterceptEventEnable = enabled;
	}

	/**
	 *
	 * 
	 * @return true for intercept ，false no
	 */
	private boolean isInterceptTouchEventEnabled() {
		return mInterceptEventEnable;
	}

	/**
	 *
	 * 
	 * @author
	 * @since
	 */
	final class SmoothScrollRunnable implements Runnable {
		/** animation effect */
		private final Interpolator mInterpolator;
		/**  */
		private final int mScrollToY;
		/**  */
		private final int mScrollFromY;
		/**  */
		private final long mDuration;
		/**  */
		private boolean mContinueRunning = true;
		/**  */
		private long mStartTime = -1;
		/**  */
		private int mCurrentY = -1;

		/**
		 *
		 * 
		 * @param fromY
		 *
		 * @param toY
		 *
		 * @param duration
		 *            animation duration
		 */
		public SmoothScrollRunnable(int fromY, int toY, long duration) {
			mScrollFromY = fromY;
			mScrollToY = toY;
			mDuration = duration;
			mInterpolator = new DecelerateInterpolator();
		}

		@Override
		public void run() {
			/**
			 * If the duration is 0, we scroll the view to target y directly.
			 */
			if (mDuration <= 0) {
				setScrollTo(0, mScrollToY);
				return;
			}

			/**
			 * Only set mStartTime if this is the first time we're starting,
			 * else actually calculate the Y delta
			 */
			if (mStartTime == -1) {
				mStartTime = System.currentTimeMillis();
			} else {

				/**
				 * We do do all calculations in long to reduce software float
				 * calculations. We use 1000 as it gives us good accuracy and
				 * small rounding errors
				 */
				final long oneSecond = 1000; // SUPPRESS CHECKSTYLE
				long normalizedTime = (oneSecond * (System.currentTimeMillis() - mStartTime)) / mDuration;
				normalizedTime = Math.max(Math.min(normalizedTime, oneSecond), 0);

				final int deltaY = Math.round((mScrollFromY - mScrollToY)
						* mInterpolator.getInterpolation(normalizedTime / (float) oneSecond));
				mCurrentY = mScrollFromY - deltaY;

				setScrollTo(0, mCurrentY);
			}

			// If we're not at the target Y, keep going...
			if (mContinueRunning && mScrollToY != mCurrentY) {
				PullToRefreshBase.this.postDelayed(this, 16);// SUPPRESS
																// CHECKSTYLE
			}
		}

		/**
		 * stop scrolling
		 */
		public void stop() {
			mContinueRunning = false;
			removeCallbacks(this);
		}
	}

	/**
	 *
	 * 
	 * @return
	 */
	protected boolean hasMoreData() {
		return mFooterLayout.getState() != State.NO_MORE_DATA;
	}

	/**
	 *
	 * 
	 * @param hasMoreData
	 *
	 */
	public void setHasMoreData(boolean hasMoreData) {
		if (!hasMoreData) {
			mFooterLayout.setState(State.NO_MORE_DATA);
		} else {
			mFooterLayout.setState(State.HAS_MORE_DATA);
		}
	}
}
