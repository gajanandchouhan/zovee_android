package com.zoho.app.utils;

import android.os.Environment;

/**
 * Created by hp on 01-06-2017.
 */

public interface ConstantLib {
    String DEVELOPER_KEY = "AIzaSyAB4ZuUiGR_CcPMVV5IwEArciw-NMHWxrA";
    String RESPONSE_SUCCESS = "1";
    String KEY_CATEGORY = "category_model";
    String SUB_MODEL = "sub_model";
    String VIDEO_MODEL = "video_model";
    String APP_URL = "https://play.google.com/store/apps/details?id=com.zovee.app";
    String ABOUT_US_TEXT = "Infobyd holds wide experience in Information and Communication Technology. We work together with each of our clients to match their expectations and reach their goals as efficiently and pleasantly as possible. We build our teams around your domain and technology requirements, offering specialized services and solutions that meet the distinct needs of your business.\n" +
            "\n" +
            "We enable our clients to use Customer Relationship Management(CRM) model for managing there company's interaction with current and future customers. It involves using technology to organize, automate, and synchronize sales, marketing, customer service and technical support.";
    String ZOHO_DIRECTORY = "/ZoVee";
    String ZOVEE_PROFILE = "/ZoVee/Profile";

    String VIDEO_DETAILSURL = "https://www.googleapis.com/youtube/v3/videos?id=F4HuIqwGuBU&key=AIzaSyAB4ZuUiGR_CcPMVV5IwEArciw-NMHWxrA&fields=items(id,snippet(description,channelId,title,categoryId),statistics)&part=snippet,statistics";
    String FILE_PATH = Environment.getExternalStorageDirectory().getPath() + ZOHO_DIRECTORY;
    String PROFILE_PATH = Environment.getExternalStorageDirectory().getPath() + ZOVEE_PROFILE;
    String PROFILE_FILE = "/profile.png";

    String PROFILE_PIC_PATH = PROFILE_PATH + PROFILE_FILE;
}
