package com.example.moviecollection;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

import java.io.IOException;

public class ApiClient {

    // Базовый URL официального Kinopoisk API (v1.4)
    private static final String BASE_URL = "https://api.kinopoisk.dev/v1.4/";

    private static Retrofit retrofit;

    /**
     * Создает и возвращает синглтон-инстанс Retrofit.
     * Добавляет API-ключ в каждый запрос автоматически.
     */
    public static Retrofit getRetrofitInstance() {
        if (retrofit == null) {

            // OkHttp-клиент с Interceptor для добавления заголовка Authorization
            OkHttpClient client = new OkHttpClient.Builder()
                    .addInterceptor(new Interceptor() {
                        @Override
                        public Response intercept(Chain chain) throws IOException {
                            Request original = chain.request();

                            // Вставляем заголовок "X-API-KEY" из BuildConfig
                            Request requestWithToken = original.newBuilder()
                                    .header("X-API-KEY", BuildConfig.KINOPOISK_API_KEY)
                                    .method(original.method(), original.body())
                                    .build();

                            return chain.proceed(requestWithToken);
                        }
                    })
                    .build();

            // Retrofit с базовым URL, JSON-конвертером и настроенным клиентом
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .client(client)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }

        return retrofit;
    }
}
