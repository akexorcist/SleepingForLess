package com.akexorcist.sleepingforless.widget;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.akexorcist.sleepingforless.R;

/**
 * Created by Akexorcist on 3/15/2016 AD.
 */
public class MenuButton extends LinearLayout {
    private TextView tvMenuText;
    private ImageView ivMenuIcon;
    private String text;
    private int iconResId;

    public MenuButton(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        setupStyleable(context, attrs);
        setupView(context);
    }

    public MenuButton(Context context, AttributeSet attrs) {
        super(context, attrs);
        setupStyleable(context, attrs);
        setupView(context);
    }

    public void setupStyleable(Context context, AttributeSet attrs) {
        TypedArray typedArray = context.obtainStyledAttributes(attrs, R.styleable.MenuButton);
        text = typedArray.getString(R.styleable.MenuButton_mbText);
        iconResId = typedArray.getResourceId(R.styleable.MenuButton_mbIconSrc, 0);
        typedArray.recycle();
    }

    public void setupView(Context context) {
        inflate(context, R.layout.view_menu_button_widget, this);
        tvMenuText = (TextView) findViewById(R.id.menu_button_tv_text);
        ivMenuIcon = (ImageView) findViewById(R.id.menu_button_iv_icon);
        updateText();
        updateIcon();
    }

    public void setText(String text) {
        this.text = text;
        updateText();
    }

    public void setIconResource(int iconResId) {
        this.iconResId = iconResId;
        updateIcon();
    }

    private void updateText() {
        tvMenuText.setText(text);
    }

    private void updateIcon() {
        ivMenuIcon.setImageResource(iconResId);
    }

    public String getText() {
        return text;
    }

    public int getIconResourceId() {
        return iconResId;
    }
}
