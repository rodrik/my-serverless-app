package com.serverless;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.GET;

public interface AwsIpService {

    @GET("/")
    Call<ResponseBody> getMyIp();

}
