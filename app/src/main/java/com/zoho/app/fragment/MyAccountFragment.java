package com.zoho.app.fragment;

import android.Manifest;
import android.content.Context;
import android.content.CursorLoader;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.squareup.picasso.Picasso;
import com.zoho.app.R;
import com.zoho.app.activity.RegisterActivity;
import com.zoho.app.custom.CustomProgressDialog;
import com.zoho.app.model.response.LoginResponseData;
import com.zoho.app.model.response.LoginResponseModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.ApiInterface;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.perisistance.PrefConstants;
import com.zoho.app.perisistance.PrefManager;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.channels.FileChannel;

import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.RequestBody;
import okhttp3.internal.Util;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 06-10-2017.
 */

public class MyAccountFragment extends Fragment {
    private static final int PERMISSION_CODE = 112;
    EditText nameEditText, lastNameEditText, companyNameEditText, emailEditText;
    ImageView cameraImageView;
    ImageView profileImageView;
    private RelativeLayout submitButton;
    private static final int PICK_IMAGE = 113;
    private File profilePath;
    private String imagepath;
    private Uri uriImagePath;
    private File userImageFile;
    private int uId;

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
        profileImageView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Utils.hasPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE})) {
                    pickImage();
                } else {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE, Manifest.permission.READ_EXTERNAL_STORAGE}, PERMISSION_CODE);
                }
            }
        });
        emailEditText.setEnabled(false);
        emailEditText.setAlpha(0.5f);

        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                update();
            }
        });
        setView();
    }

    private void setView() {
        PrefManager instance = PrefManager.getInstance(getActivity());
        String firstName = instance.getString(PrefConstants.NAME);
        String lastName = instance.getString(PrefConstants.LASTNAME);
        String email = instance.getString(PrefConstants.EMAIL);
        String companyName = instance.getString(PrefConstants.COMPANY);
        String imageUrl = instance.getString(PrefConstants.IMAGE);
        uId = instance.getInt(PrefConstants.U_ID);
        nameEditText.setText(firstName);
        lastNameEditText.setText(lastName);
        emailEditText.setText(email);
        companyNameEditText.setText(companyName);
        Picasso.with(profileImageView.getContext()).load(imageUrl).placeholder(R.drawable.user).into(profileImageView);
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

    private void update() {
        if (!CheckNetworkState.isOnline(getActivity())) {
            Utils.showToast(getActivity(), getString(R.string.no_internet));
            return;
        }
        String name = nameEditText.getText().toString().trim();
        String lastName = lastNameEditText.getText().toString().trim();
        String company = companyNameEditText.getText().toString().trim();
        if (name.length() == 0 && lastName.length() == 0 && company.length() == 0) {
            Utils.showToast(getActivity(), "Please enter something");
            return;
        }
       /* if (userImageFile==null){
            Utils.showToast(getActivity(),getString(R.string.select_image));
            return;
        }*/
        MultipartBody.Part body = null;
        if (userImageFile != null) {
            final RequestBody requestFile =
                    RequestBody.create(MediaType.parse("multipart/form-data"), userImageFile);

// MultipartBody.Part is used to send also the actual file name

            body = MultipartBody.Part.createFormData("ProfilePicture", userImageFile != null ? userImageFile.getName() : "", requestFile);
        }


// add another part within the multipart request
        RequestBody fName =
                RequestBody.create(
                        MediaType.parse("text/plain"), name);
        RequestBody lName =
                RequestBody.create(
                        MediaType.parse("text/plain"), lastName);
        RequestBody uid =
                RequestBody.create(
                        MediaType.parse("text/plain"), String.valueOf(uId));
        RequestBody cName =
                RequestBody.create(
                        MediaType.parse("text/plain"), company);
        final CustomProgressDialog progressDialog = new CustomProgressDialog(getActivity());
        progressDialog.setCancelable(false);
        progressDialog.show();

        ApiInterface apiInterface = ApiClient.getApiInterface();
        Call<LoginResponseModel> loginResponseModelCall = apiInterface.updateProfile(fName, lName, uid, cName, body);
        loginResponseModelCall.enqueue(new Callback<LoginResponseModel>() {
            @Override
            public void onResponse(Call<LoginResponseModel> call, Response<LoginResponseModel> response) {
                progressDialog.dismiss();
                if (response.body() != null) {
                    LoginResponseModel body1 = response.body();
                    if (body1.getResponseCode().equalsIgnoreCase(ConstantLib.RESPONSE_SUCCESS)) {
                        LoginResponseData responseData = body1.getResponseData();
                        Context mContext = getActivity();
                        PrefManager.getInstance(mContext).putString(PrefConstants.NAME, responseData.getFirstName());
                        PrefManager.getInstance(mContext).putString(PrefConstants.LASTNAME, responseData.getLastName());
                        PrefManager.getInstance(mContext).putString(PrefConstants.COMPANY, responseData.getCompanyName());
                        PrefManager.getInstance(mContext).putString(PrefConstants.EMAIL, responseData.getEmail());
                        PrefManager.getInstance(mContext).putString(PrefConstants.IMAGE, responseData.getImageUrl());
                        PrefManager.getInstance(mContext).putInt(PrefConstants.U_ID, responseData.getUserId());
                        setView();
                    }
                    Utils.showToast(getActivity(), body1.getResponseMessage());
                } else {
                    Utils.showToast(getActivity(), getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<LoginResponseModel> call, Throwable t) {
                t.printStackTrace();
                progressDialog.dismiss();
                Utils.showToast(getActivity(), getString(R.string.server_error));
            }
        });
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
        userImageFile = sourceFile;
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
        String filePath = Utils.getPath(getActivity(), uri);
    /*    if (uri.getHost().contains("com.android.providers.media")) {
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
        }*/
        return filePath;
    }

    private String getRealPathFromURI_API11to18(Uri contentUri) {
        String[] proj = {MediaStore.Images.Media.DATA};
        String result = null;

        CursorLoader cursorLoader = new CursorLoader(
                getActivity(),
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

    public void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == getActivity().RESULT_OK) {
            switch (requestCode) {

                case PICK_IMAGE:
                    Log.d("onActivityResult", "uriImagePath Gallary :" + data.getData().toString());
                    //   File f = new File(imagepath);

                    try {
                        //   f.createNewFile();
                        String path = "";
                        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                            path = getRealPathFromURI(data.getData());
                        } else {
                            path = getRealPathFromURI_API11to18(data.getData());
                        }
                        userImageFile = new File(path);
                        cameraImageView.setVisibility(View.GONE);
                        //Picasso.with(this).load(path).into(profileImageView);
                        profileImageView.setImageURI(data.getData());
                        // copyFile(new File(path), f);
                    } catch (Exception e) {
                        // TODO Auto-generated catch block
                        e.printStackTrace();
                    }

                    break;


            }
        }


    }
}
