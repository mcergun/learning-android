package com.mcergun.covidinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {
    TextView tvTotalCases;
    TextView tvTotalDeaths;
    TextView tvRecoveredCases;
    TextView tvCriticalCases;
    TextView tvNewCases;
    TextView tvNewDeaths;
    TextView tvActiveCases;
    Spinner spCountry;

    protected void updateSpinners()
    {
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
                spCountry.setAdapter(countryListAdapter);
            }
        });
        rac.execute("countries");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTotalCases = findViewById(R.id.tvTotalCases);
        tvTotalDeaths = findViewById(R.id.tvTotalDeaths);
        tvRecoveredCases = findViewById(R.id.tvRecoveredCases);
        tvNewCases = findViewById(R.id.tvNewCases);
        tvNewDeaths = findViewById(R.id.tvNewDeaths);
        tvCriticalCases = findViewById(R.id.tvCriticalCases);
        tvActiveCases = findViewById(R.id.tvActiveCases);
        spCountry = findViewById(R.id.spCountry);
        spCountry.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
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
//                tvTotalCases.setText(MessageFormat.format("{0} (+{1})", ctData.totalCases, ctData.newCases));
                tvTotalCases.setText(MessageFormat.format("{0}", ctData.totalCases));
                tvNewCases.setText(MessageFormat.format("+{0}", ctData.newCases));
                tvRecoveredCases.setText(MessageFormat.format("{0}", ctData.recoveredCases));
//                tvTotalDeaths.setText(MessageFormat.format("{0} (+{1})", ctData.totalDeaths, ctData.newDeaths));
                tvNewDeaths.setText(MessageFormat.format("+{0}", ctData.newDeaths));
                tvTotalDeaths.setText(MessageFormat.format("{0}", ctData.totalDeaths));
                tvCriticalCases.setText(MessageFormat.format("{0}", ctData.criticalCases));
                tvActiveCases.setText(MessageFormat.format("{0}", ctData.activeCases));
            }
        });
        rac.execute("statistics", "country", country);
    }

    private void useless() {
        RestApiClient rac = new RestApiClient(this,
                "https://covid-193.p.rapidapi.com",
                "covid-193.p.rapidapi.com",
                "991d939994msh71b06558d279452p158f58jsn5a76c0186017");
        rac.addOnCompletedListener(new OnCompletedListener() {
            @Override
            public void onComplete(String str) {
                Log.d("deneme", str);
                CovidJsonParser jsp = new CovidJsonParser(str);
                CovidCountryData ctdata = jsp.getCountryData();
                ArrayList<String> countries = jsp.getCountryList();
            }
        });
//        rac.execute("statistics", "country", "turkey");
        rac.execute("countries");
    }
}
