package org.example.service.numberFinder;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;

@Service
public abstract class AbstractNumberFinder {

    @Autowired
    @Qualifier("RealFileWrapper")
    protected AbstractFileWrapper fileWrapper;

    @Value("${testservice.chunks_count}")
    protected int countChunks;

    public abstract boolean findNumberInFile(File file, int requestNumber) throws Exception;

    public void setCountChunks(int countChunks) {
        this.countChunks = countChunks;
    }

    public void setFileWrapper(AbstractFileWrapper fileWrapper) {
        this.fileWrapper = fileWrapper;
    }

}
