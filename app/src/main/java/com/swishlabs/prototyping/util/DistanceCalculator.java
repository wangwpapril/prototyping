package com.swishlabs.prototyping.util;

import com.google.android.gms.maps.model.LatLng;
import com.swishlabs.prototyping.entity.UserProfilePrefs;

import java.text.DecimalFormat;

public class DistanceCalculator {
	
	private static double lat1;
	private static double lng1;
	private static double lat2;
	private static double lng2;

	public static double distFromInMeters(LatLng otherProfileLocation) {
		if (UserProfilePrefs.getInstance().getMyLocation() != null) {
			lat1 = UserProfilePrefs.getInstance().getMyLocation().getLatitude();
			lng1 = UserProfilePrefs.getInstance().getMyLocation().getLongitude();
		}else {
			return 0;
		}
		
		lat2 = otherProfileLocation.latitude;
		lng2 = otherProfileLocation.longitude;
		
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);
	    
	    DecimalFormat precision = new DecimalFormat("0.00"); 
	    return Double.parseDouble(precision.format(dist));
    }
	
	public static double distFromInMiles(LatLng otherProfileLocation) {
		if (UserProfilePrefs.getInstance().getMyLocation() != null) {
			lat1 = UserProfilePrefs.getInstance().getMyLocation().getLatitude();
			lng1 = UserProfilePrefs.getInstance().getMyLocation().getLongitude();
		}else {
			return 0;
		}
		
		lat2 = otherProfileLocation.latitude;
		lng2 = otherProfileLocation.longitude;
		
		if (lat1 == 0 || lng1 == 0 || lat2 == 0 || lng2 == 0) return 0;
		
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);
	    
	    DecimalFormat precision = new DecimalFormat("0.00"); 
	    return Double.parseDouble(precision.format(dist * 0.000621371192));
    }
	
	public static double distFromInKilometers(LatLng otherProfileLocation) {
		if (UserProfilePrefs.getInstance().getMyLocation() != null) {
			lat1 = UserProfilePrefs.getInstance().getMyLocation().getLatitude();
			lng1 = UserProfilePrefs.getInstance().getMyLocation().getLongitude();
		}else {
			return 0;
		}
		
		lat2 = otherProfileLocation.latitude;
		lng2 = otherProfileLocation.longitude;
		
	    double earthRadius = 6371000; //meters
	    double dLat = Math.toRadians(lat2-lat1);
	    double dLng = Math.toRadians(lng2-lng1);
	    double a = Math.sin(dLat/2) * Math.sin(dLat/2) +
	               Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2)) *
	               Math.sin(dLng/2) * Math.sin(dLng/2);
	    double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1-a));
	    float dist = (float) (earthRadius * c);
	    
	    DecimalFormat precision = new DecimalFormat("0.00"); 
	    return Double.parseDouble(precision.format(dist * 0.001));
    }

}
