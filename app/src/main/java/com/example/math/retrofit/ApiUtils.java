package com.example.math.retrofit;

public class ApiUtils {
    private ApiUtils() {}

    public static final String BASE_URL = "https://siarez.ir/";

    public static APIService getAPIService() {

        return RetrofitClient.getClient(BASE_URL).create(APIService.class);
    }
}
