package com.mcergun.covidinfo;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.mcergun.covidinfo.databinding.FragmentCountryInfoBinding;

import java.text.MessageFormat;


/**
 * A simple {@link Fragment} subclass.
 */
public class CountryInfoFragment extends Fragment {

    public static final String COUNTRY_DATA = "CountryData";
    FragmentCountryInfoBinding binding;

    public CountryInfoFragment() {
        // Required empty public constructor
    }

    public void updateDetails(CovidCountryData ctData) {
        binding.tvCasesActive.setText(MessageFormat.format("{0}", ctData.activeCases));
        binding.tvCasesCritical.setText(MessageFormat.format("{0}", ctData.criticalCases));
        binding.tvCasesNew.setText(MessageFormat.format("+{0}", ctData.newCases));
        binding.tvCasesRecovered.setText(MessageFormat.format("{0}", ctData.recoveredCases));
        binding.tvCasesTotal.setText(MessageFormat.format("{0}", ctData.totalCases));
        binding.tvDeathsNew.setText(MessageFormat.format("+{0}", ctData.newDeaths));
        binding.tvDeathsTotal.setText(MessageFormat.format("{0}", ctData.totalDeaths));
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        binding = FragmentCountryInfoBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        CovidCountryData ctData;
        if (getArguments() != null) {
            ctData = (CovidCountryData) getArguments().getParcelable(COUNTRY_DATA);
        } else {
            ctData = new CovidCountryData();
        }
        updateDetails(ctData);
    }
}
