package com.yljt.cascadingmenu;

import java.util.ArrayList;
import com.yljt.model.Area;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

public class DBhelper {
	private SQLiteDatabase db;
	private Context context;
	private DBManager dbm;
	
	public DBhelper(Context context) {
		super();
		this.context = context;
		dbm = new DBManager(context);
	}

	public ArrayList<Area> getCity(String pcode) {
		dbm.openDatabase();
		db = dbm.getDatabase();
		ArrayList<Area> list = new ArrayList<Area>();
		
	 	try {    
	        String sql = "select * from ezt_z_city where provinceid='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String code=cursor.getString(cursor.getColumnIndex("id")); 
	        	byte bytes[]=cursor.getBlob(1); 
		        String name=new String(bytes,"utf-8");
		        Area area=new Area();
		        area.setName(name);
		        area.setCode(code);
		        area.setPcode(pcode);
		        list.add(area);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("id")); 
	        byte bytes[]=cursor.getBlob(1); 
	        String name=new String(bytes,"utf-8");
	        Area area=new Area();
	        area.setName(name);
	        area.setCode(code);
	        area.setPcode(pcode);
	        list.add(area);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	

		return list;

	}
	public ArrayList<Area> getProvince() {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
		
	 	try {    
	        String sql = "select * from ezt_z_province";  
	        Cursor cursor = db.rawQuery(sql,null);  
	        cursor.moveToFirst();
	        while (!cursor.isLast()){ 
	        	String code=cursor.getString(cursor.getColumnIndex("id")); 
		        byte bytes[]=cursor.getBlob(1); 
		        String name=new String(bytes,"utf-8");
		        Area area=new Area();
		        area.setName(name);
		        area.setCode(code);
		        list.add(area);
		        cursor.moveToNext();
	        }
	        String code=cursor.getString(cursor.getColumnIndex("id")); 
	        byte bytes[]=cursor.getBlob(1); 
	        String name=new String(bytes,"utf-8");
	        Area area=new Area();
	        area.setName(name);
	        area.setCode(code);
	        list.add(area);
	        
	    } catch (Exception e) {  
	    	return null;
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
		
	}
	public ArrayList<Area> getDistrict(String pcode) {
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	ArrayList<Area> list = new ArrayList<Area>();
	 	try {    
	        String sql = "select * from ezt_z_county where cityid='"+pcode+"'";  
	        Cursor cursor = db.rawQuery(sql,null);
	        if (cursor.moveToFirst()) {
				while (!cursor.isLast()) {
					int code = cursor.getInt(cursor
							.getColumnIndex("id"));
					byte bytes[] = cursor.getBlob(1);
					String name = new String(bytes, "utf-8");
					Area Area = new Area();
					Area.setName(name);
					Area.setCode(code+"");
					Area.setPcode(pcode);
					
					list.add(Area);
					cursor.moveToNext();
				}
				int code = cursor.getInt(cursor
						.getColumnIndex("id"));
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				Area Area = new Area();
				Area.setName(name);
				Area.setCode(code+"");
				Area.setPcode(pcode);
				list.add(Area);
			}
	        
	    } catch (Exception e) { 
	    	Log.i("wer", e.toString());
	    } 
	 	dbm.closeDatabase();
	 	db.close();	
		return list;
	}
	
	//通过city的id获取city的名字和身份的id.
	public  Area getCityName(String cityCode){
		
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	Area Area = new Area();
	 	try{
	 		
        String sql = "select * from ezt_z_city where id='"+cityCode+"'";  
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
        	
			while (!cursor.isLast()) {
				int pcode = cursor.getInt(cursor
						.getColumnIndex("provinceid"));
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				Area.setName(name);
				Area.setCode(cityCode);
				Area.setPcode(pcode+"");
				cursor.moveToNext();
			}
			int pcode = cursor.getInt(cursor
					.getColumnIndex("provinceid"));
			byte bytes[] = cursor.getBlob(1);
			String name = new String(bytes, "utf-8");
	
			Area.setName(name);
			Area.setCode(cityCode+"");
			Area.setPcode(pcode+"");
		}
	 	}catch(Exception e){
	 		Log.i("error", e.toString());
	 	}
		return Area;
	}
	//获取省份的名字。
   public  Area getProvinceName(String provinceCode){
		
		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	Area Area = new Area();
	 	try{
	
        String sql = "select * from ezt_z_province where id='"+provinceCode+"'";  
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {
        	
			while (!cursor.isLast()) {
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				Area.setName(name);
				Area.setCode(provinceCode);
				cursor.moveToNext();
			}
			byte bytes[] = cursor.getBlob(1);
			String name = new String(bytes, "utf-8");
			Area.setName(name);
			Area.setCode(provinceCode+"");
		}
	 	}catch(Exception e){
	 		Log.i("error", e.toString());
	 	}
		return Area;
	}
	//获取区的名字。
   public  Area getCountyName(String countyCode){

		dbm.openDatabase();
	 	db = dbm.getDatabase();
	 	Area Area = new Area();
	 	try{

        String sql = "select * from ezt_z_county where id='"+countyCode+"'";
        Cursor cursor = db.rawQuery(sql,null);
        if (cursor.moveToFirst()) {

			while (!cursor.isLast()) {
				byte bytes[] = cursor.getBlob(1);
				String name = new String(bytes, "utf-8");
				Area.setName(name);
				Area.setCode(countyCode);
				cursor.moveToNext();
			}
			byte bytes[] = cursor.getBlob(1);
			String name = new String(bytes, "utf-8");
			Area.setName(name);
			Area.setCode(countyCode+"");
		}
	 	}catch(Exception e){
	 		Log.i("error", e.toString());
	 	}
		return Area;
	}
}