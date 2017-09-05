package com.zoho.app.model.response;

import java.io.Serializable;

/**
 * Created by hp on 11-06-2017.
 */

public class SubCategoryModel implements Serializable {
    private String SubCategooryThumbUrl;
    private int SubCategoryId;
    private String SubCategoryName;
    private String SubCategoryDescription;

    public String getSubCategooryThumbUrl() {
        return SubCategooryThumbUrl;
    }

    public void setSubCategooryThumbUrl(String subCategooryThumbUrl) {
        SubCategooryThumbUrl = subCategooryThumbUrl;
    }

    public int getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        SubCategoryId = subCategoryId;
    }

    public String getSubCategoryName() {
        return SubCategoryName;
    }

    public void setSubCategoryName(String subCategoryName) {
        SubCategoryName = subCategoryName;
    }

    public void setSubCategoryDescription(String subCategoryDescription) {
        SubCategoryDescription = subCategoryDescription;
    }

    public String getSubCategoryDescription() {
        return SubCategoryDescription;
    }
}
