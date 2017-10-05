package com.zoho.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.method.ScrollingMovementMethod;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoho.app.R;

/**
 * Created by hp on 01-10-2017.
 */

public class DescriptionFragment extends Fragment {
    private TextView descTextView;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.fragment_description, container, false);
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        descTextView = (TextView) view.findViewById(R.id.textView_descript);
        Fragment parentFragment = getParentFragment();
        if (parentFragment instanceof DetailsFragment){
            descTextView.setText(((DetailsFragment) parentFragment).getVideoDesc());
        }
        descTextView.setMovementMethod(new ScrollingMovementMethod());


    }
}
