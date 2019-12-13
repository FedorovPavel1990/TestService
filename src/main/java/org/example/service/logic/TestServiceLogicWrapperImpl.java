package org.example.service.logic;

import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;

public class TestServiceLogicWrapperImpl implements TestServiceLogicWrapper {

    private TestServiceLogicWrapper testServiceLogicWrapper;

    public TestServiceLogicWrapperImpl(TestServiceLogicWrapper testServiceLogicWrapper) {
        this.testServiceLogicWrapper = testServiceLogicWrapper;
    }

    @Override
    public FindNumberResponse findNumber(FindNumberRequest request) {
//        ((TestServiceLogic) testServiceLogicWrapper).setFolder();
        return testServiceLogicWrapper.findNumber(request);
    }
}
