package com.swishlabs.prototyping.customViews.pullrefresh;





import android.view.View;


/**
 * Interface for refresh
 * 
 * @author
 * @since
 * @param <T>
 */
public interface IPullToRefresh<T extends View> {
    
    /**
     * enable the down refresh
     * 
     * @param pullRefreshEnabled
     */
    public void setPullRefreshEnabled(boolean pullRefreshEnabled);
    
    /**
     * enable the up fresh
     * 
     * @param pullLoadEnabled
     */
    public void setPullLoadEnabled(boolean pullLoadEnabled);
    
    /**
     * whether loading more data automatically when scroll to the bottome
     * 
     * @param scrollLoadEnabled if ture, the up fresh will be disabled
     */
    public void setScrollLoadEnabled(boolean scrollLoadEnabled);
    
    /**
     *
     * 
     * @return
     */
    public boolean isPullRefreshEnabled();
    
    /**
     *
     * 
     * @return
     */
    public boolean isPullLoadEnabled();
    
    /**
     *
     * 
     * @return true
     */
    public boolean isScrollLoadEnabled();
    
    /**
     * set refresh listener
     * 
     * @param refreshListener
     */
    public void setOnRefreshListener(PullToRefreshBase.OnRefreshListener<T> refreshListener);
    
    /**
     * down refresh complete
     */
    public void onPullDownRefreshComplete();
    
    /**
     * up refresh complete
     */
    public void onPullUpRefreshComplete();
    
    /**
     * get the refreshable view
     * 
     * @return {@link #createRefreshableView(Context, AttributeSet)}
     */
    public T getRefreshableView();
    
    /**
     * get Header layout
     * 
     * @return Header
     */
    public LoadingLayout getHeaderLoadingLayout();
    
    /**
     * get Footer layout
     * 
     * @return Footer
     */
    public LoadingLayout getFooterLoadingLayout();
    
    /**
     * set the text of the last updated time
     * 
     * @param label
     */
    public void setLastUpdatedLabel(CharSequence label);
}
