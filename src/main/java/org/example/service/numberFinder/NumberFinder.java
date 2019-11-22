package org.example.service.numberFinder;

import java.io.File;
import java.io.IOException;

public interface NumberFinder {

    boolean findNumberInFile(File file, int requestNumber) throws IOException;

}
