package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TestServiceEndpoint {

    private static final Logger LOG = LogManager.getLogger(TestServiceEndpoint.class);

    private static final String NAMESPACE_URI = "http://example.org/TestService";

    @Autowired
    private TestServiceLogic testServiceLogic;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findNumberRequest")
    @ResponsePayload
    public FindNumberResponse findNumber(@RequestPayload FindNumberRequest request) {
        FindNumberResponse response = new FindNumberResponse();

        try {
            response = testServiceLogic.findNumber(request);
        } catch (Exception e) {
            LOG.error("Возникла техническая ошибка", e);
        }

        return response;
    }
}
