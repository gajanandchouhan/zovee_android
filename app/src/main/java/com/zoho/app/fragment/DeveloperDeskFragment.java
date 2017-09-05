package com.zoho.app.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.method.LinkMovementMethod;
import android.text.style.ForegroundColorSpan;
import android.text.style.UnderlineSpan;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.zoho.app.R;

/**
 * Created by hp on 20-06-2017.
 */

public class DeveloperDeskFragment extends Fragment {
    private View view;
    private TextView textViewDeveloperDesk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_developer_desk, container, false);
        textViewDeveloperDesk = (TextView) view.findViewById(R.id.textView_developer_desk);
        textViewDeveloperDesk.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable wordtoSpan = new SpannableString(getString(R.string.developer_desk_content));
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), getString(R.string.developer_desk_content).indexOf("www.infobyd.com"),getString(R.string.developer_desk_content).indexOf("or you can")-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), getString(R.string.developer_desk_content).indexOf("Team@infobyd.com"), getString(R.string.developer_desk_content).indexOf("We will be happy")-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new UnderlineSpan(),getString(R.string.developer_desk_content).indexOf("www.infobyd.com"),getString(R.string.developer_desk_content).indexOf("or you can")-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
       wordtoSpan.setSpan(new UnderlineSpan(),getString(R.string.developer_desk_content).indexOf("Team@infobyd.com"), getString(R.string.developer_desk_content).indexOf("We will be happy")-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewDeveloperDesk.setText(wordtoSpan);
        return view;
    }


}
