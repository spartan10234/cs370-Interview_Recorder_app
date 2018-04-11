package com.example.denver.recorder_ui;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import database.RecordingEntity;

public class RecordingEntityAdapter extends RecyclerView.Adapter<RecordingEntityAdapter.ViewHolder> {

    private List<RecordingEntity> items;
    private int itemLayout;

    public RecordingEntityAdapter(List<RecordingEntity> items, int itemLayout){
        this.items = items;
        this.itemLayout = itemLayout;
    }

    @NonNull
    @Override
    public RecordingEntityAdapter.ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(parent.getContext()).inflate(itemLayout, parent, false);
        return new ViewHolder(v);

    }

    @Override
    public void onBindViewHolder(@NonNull RecordingEntityAdapter.ViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        public TextView title_field;
        public ViewHolder(View itemView) {
            super(itemView);
            title_field = (TextView) itemView.findViewById(R.id.list_item_title);
        }

    }

}
