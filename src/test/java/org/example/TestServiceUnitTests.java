package org.example;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.TestServiceLogic;
import org.example.service.numberFinder.*;
import org.junit.Assert;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.RandomAccessFile;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.util.Objects;

@SpringBootTest
public class TestServiceUnitTests {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Value("${testservice.chunks_count}")
    private int countChunks;

    @Autowired
    @Qualifier("MockFileWrapper")
    AbstractFileWrapper fileWrapper;

    @Value("${testservice.folder}")
    private String folder;

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

    private void assertTest_OK(NumberFinder numberFinder) throws Exception {
        numberFinder.setFileWrapper(fileWrapper);
        File folder = new File(this.folder);
        File file = Objects.requireNonNull(folder.listFiles())[0];
        boolean actual = numberFinder.findNumberInFile(file, 516854);
        Assert.assertTrue(numberFinder.getClass().getSimpleName() + "_OK - FAILED", actual);
        LOG.info(numberFinder.getClass().getSimpleName() + "_OK - OK");
    }

    private void assertTest_NotFound(NumberFinder numberFinder) throws Exception {
        numberFinder.setFileWrapper(fileWrapper);
        File folder = new File(this.folder);
        File file = Objects.requireNonNull(folder.listFiles())[0];
        boolean actual = numberFinder.findNumberInFile(file, 123);
        Assert.assertFalse(numberFinder.getClass().getSimpleName() + "_NotFound - FAILED", actual);
        LOG.info(numberFinder.getClass().getSimpleName() + "_NotFound - OK");
    }

    @Test
    @Disabled
    public void tmpTest() throws Exception {
        String mockFile = "1243124124124124";
        ByteBuffer byteBuffer = ByteBuffer.wrap(mockFile.getBytes());
        for (int i = 0; i < 16; i++) {
            System.out.print(byteBuffer.get());
        }

        System.out.println();

        File file = Files.createTempFile("tmpTestFile", ".tmp").toFile();
        file.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            writer.write(mockFile);
        }
        try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, file.length());
            for (int i = 0; i < 16; i++) {
                System.out.print(mappedByteBuffer.get());
            }
            NumberFinderUtil.closeMappedByteBuffer(mappedByteBuffer);
        }
    }
}
