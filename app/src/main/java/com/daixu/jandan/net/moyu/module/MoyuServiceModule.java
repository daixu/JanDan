package com.daixu.jandan.net.moyu.module;

import com.daixu.jandan.net.moyu.retrofit.MoyuRetrofit;
import com.daixu.jandan.net.moyu.service.MoyuServer;

import java.util.concurrent.TimeUnit;

import javax.inject.Singleton;

import dagger.Module;
import dagger.Provides;
import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import timber.log.Timber;

import static java.lang.String.format;

@Module
public class MoyuServiceModule {

    public MoyuServiceModule() {
    }

    @Provides
    OkHttpClient providerOkHttpClient() {
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

    @Singleton
    @Provides
    MoyuServer providerApiService(MoyuRetrofit retrofit) {
        return retrofit.getRetrofit().create(MoyuServer.class);
    }

    @Singleton
    @Provides
    MoyuRetrofit providerMoyuRetrofit() {
        return new MoyuRetrofit();
    }
}