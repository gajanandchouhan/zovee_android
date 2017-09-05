package com.zoho.app.activity;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.DocumentsContract;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;

import com.zoho.app.R;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.presentor.RegisterPresentor;
import com.zoho.app.presentor.SplashPresentor;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.PdfCreater;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.RegisterView;
import com.zoho.app.view.SplashView;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

public class RegisterActivity extends AppCompatActivity implements RegisterView {
    private static final int PERMISSION_CODE = 112;
    private RegisterPresentor registerPresentor;
    ProgressBar progressBar;
    EditText nameEditText, lastNameEditText, companyNameEditText, emailEditText;
    ImageView cameraImageView;
    ImageView profileImageView;
    private static final int PICK_IMAGE = 113;
    private File profilePath;
    private String imagepath;
    private Uri uriImagePath;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        registerPresentor = new RegisterPresentor(this, this);
        bindViews();

        findViewById(R.id.btn_register).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                register();
            }
        });
    }

    private void bindViews() {
        nameEditText = (EditText) findViewById(R.id.et_name);
        lastNameEditText = (EditText) findViewById(R.id.et_lastname);
        companyNameEditText = (EditText) findViewById(R.id.et_companyName);
        emailEditText = (EditText) findViewById(R.id.et_email);
        progressBar = (ProgressBar) findViewById(R.id.loading);

        cameraImageView = (ImageView) findViewById(R.id.camera);
        profileImageView = (ImageView) findViewById(R.id.imageView);

        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.hasPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) {
                    pickImage();
                } else {
                    ActivityCompat.requestPermissions(RegisterActivity.this, new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                }
            }
        });
    }

    private void register() {
        if (!CheckNetworkState.isOnline(this)) {
            Utils.showToast(this, getString(R.string.no_internet));
            return;
        }
        String name = nameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String company = companyNameEditText.getText().toString().trim();
        String email = emailEditText.getText().toString().trim();
        if (name.length() == 0) {
            nameEditText.setError(getString(R.string.name_empty));
            return;
        } else if (lastName.length() == 0) {
            lastNameEditText.setError(getString(R.string.lastname_empty));
            return;
        } else if (company.length() == 0) {
            companyNameEditText.setError(getString(R.string.companyName_empty));
            return;
        } else if (email.length() == 0) {
            emailEditText.setError(getString(R.string.email_empty));
            return;
        } else if (!Utils.isValidEmail(email)) {
            emailEditText.setError(getString(R.string.invalid_email));
            return;
        }
        enableDisableView(true);
        registerPresentor.registerBySendingMail(name, lastName, company, email);
    }

    private void enableDisableView(boolean disable) {
        if (disable) {
            nameEditText.setEnabled(false);
            emailEditText.setEnabled(false);
            companyNameEditText.setEnabled(false);
            lastNameEditText.setEnabled(false);
            profileImageView.setEnabled(false);
        } else {
            nameEditText.setEnabled(true);
            emailEditText.setEnabled(true);
            companyNameEditText.setEnabled(true);
            lastNameEditText.setEnabled(true);
            profileImageView.setEnabled(true);
        }
    }


    @Override
    public void showProgress() {
        progressBar.setVisibility(View.VISIBLE);
    }

    @Override
    public void onRegister() {
        Utils.showToast(this, "welcome " + nameEditText.getText().toString().trim());
        Utils.startActivity(this, MainActivity.class, null);
        finish();
    }

    @Override
    public void hideProgress() {
        enableDisableView(false);
        progressBar.setVisibility(View.INVISIBLE);
    }


    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        switch (requestCode) {
            case PERMISSION_CODE: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    pickImage();

                } else {

                    // permission denied, boo! Disable the
                    // functionality that depends on this permission.
                }
                return;
            }

            // other 'case' lines to check for other
            // permissions this app might request
        }

    }

    private void pickImage() {
        File zoveePath = new File(ConstantLib.FILE_PATH);
        profilePath = new File(ConstantLib.PROFILE_PATH);
        if (!zoveePath.exists()) {
            zoveePath.mkdir();
        }
        if (!profilePath.exists()) {
            profilePath.mkdir();
        }
        Intent photoPickerIntent = new Intent(Intent.ACTION_GET_CONTENT);
        imagepath = ConstantLib.PROFILE_PATH + ConstantLib.PROFILE_FILE;
        uriImagePath = Uri.fromFile(new File(imagepath));
        photoPickerIntent.setType("image/*");
        photoPickerIntent.putExtra(MediaStore.EXTRA_OUTPUT, uriImagePath);
        photoPickerIntent.putExtra("outputFormat", Bitmap.CompressFormat.PNG.name());
        photoPickerIntent.putExtra("return-data", true);
        startActivityForResult(photoPickerIntent, PICK_IMAGE);

    }


    private void copyFile(File sourceFile, File destFile) throws IOException {

        FileChannel source = null;
        FileChannel destination = null;
        source = new FileInputStream(sourceFile).getChannel();
        destination = new FileOutputStream(destFile).getChannel();
        if (destination != null && source != null) {
            destination.transferFrom(source, 0, source.size());
        }
        if (source != null) {
            source.close();
        }
        if (destination != null) {
            destination.close();
        }


    }

    public String getRealPathFromURI(Uri uri) {
        String filePath = "";
        if (uri.getHost().contains("com.android.providers.media")) {
            // Image pick from recent
            String wholeID = null;
            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                wholeID = DocumentsContract.getDocumentId(uri);
                String id = wholeID.split(":")[1];

                String[] column = {MediaStore.Images.Media.DATA};

                // where id is equal to
                String sel = MediaStore.Images.Media._ID + "=?";

                Cursor cursor = getContentResolver().query(MediaStore.Images.Media.EXTERNAL_CONTENT_URI,
                        column, sel, new String[]{id}, null);

                int columnIndex = cursor.getColumnIndex(column[0]);

                if (cursor.moveToFirst()) {
                    filePath = cursor.getString(columnIndex);
                }
                cursor.close();
                return filePath;

            }
            // Split at colon, use second item in the array
        }
        return null;
    }

    private String getRealPathFromURI_API11to18(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                this,
                contentUri, proj, null, null, null);
        Cursor cursor = cursorLoader.loadInBackground();

        if (cursor != null) {
            int column_index =
                    cursor.getColumnIndexOrThrow(MediaStore.Images.Media.DATA);
            cursor.moveToFirst();
            result = cursor.getString(column_index);
        }
        return result;
    }

    protected void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == RESULT_OK) {
            switch (requestCode) {

                case PICK_IMAGE:
                    Log.d("onActivityResult", "uriImagePath Gallary :" + data.getData().toString());
                    File f = new File(imagepath);

                    try {
                        f.createNewFile();
                        profileImageView.setImageURI(data.getData());
                        cameraImageView.setVisibility(View.GONE);
                        String path = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            path = getRealPathFromURI(data.getData());
                        } else {
                            path = getRealPathFromURI_API11to18(data.getData());
                        }
                        copyFile(new File(path), f);
                    } catch (IOException e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;


            }
        }


    }
}
