package com.akexorcist.sleepingforless.view.message;

import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;
import com.akexorcist.sleepingforless.util.Utility;
import com.ms.square.android.etsyblur.BlurDialogFragmentHelper;

/**
 * Created by Akexorcist on 3/25/2016 AD.
 */
public class MessageDialogFragment extends DialogFragment implements View.OnLongClickListener {
    private static final String KEY_MESSAGE = "key_message";

    private TextView tvDialogMessage;

    private BlurDialogFragmentHelper mHelper;
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
        mHelper = new BlurDialogFragmentHelper(this);
        mHelper.onCreate();
        mHelper.setBgColorResId(R.color.light_alpha_black);
        mHelper.setAnimDuration(1000);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        return inflater.inflate(R.layout.dialog_message, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        if (savedInstanceState == null) {
            message = getArguments().getString(KEY_MESSAGE);
        } else {
            message = savedInstanceState.getString(KEY_MESSAGE);
        }

        tvDialogMessage = (TextView) view.findViewById(R.id.tv_dialog_message);
        tvDialogMessage.setText(message);
        tvDialogMessage.setOnLongClickListener(this);
    }

    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        mHelper.onActivityCreated();
    }

    @Override
    public void onSaveInstanceState(Bundle outState) {
        outState.putString(KEY_MESSAGE, message);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onStart() {
        super.onStart();
        mHelper.onStart();
    }

    @Override
    public void onDismiss(DialogInterface dialog) {
        mHelper.onDismiss();
        super.onDismiss(dialog);
        if (listener != null) {
            listener.onDismiss();
        }
    }

    public void setOnDismissListener(OnDismissListener listener) {
        this.listener = listener;
    }

    @Override
    public boolean onLongClick(View v) {
        Utility.getInstance().copyTextToClipboard("Message", message);
        Snackbar.make(tvDialogMessage, R.string.copy_message_to_clipboard, Snackbar.LENGTH_SHORT).show();
        return true;
    }

    public interface OnDismissListener {
        void onDismiss();
    }
}

