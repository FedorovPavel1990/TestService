package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.ResultCodes;
import org.example.service.AsyncService;
import org.example.service.DatabaseService;
import org.example.service.TestServiceEndpoint;
import org.example.service.TestServiceLogic;
import org.example.service.numberFinder.AbstractFileWrapper;
import org.example.service.numberFinder.AbstractNumberFinder;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.example.testservice.Result;
import org.junit.Assert;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import static org.hamcrest.Matchers.is;

@SpringBootTest
@Disabled
public class TestServiceIntegrationTests {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Autowired
    @InjectMocks
    private TestServiceEndpoint endpoint;

    @Mock
    private DatabaseService databaseService;

    @Autowired
    @Qualifier("MockFileWrapper")
    private AbstractFileWrapper fileWrapper;

    @Value("${testservice.folder}")
    private String folder;

    @Autowired
    private AsyncService asyncService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
        Mockito.doNothing().when(databaseService);

        AbstractNumberFinder numberFinder = asyncService.getNumberFinder();
        numberFinder.setFileWrapper(fileWrapper);
    }

    @Test
    public void TestServiceEndpoint_OK() {
        FindNumberRequest request = new FindNumberRequest();
        request.setN(516854);
        FindNumberResponse response = endpoint.findNumber(request);
        Result result = response.getResult();
        File folder = new File(this.folder);
        File[] fileList = folder.listFiles();
        List<String> fileNameList = new ArrayList<>();
        assert fileList != null;
        for (File file : fileList) {
            fileNameList.add(file.getName());
        }

        Collections.sort(result.getFileNames());
        Collections.sort(fileNameList);

        String expectedCode = ResultCodes.FindNumber_00.getCode();

        Assert.assertEquals("TestServiceEndpoint_OK - FAILED", expectedCode, result.getCode());
        Assert.assertThat("TestServiceEndpoint_OK - FAILED", result.getFileNames(), is(fileNameList));
        LOG.info("TestServiceEndpoint_OK - OK");
    }

    @Test
    public void TestServiceEndpoint_NotFound() {
        FindNumberRequest request = new FindNumberRequest();
        request.setN(123);
        FindNumberResponse response = endpoint.findNumber(request);
        Result result = response.getResult();

        String expectedCode = ResultCodes.FindNumber_01.getCode();
        String expectedError = ResultCodes.FindNumber_01.getError();

        Assert.assertEquals("TestServiceEndpoint_NotFound - FAILED", expectedCode, result.getCode());
        Assert.assertEquals("TestServiceEndpoint_NotFound - FAILED", expectedError, result.getError());
        LOG.info("TestServiceEndpoint_NotFound - OK");
    }
}
