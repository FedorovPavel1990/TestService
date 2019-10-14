package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.ResultDAO;
import org.example.dao.ResultDAOImpl;
import org.example.enums.ResultCodes;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.springframework.ws.server.endpoint.annotation.Endpoint;
import org.springframework.ws.server.endpoint.annotation.PayloadRoot;
import org.springframework.ws.server.endpoint.annotation.RequestPayload;
import org.springframework.ws.server.endpoint.annotation.ResponsePayload;

@Endpoint
public class TestServiceEndpoint {
    private static final Logger LOG = LogManager.getLogger(TestServiceEndpoint.class);

    private static final String NAMESPACE_URI = "http://example.org/TestService";

    @PayloadRoot(namespace = NAMESPACE_URI, localPart = "findNumberRequest")
    @ResponsePayload
    public FindNumberResponse findNumber(@RequestPayload FindNumberRequest request) {
        FindNumberResponse response = new FindNumberResponse();

        try {
            response = TestServiceLogic.findNumber(request);
        } catch (Exception e) {
            LOG.error("Возникла техническая ошибка", e);
            TestServiceLogic.getNotSuccessResponse(response, response.getResult(), ResultCodes.FindNumber_02.getCode(), ResultCodes.FindNumber_02.getError());
        }
        try {
            ResultDAO resultDAO = new ResultDAOImpl();
            resultDAO.saveResult(request, response.getResult());
        } catch (Exception e) {
            LOG.error("Ошибка записи в БД", e);
        }
        return response;
    }
}
