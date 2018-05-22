package com.zoho.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;

import com.zoho.app.R;
import com.zoho.app.activity.MainActivity;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.MailConstant;
import com.zoho.app.utils.Utils;

/**
 * Created by hp on 20-06-2017.
 */

public class FeedbackFragment extends Fragment {
    private View view;
    private EditText feedbackEditText;
    private Button sendButton;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_feedback, container, false);
        feedbackEditText = (EditText) view.findViewById(R.id.et_feedback);
        sendButton = (Button) view.findViewById(R.id.btn_rfer);
        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                feedback();
            }
        });
        return view;
    }
    private void feedback() {
        String msg = feedbackEditText.getText().toString().trim();
        if (!CheckNetworkState.isOnline(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.no_internet));
            return;
        }
        if (msg.length() == 0) {
            feedbackEditText.setError(getString(R.string.enter_feedback));
            return;
        }
        final CustomProgressDialog dialog = new CustomProgressDialog(getActivity());
        dialog.setCancelable(false);
        dialog.show();
        PrefManager instance = PrefManager.getInstance(getActivity());
        String user = instance.getString(PrefConstants.NAME) + " " + instance.getString(PrefConstants.LASTNAME);
        String email = instance.getString(PrefConstants.EMAIL);
        /*BackgroundMail.newBuilder(getActivity())
                .withUsername(MailConstant.USER_ID)
                .withPassword(MailConstant.PASSWORD)
                .withMailto(MailConstant.TO_ID)
                .withType(BackgroundMail.TYPE_PLAIN)
                .withSubject(String.format(getString(R.string.subject_feedback), user))
                .withBody("Hello team,\nHere is user feedback-\nUser name: " + user + "\nUser email: " + email + "\nFeedback Message: " + msg)
                .withOnSuccessCallback(new BackgroundMail.OnSuccessCallback() {
                    @Override
                    public void onSuccess() {
                        //do some magic
                        dialog.dismiss();
                        Utils.showToast(getActivity(), getString(R.string.feedback_success));
                        ((MainActivity)getActivity()).onBackPressed();

                    }
                })
                .withOnFailCallback(new BackgroundMail.OnFailCallback() {
                    @Override
                    public void onFail() {
                        //do some magic
                        dialog.dismiss();
                        Utils.showToast(getActivity(), getString(R.string.error));
                    }
                })
                .send();*/
    }

}
