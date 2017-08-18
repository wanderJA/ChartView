package com.wander.chartview.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.RectF;
import android.graphics.Shader;
import android.text.TextPaint;

/**
 * Created by wander on 2017/8/15.
 */

public class LineChart extends ChartBean {
    private LinearGradient bgGradient;

    private RectF chartRect;
    private Paint chartPaint;

    public LineChart(Context mContext) {
        super(mContext);
        chartPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        chartPaint.setColor(Color.WHITE);
        chartPaint.setAlpha(220);
        chartPaint.setStyle(Paint.Style.STROKE);
        chartPaint.setStrokeWidth(getDp(1.58f));

    }


    @Override
    protected void updateUI() {
        super.updateUI();
        bgGradient = new LinearGradient(0, 0, 0, getHeight(), builder.topColor, builder.bottomColor, Shader.TileMode.CLAMP);
        chartRect = new RectF(0, rulerSpaceY / 3, getWidth(), rulerSpaceY * 5);
    }

    @Override
    protected void onWidthChange(int width) {
        chartRect = new RectF(0, rulerSpaceY / 3, getWidth(), rulerSpaceY * 5);
    }

    @Override
    public void drawBackground(Canvas canvas) {
        bgPaint.setShader(bgGradient);
        canvas.drawRect(chartRect, bgPaint);
    }

    @Override
    public void drawRuler(Canvas canvas, TextPaint mTextPaint) {
        builder.mTextPaint.setColor(Color.WHITE);
        for (int i = 0; i < 5; i++) {
            int y = startY + i * rulerSpaceY;
            float f = builder.maxValue - i * builder.spaceValue;
            canvas.drawText((builder.hasPlus ? "+" : "") + decimalFormat.format(f), startX, y + builder.textBaseLine, builder.mTextPaint);
            mLinePath.reset();
            mLinePath.moveTo(lineStart, y);
            mLinePath.lineTo(lineStop, y);
            canvas.drawPath(mLinePath, linePaint);
        }

        builder.mTextPaint.setColor(builder.grayColor);
        int y = (int) (rulerSpaceY * 5.3f);
        for (int i = 0; i < builder.data.size(); i++) {
            ChartData o = (ChartData) builder.data.get(i);
            String date = o.getDate();
            if (i == 0) {
                canvas.drawText(date, coordinates[i] - builder.firstDateLength * 0.7f, y, builder.mTextPaint);
            } else {
                canvas.drawText(date.substring(date.length() - 2), coordinates[i] - builder.twoTextLength * 0.5f, y, builder.mTextPaint);
            }
        }

    }

    @Override
    public void drawChart(Canvas canvas) {
        mLinePath.reset();
        int totalY = 4 * rulerSpaceY;
        int y = startY + totalY;
        for (int i = 0; i < builder.data.size(); i++) {
            ChartData o = (ChartData) builder.data.get(i);
            int coordinate = coordinates[i];
            float money = o.getMoney();
            float percent = (builder.maxValue - money) / builder.spaceValue;
            float pointY = startY + rulerSpaceY * percent;
            if (i == 0) {
                mLinePath.moveTo(coordinate, pointY);
            } else {
                mLinePath.lineTo(coordinate, pointY);
            }
        }
        canvas.drawPath(mLinePath, chartPaint);
        mLinePath.lineTo(coordinates[coordinates.length - 1], y);
        mLinePath.lineTo(coordinates[0], y);
        mLinePath.close();
        bgPaint.setShader(shadowGradient);
        canvas.drawPath(mLinePath, bgPaint);

    }

    @Override
    public void drawIndicator(Canvas canvas, TextPaint mTextPaint) {
        ChartData currentData = getCurrentData();
        if (currentData == null) {
            return;
        }
        builder.mTextPaint.setColor(Color.WHITE);
        int coordinate = coordinates[getCurrentPosition()];
        float bottom = rulerSpaceY / 3 + builder.textHeight * 3 / 4;
        tempRectF.set(coordinate - builder.firstDateLength
                , rulerSpaceY / 3 - builder.textHeight * 3 / 4
                , coordinate + builder.textHeight
                , bottom);
        canvas.drawRoundRect(tempRectF
                , builder.textHeight / 3
                , builder.textHeight / 3
                , builder.indicatorPaint);
        //画三角
        mLinePath.reset();
        mLinePath.moveTo(coordinate, bottom + triangleWidth);
        mLinePath.lineTo(coordinate - triangleWidth / 2, bottom - 2);
        mLinePath.lineTo(coordinate + triangleWidth / 2, bottom - 2);
        canvas.drawPath(mLinePath, builder.indicatorPaint);
        //画底部圆
        int stopY = startY + rulerSpaceY * 4;

        float money = currentData.getMoney();
        float percent = (builder.maxValue - money) / builder.spaceValue;
        float pointY = startY + rulerSpaceY * percent;
        builder.indicatorPaint.setAlpha(50);
        canvas.drawCircle(coordinate, pointY, triangleWidth, builder.indicatorPaint);
        builder.indicatorPaint.setAlpha(255);
        canvas.drawCircle(coordinate, pointY, triangleWidth / 2, builder.indicatorPaint);

        builder.indicatorPaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(coordinate, startY, coordinate, stopY, builder.indicatorPaint);
        builder.indicatorPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(currentData.getDate(), coordinate - builder.firstDateLength + builder.textHeight / 2, rulerSpaceY / 3 + builder.textBaseLine, mTextPaint);
    }
}
