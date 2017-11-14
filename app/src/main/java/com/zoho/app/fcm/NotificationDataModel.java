package com.zoho.app.fcm;

import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Generated;

/**
 * Created by hp on 14-11-2017.
 */
@Entity
public class NotificationDataModel {
    @Id
    private Long id;
    private String title;
    private String message;
    private int subCategoryId;
    private String subCategoryThumbUrl;
    private String subcategoryName;
    private String subCategoryDescription;

    @Generated(hash = 766461173)
    public NotificationDataModel(Long id, String title, String message,
            int subCategoryId, String subCategoryThumbUrl, String subcategoryName,
            String subCategoryDescription) {
        this.id = id;
        this.title = title;
        this.message = message;
        this.subCategoryId = subCategoryId;
        this.subCategoryThumbUrl = subCategoryThumbUrl;
        this.subcategoryName = subcategoryName;
        this.subCategoryDescription = subCategoryDescription;
    }

    @Generated(hash = 1359623756)
    public NotificationDataModel() {
    }

    public Long getId() {
        return this.id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getTitle() {
        return this.title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getMessage() {
        return this.message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public int getSubCategoryId() {
        return this.subCategoryId;
    }

    public void setSubCategoryId(int subCategoryId) {
        this.subCategoryId = subCategoryId;
    }

    public String getSubCategoryThumbUrl() {
        return this.subCategoryThumbUrl;
    }

    public void setSubCategoryThumbUrl(String subCategoryThumbUrl) {
        this.subCategoryThumbUrl = subCategoryThumbUrl;
    }

    public String getSubcategoryName() {
        return this.subcategoryName;
    }

    public void setSubcategoryName(String subcategoryName) {
        this.subcategoryName = subcategoryName;
    }

    public String getSubCategoryDescription() {
        return this.subCategoryDescription;
    }

    public void setSubCategoryDescription(String subCategoryDescription) {
        this.subCategoryDescription = subCategoryDescription;
    }

}
