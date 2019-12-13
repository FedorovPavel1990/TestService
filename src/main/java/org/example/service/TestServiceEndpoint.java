package org.example.service;

import org.example.service.logic.TestServiceLogic;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TestServiceEndpoint {

    private static final String NAMESPACE_URI = "http://example.org/TestService";

    @Autowired
    private TestServiceLogic testServiceLogic;

    @Autowired
    private DatabaseService databaseService;

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findNumberRequest")
    @ResponsePayload
    public FindNumberResponse findNumber(@RequestPayload FindNumberRequest request) {
        FindNumberResponse response = testServiceLogic.findNumber(request);
        databaseService.addResultInDB(request, response);
        return response;
    }
}
