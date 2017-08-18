package com.wander.chartview.chart;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.text.TextPaint;
import android.util.AttributeSet;
import android.util.TypedValue;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.View;


import java.util.List;

/**
 * Created by wander on 2017/8/15.
 */

public class ChartView extends View {
    private ChartBean mChartBean;
    private TextPaint mTextPaint;
    public static final int LINE_CHART = 1;
    public static final int BAR_CHART = 2;
    private GestureDetector mGestureDetector;
    private OnItemSelectListener onItemSelectListener;


    public ChartView(Context context) {
        this(context, null);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public ChartView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init();
    }

    private void init() {
        mGestureDetector = new GestureDetector(getContext(), new OnChartScrollListener(this));
        mTextPaint = new TextPaint(Paint.ANTI_ALIAS_FLAG);
        mTextPaint.setColor(Color.WHITE);
        float textSize = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 12, getResources().getDisplayMetrics());
        mTextPaint.setTextSize(textSize);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        if (mChartBean == null) {
            mTextPaint.setColor(Color.BLACK);
            canvas.drawText("计算中", 0, 30, mTextPaint);
        } else {
            mTextPaint.setColor(Color.WHITE);
            mChartBean.drawBackground(canvas);
            mChartBean.drawRuler(canvas, mTextPaint);
            mChartBean.drawChart(canvas);
            mChartBean.drawIndicator(canvas, mTextPaint);
        }
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (mChartBean != null) {
            mChartBean.setWidth(w);
        }
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        if (mChartBean != null) {
            int widthMode = MeasureSpec.getMode(widthMeasureSpec);
            int heightMode = MeasureSpec.getMode(heightMeasureSpec);
            if (widthMode != MeasureSpec.EXACTLY) {
                int widthSize = getContext().getResources().getDisplayMetrics().widthPixels;
                widthMeasureSpec = MeasureSpec.makeMeasureSpec(widthSize, MeasureSpec.EXACTLY);
            }
            if (heightMode != MeasureSpec.EXACTLY) {
                int heightSize = mChartBean.getHeight();
                heightMeasureSpec = MeasureSpec.makeMeasureSpec(heightSize, MeasureSpec.EXACTLY);
            }
        } else {
            heightMeasureSpec = MeasureSpec.makeMeasureSpec(400, MeasureSpec.EXACTLY);
        }
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        switch (event.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                getParent().requestDisallowInterceptTouchEvent(true);
                return true;
            case MotionEvent.ACTION_MOVE:
                mGestureDetector.onTouchEvent(event);
                return true;
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
                getParent().requestDisallowInterceptTouchEvent(false);
                mGestureDetector.onTouchEvent(event);
                return true;
        }
        return super.onTouchEvent(event);
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent ev) {
        switch (ev.getActionMasked()) {
            case MotionEvent.ACTION_DOWN:
                mGestureDetector.onTouchEvent(ev);
                break;
        }
        return super.dispatchTouchEvent(ev);
    }

    public static class Builder<T extends ChartData> {
        public static int DEFAULT_ITEMS = 6;
        float textHeight;
        List<T> data;
        int topColor = Color.parseColor("#FDCD9C");
        int bottomColor = Color.parseColor("#FD818E");
        int type;
        Context mContext;
        ChartView mChartView;
        ChartBean<T> mChartBean;
        float maxValue;
        float minValue;
        float spaceValue;
        TextPaint mTextPaint;
        float maxTextLength;
        int grayColor = Color.parseColor("#47000000");
        float firstDateLength;
        float twoTextLength;
        boolean hasPlus;
        int lineColor = Color.RED;
        Paint indicatorPaint;
        /**
         * 画文字时基线增加此数值  文字居中
         */
        float textBaseLine;
        int spaceSize = 4;


        public Builder(ChartView mChartView, int type) {
            this.mChartView = mChartView;
            this.mContext = mChartView.getContext();
            this.mTextPaint = mChartView.getTextPaint();
            textHeight = mTextPaint.descent() - mTextPaint.ascent();
            textBaseLine = -(mTextPaint.descent() + mTextPaint.ascent()) / 2;
            indicatorPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
            indicatorPaint.setColor(lineColor);
            indicatorPaint.setStyle(Paint.Style.FILL);
            setType(type);
            if (mChartBean != null)
                mChartBean.setBuilder(this);
        }

        public Builder setData(@NonNull List<T> data) {
            float maxValue = 0;
            float minValue = 0;

            this.data = data;
            if (data.size() > DEFAULT_ITEMS) {
                this.data = data.subList(data.size() - 6, data.size());
            }
            if (data.size() >= 1) {
                minValue = data.get(0).getMoney();
            }
            for (ChartData chartData : data) {
                float money = chartData.getMoney();
                if (money > maxValue) {
                    maxValue = money;
                }
                if (money < minValue) {
                    minValue = money;
                }
                float money2 = chartData.getMoney2();
                if (money2 > maxValue) {
                    maxValue = money2;
                }
            }
            if (type == ChartView.LINE_CHART) {
                this.maxValue = maxValue;
                this.minValue = minValue;
                this.spaceValue = (maxValue - minValue) / spaceSize;
            } else {
//                Math.
//                maxValue
                this.maxValue = maxValue;
                this.spaceValue = maxValue / spaceSize;
            }
            maxTextLength = mTextPaint.measureText((hasPlus ? "+" : "") + String.valueOf(this.maxValue) + "111");
            firstDateLength = mTextPaint.measureText("2017-08");
            twoTextLength = mTextPaint.measureText("00");

            return this;
        }

        public Builder setLineColor(int lineColor) {
            this.lineColor = lineColor;
            return this;
        }

        public Builder setHasPlus(boolean hasPlus) {
            this.hasPlus = hasPlus;
            return this;
        }

        public Builder setTopColor(int topColor) {
            this.topColor = topColor;
            indicatorPaint.setColor(lineColor);
            return this;
        }

        public Builder setBottomColor(int bottomColor) {
            this.bottomColor = bottomColor;
            return this;
        }

        private Builder setType(int type) {
            this.type = type;
            switch (type) {
                case LINE_CHART:
                    mChartBean = new LineChart(mContext);
                    spaceSize = 4;
                    break;
                case BAR_CHART:
                    mChartBean = new BarChart(mContext);
                    spaceSize = 5;
                    break;
                default:
                    break;
            }
            return this;
        }

        public ChartBean<T> build() {
            mChartView.requestLayout();
            if (mChartBean != null) {
                mChartBean.updateUI();
                mChartView.setChartBean(mChartBean);
            }
            mChartView.postInvalidateDelayed(100);
            return mChartBean;
        }
    }

    public ChartBean getChartBean() {
        return mChartBean;
    }

    private ChartView setChartBean(ChartBean mChartBean) {
        this.mChartBean = mChartBean;
        return this;
    }

    public TextPaint getTextPaint() {
        return mTextPaint;
    }


    public void onScroll(float distanceX) {
        if (mChartBean != null && mChartBean.builder.data != null && mChartBean.builder.data.size() >= 6) {
            mChartBean.updateX(distanceX);
            invalidate();
            if (onItemSelectListener != null) {
                onItemSelectListener.onSelect(mChartBean.getCurrentData(), mChartBean.getCurrentPosition());
            }
        }
    }

    public void setOnItemSelectListener(OnItemSelectListener onItemSelectListener) {
        this.onItemSelectListener = onItemSelectListener;
    }

    public OnItemSelectListener getOnItemSelectListener() {
        return onItemSelectListener;
    }
}
