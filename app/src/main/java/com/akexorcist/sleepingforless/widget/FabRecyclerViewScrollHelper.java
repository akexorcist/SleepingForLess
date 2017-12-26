package com.akexorcist.sleepingforless.widget;

import android.support.design.widget.FloatingActionButton;
import android.support.v7.widget.RecyclerView;
import android.view.View;

/**
 * Created by Akexorcist on 27/12/2017 AD.
 */

public class FabRecyclerViewScrollHelper extends RecyclerView.OnScrollListener {
    private FloatingActionButton fabMenu;

    public FabRecyclerViewScrollHelper(FloatingActionButton fabMenu) {
        this.fabMenu = fabMenu;
    }

    @Override
    public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
        super.onScrolled(recyclerView, dx, dy);
        if (dy > 0 && fabMenu.getVisibility() == View.VISIBLE) {
            fabMenu.hide();
        } else if (dy < 0 && fabMenu.getVisibility() != View.VISIBLE) {
            fabMenu.show();
        }
    }
}

