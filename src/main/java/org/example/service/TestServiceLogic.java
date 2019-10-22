package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.dao.ResultDAO;
import org.example.enums.ResultCodes;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.example.testservice.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import utils.TestFilesGenerator;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
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

    FindNumberResponse findNumber(FindNumberRequest request) throws InterruptedException {

        int requestNumber = request.getN();
        FindNumberResponse response = new FindNumberResponse();
        Result result = new Result();

        File folder = new File(TestFilesGenerator.getFolder());
//        File folder = new File("C:/2");
        List<File> files;

        if (folder.list() != null && Objects.requireNonNull(folder.list()).length > 0) {
            files = Arrays.asList(Objects.requireNonNull(folder.listFiles()));
        } else {
            LOG.error("Папка {} пуста", folder.getName());
            fillResponseNotSuccessResult(response, result, ResultCodes.FindNumber_02.getCode(), ResultCodes.FindNumber_02.getError());
            addResultInDB(request, response);
            return response;
        }

        List<String> filesWithN = new ArrayList<>();

        List<Callable<Boolean>> tasks = new ArrayList<>();
        ExecutorService executor = Executors.newFixedThreadPool(10);

        for (File file : files) {
            Callable<Boolean> task = () -> {
                LOG.debug("\n------------------------\nTASK FOR {} STARTED\n------------------------", file.getName());
                boolean isNumberFoundInFile;
                try {
                    isNumberFoundInFile = findNumberInFile(file, requestNumber);
                } catch (Exception e) {
                    LOG.error("Возникла техническая ошибка", e);
                    fillResponseNotSuccessResult(response, result, ResultCodes.FindNumber_02.getCode(), ResultCodes.FindNumber_02.getError());
                    LOG.debug("\n------------------------\nTASK FOR {} ENDED WITH FAIL\n------------------------\n {}", file.getName(), e.getMessage());
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

        List<Future<Boolean>> futures = executor.invokeAll(tasks);
        executor.shutdown();

        //task возвращает true, если отработал успешно и false если была ошибка при выполнении, здесь смотрим была ли ошибка по какому-нибудь task
        boolean isError = false;
        for (Future future : futures) {
            try {
                isError = !(boolean) future.get();
            } catch (InterruptedException | ExecutionException e) {
                LOG.error("Возникла техническая ошибка", e);
                fillResponseNotSuccessResult(response, result, ResultCodes.FindNumber_02.getCode(), ResultCodes.FindNumber_02.getError());
                addResultInDB(request, response);
                return response;
            }
        }

        if (isError) {
            fillResponseNotSuccessResult(response, result, ResultCodes.FindNumber_02.getCode(), ResultCodes.FindNumber_02.getError());
            addResultInDB(request, response);
            return response;
        }
        if (filesWithN.isEmpty()) {
            fillResponseNotSuccessResult(response, result, ResultCodes.FindNumber_01.getCode(), ResultCodes.FindNumber_01.getError());
            addResultInDB(request, response);
            return response;
        } else {
            fillResponseSuccessResult(response, result, ResultCodes.FindNumber_00.getCode(), filesWithN);
            addResultInDB(request, response);
            return response;
        }
    }

    private void addResultInDB(FindNumberRequest request, FindNumberResponse response) {
        resultDAO.saveResult(request, response.getResult());
    }

    private void fillResponseNotSuccessResult(FindNumberResponse response, Result result, String code, String error) {
        if (code != null && !code.isEmpty()) {
            result.setCode(code);
        }
        if (error != null && !error.isEmpty()) {
            result.setError(error);
        }
        response.setResult(result);
    }

    private void fillResponseSuccessResult(FindNumberResponse response, Result result, String code, List<String> fileNames) {
        if (code != null && !code.isEmpty()) {
            result.setCode(code);
        }
        if (fileNames != null && !fileNames.isEmpty()) {
            result.getFileNames().addAll(fileNames);
        }
        response.setResult(result);
    }

    private boolean findNumberInFile(File file, int requestNumber) throws IOException {
        long length = file.length();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MappedByteBuffer mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
            int i = 0;

            while (i < length) {
                StringBuilder builder = new StringBuilder();
                while (i < length) {
                    char ch = (char) mappedByteBuffer.get(i++);
                    if (ch == ',') {
                        break;
                    }
                    builder.append(ch);
                }
                if (requestNumber == Integer.parseInt(builder.toString())) {
                    return true;
                }
            }
        }
        return false;
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
}
