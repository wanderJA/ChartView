package com.wander.chartview;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

import com.wander.chartview.chart.ChartData;
import com.wander.chartview.chart.ChartTest;
import com.wander.chartview.chart.ChartView;
import com.wander.chartview.chart.OnItemSelectListener;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ChartView chartView1 = (ChartView) findViewById(R.id.chart1);
        ChartView chartView2 = (ChartView) findViewById(R.id.chart2);
        ChartView chartView3 = (ChartView) findViewById(R.id.chart3);
        final TextView des = (TextView) findViewById(R.id.des);

        List<ChartData> list = new ArrayList<>();
        list.add(new ChartTest(14231.23f, 21352.33f, "2017.03"));
        list.add(new ChartTest(5231.23f, 21342.33f, "2017.04"));
        list.add(new ChartTest(9631.23f, 12332.33f, "2017.05"));
        list.add(new ChartTest(45231.23f, 11232.33f, "2017.06"));
        list.add(new ChartTest(21231.23f, 12432.33f, "2017.07"));
        list.add(new ChartTest(12331.23f, 23232.33f, "2017.08"));

        new ChartView.Builder(chartView1, ChartView.LINE_CHART)
                .setData(list)
                .setLineColor(Color.parseColor("#3846F2"))
                .setTopColor(Color.parseColor("#91C1F5"))
                .setBottomColor(Color.parseColor("#7580CE"))
                .build();
        chartView1.setOnItemSelectListener(new OnItemSelectListener() {
            @Override
            public void onSelect(ChartData chartData, int position) {
                if (chartData instanceof ChartTest) {
                    des.setText(chartData.getDate() + "\t\tmoney: " + chartData.getMoney());
                }
            }
        });

        new ChartView.Builder(chartView2, ChartView.LINE_CHART)
                .setData(list)
                .setHasPlus(true)
                .build();

        new ChartView.Builder(chartView3, ChartView.BAR_CHART)
                .setData(list)
                .build();

    }
}
