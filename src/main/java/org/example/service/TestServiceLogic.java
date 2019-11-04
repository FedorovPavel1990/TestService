package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.enums.ResultCodes;
import org.example.testservice.FindNumberRequest;
import org.example.testservice.FindNumberResponse;
import org.example.testservice.Result;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Objects;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.Future;

@Service
public class TestServiceLogic {
    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Autowired
    private AsyncService asyncService;

    @Value("${testservice.folder}")
    private String folder;

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

        List<String> resultFileList = new ArrayList<>();

        List<Future<Boolean>> futures = new ArrayList<>();
        for (File file : files) {
            futures.add(asyncService.asyncFindNumberInFile(file, requestNumber, resultFileList));
        }

//        asyncFindNumberInFile возвращает true, если отработал успешно и false если была ошибка при выполнении, здесь смотрим была ли ошибка по какому-нибудь файлу
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
        } else if (resultFileList.isEmpty()) {
            return getResponse(getResultWithNumberNotFound());
        } else {
            return getResponse(getResultWithOk(resultFileList));
        }
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
}
