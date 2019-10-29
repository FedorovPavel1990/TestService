package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.ResultDAO;
import org.example.enums.ResultCodes;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.example.testservice.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.*;

@Service
public class TestServiceLogic {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Autowired
    private ResultDAO resultDAO;

    @Value("${testservice.folder}")
    private String folder;

    @Value("${testservice.executors_count}")
    private String executorsCount;

    public FindNumberResponse findNumber(FindNumberRequest request) {

        int requestNumber = request.getN();

        File folder = new File(this.folder);
        List<File> files;

        if (folder.list() != null && Objects.requireNonNull(folder.list()).length > 0) {
            files = Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        } else {
            LOG.error("Папка {} пуста", folder.getName());
            return getResponse(getResultWithError());
        }

        List<String> filesWithN = new ArrayList<>();
        List<Callable<Boolean>> tasks = new ArrayList<>();
        List<Future<Boolean>> futures;

        int executorsCount = Integer.parseInt(this.executorsCount);
        ExecutorService executor = Executors.newFixedThreadPool(executorsCount);

        try {
            for (File file : files) {
                Callable<Boolean> task = () -> {
                    LOG.debug("\n------------------------\nTASK FOR {} STARTED\n------------------------", file.getName());
                    boolean isNumberFoundInFile;
                    try {
                        isNumberFoundInFile = findNumberInFile(file, requestNumber);
                    } catch (Exception e) {
                        LOG.error("Сбой при чтении файла {}", file.getName(), e);
                        return Boolean.FALSE;
                    }
                    if (isNumberFoundInFile) {
                        filesWithN.add(file.getName());
                    }
                    LOG.debug("\n------------------------\nTASK FOR {} ENDED\n------------------------", file.getName());
                    return Boolean.TRUE;
                };
                tasks.add(task);
            }

            futures = executor.invokeAll(tasks);
        } catch (Exception e) {
            LOG.error("Возникла техническая ошибка", e);
            return getResponse(getResultWithError());
        } finally {
            executor.shutdown();
        }

        //task возвращает true, если отработал успешно и false если была ошибка при выполнении, здесь смотрим была ли ошибка по какому-нибудь task
        boolean isError = false;
        for (Future future : futures) {
            try {
                isError = !(boolean) future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Возникла техническая ошибка", e);
                return getResponse(getResultWithError());
            }
        }

        if (isError) {
            return getResponse(getResultWithError());
        } else if (filesWithN.isEmpty()) {
            return getResponse(getResultWithNumberNotFound());
        } else {
            return getResponse(getResultWithOk(filesWithN));
        }
    }

    @Transactional
    public void addResultInDB(FindNumberRequest request, FindNumberResponse response) {
        resultDAO.saveResult(request, response.getResult());
    }

    private FindNumberResponse getResponse(Result result) {
        FindNumberResponse response = new FindNumberResponse();
        response.setResult(result);
        return response;
    }

    private Result getResultWithError() {
        Result result = new Result();
        result.setCode(ResultCodes.FindNumber_02.getCode());
        result.setError(ResultCodes.FindNumber_02.getError());
        return result;
    }

    private Result getResultWithNumberNotFound() {
        Result result = new Result();
        result.setCode(ResultCodes.FindNumber_01.getCode());
        result.setError(ResultCodes.FindNumber_01.getError());
        return result;
    }

    private Result getResultWithOk(List<String> fileNames) {
        Result result = new Result();
        result.setCode(ResultCodes.FindNumber_00.getCode());
        result.getFileNames().addAll(fileNames);
        return result;
    }

    private boolean findNumberInFile(File file, int requestNumber) throws IOException {
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
