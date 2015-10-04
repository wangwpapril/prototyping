package com.swishlabs.prototyping.util;

import android.view.View;
import android.view.animation.Animation;
import android.view.animation.Transformation;
import android.widget.RelativeLayout;

public class ViewAnimation extends Animation {
	private int mFromXType = ABSOLUTE;
    private int mToXType = ABSOLUTE;

    private int mFromYType = ABSOLUTE;
    private int mToYType = ABSOLUTE;

    private float mFromXValue = 0.0f;
    private float mToXValue = 0.0f;

    private float mFromYValue = 0.0f;
    private float mToYValue = 0.0f;

    private float mFromXDelta;
    private float mToXDelta;
    private float mFromYDelta;
    private float mToYDelta;
    private float mFromRotateDeg;
    private float mToRotateDeg;
	private View mView;
	
	public ViewAnimation(View view, 
			float fromXDelta, float toXDelta, 
			float fromYDelta, float toYDelta)
	{
		mView = view;
		
		mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;
        
        mFromRotateDeg = 0;
        mToRotateDeg = 0;

        mFromXType = ABSOLUTE;
        mToXType = ABSOLUTE;
        mFromYType = ABSOLUTE;
        mToYType = ABSOLUTE;
	}
    
	public ViewAnimation(View view, 
			float fromXDelta, float toXDelta, 
			float fromYDelta, float toYDelta,
			float fromRotateDeg, float toRotateDeg)
	{
		mView = view;
		
		mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;
        
        mFromRotateDeg = fromRotateDeg;
        mToRotateDeg = toRotateDeg;

        mFromXType = ABSOLUTE;
        mToXType = ABSOLUTE;
        mFromYType = ABSOLUTE;
        mToYType = ABSOLUTE;
	}
	
	public ViewAnimation(RelativeLayout view, 
			float fromXDelta, int fromXAnimationType, 
			float toXDelta, int toXAnimationType, 
			float fromYDelta, int fromYAnimationType, 
			float toYDelta, int toYAnimationType)
	{
		mView = view;
		
		mFromXValue = fromXDelta;
        mToXValue = toXDelta;
        mFromYValue = fromYDelta;
        mToYValue = toYDelta;

        mFromXType = fromXAnimationType;
        mToXType = toXAnimationType;
        mFromYType = fromYAnimationType;
        mToYType = toYAnimationType;
	}
	
	@Override
    protected void applyTransformation(float interpolatedTime, Transformation t) {
        float dx = mFromXDelta;
        float dy = mFromYDelta;
        
        if (mFromXDelta != mToXDelta) {
            dx = mFromXDelta + ((mToXDelta - mFromXDelta) * interpolatedTime);
        }
        if (mFromYDelta != mToYDelta) {
            dy = mFromYDelta + ((mToYDelta - mFromYDelta) * interpolatedTime);
        }
        if (mFromRotateDeg != mToRotateDeg)
        	mFromRotateDeg = mFromRotateDeg  * 0.1f * interpolatedTime;
        	
        mView.setRotation(mFromRotateDeg);
        mView.setTranslationX(dx);
        mView.setTranslationY(dy);
    }

    @Override
    public void initialize(int width, int height, int parentWidth, int parentHeight) {
        super.initialize(width, height, parentWidth, parentHeight);
        mFromXDelta = resolveSize(mFromXType, mFromXValue, width, parentWidth);
        mToXDelta = resolveSize(mToXType, mToXValue, width, parentWidth);
        mFromYDelta = resolveSize(mFromYType, mFromYValue, height, parentHeight);
        mToYDelta = resolveSize(mToYType, mToYValue, height, parentHeight);
    }

}


