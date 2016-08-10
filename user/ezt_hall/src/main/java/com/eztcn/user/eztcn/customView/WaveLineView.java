package com.eztcn.user.eztcn.customView;

import java.lang.ref.SoftReference;
import com.eztcn.user.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Matrix;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.view.View;
/**
 * @author LX
 * 无缝花边  View
 * @author touch_ping  
 */

public class WaveLineView extends View {  
    private final static int waveRes = R.drawable.img_wave;  
    private static SoftReference<Bitmap> bitmap_wave;// 背景  
    private Context mcontext;  
    private static Paint paint;  
    private static Matrix matrix;  
    private static float waveWidth;  
    private static float waveScale;  
    private static int waveCount;  
      
    public WaveLineView(Context context) {  
        super(context);  
        init(context);  
    }  
  
    public WaveLineView(Context context, AttributeSet attrs) {  
        super(context, attrs, 0);  
        init(context);  
    }  
      
    private void init (Context context) {  
        mcontext = context;  
    }  
    @Override  
    protected void onDraw(Canvas canvas) {  
        super.onDraw(canvas);  
        if (paint==null) {  
            paint = new Paint();  
        }  
        if (matrix==null) {  
            matrix = new Matrix();  
        }  
        if (bitmap_wave==null || bitmap_wave.get()==null) {  
            // 从资源文件中生成位图  
            Bitmap bitmap = BitmapFactory.decodeResource(getResources(), waveRes);  
            float scale = getMeasuredHeight()*1f/bitmap.getHeight();  
            float width = bitmap.getWidth()*1f*scale;  
            waveCount = (int)(getMeasuredWidth()*1f/width)+1;  
            waveWidth = width - ((width*waveCount-getMeasuredWidth())/waveCount);  
            waveScale = waveWidth*1f/bitmap.getWidth();  
              
            bitmap_wave = new SoftReference<Bitmap>(bitmap);  
        }  
          
        matrix.setScale(waveScale, waveScale);  
        for (int i = 0; i < waveCount; i++) {  
            canvas.drawBitmap(bitmap_wave.get(), matrix, paint);  
            matrix.postTranslate(waveWidth, 0);  
        }  
    }  
}  