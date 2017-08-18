package com.wander.chartview.chart;

import android.view.GestureDetector;
import android.view.MotionEvent;
import android.view.ViewConfiguration;


/**
 * Created by wander on 2017/8/15.
 */

public class OnChartScrollListener extends GestureDetector.SimpleOnGestureListener {
    private int mScaledTouchSlop;
    private ChartView mChartView;

    public OnChartScrollListener(ChartView mChartView) {
        this.mChartView = mChartView;
        ViewConfiguration viewConfiguration = ViewConfiguration.get(mChartView.getContext());
        mScaledTouchSlop = viewConfiguration.getScaledTouchSlop();
    }

    @Override
    public boolean onDown(MotionEvent e) {
        mChartView.onScroll(e.getX());
        return true;
    }

    @Override
    public boolean onScroll(MotionEvent e1, MotionEvent e2, float distanceX, float distanceY) {
        if (Math.abs(distanceY) > mScaledTouchSlop) {
            mChartView.getParent().requestDisallowInterceptTouchEvent(false);
        } else {
            mChartView.getParent().requestDisallowInterceptTouchEvent(true);
        }
        mChartView.onScroll(e2.getX());
        return true;
    }
}
