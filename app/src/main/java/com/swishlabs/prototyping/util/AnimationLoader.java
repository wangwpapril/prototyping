package com.swishlabs.prototyping.util;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.res.AssetManager;
import android.graphics.drawable.AnimationDrawable;
import android.graphics.drawable.Drawable;
import android.os.Handler;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;

public class AnimationLoader extends AnimationDrawable {
	
	private Activity mActivity;
	private View mView;
	private String _dir;
	private AssetManager mAssetManager;
	private int _duration;
	private int _totalTasks;
	private String[] listImages;
	private AnimationDrawable activeDrawable;
	private int _loadPortion;
	private int _totalDuration;
	private int _currentTasks;
	private int _currentTotalDuration;
	private Handler mAnimationHandler;
	private boolean _isFinish;
	private boolean _needToLoadNextOne;
	private int _portion;

	protected AnimationLoader(Activity activity)
	{
		mActivity = activity;
		_isFinish = false;
		_totalDuration = 0;
		_duration = 100;
		_totalTasks = 1;
		_needToLoadNextOne = false;
	}
	
	@SuppressLint("NewApi")
	private void init()
	{
		mAssetManager = mActivity.getAssets();
		
		try {
			listImages = mAssetManager.list(_dir+"");
			
			if (_totalTasks == 1)
			{
				for (int i=0; i<listImages.length; i++)
				{
					InputStream inStr = mAssetManager.open(_dir + "/" + listImages[i]);
					
					Drawable tmpDrawable = Drawable.createFromStream(inStr, listImages[i]);
					this.addFrame(tmpDrawable, _duration);
					_totalDuration  += _duration;
				}
				mView.setBackground(this);
				mView.invalidate();
			}
			else
			{
				_portion = listImages.length / _currentTasks;
				
				activeDrawable = loadImages(_portion, _duration);
				
				_loadPortion = _portion;
				mView.setBackground(activeDrawable);
				mView.invalidate();
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}	
	
	public void nextTask()
	{
		getNextTask(_duration);
	}
	
	public void nextTask(int duration)
	{
		_duration = duration;
		getNextTask(_duration);
	}
	
	public void lastTask()
	{
		_currentTasks = 1;
		redrawAnimationDrawable(_duration);
	}
	
	public void lastTask(int duration)
	{
		_currentTasks = 1;
		_duration = duration;
		redrawAnimationDrawable(_duration);
	}
	
	@Override
	public void start() {
		if (_totalTasks == 1)
			super.start();
		else
			activeDrawable.start();
		
		setHandler();
	}
	
	private void setHandler()
	{
		mAnimationHandler = new Handler();
        mAnimationHandler.postDelayed(new Runnable() {

            public void run() {
                onAnimationFinish();
            }
        }, (_totalDuration == 1) ? _totalDuration : _currentTotalDuration);
	}
	
	@SuppressLint("NewApi")
	private void onAnimationFinish()
	{
		_isFinish = true;
		
		if (_needToLoadNextOne)
		{
			if (activeDrawable != null && activeDrawable.getNumberOfFrames() > 1)
			{
				if (_isFinish)
				{
					_currentTotalDuration = 0;
					activeDrawable.stop();
					
					int porLoadImage = _loadPortion + _portion;
					
					activeDrawable = loadImages(porLoadImage, _duration);
					
					mView.setBackground(activeDrawable);
					mView.invalidate();
					_needToLoadNextOne = false;
					_loadPortion += _portion;
					
					this.start();
				}
			}
		}
	}

	private void getNextTask(int duration)
	{
		if (_totalTasks > 0 && _currentTasks > 0)
			_currentTasks -= 1;
		
		redrawAnimationDrawable(duration);
	}
	
	@SuppressLint("NewApi")
	private void redrawAnimationDrawable(int duration)
	{
		if (activeDrawable != null && activeDrawable.getNumberOfFrames() > 1)
		{
			if (_isFinish)
			{
				if (_currentTasks > 1)
				{
					_currentTotalDuration = 0;
					activeDrawable.stop();
					
					int porLoadImage = _loadPortion + _portion;
					
					activeDrawable = loadImages(porLoadImage, duration);
					
					mView.setBackground(activeDrawable);
					mView.invalidate();
					_needToLoadNextOne = false;
					_loadPortion += _portion;
				}
				else
				{
					_currentTotalDuration = 0;
					activeDrawable.stop();
					
					activeDrawable = loadImages(listImages.length, duration);
					
					mView.setBackground(activeDrawable);
					mView.invalidate();
					_needToLoadNextOne = false;
					_loadPortion += _portion;
				}
				
				
				this.start();
			}
			else
				_needToLoadNextOne = true;
		}
	}
	
	private AnimationDrawable loadImages(int portion, int duration)
	{
		AnimationDrawable drawable = new AnimationDrawable();
		for (int i=_loadPortion; i < portion; i++)
		{
			InputStream inStr;
			try {
				inStr = mAssetManager.open(_dir + "/" + listImages[i]);
				Drawable tmpDrawable = Drawable.createFromStream(inStr, listImages[i]);
				drawable.addFrame(tmpDrawable, duration);
				_currentTotalDuration += duration;
				
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		_isFinish = false;
		return drawable;
	}
	
	public boolean isFinished()
	{
		return _isFinish;
	}
	
	protected void setView(View view)
	{
		mView = view;
	}
	protected void setDirectory(String dir)
	{
		_dir = dir;
	}
	protected void setRepeat(boolean repeat)
	{
		if (_totalTasks > 1)
			this.setOneShot(true);
		else
			this.setOneShot(repeat);
	}
	protected void setDuration(int duration)
	{
		_duration = duration;
	}
	protected void setTotalTask(int tasks)
	{
		_totalTasks = tasks;
		_currentTasks = _totalTasks;
		if (_totalTasks > 1)
			this.setOneShot(true);
	}
	
	//Builder
	public static class Builder
	{
		AnimationLoader animLoad = null;
		
		public Builder (Activity activity)
		{
			animLoad = new AnimationLoader(activity);
		}
		
		public void setView (View view)
		{
			animLoad.setView(view);
		}
		
		public void setPath(String path)
		{
			animLoad.setDirectory(path);
		}
		
		public void setRepeat(boolean repeat)
		{
			animLoad.setRepeat(!repeat);
		}
		
		public void setDuration(int duration)
		{
			animLoad.setDuration(duration);
		}
		
		public void setTotalTask(int tasks)
		{
			animLoad.setTotalTask(tasks);
		}
		
		public AnimationLoader build()
		{
			animLoad.init();
			return animLoad;
		}
	}

}
