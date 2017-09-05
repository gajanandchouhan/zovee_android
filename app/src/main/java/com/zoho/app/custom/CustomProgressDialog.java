package com.zoho.app.custom;

import android.app.Dialog;
import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.view.Window;

import com.zoho.app.R;

/**
 * Created by hp on 11-08-2017.
 */

public class CustomProgressDialog extends Dialog {


    public CustomProgressDialog(Context context) {
        super(context);
        initView();
    }

    private void initView() {
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        setContentView(R.layout.dialog_view);
        getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));

    }
}
