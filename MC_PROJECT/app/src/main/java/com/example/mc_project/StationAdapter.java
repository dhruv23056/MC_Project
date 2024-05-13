package com.example.mc_project;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;
import java.util.List;

public class StationAdapter extends RecyclerView.Adapter<StationAdapter.StationViewHolder> {

    private List<String> stations;
    private int currentStationIndex = 0; // Track current station index
    private String destinationName;
    private String singleStationName;
    private int singleStationIndex = -1;

    public StationAdapter(List<String> stations, List<String> stopIds, String destinationName, String singleStationName) {
        this.stations = stations;
        this.destinationName = destinationName;
        this.singleStationName = singleStationName;
        // Find the index of the single station name
        singleStationIndex = stations.indexOf(singleStationName);
        // Mark stations as passed before the single station
//        markStationsAsPassed(currentStationIndex);
    }

    @NonNull
    @Override
    public StationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.station_item, parent, false);
        return new StationViewHolder(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull StationViewHolder holder, int position) {
        String station = stations.get(position);
        holder.stationNameTextView.setText(station);

        // Customize the appearance of the station item based on its position
        if (station.equals(singleStationName)) {
            // Single station
            holder.itemView.setBackgroundResource(R.drawable.start_station_background);
            holder.stationNameTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        }
        else if (station.equals(destinationName)) {
            // Destination station
            holder.itemView.setBackgroundResource(R.drawable.destination_station_background);
            holder.stationNameTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        }
        else if (position == currentStationIndex) {
            // Current station
            holder.itemView.setBackgroundResource(R.drawable.current_station_background);
            holder.stationNameTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.white));
        } else if (position < currentStationIndex) {
            // Passed station
            holder.itemView.setBackgroundResource(R.drawable.passed_station_background);
            holder.stationNameTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.darker_gray));
        } else {
            // Future station
            holder.itemView.setBackgroundResource(R.drawable.future_station_background);
            holder.stationNameTextView.setTextColor(holder.itemView.getContext().getResources().getColor(android.R.color.black));
        }
    }

    @Override
    public int getItemCount() {
        return stations.size();
    }

    static class StationViewHolder extends RecyclerView.ViewHolder {
        TextView stationNameTextView;

        StationViewHolder(View itemView) {
            super(itemView);
            stationNameTextView = itemView.findViewById(R.id.stationNameTextView);
        }
    }

    // Method to update current station index
    public void setCurrentStationIndex(int index) {
        currentStationIndex = index;
        notifyDataSetChanged();
    }

}
