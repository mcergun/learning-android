package com.mcergun.covidinfo;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.TextView;

import com.mcergun.covidinfo.R;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Iterator;

public class MainActivity extends AppCompatActivity {

    // Test string for JSON
    //   "Algeria": [
    //    {
    //      "date": "2020-1-22",
    //      "confirmed": 0,
    //      "deaths": 0,
    //      "recovered": 0
    //    },

    TextView tv_single_country;
    TextView tv_single_date;
    TextView tv_single_cases;
    TextView tv_single_deaths;
    TextView tv_single_recoveries;
    Spinner sp_country;
    Spinner sp_date;
    String covid_data;
    int datesCount = 0;

    protected String fetchCovidData()
    {
        String jsonData = null;
        try {
            JsonDownloader jsd = new JsonDownloader(this, "https://pomber.github.io/covid19/timeseries.json");
            jsd.execute();
            jsonData = jsd.jsonData;
            if (jsonData != null)
            {
                Log.d("deneme", "deneme");
            }
        }
        catch (Exception e) {
            e.printStackTrace();
            Log.d("deneme", "shit");
        }
        return jsonData;
    }

    protected void selectCovidCountry() {
        try {
            JSONObject jso = new JSONObject(covid_data);
            JSONArray dates = jso.getJSONArray((String)sp_country.getSelectedItem());
            JSONObject date = dates.getJSONObject(datesCount - sp_date.getSelectedItemPosition() - 1);
            tv_single_country.setText((String)sp_country.getSelectedItem());
            tv_single_date.setText(date.getString("date"));
            tv_single_cases.setText(date.getString("confirmed"));
            tv_single_deaths.setText(date.getString("deaths"));
            tv_single_recoveries.setText(date.getString("recovered"));
        }
        catch (Exception e) {
            e.printStackTrace();
        }
    }

    public void updateCovidData(String jsonData) {
        covid_data = jsonData;
        updateSpinners();
        selectCovidCountry();
    }

    protected void updateSpinners()
    {
        try {
            JSONObject jso = new JSONObject(covid_data);
            Iterator<String> keys = jso.keys();
            JSONArray dates = jso.getJSONArray("Turkey");
            ArrayList<String> countryList = new ArrayList<>();
            ArrayList<String> dateList = new ArrayList<>();
            while (keys.hasNext()) {
                countryList.add(keys.next());
            }
            datesCount = dates.length();
            for (int i = datesCount - 1; i > 0; i--) {
                dateList.add(dates.getJSONObject(i).getString("date"));
            }
            ArrayAdapter<String> datesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dateList);
            ArrayAdapter<String> countriesAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, countryList);
            datesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            countriesAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
            sp_date.setAdapter(datesAdapter);
            sp_country.setAdapter(countriesAdapter);
        }
        catch (Exception e) {
            e.printStackTrace();
        }
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
                selectCovidCountry();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        sp_date.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                selectCovidCountry();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent)
            {
            }
        });
        ArrayAdapter<CharSequence> adapter = ArrayAdapter.createFromResource(this, R.array.countries_array, android.R.layout.simple_spinner_item);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        sp_country.setAdapter(adapter);
        fetchCovidData();
    }

    @Override
    protected void onStart() {
        super.onStart();
    }

    private void useless() {
        RestApiClient rac = new RestApiClient(this);
        rac.addOnCompletedListener(new OnCompletedListener() {
            @Override
            public void onComplete(String str) {
                Log.d("deneme", str);
                CovidJsonParser jsp = new CovidJsonParser(str);
                CovidCountryData ctdata = jsp.getCountryData();
                Log.d("deneme", ctdata.toString());
            }
        });
        rac.execute("");
    }
}
