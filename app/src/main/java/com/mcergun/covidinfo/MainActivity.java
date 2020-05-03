package com.mcergun.covidinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;

import com.mcergun.covidinfo.databinding.ActivityMainBinding;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        binding.spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDisplay((String) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        updateSpinners();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        binding = null;
    }

    protected void updateSpinners() {
        RestApiClient rac = new RestApiClient(this,
                "https://covid-193.p.rapidapi.com",
                "covid-193.p.rapidapi.com",
                "991d939994msh71b06558d279452p158f58jsn5a76c0186017");
        rac.addOnCompletedListener(new OnCompletedListener() {
            @Override
            public void onComplete(String str) {
                CovidJsonParser cjp = new CovidJsonParser(str);
                ArrayList<String> countryList = cjp.getCountryList();
                ArrayAdapter<String> countryListAdapter = new ArrayAdapter<String>(MainActivity.this,
                        android.R.layout.simple_spinner_item, countryList);
                countryListAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
                binding.spCountry.setAdapter(countryListAdapter);
            }
        });
        rac.execute("countries");
    }

    private void updateDisplay(String country) {
        RestApiClient rac = new RestApiClient(this,
                "https://covid-193.p.rapidapi.com",
                "covid-193.p.rapidapi.com",
                "991d939994msh71b06558d279452p158f58jsn5a76c0186017");
        rac.addOnCompletedListener(new OnCompletedListener() {
            @Override
            public void onComplete(String str) {
                CovidJsonParser jsp = new CovidJsonParser(str);
                CovidCountryData ctData = jsp.getCountryData();
                binding.tvCasesTotal.setText(MessageFormat.format("{0}", ctData.totalCases));
                binding.tvCasesNew.setText(MessageFormat.format("+{0}", ctData.newCases));
                binding.tvCasesCritical.setText(MessageFormat.format("{0}", ctData.criticalCases));
                binding.tvCasesActive.setText(MessageFormat.format("{0}", ctData.activeCases));
                binding.tvCasesRecovered.setText(MessageFormat.format("{0}", ctData.recoveredCases));
                binding.tvDeathsNew.setText(MessageFormat.format("+{0}", ctData.newDeaths));
                binding.tvDeathsTotal.setText(MessageFormat.format("{0}", ctData.totalDeaths));
            }
        });
        rac.execute("statistics", "country", country);
    }
}
