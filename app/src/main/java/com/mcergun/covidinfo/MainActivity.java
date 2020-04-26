package com.mcergun.covidinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;
import org.json.JSONArray;
import org.json.JSONObject;

import java.text.MessageFormat;
import java.util.ArrayList;

public class MainActivity extends AppCompatActivity {

    TextView tv_single_country;
    TextView tv_single_date;
    TextView tv_single_cases;
    TextView tv_single_deaths;
    TextView tv_single_recoveries;
    Spinner sp_country;
    Spinner sp_date;
    String covid_data;
    int datesCount = 0;

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
                sp_country.setAdapter(countryListAdapter);
            }
        });
        rac.execute("countries");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tv_single_country = findViewById(R.id.tv_single_country);
        tv_single_date = findViewById(R.id.tv_single_date);
        tv_single_cases = findViewById(R.id.tv_single_cases);
        tv_single_deaths = findViewById(R.id.tv_single_deaths);
        tv_single_recoveries = findViewById(R.id.tv_single_recoveries);
        sp_country = findViewById(R.id.sp_country);
        sp_date = findViewById(R.id.sp_date);
        sp_country.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                updateDisplay((String) parent.getSelectedItem());
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
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
                tv_single_cases.setText(MessageFormat.format("{0}\n(+{1})", ctData.totalCases, ctData.newCases));
                tv_single_recoveries.setText(MessageFormat.format("{0}", ctData.recoveredCases));
                tv_single_deaths.setText(MessageFormat.format("{0}\n(+{1})", ctData.totalDeaths, ctData.newDeaths));
                tv_single_country.setText(ctData.name);
                tv_single_date.setText(ctData.date.substring(0, 10));
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
