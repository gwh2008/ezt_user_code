package com.eztcn.user.eztcn.utils;

import android.app.Activity;  
import android.graphics.Typeface;  
import android.view.View;  
import android.view.ViewGroup;  
import android.widget.Button;  
import android.widget.EditText;  
import android.widget.TextView;  


import android.app.Application;
import android.content.Context;
import android.graphics.Typeface;
//import android.support.annotation;


import java.lang.ref.SoftReference;
import java.lang.reflect.Field;
import java.util.HashMap;
import java.util.Map;

public class FontUtils {  
	public static Typeface FontApple=null; 
//	 private static final String TAG = FontUtils.class.getSimpleName();
//	    private Map<String, SoftReference<Typeface>> mCache = new HashMap<>();
//	    private static FontUtils sSingleton = null;
//
//	    public static Typeface DEFAULT = Typeface.DEFAULT;
//
//	    // disable instantiate
//	    private FontUtils() {}
//
//	    public static FontUtils getInstance() {
//	        // double check
//	        if (sSingleton == null) {
//	            synchronized(FontUtils.class) {
//	                if (sSingleton == null) {
//	                    sSingleton = new FontUtils();
//	                }
//	            }
//	        }
//	        return sSingleton;
//	    }
//
//	    /**
//	     * <p>Replace the font of specified view and it's children</p>
//	     * @param root The root view.
//	     * @param fontPath font file path relative to 'assets' directory.
//	     */
//	    public void replaceFontFromAsset(@NonNull View root, @NonNull String fontPath) {
//	        replaceFont(root, createTypefaceFromAsset(root.getContext(), fontPath));
//	    }
//
//	    /**
//	     * <p>Replace the font of specified view and it's children</p>
//	     * @param root The root view.
//	     * @param fontPath font file path relative to 'assets' directory.
//	     * @param style One of {@link Typeface#NORMAL}, {@link Typeface#BOLD}, {@link Typeface#ITALIC}, {@link Typeface#BOLD_ITALIC}
//	     */
//	    public void replaceFontFromAsset(@NonNull View root, @NonNull String fontPath, int style) {
//	        replaceFont(root, createTypefaceFromAsset(root.getContext(), fontPath), style);
//	    }
//
//	    /**
//	     * <p>Replace the font of specified view and it's children</p>
//	     * @param root The root view.
//	     * @param fontPath The full path to the font data.
//	     */
//	    public void replaceFontFromFile(@NonNull View root, @NonNull String fontPath) {
//	        replaceFont(root, createTypefaceFromFile(fontPath));
//	    }
//
//	    /**
//	     * <p>Replace the font of specified view and it's children</p>
//	     * @param root The root view.
//	     * @param fontPath The full path to the font data.
//	     * @param style One of {@link Typeface#NORMAL}, {@link Typeface#BOLD}, {@link Typeface#ITALIC}, {@link Typeface#BOLD_ITALIC}
//	     */
//	    public void replaceFontFromFile(@NonNull View root, @NonNull String fontPath, int style) {
//	        replaceFont(root, createTypefaceFromFile(fontPath), style);
//	    }
//
//	    /**
//	     * <p>Replace the font of specified view and it's children with specified typeface</p>
//	     */
//	    private void replaceFont(@NonNull View root, @NonNull Typeface typeface) {
//	        if (root == null || typeface == null) {
//	            return;
//	        }
//
//	        if (root instanceof TextView) { // If view is TextView or it's subclass, replace it's font
//	            TextView textView = (TextView)root;
//	            // Extract previous style of TextView
//	            int style = Typeface.NORMAL;
//	            if (textView.getTypeface() != null) {
//	                style = textView.getTypeface().getStyle();
//	            }
//	            textView.setTypeface(typeface, style);
//	        } else if (root instanceof ViewGroup) { // If view is ViewGroup, apply this method on it's child views
//	            ViewGroup viewGroup = (ViewGroup) root;
//	            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
//	                replaceFont(viewGroup.getChildAt(i), typeface);
//	            }
//	        } // else return
//	    }
//
//	    /**
//	     * <p>Replace the font of specified view and it's children with specified typeface and text style</p>
//	     * @param style One of {@link Typeface#NORMAL}, {@link Typeface#BOLD}, {@link Typeface#ITALIC}, {@link Typeface#BOLD_ITALIC}
//	     */
//	    private void replaceFont(@NonNull View root, @NonNull Typeface typeface, int style) {
//	        if (root == null || typeface == null) {
//	            return;
//	        }
//	        if (style < 0 || style > 3) {
//	            style = Typeface.NORMAL;
//	        }
//
//	        if (root instanceof TextView) { // If view is TextView or it's subclass, replace it's font
//	            TextView textView = (TextView)root;
//	            textView.setTypeface(typeface, style);
//	        } else if (root instanceof ViewGroup) { // If view is ViewGroup, apply this method on it's child views
//	            ViewGroup viewGroup = (ViewGroup) root;
//	            for (int i = 0; i < viewGroup.getChildCount(); ++i) {
//	                replaceFont(viewGroup.getChildAt(i), typeface, style);
//	            }
//	        } // else return
//	    }
//
//	    /**
//	     * <p>Create a Typeface instance with specified font file</p>
//	     * @param fontPath font file path relative to 'assets' directory.
//	     * @return Return created typeface instance.
//	     */
//	    private Typeface createTypefaceFromAsset(Context context, String fontPath) {
//	        SoftReference<Typeface> typefaceRef = mCache.get(fontPath);
//	        Typeface typeface = null;
//	        if (typefaceRef == null || (typeface = typefaceRef.get()) == null) {
//	            typeface = Typeface.createFromAsset(context.getAssets(), fontPath);
//	            typefaceRef = new SoftReference<>(typeface);
//	            mCache.put(fontPath, typefaceRef);
//	        }
//	        return typeface;
//	    }
//
//	    private Typeface createTypefaceFromFile(String fontPath) {
//	        SoftReference<Typeface> typefaceRef = mCache.get(fontPath);
//	        Typeface typeface = null;
//	        if (typefaceRef == null || (typeface = typefaceRef.get()) == null) {
//	            typeface = Typeface.createFromFile(fontPath);
//	            typefaceRef = new SoftReference<>(typeface);
//	            mCache.put(fontPath, typefaceRef);
//	        }
//	        return typeface;
//	    }
//
//	    /**
//	     * <p>Replace system default font. <b>Note:</b>you should also add code below to your app theme in styles.xml. </p>
//	     * {@code <item name="android:typeface">monospace</item>}
//	     * <p>The best place to call this method is {@link Application#onCreate()}, it will affect
//	     * whole app font.If you call this method after view is visible, you need to invalid the view to make it effective.</p>
//	     * @param context {@link Context Context}
//	     * @param fontPath font file path relative to 'assets' directory.
//	     */
//	    public void replaceSystemDefaultFontFromAsset(@NonNull Context context, @NonNull String fontPath) {
//	        replaceSystemDefaultFont(createTypefaceFromAsset(context, fontPath));
//	    }
//
//	    /**
//	     * <p>Replace system default font. <b>Note:</b>you should also add code below to your app theme in styles.xml. </p>
//	     * {@code <item name="android:typeface">monospace</item>}
//	     * <p>The best place to call this method is {@link Application#onCreate()}, it will affect
//	     * whole app font.If you call this method after view is visible, you need to invalid the view to make it effective.</p>
//	     * @param context {@link Context Context}
//	     * @param fontPath The full path to the font data.
//	     */
//	    public void replaceSystemDefaultFontFromFile(@NonNull Context context, @NonNull String fontPath) {
//	        replaceSystemDefaultFont(createTypefaceFromFile(fontPath));
//	    }
//
//	    /**
//	     * <p>Replace system default font. <b>Note:</b>you should also add code below to your app theme in styles.xml. </p>
//	     * {@code <item name="android:typeface">monospace</item>}
//	     * <p>The best place to call this method is {@link Application#onCreate()}, it will affect
//	     * whole app font.If you call this method after view is visible, you need to invalid the view to make it effective.</p>
//	     */
//	    private void replaceSystemDefaultFont(@NonNull Typeface typeface) {
//	        modifyObjectField(null, "MONOSPACE", typeface);
//	    }
//
//	    private void modifyObjectField(Object obj, String fieldName, Object value) {
//	        try {
//	            Field defaultField = Typeface.class.getDeclaredField(fieldName);
//	            defaultField.setAccessible(true);
//	            defaultField.set(obj, value);
//
//	        } catch (NoSuchFieldException e) {
//	            e.printStackTrace();
//	        } catch (IllegalAccessException e) {
//	            e.printStackTrace();
//	        }
//	    }
	
//    public static void changeFonts(ViewGroup root, Activity act) {  
//       Typeface tf = Typeface.createFromAsset(act.getAssets(),  
//              "fonts/apple.ttf");  
//       for (int i = 0; i < root.getChildCount(); i++) {  
//           View v = root.getChildAt(i);  
//           if (v instanceof TextView) {  
//              ((TextView) v).setTypeface(tf);  
//           }
//       }  
//    }
    
    public static void changeFonts(final Activity act, final View root) {
        try {
            if (root instanceof ViewGroup) {
                ViewGroup viewGroup = (ViewGroup) root;
                for (int i = 0; i < viewGroup.getChildCount(); i++)
                	changeFonts(act, viewGroup.getChildAt(i));
            } else if (root instanceof TextView)
                ((TextView) root).setTypeface(FontApple);//Typeface.createFromAsset(act.getAssets(), "fonts/apple.ttf")
        } catch (Exception e) {
//            Log.e(TAG, String.format("Error occured when trying to apply %s font for %s view", fontName, root));
//            e.printStackTrace();
        }
    }
    
    public static void tvChangeFont(TextView tv, Activity act) {  
    	 
    	tv.setTypeface(FontApple);
    }
}