package com.example.tomislavrajic.themoviedbsearch2;

import android.graphics.Canvas;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;

import com.example.tomislavrajic.themoviedbsearch2.adapters.WatchedMoviesRecyclerViewAdapter;

public class SwipeToDeleteCallback extends ItemTouchHelper.SimpleCallback {

    private WatchedMoviesRecyclerViewAdapter watchedMoviesRecyclerViewAdapter;

    SwipeToDeleteCallback(WatchedMoviesRecyclerViewAdapter watchedMoviesRecyclerViewAdapter) {
        super(0, ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT);
        this.watchedMoviesRecyclerViewAdapter = watchedMoviesRecyclerViewAdapter;
    }

    @Override
    public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder viewHolder1) {
        return false;
    }

    @Override
    public void onChildDraw(@NonNull Canvas c, @NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, float dX, float dY, int actionState, boolean isCurrentlyActive) {
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive);
    }

    @Override
    public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int i) {
        int position = viewHolder.getAdapterPosition();
        watchedMoviesRecyclerViewAdapter.deleteItem(position);
    }
}
