package com.akexorcist.sleepingforless.view.feed;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/10/2016 AD.
 */
public class FeedDetailBottomSheet extends BottomSheetDialogFragment {

    static FeedDetailBottomSheet newInstance() {
        return new FeedDetailBottomSheet();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return LayoutInflater.from(getContext()).inflate(R.layout.view_bottom_sheet_feed_detail, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);


    }
}
