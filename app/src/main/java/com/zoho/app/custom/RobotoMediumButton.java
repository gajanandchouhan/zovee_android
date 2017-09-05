package com.zoho.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.Button;


/**
 * Created by user on 6/25/2016.
 */
public class RobotoMediumButton extends Button {
    public RobotoMediumButton(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public RobotoMediumButton(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public RobotoMediumButton(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(FontTypes.MEDIUM, context);
        setTypeface(customFont);
    }
}
