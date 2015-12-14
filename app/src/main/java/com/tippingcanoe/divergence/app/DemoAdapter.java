package com.tippingcanoe.divergence.app;

import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.tippingcanoe.divergence.SectionAdapter;

import java.util.List;

public class DemoAdapter extends SectionAdapter {
    String identifier;

    public DemoAdapter(String identifier) {
        this.identifier = identifier;
    }

    @Nullable
    @Override
    protected RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType) {
        return new ViewHolder(parent);
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        ((ViewHolder) holder).textView.setText(identifier + ": " + position);
    }

    @Override
    protected void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position) {
        ((ViewHolder) holder).textView.setText(identifier + ": " + position);
    }

    @Override
    protected int getDataViewType(int position) {
        return R.layout.list_cell;
    }

    @Override
    protected int getDataId(int position) {
        return position;
    }

    @Override
    protected int getDataCount() {
        return 10;
    }

    @Override
    protected void onBindNoResultsView(View noResultsView) {

    }

    @Override
    protected void onBindLoadingView(View loadingView) {

    }

    @Override
    protected void onBindErrorView(View errorView) {

    }

    @Nullable
    @Override
    protected Integer getNoResultsLayout() {
        return null;
    }

    @Nullable
    @Override
    protected Integer getLoadingResultsLayout() {
        return null;
    }

    @Nullable
    @Override
    protected Integer getErrorResultsLayout() {
        return null;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        TextView textView;

        public ViewHolder(ViewGroup parent) {
            super(LayoutInflater.from(parent.getContext()).inflate(R.layout.list_cell, parent, false));
            textView = (TextView) itemView.findViewById(R.id.textView);
        }
    }
}
