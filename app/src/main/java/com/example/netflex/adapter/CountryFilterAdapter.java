package com.example.netflex.adapter;

import android.content.res.ColorStateList;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.netflex.R;
import com.example.netflex.model.Country;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class CountryFilterAdapter extends RecyclerView.Adapter<CountryFilterAdapter.CountryViewHolder> {

    public interface OnCountryClickListener {
        void onCountryChange(List<Country> selectedCountries);
    }

    private List<Country> countries;
    private Set<Country> selectedCountries = new HashSet<>();
    private final OnCountryClickListener listener;

    public CountryFilterAdapter(List<Country> countryList, OnCountryClickListener listener) {
        this.countries = new ArrayList<>();

        Country all = new Country();
        Country.Name allName = new Country.Name();
        allName.setCommon("All Country");
        all.setName(allName);
        all.setCca3(null);
        this.countries.add(all);

        this.countries.addAll(countryList);
        this.listener = listener;

        this.selectedCountries.add(this.countries.get(0));
        listener.onCountryChange(new ArrayList<>(selectedCountries));
    }

    @NonNull
    @Override
    public CountryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_country_button, parent, false);
        return new CountryViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CountryViewHolder holder, int position) {
        Country country = countries.get(position);
        holder.btnCountry.setText(country.getName().getCommon());

        boolean isSelected = selectedCountries.contains(country);
        holder.btnCountry.setBackgroundTintList(ColorStateList.valueOf(
                isSelected ? Color.parseColor("#FFCC00") : Color.parseColor("#303040")
        ));

        holder.btnCountry.setOnClickListener(v -> {
            selectedCountries.clear();
            selectedCountries.add(country);

            listener.onCountryChange(new ArrayList<>(selectedCountries));
            notifyDataSetChanged();
        });

    }

    @Override
    public int getItemCount() {
        return countries.size();
    }

    static class CountryViewHolder extends RecyclerView.ViewHolder {
        Button btnCountry;
        CountryViewHolder(@NonNull View itemView) {
            super(itemView);
            btnCountry = itemView.findViewById(R.id.btnCountry);
        }
    }
}
