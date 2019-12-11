package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.ResultCodes;
import org.example.service.TestServiceLogic;
import org.example.service.numberFinder.NumberFinderWithByteArrayInputStream;
import org.example.service.numberFinder.NumberFinderWithChunksOfMappedByteBuffer;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.example.testservice.Result;
import org.junit.Assert;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.*;

import static org.hamcrest.CoreMatchers.is;

@SpringBootTest
public class TestServiceTests {

    private static final Logger LOG = LogManager.getLogger(TestServiceTests.class);

    @Autowired
    private TestServiceLogic logic;

    private File testFolder;

    @BeforeEach
    public void createTempFolder() throws IOException {
        testFolder = Files.createTempDirectory("tmpTestFiles").toFile();
        testFolder.deleteOnExit();

        for (int i = 0; i < 5; i++) {
            File file = Files.createFile(Paths.get(testFolder.getAbsolutePath() + "/testFile" + (i + 1))).toFile();
            file.deleteOnExit();
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
                for (int j = 0; j < 100; j++) {
                    writer.write((new Random().nextInt(99999) - j * 1000) + ",");
                }
                writer.write(String.valueOf(100001 + i));
            }
        }

        logic.setFolder(testFolder.getAbsolutePath());
    }

    @AfterEach
    public void deleteTempFolder() throws IOException {
        if (testFolder != null) {
            File[] filesInTempFolder = testFolder.listFiles();
            for (File file : filesInTempFolder) {
                Files.delete(file.toPath());
            }
            Files.delete(testFolder.toPath());
        }
    }

    @Test
    public void test_TestServiceLogic_OK() {
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
    public void test_NumberFinderWithByteArrayInputStream() throws IOException {
        NumberFinderWithByteArrayInputStream numberFinder = new NumberFinderWithByteArrayInputStream();
        File file = new File("C:/2/testFile5");
        Assert.assertTrue("NumberFinderWithByteArrayInputStream - FAILED", numberFinder.findNumberInFile(file, 611197352));
    }

    @Test
    public void test_NumberFinderWithChunksOfMappedByteBuffer() throws IOException {
        NumberFinderWithChunksOfMappedByteBuffer numberFinder = new NumberFinderWithChunksOfMappedByteBuffer();
    }

}
