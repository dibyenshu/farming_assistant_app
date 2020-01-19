package com.example.fhelp;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

public class CropListAdapter extends RecyclerView.Adapter<CropListAdapter.CropListViewHolder> {

    private ArrayList<String> cropList;

    public CropListAdapter(ArrayList<String> cropList) {
        this.cropList = cropList;
    }

    @NonNull
    @Override
    public CropListViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.list_item_frag1_layout, parent, false);
        return new CropListViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CropListViewHolder holder, int position) {
        String cropName = cropList.get(position);
        holder.textView.setText(cropName);
    }

    @Override
    public int getItemCount() {
        return cropList.size();
    }

    public class CropListViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public CropListViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
        }
    }
}
