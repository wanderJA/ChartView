package com.wander.chartview.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.text.TextPaint;


import java.text.DecimalFormat;

/**
 * Created by wander on 2017/8/15.
 */

public class BarChart extends ChartBean {
    private LinearGradient bgGradient;
    private LinearGradient bgGradient2;


    public BarChart(Context mContext) {
        super(mContext);
        rulerSpaceY = (int) getDp(20);
        linePaint.setColor(Color.BLACK);
        linePaint.setAlpha(50);
        decimalFormat = new DecimalFormat("#,##0");
    }

    @Override
    protected void updateUI() {
        commonUpdate();
        rulerSpaceX = (int) ((lineStop - lineStart) / builder.data.size());
        for (int i = 0; i < coordinates.length; i++) {
            coordinates[i] = ((int) (lineStart + rulerSpaceX * (4 * i + 1) / 4));
        }
        //X轴
        int baseX = startY + builder.spaceSize * rulerSpaceY;

        startY = rulerSpaceY * 2;
        bgGradient = new LinearGradient(0, rulerSpaceY * 2, 0, baseX, Color.parseColor("#91C1F5"), Color.parseColor("#7580CE"), Shader.TileMode.CLAMP);
        bgGradient2 = new LinearGradient(0, baseX, 0, baseX + builder.spaceSize * rulerSpaceY, builder.bottomColor, builder.topColor, Shader.TileMode.CLAMP);
    }

    @Override
    protected void onWidthChange(int width) {
    }

    @Override
    public int getHeight() {
        return (int) (rulerSpaceY * 15f);
    }

    @Override
    public void drawBackground(Canvas canvas) {

    }

    @Override
    public void drawRuler(Canvas canvas, TextPaint mTextPaint) {
        builder.mTextPaint.setColor(builder.grayColor);
        for (int i = 0; i < builder.spaceSize * 2 + 1; i++) {
            int y = startY + i * rulerSpaceY;
            float f = builder.maxValue - i * builder.spaceValue;
            canvas.drawText((f >= 0 ? " " : "") + decimalFormat.format(f), startX, y + builder.textBaseLine, builder.mTextPaint);
            if (i == builder.spaceSize) {
                builder.indicatorPaint.setStyle(Paint.Style.STROKE);
                builder.indicatorPaint.setColor(Color.BLACK);
                builder.indicatorPaint.setAlpha(100);
                builder.indicatorPaint.setStrokeWidth(indicatorWidth * 1.3f);
                canvas.drawLine(lineStart, y, lineStop, y, builder.indicatorPaint);
                builder.indicatorPaint.setStrokeWidth(indicatorWidth);
                builder.indicatorPaint.setStyle(Paint.Style.FILL);
                builder.indicatorPaint.setColor(builder.lineColor);
                builder.indicatorPaint.setAlpha(255);
            } else {
                mLinePath.reset();
                mLinePath.moveTo(lineStart, y);
                mLinePath.lineTo(lineStop, y);
                canvas.drawPath(mLinePath, linePaint);
            }
        }

        int y = (int) (rulerSpaceY * 13f);
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
        //X轴
        int baseX = startY + builder.spaceSize * rulerSpaceY;
        int halfX = rulerSpaceX / 5;
        for (int i = 0; i < builder.data.size(); i++) {
            ChartData o = (ChartData) builder.data.get(i);
            int coordinate = coordinates[i];
            float money = o.getMoney();
            float money2 = o.getMoney2();
            float percent = money / builder.spaceValue;
            float percent2 = money2 / builder.spaceValue;
            float pointY = baseX - rulerSpaceY * percent;
            float pointY2 = baseX + rulerSpaceY * percent2;

            tempRectF.set(coordinate - halfX, pointY, coordinate + halfX, baseX);
            bgPaint.setShader(bgGradient);
            canvas.drawRect(tempRectF, bgPaint);

            tempRectF.set(coordinate - halfX, baseX, coordinate + halfX, pointY2);
            bgPaint.setShader(bgGradient2);
            canvas.drawRect(tempRectF, bgPaint);

        }
    }

    @Override
    public void drawIndicator(Canvas canvas, TextPaint mTextPaint) {
        ChartData currentData = getCurrentData();
        if (currentData == null) {
            return;
        }
        builder.mTextPaint.setColor(Color.WHITE);
        int coordinate = coordinates[getCurrentPosition()];
        float bottom = rulerSpaceY + builder.textHeight * 3 / 4;
        tempRectF.set(coordinate - builder.firstDateLength
                , rulerSpaceY - builder.textHeight * 3 / 4
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
        int stopY = startY + rulerSpaceY * 10;
        //X轴
        int baseX = startY + builder.spaceSize * rulerSpaceY;

        float money = currentData.getMoney();
        float money2 = currentData.getMoney2();
        float percent = money / builder.spaceValue;
        float percent2 = money2 / builder.spaceValue;
        float pointY = baseX - rulerSpaceY * percent;
        float pointY2 = baseX + rulerSpaceY * percent2;

        builder.indicatorPaint.setAlpha(50);
        canvas.drawCircle(coordinate, pointY, triangleWidth, builder.indicatorPaint);
        builder.indicatorPaint.setAlpha(255);
        canvas.drawCircle(coordinate, pointY, triangleWidth / 2, builder.indicatorPaint);

        builder.indicatorPaint.setAlpha(50);
        canvas.drawCircle(coordinate, pointY2, triangleWidth, builder.indicatorPaint);
        builder.indicatorPaint.setAlpha(255);
        canvas.drawCircle(coordinate, pointY2, triangleWidth / 2, builder.indicatorPaint);

        builder.indicatorPaint.setStyle(Paint.Style.STROKE);
        canvas.drawLine(coordinate, startY, coordinate, stopY, builder.indicatorPaint);
        builder.indicatorPaint.setStyle(Paint.Style.FILL);
        canvas.drawText(currentData.getDate(), coordinate - builder.firstDateLength + builder.textHeight / 2, rulerSpaceY + builder.textBaseLine, mTextPaint);

    }

}
