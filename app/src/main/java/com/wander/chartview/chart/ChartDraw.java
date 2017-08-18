package com.wander.chartview.chart;

import android.graphics.Canvas;
import android.text.TextPaint;

/**
 * Created by wander on 2017/8/16.
 */

public interface ChartDraw {

     void updateUI();

     void drawBackground(Canvas canvas);

     void drawRuler(Canvas canvas, TextPaint mTextPaint);

     void drawChart(Canvas canvas);

     void drawIndicator(Canvas canvas, TextPaint mTextPaint);
}
