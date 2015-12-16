package com.tippingcanoe.divergence;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import java.security.InvalidParameterException;
import java.util.List;

public abstract class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    boolean isLoading = false;
    boolean isError = false;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (getLoadingResultsLayout() != null && getItemViewType(position) == getLoadingResultsLayout()) {
            onBindLoadingView(holder.itemView);
        } else if (getErrorResultsLayout() != null && getItemViewType(position) == getErrorResultsLayout()) {
            onBindErrorView(holder.itemView);
        } else if (getNoResultsLayout() != null && getItemViewType(position) == getNoResultsLayout()) {
            onBindNoResultsView(holder.itemView);
        } else {
            onBindDataViewHolder(holder, position, payloads);
        }
    }

    @Override
    public int getItemViewType(int position) {
        if (getLoadingResultsLayout() != null && isLoading && position >= getDataCount()) {
            return getLoadingResultsLayout();
        }

        if (getErrorResultsLayout() != null && isError && position >= getDataCount()) {
            return getErrorResultsLayout();
        }

        if (getNoResultsLayout() != null && getDataCount() == 0 && position == 0) {
            return getNoResultsLayout();
        }

        return getDataViewType(position);
    }

    @Override
    public long getItemId(int position) {
        if (getLoadingResultsLayout() != null && getItemViewType(position) == getLoadingResultsLayout()) {
            return getLoadingResultsLayout();
        }

        if (getErrorResultsLayout() != null && getItemViewType(position) == getErrorResultsLayout()) {
            return getErrorResultsLayout();
        }

        if (getNoResultsLayout() != null && getItemViewType(position) == getNoResultsLayout()) {
            return getNoResultsLayout();
        }

        return getDataId(position);
    }

    @Override
    public int getItemCount() {
        int itemCount = getDataCount();

        if (isLoading && getLoadingResultsLayout() != null) {
            itemCount++;
        }

        if (isError && getErrorResultsLayout() != null) {
            itemCount++;
        }

        if (itemCount == 0 && getNoResultsLayout() != null) {
            itemCount++;
        }

        return itemCount;
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        if (getLoadingResultsLayout() != null && getItemViewType(position) == getLoadingResultsLayout()) {
            onBindLoadingView(holder.itemView);
        } else if (getErrorResultsLayout() != null && getItemViewType(position) == getErrorResultsLayout()) {
            onBindErrorView(holder.itemView);
        } else if (getNoResultsLayout() != null && getItemViewType(position) == getNoResultsLayout()) {
            onBindNoResultsView(holder.itemView);
        } else {
            onBindDataViewHolder(holder, position);
        }
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (getLoadingResultsLayout() != null && viewType == getLoadingResultsLayout()) {
            return onCreateLoadingResultsViewHolder(parent, viewType);
        }

        if (getErrorResultsLayout() != null && viewType == getErrorResultsLayout()) {
            return onCreateErrorResultsViewHolder(parent, viewType);
        }

        if (getNoResultsLayout() != null && viewType == getNoResultsLayout()) {
            return onCreateNoResultsViewHolder(parent, viewType);
        }

        return onCreateDataViewHolder(parent, viewType);
    }

    protected RecyclerView.ViewHolder onCreateLoadingResultsViewHolder(ViewGroup parent, int viewType) {
        if (getLoadingResultsLayout() != null) {
            return new ViewHolder(parent, getLoadingResultsLayout());
        }

        return null;
    }

    protected RecyclerView.ViewHolder onCreateErrorResultsViewHolder(ViewGroup parent, int viewType) {
        if (getErrorResultsLayout() != null) {
            return new ViewHolder(parent, getErrorResultsLayout());
        }

        return null;
    }

    protected RecyclerView.ViewHolder onCreateNoResultsViewHolder(ViewGroup parent, int viewType) {
        if (getNoResultsLayout() != null) {
            return new ViewHolder(parent, getNoResultsLayout());
        }

        return null;
    }

    public boolean isLoading() {
        return isLoading;
    }

    public void setLoading(boolean loading) {
        if (getLoadingResultsLayout() == null) {
            throw new InvalidParameterException("You did not provide a layout for the loading cell.");
        }


        if (isLoading != loading) {
            boolean wasError = isError;

            isLoading = loading;
            isError = false;

            if (wasError) {
                notifyItemChanged(getDataCount());
            } else {
                notifyItemInserted(getDataCount());
            }
        }
    }

    public boolean isError() {
        return isError;
    }

    public void setError(boolean error) {
        if (getErrorResultsLayout() == null) {
            throw new InvalidParameterException("You did not provide a layout for the error cell.");
        }

        if (isError != error) {
            boolean wasLoading = isLoading;

            isLoading = false;
            isError = error;

            if (wasLoading) {
                notifyItemChanged(getDataCount());
            } else {
                notifyItemInserted(getDataCount());
            }
        }
    }

    protected abstract
    @Nullable
    RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType);

    protected abstract void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads);

    protected abstract void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position);

    protected abstract int getDataViewType(int position);

    protected abstract int getDataId(int position);

    protected abstract int getDataCount();

    protected abstract void onBindNoResultsView(View noResultsView);

    protected abstract void onBindLoadingView(View loadingView);

    protected abstract void onBindErrorView(View errorView);

    protected abstract
    @Nullable
    @LayoutRes
    Integer getNoResultsLayout();

    protected abstract
    @Nullable
    @LayoutRes
    Integer getLoadingResultsLayout();

    protected abstract
    @Nullable
    @LayoutRes
    Integer getErrorResultsLayout();

    class ViewHolder extends RecyclerView.ViewHolder {
        public ViewHolder(ViewGroup parent, @LayoutRes int layoutRes) {
            super(LayoutInflater.from(parent.getContext()).inflate(layoutRes, parent, false));
        }
    }
}
