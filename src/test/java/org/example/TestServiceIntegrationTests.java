package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.ResultCodes;
import org.example.service.TestServiceLogic;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.example.testservice.Result;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Random;

import static org.hamcrest.Matchers.is;

@SpringBootTest
public class TestServiceIntegrationTests {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);
//----------------------------------------------------------------------------------
    @Autowired
    private TestServiceLogic logic;

    private static File TEST_FOLDER;

    @BeforeAll
    static void createTempFolder() throws IOException {
        TEST_FOLDER = Files.createTempDirectory("tmpTestFiles").toFile();
        TEST_FOLDER.deleteOnExit();

        for (int i = 0; i < 5; i++) {
            File file = Files.createFile(Paths.get(TEST_FOLDER.getAbsolutePath() + "/testFile" + (i + 1))).toFile();
            file.deleteOnExit();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int j = 0; j < 100; j++) {
                    writer.write((new Random().nextInt(99999) - j * 1000) + ",");
                }
                writer.write(String.valueOf(100001 + i));
            }
        }
    }

    @AfterAll
    static void deleteTempFolder() throws IOException {
        if (TEST_FOLDER != null) {
            File[] filesInTempFolder = TEST_FOLDER.listFiles();
            assert filesInTempFolder != null;
            for (File file : filesInTempFolder) {
                Files.delete(file.toPath());
            }
            Files.delete(TEST_FOLDER.toPath());
        }
    }


    @Test
    public void TestServiceLogic_OK() {
        logic.setFolder(TEST_FOLDER.getAbsolutePath());
        FindNumberRequest request = new FindNumberRequest();
        request.setN(100005);
        FindNumberResponse response = logic.findNumber(request);
        Result result = response.getResult();

        String expectedCode = ResultCodes.FindNumber_00.getCode();
        List<String> expectedFileNamesList = Collections.singletonList("testFile5");

        Assert.assertEquals(result.getCode(), expectedCode);
        Assert.assertThat(result.getFileNames(), is(expectedFileNamesList));
    }

    @Test
    public void TestServiceLogic_NotFound() {
        logic.setFolder(TEST_FOLDER.getAbsolutePath());
        FindNumberRequest request = new FindNumberRequest();
        request.setN(100006);
        FindNumberResponse response = logic.findNumber(request);
        Result result = response.getResult();

        String expectedCode = ResultCodes.FindNumber_01.getCode();
        String expectedError = ResultCodes.FindNumber_01.getError();

        Assert.assertEquals(result.getCode(), expectedCode);
        Assert.assertEquals(result.getError(), expectedError);
    }
//----------------------------------------------------------------------------------
//    @Autowired
//    private AsyncService asyncService;
//
//    @Test
//    public void test_TestAsyncService() throws Exception {
//        String str = "123,1231,23,124,124,12,41,24,12,4,12,41,24,124";
//
//
//
//
//        NumberFinder numberFinder = mock(NumberFinder.class);
//        when(numberFinder.findNumberInFile(new File("Test"), 1)).thenReturn(true);
//        boolean actual = asyncService.asyncFindNumberInFile(new File("Test"),1, new ArrayList<>()).get();
//        Assert.assertTrue("AsyncService - FAILED", actual);
//    }
}
