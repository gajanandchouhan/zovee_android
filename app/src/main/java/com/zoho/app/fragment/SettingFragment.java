package com.zoho.app.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.widget.SwitchCompat;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.style.ForegroundColorSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
import android.widget.TextView;

import com.itextpdf.tool.xml.svg.graphic.Text;
import com.zoho.app.R;
import com.zoho.app.activity.ChangePasswordActivity;
import com.zoho.app.activity.LoginActivity;
import com.zoho.app.activity.MainActivity;
import com.zoho.app.activity.MyAccountActivity;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.model.request.NotificationStatusModel;
import com.zoho.app.model.response.BaseResponseModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.ApiInterface;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.DialogClickListnenr;
import com.zoho.app.utils.Utils;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 20-06-2017.
 */

public class SettingFragment extends Fragment implements View.OnClickListener {
    private View view;
    private TextView textViewChangePass, textViewLogout, textViewMyAccount;
    private SwitchCompat switchCompat;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_setting, container, false);
        textViewChangePass = view.findViewById(R.id.text_change_password);
        textViewLogout = view.findViewById(R.id.textView_logout);
        textViewMyAccount = view.findViewById(R.id.textView_myaccount);
        switchCompat = view.findViewById(R.id.switch_not);
        textViewMyAccount.setOnClickListener(this);
        textViewChangePass.setOnClickListener(this);
        textViewLogout.setOnClickListener(this);
        int anInt = PrefManager.getInstance(getActivity()).getInt(PrefConstants.NOTIFICATION_STATUS);
        if (anInt == 1) {
            switchCompat.setChecked(true);
        } else {
            switchCompat.setChecked(false);
        }
        if (PrefManager.getInstance(getActivity()).getString(PrefConstants.LOGIN_TYPE).equalsIgnoreCase("1")) {
            textViewChangePass.setVisibility(View.VISIBLE);
        } else {
            textViewChangePass.setVisibility(View.GONE);
        }

        switchCompat.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                changeStatus(b);
            }
        });
        return view;
    }

    private void changeStatus(final boolean b) {
        if (!CheckNetworkState.isOnline(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.no_internet));
            switchCompat.setChecked(!b);
            return;
        }
        final CustomProgressDialog dialog = new CustomProgressDialog(getActivity());
        dialog.show();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        NotificationStatusModel notificationStatusModel = new NotificationStatusModel();
        notificationStatusModel.setId(PrefManager.getInstance(getActivity()).getInt(PrefConstants.U_ID));
        notificationStatusModel.setNotification(b ? 1 : 0);
        apiInterface.setNotification(notificationStatusModel).enqueue(new Callback<BaseResponseModel>() {
            @Override
            public void onResponse(Call<BaseResponseModel> call, Response<BaseResponseModel> response) {
                dialog.dismiss();
                if (response.body() != null) {
                    Utils.showToast(getActivity(), response.body().getResponseMessage());
                    switchCompat.setChecked(b);
                    PrefManager.getInstance(getActivity()).putInt(PrefConstants.NOTIFICATION_STATUS, b ? 1 : 0);
                } else {
                    switchCompat.setChecked(!b);
                    Utils.showToast(getActivity(), getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<BaseResponseModel> call, Throwable t) {
                dialog.dismiss();
                t.printStackTrace();
                switchCompat.setChecked(!b);
                Utils.showToast(getActivity(), getString(R.string.server_error));
            }
        });
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
