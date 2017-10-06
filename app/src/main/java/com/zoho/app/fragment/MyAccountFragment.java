package com.zoho.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.zoho.app.R;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.ConstantLib;

import java.io.File;

/**
 * Created by hp on 06-10-2017.
 */

public class MyAccountFragment extends Fragment {
    EditText nameEditText, lastNameEditText, companyNameEditText, emailEditText;
    ImageView cameraImageView;
    ImageView profileImageView;
    private RelativeLayout submitButton;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_myaccount, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        submitButton = (RelativeLayout) view.findViewById(R.id.btn_submit);
        nameEditText = (EditText) view.findViewById(R.id.et_name);
        lastNameEditText = (EditText) view.findViewById(R.id.et_lastname);
        companyNameEditText = (EditText) view.findViewById(R.id.et_companyName);
        emailEditText = (EditText) view.findViewById(R.id.et_email);
        cameraImageView = (ImageView) view.findViewById(R.id.camera);
        profileImageView = (ImageView) view.findViewById(R.id.imageView);
        enableDisableView(true);
        setView();
    }

    private void setView() {
        PrefManager instance = PrefManager.getInstance(getActivity());
        String firstName = instance.getString(PrefConstants.NAME);
        String lastName = instance.getString(PrefConstants.LASTNAME);
        String email = instance.getString(PrefConstants.EMAIL);
        String companyName = instance.getString(PrefConstants.COMPANY);
        String imageUrl = instance.getString(PrefConstants.IMAGE);
        int uId = instance.getInt(PrefConstants.U_ID);
        nameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        emailEditText.setText(email);
        companyNameEditText.setText(companyName);
        Picasso.with(profileImageView.getContext()).load(imageUrl).into(profileImageView);
    }

    private void enableDisableView(boolean disable) {
        if (disable) {
            nameEditText.setEnabled(false);
            emailEditText.setEnabled(false);
            companyNameEditText.setEnabled(false);
            lastNameEditText.setEnabled(false);
            profileImageView.setEnabled(false);
            submitButton.setEnabled(false);
        } else {
            nameEditText.setEnabled(true);
            emailEditText.setEnabled(true);
            companyNameEditText.setEnabled(true);
            lastNameEditText.setEnabled(true);
            submitButton.setEnabled(false);
            profileImageView.setEnabled(true);
        }
    }
}
