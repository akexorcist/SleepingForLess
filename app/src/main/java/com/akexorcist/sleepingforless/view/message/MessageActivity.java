package com.akexorcist.sleepingforless.view.message;

import android.os.Bundle;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.common.SFLActivity;

public class MessageActivity extends SFLActivity implements MessageDialogFragment.OnDismissListener {
    public static final String KEY_MESSAGE = "key_message";

    private String message;
    private MessageDialogFragment dialogFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (savedInstanceState == null) {
            restoreIntentData();
            showDialogFragment();
        }
    }

    @Override
    public void finish() {
        super.finish();
        overridePendingTransition(R.anim.anim_default_fade_in, R.anim.anim_default_fade_out);
    }

    private void restoreIntentData() {
        message = getIntent().getStringExtra(KEY_MESSAGE);
    }

    public void showDialogFragment() {
        dialogFragment = MessageDialogFragment.newInstance(message);
        dialogFragment.setOnDismissListener(this);
        dialogFragment.setRetainInstance(true);
        dialogFragment.show(getSupportFragmentManager(), "dialog");
    }

    @Override
    public void onDismiss() {
        finish();
    }
}
