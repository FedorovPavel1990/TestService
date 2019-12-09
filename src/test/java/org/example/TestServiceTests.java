package org.example;

import org.example.enums.ResultCodes;
import org.example.service.TestServiceEndpoint;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.example.testservice.Result;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Collections;
import java.util.List;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
public class TestServiceTests {

    @Autowired
    TestServiceEndpoint endpoint;

    @Test
    public void test() {
        FindNumberRequest request = new FindNumberRequest();
        request.setN(611197352);
        FindNumberResponse response = endpoint.findNumber(request);
        Result result = response.getResult();

        String expectedCode = ResultCodes.FindNumber_00.getCode();
        List<String> expectedFileNamesList = Collections.singletonList("testFile5");

        Assert.assertEquals(result.getCode(), expectedCode);
        Assert.assertThat(result.getFileNames(), is(expectedFileNamesList));
    }

}
