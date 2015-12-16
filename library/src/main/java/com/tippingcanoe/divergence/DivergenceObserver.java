package com.tippingcanoe.divergence;

import android.support.v7.widget.RecyclerView;

public class DivergenceObserver extends RecyclerView.AdapterDataObserver {
    DivergenceAdapter divergenceAdapter;
    String adapterIdentifier;

    public DivergenceObserver(DivergenceAdapter divergenceAdapter, String adapterIdentifier) {
        this.divergenceAdapter = divergenceAdapter;
        this.adapterIdentifier = adapterIdentifier;
    }

    @Override
    public void onChanged() {
        divergenceAdapter.onChanged(adapterIdentifier);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount) {
        divergenceAdapter.onItemRangeChanged(adapterIdentifier, positionStart, itemCount);
    }

    @Override
    public void onItemRangeChanged(int positionStart, int itemCount, Object payload) {
        divergenceAdapter.onItemRangeChanged(adapterIdentifier, positionStart, itemCount, payload);
    }

    @Override
    public void onItemRangeInserted(int positionStart, int itemCount) {
        divergenceAdapter.onItemRangeInserted(adapterIdentifier, positionStart, itemCount);
    }

    @Override
    public void onItemRangeRemoved(int positionStart, int itemCount) {
        divergenceAdapter.onItemRangeRemoved(adapterIdentifier, positionStart, itemCount);
    }

    @Override
    public void onItemRangeMoved(int fromPosition, int toPosition, int itemCount) {
        divergenceAdapter.onItemRangeMoved(adapterIdentifier, fromPosition, toPosition, itemCount);
    }
}
