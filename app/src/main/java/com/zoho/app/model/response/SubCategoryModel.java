package com.zoho.app.model.response;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;

import java.io.Serializable;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hp on 11-06-2017.
 */
@Entity
public class SubCategoryModel implements Serializable {
    public static final long serialVersionUID = 42L;
    private String SubCategooryThumbUrl;
    @Id
    private Long SubCategoryId;
    private String SubCategoryName;
    private String SubCategoryDescription;

    @Generated(hash = 1283895120)
    public SubCategoryModel(String SubCategooryThumbUrl, Long SubCategoryId,
            String SubCategoryName, String SubCategoryDescription) {
        this.SubCategooryThumbUrl = SubCategooryThumbUrl;
        this.SubCategoryId = SubCategoryId;
        this.SubCategoryName = SubCategoryName;
        this.SubCategoryDescription = SubCategoryDescription;
    }

    @Generated(hash = 1308654413)
    public SubCategoryModel() {
    }

    public String getSubCategooryThumbUrl() {
        return SubCategooryThumbUrl;
    }

    public void setSubCategooryThumbUrl(String subCategooryThumbUrl) {
        SubCategooryThumbUrl = subCategooryThumbUrl;
    }

    public Long getSubCategoryId() {
        return SubCategoryId;
    }

    public void setSubCategoryId(Long subCategoryId) {
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
