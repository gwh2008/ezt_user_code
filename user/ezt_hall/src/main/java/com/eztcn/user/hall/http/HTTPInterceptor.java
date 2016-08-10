package com.eztcn.user.hall.http;


import android.util.Log;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Locale;

import okhttp3.Headers;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okio.Buffer;

/**
 * @Author: lizhipeng
 * @Data: 16/4/12 下午5:19
 * @Description: 定义http拦截器，用于设置http协议和日志调试
 */
public class HTTPInterceptor implements Interceptor {
    public static String TAG = "HTTP";
    private static final Charset UTF8 = Charset.forName("UTF-8");

    @Override
    public Response intercept(Chain chain) throws IOException {
        //封装headers
        Request request = chain.request().newBuilder()
                //TODO 添加请求头信息,和后台协调编码，不过通常都是utf-8
//                .addHeader("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8")
//                .addHeader("Content-Type", "text/html;charset=utf-8")
//                .addHeader("Accept-Encoding", "gzip, deflate")
//                .addHeader("Connection", "keep-alive")
//                .addHeader("Accept", "*/*")
//                .addHeader("Cookie", "add cookies here")
                .build();
        Headers headers = request.headers();
        String requestUrl = request.url().toString(); //获取请求url地址
        String methodStr = request.method(); //获取请求方式
        RequestBody body = request.body(); //获取请求body
//        Log.i(TAG, "======================================================================");
        Log.i(TAG, "============================HTTP START================================");
//        Log.i(TAG, "======================================================================");
        Log.i(TAG, "===========================HTTP REQUEST===============================");
//        Log.i(TAG, "======================================================================");
        Log.i(TAG, "requestUrl=====>" + requestUrl);
        Log.i(TAG, "requestMethod=====>" + methodStr);
//        Log.i(TAG, "requestHeaders=====>" + headers);
//        L.i(TAG, "requestHeaders=====>" + headers.get("Content-Type"));
//        L.i(TAG, "requestHeaders=====>" + headers.get("Host"));
        Log.i(TAG, "======================request body======================");
        if (body != null) {
            Buffer buffer = new Buffer();
            body.writeTo(buffer);
            Charset charset = UTF8;
            MediaType contentType = body.contentType();
            if (contentType != null) {
                charset = contentType.charset(UTF8);
            }
            Log.i(TAG, "request body=====>" + buffer.readString(charset));
        } else {
            Log.i(TAG, "request body=====>" + "body is null");
        }

//        Log.i(TAG, "request:" + request.toString());
//        Log.i(TAG, "======================================================================");
        Log.i(TAG, "===========================HTTP RESPONSE==============================");
//        Log.i(TAG, "======================================================================");
        long t1 = System.nanoTime();
        Response response = chain.proceed(chain.request());
        long t2 = System.nanoTime();
//        Log.i(TAG, String.format(Locale.getDefault(), "Received response for %s in (%.1fms)%n%s",
//                response.request().url(), (t2 - t1) / 1e6d, response.headers()));
        Long content_length = response.body().contentLength();
        ResponseBody responseBody;
        if (content_length < 0) {
            responseBody = response.peekBody(1024 * 4);
        } else {
            responseBody = response.peekBody(content_length);
        }
        Log.i(TAG, "======================response body======================");
        String sss = responseBody.string();
        if (sss.length() > 0) {
            Log.i(TAG, "response body:" + sss);
        }else {
            Log.i(TAG, "response body: body is null");
        }
//        Log.i(TAG, "======================================================================");
        Log.i(TAG, "=============================HTTP END=================================");
//        Log.i(TAG, "======================================================================");
        return response;
    }
}
