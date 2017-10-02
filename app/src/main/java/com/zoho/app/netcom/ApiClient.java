package com.zoho.app.netcom;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
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
            HttpLoggingInterceptor logging = new HttpLoggingInterceptor();
// set your desired log level
            logging.setLevel(HttpLoggingInterceptor.Level.BODY);

            OkHttpClient.Builder httpClient = new OkHttpClient.Builder();
// add your other interceptors …

// add logging as last interceptor
            httpClient.addInterceptor(logging);  // <-- this is the important line!
            client = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(httpClient.build())
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
