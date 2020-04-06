package com.serverless;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

public class MyHandlerTest {

    @Test
    public void myTest() {
        MyIpHandler myHandler = new MyIpHandler();
        ApiGatewayResponse apiGatewayResponse = myHandler.handleRequest(null, null);

        Assertions.assertThat(apiGatewayResponse.getBody()).contains("Hello world from my Lambda using Java: ");
        Assertions.assertThat(apiGatewayResponse.getBody()).isNotEqualTo("Hello world from my Lambda using Java: ");

        System.out.println(apiGatewayResponse.getBody());
    }
}
