package com.swishlabs.prototyping.util;

import java.util.List;

import net.tsz.afinal.FinalDb;

public class FinalDBHelper {
		
	public static void saveData(FinalDb finalDb, List<?> dataList){
		if (dataList!=null && dataList.size() > 0){
			for(Object item: dataList){
				finalDb.save(item);
			}			
		}
	}
	
/*	public static void saveVideoList(List<VideoInfo> dataList, FinalDb finalDb) {
		if (dataList == null) {
			return;
		}
		for (VideoInfo info : dataList) {
			finalDb.deleteByWhere(VideoInfo.class, " videoId = " + info.getVideoId());
			info.setUserId(info.getUserinfo().getUserId());
			finalDb.save(info);
			finalDb.deleteByWhere(UserInfo.class, " userId = " + info.getUserId());
			finalDb.save(info.getUserinfo());
		}
	}
	
	public static List<VideoInfo> getVideoList(FinalDb finalDb) {
		List<VideoInfo> videoList = finalDb.findAll(VideoInfo.class);
		for (VideoInfo info : videoList) {
			List<UserInfo> users = finalDb.findAllByWhere(UserInfo.class, " userId = " + info.getUserId());
			if (users != null && !users.isEmpty()) {
				info.setUserinfo(users.get(0));
			}
		}
		return videoList;
	}
	
	public static void saveUpdateInfo(UpdateInfo info, FinalDb finalDb) {
		if(info == null)
			return;
		
		finalDb.deleteAll(UpdateInfo.class);
		finalDb.save(info);
	}
	
	public static UpdateInfo getUpdateInfo(FinalDb finalDb){
		List<UpdateInfo> list =  finalDb.findAll(UpdateInfo.class);
		if(list == null || list.isEmpty())
			return null;
		
		return list.get(0);
	}*/
}
