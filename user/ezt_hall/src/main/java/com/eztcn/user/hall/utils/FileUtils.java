package com.eztcn.user.hall.utils;

import android.content.ContentResolver;
import android.content.Context;
import android.content.SharedPreferences;
import android.database.Cursor;
import android.net.Uri;
import android.provider.MediaStore;

import com.eztcn.user.eztcn.bean.EztUser;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;

/**
 * Created by lx on 2016/6/8.
 * 文件存储。
 */
public class FileUtils {

    public  static void saveObject(Context context, String name, Object object){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(name, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(object);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }


    //保存原来的userBean
    public  static void saveEztUserObject(Context context, String name, EztUser user){
        FileOutputStream fos = null;
        ObjectOutputStream oos = null;
        try {
            fos = context.openFileOutput(name, context.MODE_PRIVATE);
            oos = new ObjectOutputStream(fos);
            oos.writeObject(user);
        } catch (Exception e) {
            e.printStackTrace();
            //这里是保存文件产生异常
        } finally {
            if (fos != null){
                try {
                    fos.close();
                } catch (IOException e) {
                    //fos流关闭异常
                    e.printStackTrace();
                }
            }
            if (oos != null){
                try {
                    oos.close();
                } catch (IOException e) {
                    //oos流关闭异常
                    e.printStackTrace();
                }
            }
        }
    }
    //
    public static Object getObject(Context context,String name){
        FileInputStream fis = null;
        ObjectInputStream ois = null;
        try {
            fis = context.openFileInput(name);
            ois = new ObjectInputStream(fis);
            return ois.readObject();
        } catch (Exception e) {
            e.printStackTrace();
            //这里是读取文件产生异常
        } finally {
            if (fis != null){
                try {
                    fis.close();
                } catch (IOException e) {
                    //fis流关闭异常
                    e.printStackTrace();
                }
            }
            if (ois != null){
                try {
                    ois.close();
                } catch (IOException e) {
                    //ois流关闭异常
                    e.printStackTrace();
                }
            }
        }
        //读取产生异常，返回null
        return null;
    }


/**
 * Uri转换为URL
 */

public static String getRealFilePath( final Context context, final Uri uri ) {
    if ( null == uri ) return null;
    final String scheme = uri.getScheme();
    String data = null;
    if ( scheme == null )
        data = uri.getPath();
    else if ( ContentResolver.SCHEME_FILE.equals( scheme ) ) {
        data = uri.getPath();
    } else if ( ContentResolver.SCHEME_CONTENT.equals( scheme ) ) {
        Cursor cursor = context.getContentResolver().query( uri, new String[] { MediaStore.Images.ImageColumns.DATA }, null, null, null );
        if ( null != cursor ) {
            if ( cursor.moveToFirst() ) {
                int index = cursor.getColumnIndex( MediaStore.Images.ImageColumns.DATA );
                if ( index > -1 ) {
                    data = cursor.getString( index );
                }
            }
            cursor.close();
        }
    }
    return data;
}

    /**
     * 用sp保存String.
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static  boolean saveStringBySp(Context context,String key,String value){

       try {
           SharedPreferences sp =context.getSharedPreferences("user",Context.MODE_PRIVATE);
           SharedPreferences.Editor editor=sp.edit();
           editor.putString(key,value);
           editor.commit();
       }catch (Exception e){
           e.printStackTrace();
           return false;
       }
        return true;
    }

    /**
     *用sp保存Boolean值。
     * @param context
     * @param key
     * @param value
     * @return
     */

    public static  boolean saveBooleanBySp(Context context,String key,boolean value){

        try {
            SharedPreferences sp =context.getSharedPreferences("user",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putBoolean(key,value);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }

    /**
     * 保存整型  数据。
     * @param context
     * @param key
     * @param value
     * @return
     */
    public static  boolean saveIntegerBySp(Context context,String key,int value){

        try {
            SharedPreferences sp =context.getSharedPreferences("user",Context.MODE_PRIVATE);
            SharedPreferences.Editor editor=sp.edit();
            editor.putInt(key,value);
            editor.commit();
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return true;
    }
/**
 * 从sp中获取String数据。
 */

public static String getStringBySp(Context context,String key){

    String value="";
    try {
        SharedPreferences sp =context.getSharedPreferences("user", Context.MODE_PRIVATE);
        value =  sp.getString(key, "");
    }catch (Exception e){
        e.printStackTrace();
        return "error";
    }
    return value;
}

    /**
     * 从sp中获取boolean数据。
     */

    public static boolean getBooleanBySp(Context context,String key){

        boolean value=false;
        try {
            SharedPreferences sp =context.getSharedPreferences("user", Context.MODE_PRIVATE);
            value =  sp.getBoolean(key, false);
        }catch (Exception e){
            e.printStackTrace();
            return false;
        }
        return value;
    }


    /**
     * 从sp中获取int数据。
     */

    public static int getIntegerBySp(Context context,String key){

        int value=-1;
        try {
            SharedPreferences sp =context.getSharedPreferences("user", Context.MODE_PRIVATE);
            value =  sp.getInt(key, -1);
        }catch (Exception e){
            e.printStackTrace();
            return -1;
        }
        return value;
    }
}