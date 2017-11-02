package com.akexorcist.sleepingforless.view.message;

import android.app.Dialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.Utility;
import com.akexorcist.sleepingforless.view.feed.FeedActivity;

/**
 * Created by Akexorcist on 3/25/2016 AD.
 */
public class MessageDialogFragment extends DialogFragment {
    private static final String KEY_MESSAGE = "key_message";

    private TextView tvDialogMessage;
    private FloatingActionButton fabClose;
    private FloatingActionButton fabOpenApp;

    private OnDismissListener listener;
    private String message;

    public static MessageDialogFragment newInstance(String message) {
        MessageDialogFragment fragment = new MessageDialogFragment();
        Bundle bundle = new Bundle();
        bundle.putString(KEY_MESSAGE, message);
        fragment.setArguments(bundle);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_message, container, false);
    }

    @NonNull
    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        Dialog dialog = super.onCreateDialog(savedInstanceState);
        // Remove background and title in Pre-Lollipop
        dialog.getWindow().requestFeature(Window.FEATURE_NO_TITLE);
        dialog.getWindow().setBackgroundDrawable(new ColorDrawable(0));
        return dialog;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        bindView(view);
        setupView(savedInstanceState);
    }

    private void bindView(View view) {
        tvDialogMessage = view.findViewById(R.id.tv_dialog_message);
        fabClose = view.findViewById(R.id.fab_dialog_close);
        fabOpenApp = view.findViewById(R.id.fab_dialog_open_app);
    }

    private void setupView(Bundle savedInstanceState) {
        tvDialogMessage.setOnClickListener(view -> copyMessageToClipboard());
        fabClose.setOnClickListener(view -> closeDialog());
        fabOpenApp.setOnClickListener(view -> openSFLApp());

        message = ((savedInstanceState == null) ? getArguments() : savedInstanceState).getString(KEY_MESSAGE);
        tvDialogMessage.setText(message);
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_MESSAGE, message);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDismiss();
        }
    }

    public void copyMessageToClipboard() {
        Utility.getInstance().copyTextToClipboard("Message", message);
        Snackbar.make(tvDialogMessage, R.string.copy_message_to_clipboard, Snackbar.LENGTH_SHORT).show();
    }

    public void closeDialog() {
        dismiss();
    }

    public void openSFLApp() {
        startActivity(new Intent(getContext(), FeedActivity.class));
        dismiss();
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}

