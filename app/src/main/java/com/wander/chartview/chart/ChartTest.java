package com.wander.chartview.chart;

/**
 * Created by wander on 2017/8/18.
 */

public class ChartTest implements ChartData {
    private float money;
    private float money2;
    private String date;

    public ChartTest(float money, float money2, String date) {
        this.money = money;
        this.money2 = money2;
        this.date = date;
    }


    @Override
    public float getMoney() {
        return money;
    }

    @Override
    public float getMoney2() {
        return money2;
    }

    @Override
    public String getDate() {
        return date;
    }
}
