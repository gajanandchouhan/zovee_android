package com.zoho.app.model.response;

import java.io.Serializable;
import java.util.List;

/**
 * Created by hp on 11-06-2017.
 */

public class CategoryModel implements Serializable {
    private String CategoryName;
    private List<SubCategoryModel> SubCategoryList;
    private List<String> MasterBanners;
    private boolean isSelected;

    public void setCategoryName(String categoryName) {
        CategoryName = categoryName;
    }

    public String getCategoryName() {
        return CategoryName;
    }

    public void setSubCategoryList(List<SubCategoryModel> subCategoryList) {
        SubCategoryList = subCategoryList;
    }

    public List<SubCategoryModel> getSubCategoryList() {
        return SubCategoryList;
    }

    public void setMasterBanners(List<String> masterBanners) {
        MasterBanners = masterBanners;
    }

    public List<String> getMasterBanners() {
        return MasterBanners;
    }

    public void setSelected(boolean selected) {
        isSelected = selected;
    }

    public boolean isSelected() {
        return isSelected;
    }
}
