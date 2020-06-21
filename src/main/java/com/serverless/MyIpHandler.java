package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import okhttp3.ResponseBody;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.jackson.JacksonConverterFactory;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.sqs.SqsClient;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlRequest;
import software.amazon.awssdk.services.sqs.model.GetQueueUrlResponse;
import software.amazon.awssdk.services.sqs.model.SendMessageRequest;

import java.util.Collections;
import java.util.Map;

public class MyIpHandler implements RequestHandler<Map<String, Object>, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(MyIpHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(Map<String, Object> input, Context context) {

        try {
            Retrofit retrofit = new Retrofit.Builder()
                    .baseUrl("http://checkip.amazonaws.com")
                    .addConverterFactory(JacksonConverterFactory.create())
                    .build();

            AwsIpService service = retrofit.create(AwsIpService.class);
            Response<ResponseBody> execute = service.getMyIp().execute();

            String myIp = execute.body().string();

            sendToQueue(myIp);

            // send the response back
            return ApiGatewayResponse.builder()
                    .setStatusCode(200)
                    .setRawBody("Hello world from my Lambda using Java: " + myIp)
                    .setHeaders(Collections.singletonMap("X-Powered-By", "AWS Lambda & Serverless"))
                    .build();

        } catch (Exception ex) {
            return ApiGatewayResponse.builder()
                    .setStatusCode(500)
                    .setObjectBody(ex)
                    .build();
        }

    }

    final SqsClient sqs = SqsClient.builder()
            .region(Region.US_EAST_1)
            .build();

    private void sendToQueue(String myIp) {
        String queueName = "serverless-queue";
        GetQueueUrlResponse getQueueUrlResponse =
                sqs.getQueueUrl(GetQueueUrlRequest.builder().queueName(queueName).build());
        String queueUrl = getQueueUrlResponse.queueUrl();
        LOG.info("Queue name: " + queueName);
        LOG.info("Queue url: " + queueUrl);
        SendMessageRequest send_msg_request = SendMessageRequest.builder()
                .queueUrl(queueUrl)
                .messageBody("This is the ip: " + myIp)
                .build();
        sqs.sendMessage(send_msg_request);
    }

}