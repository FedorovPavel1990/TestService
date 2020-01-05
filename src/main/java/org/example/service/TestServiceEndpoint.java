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
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    private static final String NAMESPACE_URI = "http://example.org/TestService";

    @Autowired
    private TestServiceLogic testServiceLogic;

    @Autowired
    private DatabaseService databaseService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findNumberRequest")
    @ResponsePayload
    public FindNumberResponse findNumber(@RequestPayload FindNumberRequest request) {

        LOG.info("\nTestService request:\nn = {}", request.getN());

        FindNumberResponse response = testServiceLogic.findNumber(request);

        LOG.info("\nTestService response:\ncode = {}\nfileNames = {}\nerrorCode = {}",
                response.getResult().getCode(),
                response.getResult().getFileNames(),
                response.getResult().getError());

//        databaseService.addResultInDB(request, response);
        return response;

    }
}
