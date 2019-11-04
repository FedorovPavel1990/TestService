package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class AsyncService {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Async
    public Future<Boolean> asyncFindNumberInFile(File file, int requestNumber, List<String> resultFileList) {
        LOG.debug("TASK FOR {} STARTED", file.getName());
        try {
            if (asyncFindNumberInFile(file, requestNumber)) {
                resultFileList.add(file.getName());
            }
        } catch (Exception e) {
            LOG.error("Сбой при чтении файла {}", file.getName(), e);
            return new AsyncResult<>(false);
        }
        LOG.debug("TASK FOR {} ENDED", file.getName());
        return new AsyncResult<>(true);
    }

    private boolean asyncFindNumberInFile(File file, int requestNumber) throws IOException {
        long length = file.length();
        try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {
            MappedByteBuffer mappedByteBuffer = randomAccessFile.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);

            while (mappedByteBuffer.hasRemaining()) {
                if (requestNumber == getNextIntFromMBF(mappedByteBuffer, ',')) {
                    return true;
                }
            }
        }
        return false;
//---------------------------------------------------------------------------------------------------
//        long length = file.length();
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            MappedByteBuffer mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
//
//            while (mappedByteBuffer.hasRemaining()) {
//                if (requestNumber == getNextIntFromMBF(mappedByteBuffer, ',')) {
//                    return true;
//                }
//            }
//        }
//        return false;
//---------------------------------------------------------------------------------------------------
//        byte[] byteArray = Files.readAllBytes(Paths.get(file.getPath()));
//        try (Scanner scanner = new Scanner(new ByteArrayInputStream(byteArray, 0, byteArray.length))) {
//            scanner.useDelimiter(",");
//            while (scanner.hasNextInt()) {
//                if (requestNumber == scanner.nextInt()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//---------------------------------------------------------------------------------------------------
//        byte[] byteArray = Files.readAllBytes(Paths.get(file.getPath()));
//        try (ByteArrayInputStream fileReader = new ByteArrayInputStream(byteArray, 0, byteArray.length)) {
//
//            int i = 0;
//            while (true) {
//                int number;
//                StringBuilder builder = new StringBuilder();
//                if (i == -1) break;
//                while (true) {
//                    i = fileReader.read();
//                    if (i == (int) ',' || i == -1) break;
//                    builder.append((char) i);
//                }
//
//                number = Integer.parseInt(builder.toString());
//
//                if (requestNumber == number) {
//                    return true;
//                }
//            }
//        }
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
}
