package com.zoho.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;


/**
 * Created by user on 6/25/2016.
 */
public class RobotoMediumTextView extends TextView {
    public RobotoMediumTextView(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public RobotoMediumTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(FontTypes.MEDIUM, context);
        setTypeface(customFont);
    }
}
