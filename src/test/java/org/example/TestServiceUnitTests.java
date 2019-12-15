package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.TestServiceLogic;
import org.example.service.numberFinder.*;
import org.junit.Assert;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.File;
import java.io.IOException;

@SpringBootTest(webEnvironment = SpringBootTest.WebEnvironment.RANDOM_PORT)
public class TestServiceUnitTests {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Test
    public void test_NumberFinderWithByteArrayInputStream() throws IOException {
        NumberFinderWithByteArrayInputStream numberFinder = new NumberFinderWithByteArrayInputStream();
        assertTest_OK(numberFinder);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void test_NumberFinderWithChunksOfMappedByteBuffer() throws IOException {
        NumberFinderWithChunksOfMappedByteBuffer numberFinder = new NumberFinderWithChunksOfMappedByteBuffer();
        assertTest_OK(numberFinder);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void test_NumberFinderWithChunksParallelStream() throws IOException {
        NumberFinderWithChunksParallelStream numberFinder = new NumberFinderWithChunksParallelStream();
        assertTest_OK(numberFinder);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void test_NumberFinderWithChunksParallelStreamV2() throws IOException {
        NumberFinderWithChunksParallelStreamV2 numberFinder = new NumberFinderWithChunksParallelStreamV2();
        assertTest_OK(numberFinder);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void test_NumberFinderWithMappedByteBuffer() throws IOException {
        NumberFinderWithMappedByteBuffer numberFinder = new NumberFinderWithMappedByteBuffer();
        assertTest_OK(numberFinder);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void test_NumberFinderWithScanner() throws IOException {
        NumberFinderWithScanner numberFinder = new NumberFinderWithScanner();
        assertTest_OK(numberFinder);
        assertTest_NotFound(numberFinder);
    }

    private void assertTest_OK(NumberFinder numberFinder) throws IOException {
        File file = new File("C:/2/testFile5");
        boolean actual = numberFinder.findNumberInFile(file, 611197352);
        Assert.assertTrue("test_" + numberFinder.getClass().getSimpleName() + "_withOkResult - FAILED", actual);
        LOG.info("test_" + numberFinder.getClass().getSimpleName() + "_withOkResult - OK");
    }

    private void assertTest_NotFound(NumberFinder numberFinder) throws IOException {
        File file = new File("C:/2/testFile5");
        boolean actual = numberFinder.findNumberInFile(file, 611197351);
        Assert.assertFalse("test_" + numberFinder.getClass().getSimpleName() + "_withNotFoundResult - FAILED", actual);
        LOG.info("test_" + numberFinder.getClass().getSimpleName() + "_withNotFoundResult - OK");
    }

}
