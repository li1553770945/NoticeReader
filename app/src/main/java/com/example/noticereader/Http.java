package com.example.noticereader;

import android.media.MediaPlayer;
import android.util.Log;

import com.alibaba.fastjson.JSONObject;

import org.json.JSONArray;

import java.io.IOException;
import java.util.Objects;

import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.FormBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.RequestBody;
import okhttp3.Response;

public class Http {
    String url = "http://tools.bugscaner.com/api/tts/?range=";

    public void post(String text)
    {
        OkHttpClient okHttpClient = new OkHttpClient();
        RequestBody requestBody = new FormBody.Builder()
                .add("text", text)
                .add("yusu","3")
                .add("fasheng","4")
                .build();
        Request request = new Request.Builder()
                .url(url+String.valueOf(System.currentTimeMillis()))
                .post(requestBody)
                .build();

        okHttpClient.newCall(request).enqueue(new Callback() {
            @Override
            public void onFailure(Call call, IOException e) {
                Log.e("http", "onFailure: " + e.getMessage());
            }

            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String StringTemp = response.body().string();
                JSONObject jsonObjectTemp = JSONObject.parseObject(StringTemp);
                JSONArray jsonResultArrayTemp = null;
                String para = Objects.requireNonNull(jsonObjectTemp.get("video")).toString();
                MediaPlayer mp = new MediaPlayer();
                mp.setDataSource("http://tools.bugscaner.com/api/tts/?"+para);
                mp.prepare();
                mp.start();
            }
        });
    }


}
