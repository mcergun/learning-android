package com.mcergun.covidinfo;

//{"get":"statistics"
//"parameters":{"country":"turkey"}
//"errors":[]
//"results":1
//"response":[
//    {
//        "country":"Turkey"
//        "cases":{
//            "new":"+2861"
//            "active":79485
//            "critical":1782
//            "recovered":25582
//            "total":107773
//        }
//        "deaths":{
//            "new":"+106"
//            "total":2706
//        }
//        "tests":{
//            "total":868565
//        }
//        "day":"2020-04-26"
//        "time":"2020-04-26T08:15:05+00:00"
//    }
//]}

import androidx.core.content.res.TypedArrayUtils;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class CovidJsonParser {
    String sourceStr;
    JSONArray responseJsa;

    public CovidJsonParser(String source) {
        sourceStr = source;
        try {
            JSONObject jso = new JSONObject(source);
            if (jso.getInt("results") > 0) {
                responseJsa = jso.getJSONArray("response");
            } else {
                throw new IllegalArgumentException("No results found for parameter");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public CovidCountryData getCountryData() {
        CovidCountryData data = null;
        try {
            if (responseJsa.length() == 1) {
                JSONObject country = responseJsa.getJSONObject(0);
                data = parseSingleCountry(country);
            } else {
                throw new IllegalArgumentException("No results found for parameter");
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return data;
    }

    public CovidCountryData getCountryData(String countryName) {
        CovidCountryData data = null;
        try {
            JSONObject country = null;
            boolean isFound = false;
            if (responseJsa.length() >= 1) {
                for (int i = 0; isFound && i < responseJsa.length(); i++) {
                    country = responseJsa.getJSONObject(i);
                    isFound = country.getString("name").equals(countryName);
                }
                if (isFound) {
                    data = parseSingleCountry(country);
                }
            } else {
                throw new IllegalArgumentException("No results found for parameter");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return data;
    }

    ArrayList<String> getCountryList() {
        ArrayList<String> countries = new ArrayList<>();
        try {
            if (responseJsa.length() > 1) {
                for (int i = 0; i < responseJsa.length(); ++i) {
                    String ct = responseJsa.getString(i);
                    if (!ct.contains("&") && !ct.contains(";")) {
                        countries.add(responseJsa.getString(i));
                    }
                }
            } else {
                throw new IllegalArgumentException("No results found for parameter");
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return countries;
    }

    public ArrayList<Integer> getDailyTestCount() {
        return getDailyCount("tests");
    }

    public ArrayList<Integer> getDailyDeathCount() {
        return getDailyCount("deaths");
    }

    public ArrayList<Integer> getDailyCaseCount() {
        return getDailyCount("cases");
    }

    public ArrayList<String> getDates() {
        ArrayList<String> result = new ArrayList<>();
        String lastDate = "";
        try {
            for (int i = 0; i < responseJsa.length(); i++) {
                JSONObject jso = responseJsa.getJSONObject(i);
                if (!jso.getString("day").equals(lastDate)) {
                    lastDate = jso.getString("day");
                    result.add(lastDate);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.reverse(result);
        return result;
    }

    protected ArrayList<Integer> getDailyCount(String property) {
        ArrayList<Integer> result = new ArrayList<>();
        String lastDate = "";
        try {
            for (int i = 0; i < responseJsa.length(); i++) {
                JSONObject jso = responseJsa.getJSONObject(i);
                if (!jso.getString("day").equals(lastDate)) {
                    result.add(jso.getJSONObject(property).getInt("total"));
                    lastDate = jso.getString("day");
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
        Collections.reverse(result);
        return result;
    }

    protected CovidCountryData parseSingleCountry(JSONObject country) {
        CovidCountryData ctd = new CovidCountryData();
        try {
            JSONObject cases = country.getJSONObject("cases");
            JSONObject deaths = country.getJSONObject("deaths");
            JSONObject tests = country.getJSONObject("tests");
            ctd.date = country.getString("time");
            ctd.name = country.getString("country");
            ctd.activeCases = cases.getInt("active");
            ctd.criticalCases = cases.getInt("critical");
            // +123
            String newStr = cases.getString("new");
            if (newStr != null && newStr != "null") {
                ctd.newCases = Integer.parseInt(newStr.substring(1));
            } else {
                ctd.newCases = 0;
            }
            ctd.recoveredCases = cases.getInt("recovered");
            ctd.totalCases = cases.getInt("total");
            // +123
            newStr = deaths.getString("new");
            if (newStr != null && newStr != "null") {
                ctd.newDeaths = Integer.parseInt(newStr.substring(1));
            } else {
                ctd.newDeaths = 0;
            }
            ctd.totalDeaths = deaths.getInt("total");
            ctd.totalTests = tests.getInt("total");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return ctd;
    }
}
