package com.zoho.app.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.drawable.Drawable;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.os.AsyncTaskCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.zoho.app.R;
import com.zoho.app.adapter.NotificationListAdapter;
import com.zoho.app.adapter.ShowMoreAdapter;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.fcm.NotificationDataModel;
import com.zoho.app.model.response.CategoryModel;
import com.zoho.app.model.response.SubCategoryModel;
import com.zoho.app.perisistance.DBHelper;
import com.zoho.app.utils.ConstantLib;

import java.lang.reflect.Field;
import java.util.Collections;
import java.util.List;

/**
 * Created by hp on 29-05-2017.
 */

public class NotificationActivity extends BaseActivity {
    private RecyclerView mRecyclerView;
    private NotificationListAdapter adapter;
    private Toolbar toolbar;
    private TextView titleTextView;
    private ImageView backImageView;
    public static boolean FROM_NOTFICATION = false;
    private List<NotificationDataModel> list;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_showmore);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        bindView();
        titleTextView.setText(getString(R.string.notifications));

        AsyncTaskCompat.executeParallel(new AsyncTask<Void, Void, Void>() {
            CustomProgressDialog progressDialog;


            @Override
            protected void onPreExecute() {
                super.onPreExecute();
                progressDialog = new CustomProgressDialog(NotificationActivity.this);
                progressDialog.show();
            }

            @Override
            protected Void doInBackground(Void... params) {
                list = DBHelper.getInstance(NotificationActivity.this).getNotificationData();
                Collections.reverse(list);
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
                setAdapter(list);
                progressDialog.dismiss();
            }

        });
        backImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onBackPressed();
            }
        });
        findViewById(R.id.clear_imageView).setVisibility(View.VISIBLE);
        findViewById(R.id.clear_imageView).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (list != null && list.size() > 0) {
                    showDeleteAlert();
                }

            }
        });
    }

    private void showDeleteAlert() {
        new AlertDialog.Builder(this).setMessage("Alert").setPositiveButton("OK", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
                DBHelper.getInstance(NotificationActivity.this).clearNotifications();
                list.clear();
                adapter.notifyDataSetChanged();
            }
        }).setNegativeButton("CANCEL", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        }).setMessage("Are you want to clear your notification list?").show();
    }

    private void setAdapter(List<NotificationDataModel> notificationDataModels) {
        if (notificationDataModels != null && notificationDataModels.size() > 0) {
            mRecyclerView.setVisibility(View.VISIBLE);
            findViewById(R.id.textView_nodata).setVisibility(View.GONE);
            adapter = new NotificationListAdapter(this, notificationDataModels);
            mRecyclerView.setAdapter(adapter);
        } else {
            mRecyclerView.setVisibility(View.GONE);
            findViewById(R.id.textView_nodata).setVisibility(View.VISIBLE);
        }
    }

    private void bindView() {
        mRecyclerView = (RecyclerView) findViewById(R.id.recyclerView);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        titleTextView = (TextView) findViewById(R.id.textView_title);
        backImageView = (ImageView) findViewById(R.id.back_imageView);
        backImageView.setVisibility(View.VISIBLE);
    }


    @Override
    public void onBackPressed() {
        if (FROM_NOTFICATION) {
            Intent intent = new Intent(this, MainActivity.class);
            intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
            startActivity(intent);
            ActivityCompat.finishAffinity(this);
            finish();
        } else {
            finish();
        }
    }
}
