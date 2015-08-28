package com.swishlabs.prototyping.customViews.pullrefresh;

/**
 * UI interface
 * 
 * @author
 * @since
 */
public interface ILoadingLayout {
	/**
	 * current state
	 */
	public enum State {

		/**
		 * Initial state
		 */
		NONE,

		/**
		 * When the UI is in a state which means that user is not interacting
		 * with the Pull-to-Refresh function.
		 */
		RESET,

		/**
		 * When the UI is being pulled by the user, but has not been pulled far
		 * enough so that it refreshes when released.
		 */
		PULL_TO_REFRESH,

		/**
		 * When the UI is being pulled by the user, and <strong>has</strong>
		 * been pulled far enough so that it will refresh when released.
		 */
		RELEASE_TO_REFRESH,

		/**
		 * When the UI is currently refreshing, caused by a pull gesture.
		 */
		REFRESHING,

		/**
		 * When the UI is currently refreshing, caused by a pull gesture.
		 */
		@Deprecated
		LOADING,

		/**
		 * No more data
		 */
		NO_MORE_DATA,

		HAS_MORE_DATA
	}

	/**
	 * current state, sub-Class should change the view accordingly
	 * 
	 * @param state
	 *
	 */
	public void setState(State state);

	/**
	 *
	 * 
	 * @return State
	 */
	public State getState();

	/**
	 * Get the current layout height, which is the threshold for the refresh
	 * 
	 * @return height
	 */
	public int getContentSize();

	/**
	 * Called when pulling
	 * 
	 * @param scale
	 *
	 */
	public void onPull(float scale);
}
