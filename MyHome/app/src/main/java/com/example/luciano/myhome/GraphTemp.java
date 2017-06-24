package com.example.luciano.myhome;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.github.mikephil.charting.charts.LineChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.github.mikephil.charting.formatter.YAxisValueFormatter;

import com.github.mikephil.charting.highlight.Highlight;
import com.github.mikephil.charting.listener.OnChartValueSelectedListener;
import com.github.mikephil.charting.utils.ViewPortHandler;

import java.util.ArrayList;
import java.util.Objects;

/**
 * Created by Luciano on 01/04/2017.
 */

public class GraphTemp extends Fragment {

    LineChart lineChart, lineChart2;
    LineData data1, data2;
    Legend legend, legend2;
    YAxis y11, y12, y21, y22;
    XAxis x1, x2;

    Button btnRefresh, btnTempInter, btnTempExter;
    TextView txtMaxTemp, txtMinTemp;
    public static TextView txtRefresh;

    String selectedTemp;
    int lastPositionGraph1, lastPositionGraph2;
    int doubleClichGraph1, doubleClichGraph2;

    MainActivity mainActivity;
    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        mainActivity = (MainActivity) getActivity();

        selectedTemp = getString(R.string.TempInter_name);


        getActivity().setTitle(R.string.title_GraphTemp);
        lineChart = (LineChart) getActivity().findViewById(R.id.linegraphTemp);
        lineChart2 = (LineChart)getActivity().findViewById(R.id.linegraphTemp2);

        txtMaxTemp = (TextView)getActivity().findViewById(R.id.textViewTempMax);
        txtMinTemp = (TextView)getActivity().findViewById(R.id.textViewTempMin);
        txtRefresh = (TextView)getActivity().findViewById(R.id.textViewRefreshGraph);

        lineChart.setDescription("");
        lineChart.setNoDataTextDescription("No data for the moment");
        lineChart.setHighlightPerDragEnabled(true);
        lineChart.setHighlightPerTapEnabled(true);
        lineChart.setTouchEnabled(true);
        lineChart.setDragEnabled(true);
        lineChart.setScaleEnabled(true);
        lineChart.setDrawGridBackground(false);
        lineChart.setPinchZoom(true);
        lineChart.setBackgroundColor(Color.BLACK);

        lineChart2.setDescription("");
        lineChart2.setNoDataTextDescription("No data for the moment");
        lineChart2.setHighlightPerDragEnabled(true);
        lineChart2.setHighlightPerTapEnabled(true);
        lineChart2.setTouchEnabled(true);
        lineChart2.setDragEnabled(true);
        lineChart2.setScaleEnabled(true);
        lineChart2.setDrawGridBackground(false);
        lineChart2.setPinchZoom(true);
        lineChart2.setBackgroundColor(Color.BLACK);

        data1 = new LineData();
        data1.setValueTextColor(Color.WHITE);

        data2 = new LineData();
        data2.setValueTextColor(Color.WHITE);

        lineChart.setData(data1);
        lineChart2.setData(data2);

        legend = lineChart.getLegend();
        legend.setForm(Legend.LegendForm.LINE);
        legend.setTextColor(Color.WHITE);

        legend2 = lineChart2.getLegend();
        legend2.setForm(Legend.LegendForm.LINE);
        legend2.setTextColor(Color.WHITE);

        x1 = lineChart.getXAxis();
        x1.setTextColor(Color.WHITE);
        x1.setEnabled(true);
        x1.setDrawGridLines(true);
        x1.setAxisLineColor(Color.BLACK);
        x1.setAvoidFirstLastClipping(true);

        x2 = lineChart2.getXAxis();
        x2.setTextColor(Color.WHITE);
        x2.setEnabled(true);
        x2.setDrawGridLines(true);
        x2.setAxisLineColor(Color.BLACK);
        x2.setAvoidFirstLastClipping(true);

        y11 = lineChart.getAxisLeft();
        y11.setTextColor(Color.WHITE);
        y11.setAxisMaxValue(50f);
        y11.setDrawGridLines(true);
        y11.setEnabled(true);
        y11.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return ((int) value) + "°C";
            }
        });

        y21 = lineChart2.getAxisLeft();
        y21.setTextColor(Color.WHITE);
        y21.setAxisMaxValue(50f);
        y21.setDrawGridLines(true);
        y21.setEnabled(true);
        y21.setValueFormatter(new YAxisValueFormatter() {
            @Override
            public String getFormattedValue(float value, YAxis yAxis) {
                return ((int) value) + "°C";
            }
        });

        y12 = lineChart.getAxisRight();
        y12.setEnabled(false);

        y22 = lineChart2.getAxisRight();
        y22.setEnabled(false);

        lastPositionGraph1 = -1;
        lastPositionGraph2 = -1;

        if (Objects.equals(selectedTemp, getString(R.string.TempInter_name))){
            refreshTempInter();
        }else if (Objects.equals(selectedTemp, getString(R.string.TempExter_name))){
            refreshTempExter();
        }


        btnRefresh = (Button)getActivity().findViewById(R.id.refreshTemp);
        btnRefresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (mainActivity.checkConnection()){
                    if (Objects.equals(selectedTemp, getString(R.string.TempInter_name))) {
                        refreshTempInter();
                    }else if (Objects.equals(selectedTemp, getString(R.string.TempExter_name))){
                        refreshTempExter();
                    }
                }else{
                    Toast.makeText(getActivity(), getString(R.string.error_NotConnected),Toast.LENGTH_SHORT).show();
                }
            }
        });

        btnTempInter = (Button)getActivity().findViewById(R.id.buttonSelectGrapnInter);
        btnTempInter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineChart2.setVisibility(View.INVISIBLE);
                lineChart.setVisibility(View.VISIBLE);
                refreshTempInter();
                selectedTemp = getString(R.string.TempInter_name);
            }
        });

        btnTempExter = (Button)getActivity().findViewById(R.id.buttonSelectGraphExter);
        btnTempExter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                lineChart.setVisibility(View.INVISIBLE);
                lineChart2.setVisibility(View.VISIBLE);
                refreshTempExter();
                selectedTemp = getString(R.string.TempExter_name);
            }
        });

    }


    private void addEntry1(Integer value, String label){
        LineData data = lineChart.getData();

        if (data != null){
            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null){
                set = creatSet1();
                data.addDataSet(set);
            }

            data.addXValue(label);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return ((int) value)+"°C";
                }
            });
            data.addEntry(new Entry(value, set.getEntryCount()),0);

            lineChart.notifyDataSetChanged();
            lineChart.setVisibleXRange(0,4);
            lineChart.moveViewToX(data.getXValCount() - 5);
        }
    }

    private void addEntry2(Integer value, String label){
        LineData data = lineChart2.getData();

        if (data != null){
            LineDataSet set = data.getDataSetByIndex(0);

            if (set == null){
                set = creatSet2();
                data.addDataSet(set);
            }

            data.addXValue(label);
            data.setValueFormatter(new ValueFormatter() {
                @Override
                public String getFormattedValue(float value, Entry entry, int dataSetIndex, ViewPortHandler viewPortHandler) {
                    return ((int) value)+"°C";
                }
            });
            data.addEntry(new Entry(value, set.getEntryCount()),0);

            lineChart2.notifyDataSetChanged();
            lineChart2.setVisibleXRange(0,4);
            lineChart2.moveViewToX(data.getXValCount() - 5);
        }
    }

    private LineDataSet creatSet1(){
        LineDataSet set = new LineDataSet(null, "Temperatura Interna (°C)");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.1f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.GREEN);
        set.setCircleColor(Color.GREEN);
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.GREEN);
        set.setHighLightColor(Color.WHITE);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        return set;
    }

    private LineDataSet creatSet2(){
        LineDataSet set = new LineDataSet(null, "Temperatura Externa (°C)");
        set.setDrawCubic(true);
        set.setCubicIntensity(0.1f);
        set.setAxisDependency(YAxis.AxisDependency.LEFT);
        set.setColor(Color.YELLOW);
        set.setCircleColor(Color.YELLOW);
        set.setLineWidth(2f);
        set.setCircleSize(4f);
        set.setFillAlpha(65);
        set.setFillColor(Color.YELLOW);
        set.setHighLightColor(Color.WHITE);
        set.setValueTextColor(Color.WHITE);
        set.setValueTextSize(10f);
        return set;
    }

    public void refreshTempInter() {
        if (mainActivity.checkConnection()) {
            if (!MainActivity.graphTempInterXAxis.isEmpty() && !MainActivity.graphTempInterYAxis.isEmpty()) {
                if (lastPositionGraph1 != MainActivity.graphTempInterYAxis.size()) {
                    for (int i = lastPositionGraph1+1; i < MainActivity.graphTempInterXAxis.size(); i++) {
                        addEntry1(MainActivity.graphTempInterYAxis.get(i), MainActivity.graphTempInterXAxis.get(i));
                        lastPositionGraph1 = i;
                    }
                    txtRefresh.setText("");
                    int valuesMax[] = getMax(MainActivity.graphTempInterYAxis);
                    int valuesMin[] = getMin(MainActivity.graphTempInterYAxis);
                    txtMaxTemp.setText("Temp. Interna MAX: " + valuesMax[0] + "°C - " + MainActivity.graphTempInterXAxis.get(valuesMax[1]));
                    txtMinTemp.setText("Temp. Interna MIN: " + valuesMin[0] + "°C - " + MainActivity.graphTempInterXAxis.get(valuesMin[1]));
                }else{
                    Toast.makeText(getActivity(),getString(R.string.txt_update), Toast.LENGTH_LONG).show();
                }

            } else {
                Toast.makeText(getActivity(), getString(R.string.error_NoData), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), getString(R.string.error_NotConnected), Toast.LENGTH_SHORT).show();
        }
    }

    public void refreshTempExter(){
        if (mainActivity.checkConnection()) {
            if (!MainActivity.graphTempExterXAxis.isEmpty() && !MainActivity.graphTempExterYAxis.isEmpty()) {
                if (lastPositionGraph2 != MainActivity.graphTempExterYAxis.size()) {
                    for (int i = lastPositionGraph2+1; i < MainActivity.graphTempExterYAxis.size(); i++) {
                        addEntry2(MainActivity.graphTempExterYAxis.get(i), MainActivity.graphTempExterXAxis.get(i));
                        lastPositionGraph2 = i;
                    }
                    txtRefresh.setText("");
                    int valuesMax[] = getMax(MainActivity.graphTempExterYAxis);
                    int valuesMin[] = getMin(MainActivity.graphTempExterYAxis);
                    txtMaxTemp.setText("Temp. Externa MAX: " + valuesMax[0] + "°C - " + MainActivity.graphTempExterXAxis.get(valuesMax[1]));
                    txtMinTemp.setText("Temp. Externa MIN: " + valuesMin[0] + "°C - " + MainActivity.graphTempExterXAxis.get(valuesMin[1]));
                }else{
                        Toast.makeText(getActivity(),getString(R.string.txt_update), Toast.LENGTH_LONG).show();
                    }
            } else {
                Toast.makeText(getActivity(), getString(R.string.error_NoData), Toast.LENGTH_SHORT).show();
            }
        }else {
            Toast.makeText(getActivity(), getString(R.string.error_NotConnected), Toast.LENGTH_SHORT).show();
        }
    }

    public int[] getMax(ArrayList<Integer> list){
        int max = Integer.MIN_VALUE;
        int maxPos = -1;
        for (int i = 0; i <  list.size(); i++) {
            int value = list.get(i);
            if (value >= max) {
                max = value;
                maxPos = i;
            }
        }
        return new int[] {max, maxPos};
    }

    public int[] getMin(ArrayList<Integer> list){
        int min = Integer.MAX_VALUE;
        int minPos = -1;
        for (int i = 0; i <  list.size(); i++) {
            int value = list.get(i);
            if (value <= min){
                min = value;
                minPos = i;
            }
        }
        return new int[] {min, minPos};
    }


    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.graph_temp, container, false);
    }
}
