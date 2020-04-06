package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import okhttp3.ResponseBody;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.util.Collections;
import java.util.Map;

public class MyIpHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://checkip.amazonaws.com")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            AwsIpService service = retrofit.create(AwsIpService.class);
            Response<ResponseBody> execute = service.getMyIp().execute();

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setRawBody("Hello world from my Lambda using Java: " + execute.body().string())
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(ex)
                    .build();
        }

    }

}