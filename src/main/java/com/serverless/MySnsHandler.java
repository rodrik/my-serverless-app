package com.serverless;

import com.amazonaws.services.lambda.runtime.Context;
import com.amazonaws.services.lambda.runtime.RequestHandler;
import com.amazonaws.services.lambda.runtime.events.SNSEvent;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

public class MySnsHandler implements RequestHandler<SNSEvent, ApiGatewayResponse> {

    private static final Logger LOG = LogManager.getLogger(MySnsHandler.class);

    @Override
    public ApiGatewayResponse handleRequest(SNSEvent snsEvent, Context context) {
        StringBuilder sb = new StringBuilder();
        for (SNSEvent.SNSRecord r : snsEvent.getRecords()) {
            sb.append(r.getSNS().toString());
            sb.append("\r\n");
        }

        LOG.info("SNSEvent: {}", sb.toString());

        return ApiGatewayResponse.builder()
                .setRawBody(sb.toString())
                .build();
    }

}
