package com.wander.chartview.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.DashPathEffect;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;
import android.util.TypedValue;


import java.text.DecimalFormat;

/**
 * Created by wander on 2017/8/15.
 */

public abstract class ChartBean<T extends ChartData> {
    protected Context mContext;
    private int width;
    Paint bgPaint;
    float distanceX;
    private int currentPosition;
    private ChartData currentData;
    ChartView.Builder<T> builder;
    private int topShadowColor;
    int rulerSpaceY;
    int rulerSpaceX;
    LinearGradient shadowGradient;
    int lineStop;
    int startX;
    float lineStart;
    int startY;
    /**
     * x 坐标位置
     */
    int[] coordinates = new int[ChartView.Builder.DEFAULT_ITEMS];
    DecimalFormat decimalFormat = new DecimalFormat("#,##0.00");
    RectF tempRectF = new RectF();
    Paint linePaint;
    Path mLinePath;
    /**
     * 指示器下三角大小
     */
    int triangleWidth;
    /**
     * 线宽
     */
    int indicatorWidth;

    public ChartBean(Context mContext) {
        this.mContext = mContext;
        initPaint();
        rulerSpaceY = (int) getDp(50);
        triangleWidth = (int) getDp(6);
        indicatorWidth = (int) getDp(1f);
        topShadowColor = Color.parseColor("#72ffffff");
        shadowGradient = new LinearGradient(0, 0, 0, getHeight(), topShadowColor, Color.TRANSPARENT, Shader.TileMode.CLAMP);
    }

    private void initPaint() {
        linePaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        linePaint.setColor(Color.WHITE);
        linePaint.setAlpha(150);
        linePaint.setStyle(Paint.Style.STROKE);
        linePaint.setStrokeWidth(getDp(1));
        linePaint.setPathEffect(new DashPathEffect(new float[]{getDp(6), getDp(1.5f)}, 0));
        bgPaint = new Paint();
        mLinePath = new Path();

    }

    public ChartView.Builder<T> getBuilder() {
        return builder;
    }

    public void setBuilder(ChartView.Builder<T> builder) {
        this.builder = builder;
        builder.indicatorPaint.setStrokeWidth(indicatorWidth);
    }

    protected void updateUI() {
        startX = (int) (getDp(50) / 2);
        startY = rulerSpaceY * 2 / 3;
        lineStop = getWidth() - startX;
        lineStart = startX + builder.maxTextLength;
        rulerSpaceX = (int) ((lineStop - lineStart) / 5);
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = ((int) (lineStart + rulerSpaceX * i));
        }
        initCurrentItem();
    }

    void initCurrentItem() {
        currentPosition = 5;
        if (builder.data != null && builder.data.size() >= 6) {
            currentData = builder.data.get(5);
        }
        OnItemSelectListener onItemSelectListener = builder.mChartView.getOnItemSelectListener();
        if (onItemSelectListener != null) {
            onItemSelectListener.onSelect(currentData, currentPosition);
        }
    }

    public abstract void drawBackground(Canvas canvas);

    public abstract void drawRuler(Canvas canvas, TextPaint mTextPaint);

    public abstract void drawChart(Canvas canvas);

    public abstract void drawIndicator(Canvas canvas, TextPaint mTextPaint);

    public int getHeight() {
        return (int) (rulerSpaceY * 6f);
    }

    public void setWidth(int width) {
        this.width = width;
        if (builder != null) {
            updateUI();
        }
        onWidthChange(width);
    }

    protected abstract void onWidthChange(int width);

    public int getWidth() {
        return width;
    }

    void updateX(float distanceX) {
        this.distanceX = distanceX;
        for (int i = 0; i < coordinates.length; i++) {
            if (distanceX < coordinates[i] + rulerSpaceX / 2) {
                currentPosition = i;
                currentData = builder.data.get(i);
                return;
            }
        }
        if (distanceX >= coordinates[coordinates.length - 1] + rulerSpaceX / 2) {
            currentPosition = 5;
            currentData = builder.data.get(5);
        }
    }

    public int getCurrentPosition() {
        return currentPosition;
    }

    public ChartData getCurrentData() {
        return currentData;
    }

    float getDp(float dp) {
        float dimension = 0;
        if (mContext != null) {
            dimension = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, dp, mContext.getResources().getDisplayMetrics());
        }
        return dimension;
    }
}
