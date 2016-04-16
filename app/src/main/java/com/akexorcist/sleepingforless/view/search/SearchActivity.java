package com.akexorcist.sleepingforless.view.search;

import android.os.Bundle;
import android.widget.FrameLayout;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.analytic.EventTracking;
import com.akexorcist.sleepingforless.bus.BusProvider;
import com.akexorcist.sleepingforless.common.SFLActivity;
import com.mypopsy.widget.FloatingSearchView;

import butterknife.Bind;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class SearchActivity extends SFLActivity implements FloatingSearchView.OnSearchListener {
    @Bind(R.id.sfv_post_search)
    FloatingSearchView sfvPostSearch;

    @Bind(R.id.layout_post_search)
    FrameLayout layoutPostSearch;

    boolean finish = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);

        ButterKnife.bind(this);
        setupView();
    }

    private void setupView() {
        sfvPostSearch.setActivated(true);
        sfvPostSearch.setOnSearchListener(this);
    }

    @OnClick(R.id.layout_post_search)
    public void onPostSearchClick() {
        finish();
    }

    @Override
    public void onSearchAction(CharSequence charSequence) {
        if (!finish) {
            finish = true;
            searchTracking(charSequence.toString());
            BusProvider.getInstance().post(new SearchRequest(charSequence.toString()));
            finish();
        }
    }

    // Google Analytics
    private void searchTracking(String keyword) {
        EventTracking.getInstance().addSearchTracking(keyword);
    }
}
