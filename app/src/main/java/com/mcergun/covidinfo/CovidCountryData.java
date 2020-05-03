package com.mcergun.covidinfo;

import android.os.Parcel;
import android.os.Parcelable;

public class CovidCountryData implements Parcelable {
    public String date;
    public String name;
    public int activeCases;
    public int criticalCases;
    public int newCases;
    public int recoveredCases;
    public int totalCases;
    public int newDeaths;
    public int totalDeaths;
    public int totalTests;

    public CovidCountryData() {

    }

    protected CovidCountryData(Parcel in) {
        date = in.readString();
        name = in.readString();
        activeCases = in.readInt();
        criticalCases = in.readInt();
        newCases = in.readInt();
        recoveredCases = in.readInt();
        totalCases = in.readInt();
        newDeaths = in.readInt();
        totalDeaths = in.readInt();
        totalTests = in.readInt();
    }

    public static final Creator<CovidCountryData> CREATOR = new Creator<CovidCountryData>() {
        @Override
        public CovidCountryData createFromParcel(Parcel in) {
            return new CovidCountryData(in);
        }

        @Override
        public CovidCountryData[] newArray(int size) {
            return new CovidCountryData[size];
        }
    };

    @Override
    public int describeContents() {
        return 0;
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(date);
        dest.writeString(name);
        dest.writeInt(activeCases);
        dest.writeInt(criticalCases);
        dest.writeInt(newCases);
        dest.writeInt(recoveredCases);
        dest.writeInt(totalCases);
        dest.writeInt(newDeaths);
        dest.writeInt(totalDeaths);
        dest.writeInt(totalTests);
    }
}
