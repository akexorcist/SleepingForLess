package com.akexorcist.sleepingforless.view.search;

import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.mypopsy.widget.FloatingSearchView;

public class SearchPostActivity extends SFLActivity implements View.OnClickListener, FloatingSearchView.OnSearchListener {
    private FloatingSearchView sfvPostSearch;
    private FrameLayout layoutPostSearch;
    private boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_post);

        layoutPostSearch = (FrameLayout) findViewById(R.id.layout_post_search);
        sfvPostSearch = (FloatingSearchView) findViewById(R.id.sfv_post_search);

        layoutPostSearch.setOnClickListener(this);
        sfvPostSearch.setActivated(true);
        sfvPostSearch.setOnSearchListener(this);
    }

    @Override
    public void onClick(View v) {
        if (v == layoutPostSearch) {
            finish();
        }
    }

    @Override
    public void onSearchAction(CharSequence charSequence) {
        if(!finish) {
            finish = true;
            BusProvider.getInstance().post(new SearchRequest(charSequence.toString()));
            finish();
        }
    }
}
