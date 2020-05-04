package com.mcergun.covidinfo;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;

import com.github.mikephil.charting.charts.LineChart;
import com.mcergun.covidinfo.databinding.ActivityDetailedInfoBinding;

import java.util.ArrayList;

public class DetailedInfoActivity extends AppCompatActivity {

    public static final String DETAILED_INFO_COUNTRY = "com.mcergun.covidinfo.DETAILED_INFO_COUNTRY";
    public static final String DETAILED_INFO_DATES = "com.mcergun.covidinfo.DETAILED_INFO_DATES";
    public static final String DETAILED_INFO_CASES = "com.mcergun.covidinfo.DETAILED_INFO_CASES";
    public static final String DETAILED_INFO_DEATHS = "com.mcergun.covidinfo.DETAILED_INFO_DEATHS";
    public static final String DETAILED_INFO_TESTS = "com.mcergun.covidinfo.DETAILED_INFO_TESTS";

    ActivityDetailedInfoBinding binding;
    ArrayList<Integer> cases;
    ArrayList<Integer> deaths;
    ArrayList<Integer> tests;
    ArrayList<String> dates;
    String country;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityDetailedInfoBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        // Grab data
        Intent it = getIntent();
        country = it.getStringExtra(DETAILED_INFO_COUNTRY);
        cases = it.getIntegerArrayListExtra(DETAILED_INFO_CASES);
        deaths = it.getIntegerArrayListExtra(DETAILED_INFO_DEATHS);
        tests = it.getIntegerArrayListExtra(DETAILED_INFO_TESTS);
        dates = it.getStringArrayListExtra(DETAILED_INFO_DATES);
        if (it != null) {
            binding.headingCountry.setText(country);
        }
        LineChartFragment frgCases = (LineChartFragment) getSupportFragmentManager().findFragmentById(R.id.chart1);
        LineChartFragment frgDeaths = (LineChartFragment) getSupportFragmentManager().findFragmentById(R.id.chart2);
        LineChartFragment frgTests = (LineChartFragment) getSupportFragmentManager().findFragmentById(R.id.chart3);
        frgCases.UpdateData(getResources().getString(R.string.detailed_heading_cases), cases, dates);
        frgDeaths.UpdateData(getResources().getString(R.string.detailed_heading_deaths), deaths, dates);
        frgTests.UpdateData(getResources().getString(R.string.detailed_heading_tests), tests, dates);
    }


}
