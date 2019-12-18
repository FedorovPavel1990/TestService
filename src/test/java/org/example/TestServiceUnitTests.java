package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.TestServiceLogic;
import org.example.service.numberFinder.*;
import org.junit.Assert;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;

@SpringBootTest
public class TestServiceUnitTests {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Value("${testservice.chunks_count}")
    private int countChunks;

    private static String TEMP_FILEPATH;

    @BeforeAll
    static void createTempFile() throws IOException {
        File file = Files.createTempFile("tmpTestFile", ".tmp").toFile();
        file.deleteOnExit();
        TEMP_FILEPATH = file.getAbsolutePath();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            for (int i = 0; i < 100; i++) {
                writer.write((new Random().nextInt(99999) - i * 1000) + ",");
            }
            writer.write(String.valueOf(100001));
        }
    }

    @AfterAll
    static void deleteTempFile() throws IOException {
        Files.delete(Path.of(TEMP_FILEPATH));
    }

    @Test
    public void NumberFinderWithByteArrayInputStream_OK() throws Exception {
        assertTest_OK(new NumberFinderWithByteArrayInputStream());
    }

    @Test
    public void NumberFinderWithByteArrayInputStream_NotFound() throws Exception {
        assertTest_NotFound(new NumberFinderWithByteArrayInputStream());
    }

    @Test
    public void NumberFinderWithChunksOfMappedByteBuffer_OK() throws Exception {
        NumberFinderWithChunksOfMappedByteBuffer numberFinder = new NumberFinderWithChunksOfMappedByteBuffer();
        numberFinder.setCountChunks(countChunks);
        assertTest_OK(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksOfMappedByteBuffer_NotFound() throws Exception {
        NumberFinderWithChunksOfMappedByteBuffer numberFinder = new NumberFinderWithChunksOfMappedByteBuffer();
        numberFinder.setCountChunks(countChunks);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStream_OK() throws Exception {
        NumberFinderWithChunksParallelStream numberFinder = new NumberFinderWithChunksParallelStream();
        numberFinder.setCountChunks(countChunks);
        assertTest_OK(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStream_NotFound() throws Exception {
        NumberFinderWithChunksParallelStream numberFinder = new NumberFinderWithChunksParallelStream();
        numberFinder.setCountChunks(countChunks);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStreamV2_OK() throws Exception {
        NumberFinderWithChunksParallelStreamV2 numberFinder = new NumberFinderWithChunksParallelStreamV2();
        numberFinder.setCountChunks(countChunks);
        assertTest_OK(numberFinder);
    }

    @Test
    public void NumberFinderWithChunksParallelStreamV2_NotFound() throws Exception {
        NumberFinderWithChunksParallelStreamV2 numberFinder = new NumberFinderWithChunksParallelStreamV2();
        numberFinder.setCountChunks(countChunks);
        assertTest_NotFound(numberFinder);
    }

    @Test
    public void NumberFinderWithMappedByteBuffer_OK() throws Exception {
        assertTest_OK(new NumberFinderWithMappedByteBuffer());
    }

    @Test
    public void NumberFinderWithMappedByteBuffer_NotFound() throws Exception {
        assertTest_NotFound(new NumberFinderWithMappedByteBuffer());
    }

    @Test
    public void NumberFinderWithScanner_OK() throws Exception {
        assertTest_OK(new NumberFinderWithScanner());
    }

    @Test
    public void NumberFinderWithScanner_NotFound() throws Exception {
        assertTest_NotFound(new NumberFinderWithScanner());
    }

    private static void assertTest_OK(NumberFinder numberFinder) throws Exception {
        File file = new File(TEMP_FILEPATH);
        boolean actual = numberFinder.findNumberInFile(file, 100001);
        Assert.assertTrue(numberFinder.getClass().getSimpleName() + "OK - FAILED", actual);
        LOG.info(numberFinder.getClass().getSimpleName() + "OK - OK");
    }

    private static void assertTest_NotFound(NumberFinder numberFinder) throws Exception {
        File file = new File(TEMP_FILEPATH);
        boolean actual = numberFinder.findNumberInFile(file, 100002);
        Assert.assertFalse(numberFinder.getClass().getSimpleName() + "_NotFound - FAILED", actual);
        LOG.info(numberFinder.getClass().getSimpleName() + "_NotFound - OK");
    }

}
