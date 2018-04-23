package com.daixu.jandan.net.api.retrofit;

import java.util.concurrent.TimeUnit;

import io.reactivex.schedulers.Schedulers;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import timber.log.Timber;

import static java.lang.String.format;

public class ApiRetrofit {

    private static Retrofit retrofit;

    public ApiRetrofit() {
        String url = "http://i.jandan.net/";
        retrofit = new Retrofit.Builder()
                .baseUrl(url)
                .client(getOkHttpClient())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private OkHttpClient getOkHttpClient() {
        HttpLoggingInterceptor logInterceptor = new HttpLoggingInterceptor(new HttpLogger());
        logInterceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        return new OkHttpClient.Builder()
                .addInterceptor(logInterceptor)
                .connectTimeout(15, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .build();
    }

    private static class HttpLogger implements HttpLoggingInterceptor.Logger {

        private StringBuilder mMessage = new StringBuilder();

        @Override
        public void log(String message) {
            // 请求或者响应开始
            if (message.startsWith("--> POST")) {
                mMessage.delete(0, mMessage.length());
            }
            mMessage.append(message.concat("\n"));
            // 请求或者响应结束，打印整条日志
            if (message.startsWith("<-- END HTTP")) {
                Timber.tag("Dagger2").e(format("mMessage=%s", mMessage));
                mMessage.delete(0, mMessage.length());
            }
        }
    }

    public Retrofit getRetrofit() {
        return retrofit;
    }
}
