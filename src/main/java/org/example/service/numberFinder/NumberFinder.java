package org.example.service.numberFinder;

import java.io.File;

public interface NumberFinder {

    boolean findNumberInFile(File file, int requestNumber) throws Exception;

}
