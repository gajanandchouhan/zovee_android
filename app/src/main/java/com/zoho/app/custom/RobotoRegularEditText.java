package com.zoho.app.custom;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.EditText;


/**
 * Created by user on 6/25/2016.
 */
public class RobotoRegularEditText extends EditText {
    public RobotoRegularEditText(Context context) {
        super(context);

        applyCustomFont(context);
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs) {
        super(context, attrs);

        applyCustomFont(context);
    }

    public RobotoRegularEditText(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);

        applyCustomFont(context);
    }

    private void applyCustomFont(Context context) {
        Typeface customFont = FontCache.getTypeface(FontTypes.REGULAR, context);
        setTypeface(customFont);
    }
}
