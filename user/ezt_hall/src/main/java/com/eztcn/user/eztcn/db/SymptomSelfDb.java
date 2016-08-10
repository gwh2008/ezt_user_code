package com.eztcn.user.eztcn.db;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.eztcn.user.R;
import com.eztcn.user.eztcn.bean.Diseases;
import com.eztcn.user.eztcn.bean.Symptom;

/**
 * @title 症状自查数据库
 * @describe
 * @author ezt
 * @created 2015年4月8日
 */
public class SymptomSelfDb {
	private final int BUFFER_SIZE = 1024;
	public static final String DB_NAME = "symptom_self.db";
	private SQLiteDatabase database;
	private Context context;
	private File file = null;

	public SymptomSelfDb(Context context) {
		this.context = context;
		openDatabase();
	}

	private void openDatabase() {
		this.database = this.openDatabase(context.getFileStreamPath(DB_NAME)
				.getPath());
	}

	private SQLiteDatabase openDatabase(String dbfile) {
		try {
			file = new File(dbfile);
			if (!file.exists()) {
				InputStream is = context.getResources().openRawResource(
						R.raw.symptom_self);

				FileOutputStream fos = new FileOutputStream(dbfile);

				byte[] buffer = new byte[BUFFER_SIZE];
				int count = 0;
				while ((count = is.read(buffer)) > 0) {
					fos.write(buffer, 0, count);
					fos.flush();
				}
				fos.close();
				is.close();
			}
			database = SQLiteDatabase.openOrCreateDatabase(dbfile, null);
			return database;
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (Exception e) {
			e.printStackTrace();
		}
		return null;
	}

	public void closeDatabase() {
		if (this.database != null)
			this.database.close();
	}

	/**
	 * 根据部位获取症状列表
	 */
	public ArrayList<Symptom> getSymptomListFromPart(String part) {
		ArrayList<Symptom> list = new ArrayList<Symptom>();
		String sql = "select * from reg_symptoms where IllPlace like '%" + part
				+ "%'";
		try {

			Cursor cursor = this.database.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String IllName = cursor.getString(cursor
						.getColumnIndex("IllName"));
				Symptom symptom = new Symptom();
				symptom.setStrName(IllName);
				symptom.setId(id + "");
				list.add(symptom);
				cursor.moveToNext();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabase();
		}

		return list;
	}

	/**
	 * 获取全部症状列表
	 * 
	 * @return
	 */
	public ArrayList<Symptom> getSymptomListAll() {
		ArrayList<Symptom> list = new ArrayList<Symptom>();
		String sql = "select * from reg_symptoms";
		try {

			Cursor cursor = this.database.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String IllName = cursor.getString(cursor
						.getColumnIndex("IllName"));
				String letter = cursor.getString(cursor
						.getColumnIndex("Letter"));

				Symptom symptom = new Symptom();
				symptom.setStrName(IllName);
				symptom.setId(id + "");
				symptom.setSortLetters(letter);
				list.add(symptom);
				cursor.moveToNext();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabase();
		}

		return list;
	}

	/**
	 * 获取全部疾病列表
	 * 
	 * @return
	 */
	public ArrayList<Diseases> getDiseasesListAll() {
		ArrayList<Diseases> list = new ArrayList<Diseases>();
		String sql = "select * from reg_diseases";
		try {

			Cursor cursor = this.database.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String name = cursor.getString(cursor.getColumnIndex("Name"));
				String letter = cursor.getString(cursor
						.getColumnIndex("Letter"));

				Diseases diseases = new Diseases();
				diseases.setdName(name);
				diseases.setId(id);
				diseases.setSortLetters(letter);
				list.add(diseases);
				cursor.moveToNext();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabase();
		}
		return list;
	}

	/**
	 * 根据疾病分类人群获取疾病列表
	 * 
	 * @param type
	 * @return
	 */
	public ArrayList<Diseases> getDiseaseListFromType(String type) {
		ArrayList<Diseases> list = new ArrayList<Diseases>();
		String sql = "select * from reg_diseases where CusceptiblePopulation like '%"
				+ type + "%'";
		try {

			Cursor cursor = this.database.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String Name = cursor.getString(cursor.getColumnIndex("Name"));
				Diseases diseases = new Diseases();
				diseases.setdName(Name);
				diseases.setId(id);
				list.add(diseases);
				cursor.moveToNext();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabase();
		}

		return list;
	}

	/**
	 * 获取常见病列表
	 * 
	 * @param hot
	 * @return
	 */
	public ArrayList<Diseases> getDiseaseListFromHot(int hot) {
		ArrayList<Diseases> list = new ArrayList<Diseases>();
		String sql = "select * from reg_diseases where hot = " + hot;
		try {

			Cursor cursor = this.database.rawQuery(sql, null);
			cursor.moveToFirst();
			while (!cursor.isLast()) {
				int id = cursor.getInt(cursor.getColumnIndex("id"));
				String Name = cursor.getString(cursor.getColumnIndex("Name"));
				Diseases diseases = new Diseases();
				diseases.setdName(Name);
				diseases.setId(id);
				list.add(diseases);
				cursor.moveToNext();
			}

		} catch (Exception e) {
			e.printStackTrace();
		} finally {
			closeDatabase();
		}

		return list;
	}

}