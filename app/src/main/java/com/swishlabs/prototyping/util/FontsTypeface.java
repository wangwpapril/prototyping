package com.swishlabs.prototyping.util;

import android.graphics.Typeface;

public class FontsTypeface {
	public static String RobotoMono_Medium = "RobotoMono_Medium";
	public static String Roboto_Regular = "Roboto_Regular";
	public static String Roboto_Medium = "Roboto_Medium";
	public static String Roboto_Bold = "Roboto_Bold";
//	public static String HelveticaNeue_Regular = "HelveticaNeue_Regular";
//	public static String HelveticaNeue_UltraLight = "HelveticaNeue_UltraLight";

	public Typeface typeFace;
	public String name;
	
	public FontsTypeface(Typeface typeFace, String name)
	{
		this.typeFace = typeFace;
		this.name = name;
	}

}
