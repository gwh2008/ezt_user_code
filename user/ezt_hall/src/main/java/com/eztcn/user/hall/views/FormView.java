package com.eztcn.user.hall.views;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.SparseArray;
import android.view.View;

import com.eztcn.user.R;
import com.eztcn.user.hall.interfaces.IFormListener;
import com.eztcn.user.hall.model.ResultResponse.WeekTimesCountDataResponse;

import java.util.ArrayList;
import java.util.HashMap;

/**
 * 绘制周期表格
 *
 * @author 蒙
 */
public class FormView extends View {

    private float  firstX = 0; // 起始点x，左上角的x值
    private float  firstY = 0; // 起始点y，左上角的y值
    private float  secondX = 80; // 第二点x，第一个格子的右下角的x值
    private float  secondY = 80; // 第二点y，第一个格子的右下角的y值
    private int widthNum = 8; // 列数默认值
    private int heightNum = 3; // 行数默认值
    private float secondSideX = 80; // 第二列的宽度默认值
    private float sideY = 80; // 每行行高默认值
    private float firstSidesX = 80; // 第一列的宽度默认值

    private String[] rowText = null;//第一行的字符串数据源
    private String[] colText = null;//第一列的字符串数据源
    private String noData = "-1";//默认-1为格子点击没反应

    private String[] rowDates = null;//第一行周几对应的日期数据源

    /**
     * 行和列指示的文本数据源
     * @param rowText  行文本
     * @param colText  列文本
     * @param rowDates 行文本的小字文本
     */
    public void setRowAndColText(String[] rowText, String[] colText,String[] rowDates) {
        this.rowText = rowText;
        this.colText = colText;
        this.rowDates=rowDates;
        invalidate();
    }

    IFormListener myFormListener;//格子的点击监听

    public IFormListener getFormListener() {
        return myFormListener;
    }

    public void setFormListener(IFormListener myFormListener) {
        this.myFormListener = myFormListener;
    }

    private Context context;
    public FormView(Context context) {
        super(context);
        this.context=context;
    }

    public FormView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    private WeekTimesCountDataResponse timesCountDataResponse;//每个格子对应的时间段数据源，是一个实体类模型
    private int indexSelected=0;//选中的格子索引

    public int getIndexSelected() {
        return indexSelected;
    }

    public void setIndexSelected(int indexSelected) {
        this.indexSelected = indexSelected;
    }

    public WeekTimesCountDataResponse getTimesCountDataResponse() {
        return timesCountDataResponse;
    }

    public void setTimesCountDataResponse(WeekTimesCountDataResponse timesCountDataResponse) {
        this.timesCountDataResponse = timesCountDataResponse;
        invalidate();
    }

    public HashMap<Integer,ArrayList<WeekTimesCountDataResponse>> timesAllData=new HashMap<>();//该控件本身需要的时间总的数据源，包含所有的需要的数据
    public int position=-1;//当前控件在viewpager中的位置，用于判断是否是处于第一个位置，来显示今天文本

    public HashMap<Integer, ArrayList<WeekTimesCountDataResponse>> getTimesAllData() {
        return timesAllData;
    }

    public void setTimesAllData(HashMap<Integer, ArrayList<WeekTimesCountDataResponse>> timesAllData) {
        this.timesAllData = timesAllData;
        this.setBackgroundResource(R.drawable.new_oval_select_border_green_weektable);
    }

    public int getPosition() {
        return position;
    }

    public void setPosition(int position) {
        this.position = position;
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (colText != null && rowText != null) {
            drawForm(canvas);
        }

    }

    private void drawForm(Canvas canvas) {

        //先对格子的宽度和高度进行计算和赋值，初始化，可以以后优化进行动态的设置
        secondX = this.getMeasuredWidth() / widthNum+0.15f; // 第二点x，加上0.1是为了让最后一列的竖线和北京的边框重合
        secondSideX = this.getMeasuredWidth() / widthNum+0.15f; // 第二列的宽
        firstSidesX = this.getMeasuredWidth() / widthNum+0.15f; // 第一列的宽

        Paint paint = new Paint();
        paint.setAntiAlias(true);
        paint.setStrokeWidth(1);
        paint.setColor(getResources().getColor(R.color.line_color_gray));
        paint.setStyle(Paint.Style.STROKE);

        float cellX = 0, cellY = 0, cellBX = 0, cellBY = 0;
        for (int i = 0; i < widthNum; i++) {

            for (int j = 0; j < heightNum; j++) {
                if (i == 0) { // 绘制第一列的宽度
                    cellX = firstX + i * firstSidesX;
                    cellY = firstY + j * sideY;
                    cellBX = firstX + (i + 1) * firstSidesX;
                    cellBY = firstY + (j + 1) * sideY;
                } else {// 绘制第其他列的宽度
                    cellX = secondX + (i - 1) * secondSideX;
                    cellY = secondY + (j - 1) * sideY;
                    cellBX = secondX + i * secondSideX;
                    cellBY = secondY + j * sideY;
                }

                canvas.drawRect(cellX, cellY, cellBX, cellBY, paint);//画格子区域
                int cellsNum = i + j * widthNum;
                if (j == 0) {//第一行
                    if (i != 0) {//绘制第一行非第一列每个格子需绘制两个个文本
                        drawCellText(canvas, cellX, cellY, cellBX, cellBY,rowText[i], rowDates[i]);
                    }
                }
                if (cellsNum % widthNum != 0) {//对不是第一列的格子进行绘制
                    if (null!=timesAllData.get(cellsNum)) {//判断该格子是否有对应数据源的数据
                        if (cellsNum % widthNum == 1&&position==0) {//代表今天的格子显示（只有第一列有可能是今天）
                            if (j==heightNum-1){
                                drawLastLineCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.parseColor("#cbf4cd"));
                            }else{
                                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.parseColor("#cbf4cd"));
                            }
                            if (indexSelected!=cellsNum) {//没有被选中才显示今天两个字，选中的需要显示时间段
                                drawTodayCellText(canvas, cellX, cellY, cellBX, cellBY);
                            }


                        } else {//其他可预约的格子颜色
                            if (j==heightNum-1){
                                drawLastLineCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.parseColor("#c8ecff"));
                            }else{
                                drawCellColor(canvas, cellX, cellY, cellBX, cellBY, Color.parseColor("#c8ecff"));
                            }

                        }
                    } else {//其他没有数据的格子为白色格子
                        if (j==heightNum-1){
                            drawLastLineCellColor(canvas, cellX, cellY, cellBX, cellBY, 0xFFF);
                        }else{
                            drawCellColor(canvas, cellX, cellY, cellBX, cellBY, 0xFFF);
                        }

                    }

                    if (indexSelected==cellsNum) {//判断该格子是否需要绘制文字，将选中的时间段绘制上
                        drawSelectedCellText(canvas, cellX, cellY, cellBX, cellBY);
                    }
                } else {// 绘制第一列里面的文本
                    drawColCellText(canvas, cellX, cellY, cellBX, cellBY,colText[cellsNum / widthNum]);
                }
            }
        }
    }

    // 绘制第一行非第一列单元格的文字
    private void drawCellText(Canvas canvas, float cellX, float cellY, float cellBX,
                              float cellBY, String week, String date) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.text_color_deep_gray));
        float textSize = (cellBY - cellY) / 7 * 2.5f;
        paint.setTextSize(textSize);
        float textX = cellX + (cellBX - cellX) / 6;
        float textY = cellBY - (cellBY - cellY) / 2;
        canvas.drawText(week, textX, textY, paint);
        paint.setTextSize(textSize / 1.5f);

        canvas.drawText(date, textX + 4, textY * 1.7f, paint);
    }
    // 绘制第一列单元格的文字
    private void drawColCellText(Canvas canvas, float cellX, float cellY, float cellBX,
                                 float cellBY, String text) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.text_color_deep_gray));
        float textSize = (cellBY - cellY) / 7 * 2.5f;
        paint.setTextSize(textSize);
        float textX = cellX + (cellBX - cellX) / 6f;
        float textY = cellBY - (cellBY - cellY) / 2.5f;
        canvas.drawText(text, textX, textY, paint);
    }
    // 绘制 今天 文本
    private void drawTodayCellText(Canvas canvas, float cellX, float cellY, float cellBX, float cellBY) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.border_line));
        float textSize = (cellBY - cellY) / 7 * 2.5f;
        paint.setTextSize(textSize);
        float textX = cellX + (cellBX - cellX) / 6f;
        float textY = cellBY - (cellBY - cellY) / 2.5f;
        canvas.drawText("今天", textX, textY, paint);
    }
    // 绘制 选中的单元格的 文本
    private void drawSelectedCellText(Canvas canvas, float cellX, float cellY, float cellBX, float cellBY) {
        Paint paint = new Paint();
        paint.setFlags(Paint.ANTI_ALIAS_FLAG);
        paint.setColor(getResources().getColor(R.color.border_line));
        float textSize = (cellBY - cellY) / 3.5f;
        paint.setTextSize(textSize);
        float textX = cellX + (cellBX - cellX) / 6f;
        float textY = cellBY - (cellBY - cellY) / 1.6f;
        canvas.drawText(timesCountDataResponse.getStartDate().substring(11,16), textX, textY, paint);

        paint.setTextSize(textSize / 1.5f);
        canvas.drawText("|", textX + 27, textY + 17, paint);

        paint.setTextSize(textSize);
        canvas.drawText(timesCountDataResponse.getEndDate().substring(11,16), textX, textY + 40, paint);
    }

    // 绘制单元格中的颜色  
    private void drawCellColor(Canvas canvas, float cellX, float cellY, float cellBX,
                               float cellBY, int color) {
        Paint paint = new Paint();
        // 绘制备选颜色边框以及其中颜色  
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(cellX + 2, cellY + 2, cellBX - 2, cellBY - 2, paint);

    }
    // 绘制最后一行单元格中的颜色,为了让底部的背景色不遮盖背景边框的颜色
    private void drawLastLineCellColor(Canvas canvas, float cellX, float cellY, float cellBX,
                               float cellBY, int color) {
        Paint paint = new Paint();
        // 绘制备选颜色边框以及其中颜色
        paint.setColor(color);
        paint.setStyle(Paint.Style.FILL);
        canvas.drawRect(cellX + 2, cellY + 2, cellBX - 2, cellBY - 4, paint);

    }

    // 检测点击事件所在的格数
    public boolean testTouchColorPanel(float x, float y) {
        if (x > secondX && y > secondY && x < firstX + firstSidesX + secondSideX * widthNum
                && y < firstY + sideY * heightNum) {

            int ty = (int) ((y - firstY) / sideY);
            int tx;

            if (x - firstX - firstSidesX > 0) {
                tx = (int) ((x - firstX - firstSidesX) / secondSideX + 1);
            } else {
                tx = 0;
            }
            int index = ty * widthNum + tx;
            if (null!=timesAllData.get(index)) {
//                indexSelected=index;
//                invalidate();
                //触发监听事件
                myFormListener.showNum(index,timesAllData.get(index));
            }

            return true;
        }
        return false;
    }
}  
