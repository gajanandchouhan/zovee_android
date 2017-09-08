package com.zoho.app.netcom;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 * Created by hp on 03-06-2017.
 */

public class ApiClient {
    private static Retrofit client;
    //http://zovee.infobyd.com/
    private static final String BASE_URL = "http://zohotrainingtest.infobyd.com/webser/Service1.svc/";
    // private static final String BASE_URL = "http://zovee.infobyd.com/webser/Service1.svc/";
    private static ApiInterface apiInterface;

    public static Retrofit getClient() {
        if (client == null) {
            client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return client;
    }

    public static ApiInterface getApiInterface() {
        if (apiInterface == null) {
            apiInterface = getClient().create(ApiInterface.class);
        }
        return apiInterface;
    }

}
