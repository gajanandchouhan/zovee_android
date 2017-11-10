package com.zoho.app.fragment;

import android.app.ProgressDialog;
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
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.creativityapps.gmailbackgroundlibrary.BackgroundMail;
import com.zoho.app.R;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.MailConstant;
import com.zoho.app.utils.Utils;

/**
 * Created by hp on 20-06-2017.
 */

public class AboutUsFragment extends Fragment {
    private View view;
    private TextView textView, abouTextView;
    private TextView textViewDeveloperDesk;

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_aboutus, container, false);
        textView = (TextView) view.findViewById(R.id.textView_version);
        abouTextView = (TextView) view.findViewById(R.id.textView_content_about);
        Spannable wordtoSpan = new SpannableString(getString(R.string.about_msg));

        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 0, 5, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), 14, 19, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.colorPrimary)), getString(R.string.about_msg).indexOf("Infobyd"), getString(R.string.about_msg).length() - 1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        abouTextView.setText(wordtoSpan);
        textView.setText(Utils.getVersion(getActivity()));


        textViewDeveloperDesk = (TextView) view.findViewById(R.id.textView_developer_desk);
       // textViewDeveloperDesk.setMovementMethod(LinkMovementMethod.getInstance());
        Spannable wordtoSpan2 = new SpannableString(getString(R.string.developer_desk_content));
        wordtoSpan2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), getString(R.string.developer_desk_content).indexOf("www.infobyd.com"),getString(R.string.developer_desk_content).indexOf("or you can")-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan2.setSpan(new ForegroundColorSpan(getResources().getColor(R.color.blue)), getString(R.string.developer_desk_content).indexOf("Team@infobyd.com"), getString(R.string.developer_desk_content).indexOf("We will be happy")-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan2.setSpan(new UnderlineSpan(),getString(R.string.developer_desk_content).indexOf("www.infobyd.com"),getString(R.string.developer_desk_content).indexOf("or you can")-1, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        wordtoSpan2.setSpan(new UnderlineSpan(),getString(R.string.developer_desk_content).indexOf("Team@infobyd.com"), getString(R.string.developer_desk_content).indexOf("We will be happy")-3, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
        textViewDeveloperDesk.setText(wordtoSpan2);
        return view;
    }


}
