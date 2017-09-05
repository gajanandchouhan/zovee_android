package com.zoho.app.presentor;

import android.content.Context;

import com.zoho.app.R;
import com.zoho.app.model.response.CategoryResponseModel;
import com.zoho.app.netcom.ApiClient;
import com.zoho.app.netcom.ApiInterface;
import com.zoho.app.netcom.CheckNetworkState;
import com.zoho.app.utils.ConstantLib;
import com.zoho.app.utils.Utils;
import com.zoho.app.view.HomeView;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Created by hp on 11-06-2017.
 */

public class HomePresentor {
    private Context mContext;
    private HomeView homeView;

    public HomePresentor(Context mContext, HomeView homeView) {
        this.mContext = mContext;
        this.homeView = homeView;
    }

    public void getCategories() {
        if (!CheckNetworkState.isOnline(mContext)) {
            Utils.showToast(mContext, mContext.getString(R.string.no_internet));
            return;
        }
        homeView.showProgress();
        ApiInterface apiInterface = ApiClient.getApiInterface();
        final Call<CategoryResponseModel> categories = apiInterface.getCategories();
        categories.enqueue(new Callback<CategoryResponseModel>() {
            @Override
            public void onResponse(Call<CategoryResponseModel> call, Response<CategoryResponseModel> response) {
                homeView.hideProgress();
                if (response.body() != null) {
                    CategoryResponseModel categoryResponseModel = response.body();
                    if (categoryResponseModel.getResponseCode().equals(ConstantLib.RESPONSE_SUCCESS)) {
                        homeView.setCategoryList(categoryResponseModel.getResponseData());
                    } else {
                        Utils.showToast(mContext, categoryResponseModel.getResponseMessage());
                    }
                } else {
                    Utils.showToast(mContext, mContext.getString(R.string.server_error));
                }
            }

            @Override
            public void onFailure(Call<CategoryResponseModel> call, Throwable t) {
                homeView.hideProgress();
                Utils.showToast(mContext, mContext.getString(R.string.server_error));
                t.printStackTrace();
            }
        });

    }
}
