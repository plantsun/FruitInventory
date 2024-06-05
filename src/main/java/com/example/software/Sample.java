package com.example.software;

import okhttp3.*;
import okhttp3.Response;
import org.json.JSONObject;

import java.io.*;

//class Sample {
//
//    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();
//
//    public static void main(String []args) throws IOException{
//        MediaType mediaType = MediaType.parse("application/json");
//        RequestBody body = RequestBody.create(mediaType, "");
//        Request request = new Request.Builder()
//                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=mTOhSUF4jeomfSyrG3e28lXo&client_secret=Q3Rc1bqobfxao3issYD1FFkApidQBIUs&grant_type=client_credentials")
//                .method("POST", body)
//                .addHeader("Content-Type", "application/json")
//                .addHeader("Accept", "application/json")
//                .build();
//        Response response = HTTP_CLIENT.newCall(request).execute();
//        System.out.println(response.body().string());
//
//    }
//}

class Sample {

    static final OkHttpClient HTTP_CLIENT = new OkHttpClient().newBuilder().build();

    public static void main(String []args) throws IOException{
        MediaType mediaType = MediaType.parse("application/json");
        RequestBody body = RequestBody.create(mediaType, "");
        Request request = new Request.Builder()
                .url("https://aip.baidubce.com/oauth/2.0/token?client_id=6CsF3zXWm7YHe6sFPPYbbGel&client_secret=M6UXperfH3P5e7bep1kt67yi82OODY8q&grant_type=client_credentials")
                .method("POST", body)
                .addHeader("Content-Type", "application/json")
                .addHeader("Accept", "application/json")
                .build();
        Response response = HTTP_CLIENT.newCall(request).execute();
        System.out.println(response.body().string());

    }
}