package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class AsyncService {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Value("${testservice.count_chunks}")
    private int countChunks;

    @Async
    public Future<Boolean> asyncFindNumberInFile(File file, int requestNumber, List<String> resultFileList) {
        LOG.debug("TASK FOR {} STARTED", file.getName());
        try {
            if (findNumberInFile(file, requestNumber)) {
                resultFileList.add(file.getName());
            }
        } catch (Exception e) {
            LOG.error("Сбой при чтении файла {}", file.getName(), e);
            return new AsyncResult<>(false);
        }
        LOG.debug("TASK FOR {} ENDED", file.getName());
        return new AsyncResult<>(true);
    }

    private boolean findNumberInFile(File file, int requestNumber) throws IOException {
        long length = file.length();
        long chunk = length / countChunks;
        long chunkPosition = 0;

        for (int i = 0; i < countChunks; i++) {

            chunk = chunkPosition + chunk >= length
                    ? length - chunkPosition
                    : chunk + getNextDelimiterPosition(file, chunkPosition + chunk, ',');

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                MappedByteBuffer mappedByteBuffer = fileInputStream
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);

                String string = StandardCharsets.UTF_8.decode(mappedByteBuffer).toString();
                List<String> ints = Arrays.asList(string.split(","));

                boolean isNumberFound = ints.parallelStream().anyMatch(n -> n.equals(String.valueOf(requestNumber)));
                if (isNumberFound) {
                    return true;
                }

            }

            chunkPosition += chunk;
        }

        return false;

//        long length = file.length();
//        long chunk = length / countChunks;
//        long chunkPosition = 0;
//
//        for (int i = 0; i < countChunks; i++) {
//
//            chunk = chunkPosition + chunk >= length
//                    ? length - chunkPosition
//                    : chunk + getNextDelimiterPosition(file, chunkPosition + chunk, ',');
//
//            try (RandomAccessFile fileInputStream = new RandomAccessFile(file, "r")) {
//                MappedByteBuffer mappedByteBuffer = fileInputStream
//                        .getChannel()
//                        .map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);
//                while (mappedByteBuffer.hasRemaining()) {
//                    if (requestNumber == getNextIntFromMBF(mappedByteBuffer, ',')) {
//                        return true;
//                    }
//                }
//            }
//
//            chunkPosition += chunk;
//        }
//
//        return false;
    }

    private int getNextIntFromMBF(MappedByteBuffer mbf, char delimiter) {
        StringBuilder builder = new StringBuilder();

        while (mbf.hasRemaining()) {
            char ch = (char) mbf.get();
            if (ch == delimiter) {
                break;
            }
            builder.append(ch);
        }

        return Integer.parseInt(builder.toString());
    }

    private static long getNextDelimiterPosition(File file, long position, char delimiter) throws IOException {
        long nextDelimiterPosition = 0;

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MappedByteBuffer mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, position, 12);
            while (mappedByteBuffer.hasRemaining()) {
                char ch = (char) mappedByteBuffer.get();
                if (ch == delimiter) {
                    nextDelimiterPosition = mappedByteBuffer.position();
                    break;
                }
            }
        }

        return nextDelimiterPosition;
    }

}
