package com.zoho.app.view;

import com.zoho.app.model.response.CategoryModel;
import com.zoho.app.model.response.CategoryResponseModel;

import java.util.List;

/**
 * Created by hp on 11-06-2017.
 */

public interface HomeView {
    void showProgress();
    void setCategoryList(List<CategoryModel> categoryList);
    void hideProgress();
}
