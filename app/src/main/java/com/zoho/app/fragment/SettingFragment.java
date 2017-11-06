package com.zoho.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.itextpdf.tool.xml.svg.graphic.Text;
import com.zoho.app.R;
import com.zoho.app.activity.ChangePasswordActivity;
import com.zoho.app.activity.LoginActivity;
import com.zoho.app.activity.MainActivity;
import com.zoho.app.activity.MyAccountActivity;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.DialogClickListnenr;
import com.zoho.app.utils.Utils;

/**
 * Created by hp on 20-06-2017.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView textViewChangePass, textViewLogout, textViewMyAccount;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        textViewChangePass = (TextView) view.findViewById(R.id.text_change_password);
        textViewLogout = (TextView) view.findViewById(R.id.textView_logout);
        textViewMyAccount = (TextView) view.findViewById(R.id.textView_myaccount);
        textViewMyAccount.setOnClickListener(this);
        textViewChangePass.setOnClickListener(this);
        textViewLogout.setOnClickListener(this);
        if (PrefManager.getInstance(getActivity()).getString(PrefConstants.LOGIN_TYPE).equalsIgnoreCase("1")){
            textViewChangePass.setVisibility(View.VISIBLE);
        }
        else{
            textViewChangePass.setVisibility(View.GONE);
        }
        return view;
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.text_change_password:
                startActivity(new Intent(getActivity(), ChangePasswordActivity.class));
                break;
            case R.id.textView_logout:

                break;
            case R.id.textView_myaccount:
                Utils.startActivity(getActivity(), MyAccountActivity.class, null);
                break;
        }
    }
}
