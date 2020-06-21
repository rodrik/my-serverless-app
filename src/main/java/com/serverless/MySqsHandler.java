package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import com.amazonaws.services.lambda.runtime.events.SQSEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MySqsHandler implements RequestHandler<SQSEvent, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(MySqsHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(SQSEvent sqsEvent, Context context) {
        StringBuilder sb = new StringBuilder();
        for (SQSEvent.SQSMessage r : sqsEvent.getRecords()) {
            sb.append(r.getMessageId().toString());
            sb.append("\r\n");
            sb.append(r.getBody().toString());
            sb.append("\r\n");
        }

        LOG.info("SQSEvent: {}", sb.toString());

        return ApiGatewayResponse.builder()
                .setRawBody(sb.toString())
                .build();
    }

}
