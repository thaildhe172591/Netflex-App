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

import java.util.ArrayList;
import java.util.List;

public class YearFilterAdapter extends RecyclerView.Adapter<YearFilterAdapter.YearViewHolder> {
    private final List<Integer> years;
    private final OnYearClickListener listener;
    private Integer selectedYear = null;

    public interface OnYearClickListener {
        void onClick(Integer year);  // null means 'All'
    }

    public YearFilterAdapter(List<Integer> years, OnYearClickListener listener) {
        this.years = new ArrayList<>();
        this.years.add(0); // special value 0 = 'All'
        this.years.addAll(years);
        this.listener = listener;
    }

    @NonNull
    @Override
    public YearViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_genre_button, parent, false);
        return new YearViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull YearViewHolder holder, int position) {
        Integer year = years.get(position);
        String label = (year == 0) ? "All Year" : String.valueOf(year);

        holder.btnYear.setText(label);

        boolean isSelected = (selectedYear == null && year == 0) || (selectedYear != null && selectedYear.equals(year));
        holder.btnYear.setBackgroundTintList(ColorStateList.valueOf(
                isSelected ? Color.parseColor("#FFCC00") : Color.parseColor("#303040")
        ));

        holder.btnYear.setOnClickListener(v -> {
            selectedYear = (year == 0) ? null : year;
            listener.onClick(selectedYear);
            notifyDataSetChanged(); // refresh all buttons
        });
    }

    @Override
    public int getItemCount() {
        return years.size();
    }

    static class YearViewHolder extends RecyclerView.ViewHolder {
        Button btnYear;

        YearViewHolder(@NonNull View itemView) {
            super(itemView);
            btnYear = itemView.findViewById(R.id.btnGenre);
        }
    }
}
