package com.swishlabs.prototyping.entity;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import net.tsz.afinal.FinalDb.DbUpdateListener;

public class FinalDbUpdateListener implements DbUpdateListener{

	@Override
	public void onUpgrade(SQLiteDatabase arg0, int arg1, int arg2) {
		String querySQL = "select sql from sqlite_master where name=? and type=?";
		
		updateUserInfo(arg0, querySQL);
	}

	private void updateUserInfo(SQLiteDatabase db, String sql){
		String[] args = new String[] { "UserInfo", "table"};
		Cursor c = db.rawQuery(sql, args);
		
		if(c != null && c.moveToFirst()){
			String tmp = c.getString(0);
			if(!tmp.contains("scnum")){
				db.execSQL("ALTER TABLE UserInfo add scnum INTEGER;");
			}
			
			if(!tmp.contains("balance")){
				db.execSQL("ALTER TABLE UserInfo add balance INTEGER;");
			}
		}
	}
	
}
