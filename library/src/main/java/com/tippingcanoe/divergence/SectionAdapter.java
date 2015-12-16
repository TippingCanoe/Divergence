package com.tippingcanoe.divergence;

import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.ViewGroup;

import java.security.InvalidParameterException;
import java.util.List;

public abstract class SectionAdapter extends RecyclerView.Adapter<RecyclerView.ViewHolder> {
    boolean isLoading = false;
    boolean isError = false;

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads) {
        if (getLoadingResultsLayout() != null && getItemViewType(position) == getLoadingResultsLayout()) {
            onBindLoadingView(holder);
        } else if (getErrorResultsLayout() != null && getItemViewType(position) == getErrorResultsLayout()) {
            onBindErrorView(holder);
        } else if (getNoResultsLayout() != null && getItemViewType(position) == getNoResultsLayout()) {
            onBindNoResultsView(holder);
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
            onBindLoadingView(holder);
        } else if (getErrorResultsLayout() != null && getItemViewType(position) == getErrorResultsLayout()) {
            onBindErrorView(holder);
        } else if (getNoResultsLayout() != null && getItemViewType(position) == getNoResultsLayout()) {
            onBindNoResultsView(holder);
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

    /**
     * Return an appropriate view holder for the given item type.
     *
     * @param parent
     * @param viewType
     * @return
     */
    protected abstract RecyclerView.ViewHolder onCreateDataViewHolder(ViewGroup parent, int viewType);

    /**
     * Perform appropriate binding of the view to the data in this position.
     *
     * @param holder
     * @param position
     * @param payloads
     */
    protected abstract void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position, List<Object> payloads);

    /**
     * Perform appropriate binding of the view to the data in this position.
     *
     * @param holder
     * @param position
     */
    protected abstract void onBindDataViewHolder(RecyclerView.ViewHolder holder, int position);

    /**
     * Return the type of view for the element in this position.
     *
     * @param position
     * @return
     */
    protected abstract int getDataViewType(int position);

    /**
     * Return the unique id for the element in this position.
     *
     * @param position
     * @return
     */
    protected abstract int getDataId(int position);

    /**
     * Calculate and return the number of elements to be displayed, not including loading indicators
     * or error indicators.
     *
     * @return
     */
    protected abstract int getDataCount();

    /**
     * Perform appropriate binding on the no results view.
     *
     * @param holder
     */
    protected abstract void onBindNoResultsView(RecyclerView.ViewHolder holder);

    /**
     * Perform appropriate binding on the loading view.
     *
     * @param holder
     */
    protected abstract void onBindLoadingView(RecyclerView.ViewHolder holder);

    /**
     * Perform appropriate binding on the error view.
     *
     * @param holder
     */
    protected abstract void onBindErrorView(RecyclerView.ViewHolder holder);

    /**
     * Optionally, return the layout resource identifier to be displayed if there are no results.
     *
     * @return
     */
    protected abstract
    @Nullable
    @LayoutRes
    Integer getNoResultsLayout();

    /**
     * Optionally, return the layout resource identifier to be displayed if results are loading.
     *
     * @return
     */
    protected abstract
    @Nullable
    @LayoutRes
    Integer getLoadingResultsLayout();

    /**
     * Optionally, return the layout resource identifier to be displayed if there is an error.
     *
     * @return
     */
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
