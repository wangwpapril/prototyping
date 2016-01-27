package com.swishlabs.prototyping.util;

import android.content.res.AssetManager;
import android.graphics.Typeface;
import android.util.Log;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class TypefaceManager {
	
	private static List<FontsTypeface> typeFaces;
	private AssetManager mAssetManager;
	private static TypefaceManager mTypefaceManager;

	public TypefaceManager(AssetManager assetManager)
	{
		mAssetManager = assetManager;
		
		if (!init())
			Log.d("error", "cannot load fonts");
	}
	
	public static TypefaceManager GetInstance(AssetManager assetManger)
	{
		if (mTypefaceManager == null)
			mTypefaceManager = new TypefaceManager(assetManger);
		
		return mTypefaceManager;
	}
	
	public List<FontsTypeface> getTypefaces()
	{
		return typeFaces;
	}
	
	public Typeface getTypeFace(String name)
	{
		for (int i=0; i<typeFaces.size(); i++)
		{
			if (typeFaces.get(i).name.equals(name))
				return typeFaces.get(i).typeFace;
		}
		return null;
	}
	
	private boolean init()
	{
		typeFaces = new ArrayList<FontsTypeface>();
		if (mAssetManager != null)
		{
			String[] fonts;
			try {
				fonts = mAssetManager.list("fonts");
				
				for (int i=0; i< fonts.length;i++)
				{
					Typeface tmpTypeFace = Typeface.createFromAsset(mAssetManager, "fonts/"+fonts[i]);
					String name = fonts[i].substring(0, fonts[i].indexOf('.'));
					typeFaces.add(new FontsTypeface(tmpTypeFace, name));
				}
				return true;
			} catch (IOException e) {
				e.printStackTrace();
				return false;
			}
		}
		return false;
	}

}
