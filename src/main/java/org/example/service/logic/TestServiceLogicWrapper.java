package org.example.service.logic;

import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;

public interface TestServiceLogicWrapper {

    FindNumberResponse findNumber(FindNumberRequest request);

}
