package com.mcergun.covidinfo;

import android.graphics.Color;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.github.mikephil.charting.components.AxisBase;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.LineData;
import com.github.mikephil.charting.data.LineDataSet;
import com.github.mikephil.charting.formatter.ValueFormatter;
import com.mcergun.covidinfo.databinding.FragmentLineChartBinding;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;


/**
 * A simple {@link Fragment} subclass.
 */
public class LineChartFragment extends Fragment {

    public static final String LINE_CHART_FRAGMENT_TITLE = "com.mcergun.covidinfo.LINE_CHART_FRAGMENT_TITLE";
    public static final String LINE_CHART_FRAGMENT_YAXIS = "com.mcergun.covidinfo.LINE_CHART_FRAGMENT_YAXIS";
    public static final String LINE_CHART_FRAGMENT_XAXIS = "com.mcergun.covidinfo.LINE_CHART_FRAGMENT_XAXIS";
    String title;
    ArrayList<String> xAxisData;
    ArrayList<Integer> yAxisData;
    private FragmentLineChartBinding binding;

    public LineChartFragment() {
        // Required empty public constructor
    }

    public void UpdateData(String title, ArrayList<Integer> yData, ArrayList<String> xData) {
        this.title = title;
        yAxisData = yData;
        xAxisData = xData;
        List<Entry> entries = new ArrayList<>();
        for (int i = 0; i < yAxisData.size(); ++i) {
            entries.add(new Entry(i, yAxisData.get(i)));
        }
        showEntries(entries, title);
        binding.tvTitle.setText(title);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentLineChartBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    protected void showEntries(List<Entry> items, String label) {
        LineDataSet lds = new LineDataSet(items, label);
        lds.setColor(getRandomDarkColor());
        lds.setLineWidth(3);
        lds.setDrawCircles(false);
        lds.setDrawValues(false);
        lds.setAxisDependency(YAxis.AxisDependency.LEFT);
        LineData ld = new LineData(lds);
        binding.chart.setData(ld);
        if (xAxisData != null) {
            ValueFormatter vf = new ValueFormatter() {
                @Override
                public String getAxisLabel(float value, AxisBase axis) {
                    return xAxisData.get((int) value);
                }
            };
            XAxis xa = binding.chart.getXAxis();
            binding.chart.getAxisRight().setEnabled(false);
            xa.setGranularity(5.0f);
            xa.setValueFormatter(vf);
        }
        binding.chart.invalidate();
    }

    private int getRandomDarkColor() {
        int color = 0xFF000000;
        final int divider = 192;
        Random rnd = new Random();
        int num = rnd.nextInt() % divider;
        num = num > 0 ? num : -num;
        color += num;
        num = rnd.nextInt() % divider;
        num = num > 0 ? num : -num;
        color += num << 8;
        num = rnd.nextInt() % divider;
        num = num > 0 ? num : -num;
        color += num << 16;
        return color;
    }
}
