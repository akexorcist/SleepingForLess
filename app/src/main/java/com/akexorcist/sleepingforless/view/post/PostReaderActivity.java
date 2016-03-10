package com.akexorcist.sleepingforless.view.post;

import android.os.Bundle;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLDraggerActivity;

public class PostReaderActivity extends SFLDraggerActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_post_reader);
        setSlideEnabled(true);
    }
}
