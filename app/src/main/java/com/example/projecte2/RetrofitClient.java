package com.example.projecte2;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    //ip wifi cole 192.168.17.85
    //ip otra  192.168.1.39
    //ip servidor 13.219.159.223
    // ip servidor dominio https://digimarketing.brawledgar.cat/
    private static final String BASE_URL = "http://13.219.159.223/";
    private static Retrofit retrofit;

    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}

