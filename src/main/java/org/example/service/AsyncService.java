package org.example.service;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.example.service.numberFinder.AbstractNumberFinder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.AsyncResult;
import org.springframework.stereotype.Service;

import java.io.File;
import java.util.List;
import java.util.concurrent.Future;

@Service
public class AsyncService {

    private static final Logger LOG = LogManager.getLogger(TestServiceLogic.class);

    @Autowired
    @Qualifier("ChunksOfMappedByteBuffer")
    private AbstractNumberFinder numberFinder;

    @Async
    public Future<Boolean> asyncFindNumberInFile(File file, int requestNumber, List<String> resultFileList) {
        LOG.debug("TASK FOR {} STARTED WITH {}", file.getName(), numberFinder.getClass().getSimpleName());
        try {
            if (numberFinder.findNumberInFile(file, requestNumber)) {
                resultFileList.add(file.getName());
            }
        } catch (Exception e) {
            LOG.error("Сбой при чтении файла {}", file.getName(), e);
            return new AsyncResult<>(false);
        }
        LOG.debug("TASK FOR {} ENDED", file.getName());
        return new AsyncResult<>(true);
    }

    public AbstractNumberFinder getNumberFinder() {
        return this.numberFinder;
    }

}
