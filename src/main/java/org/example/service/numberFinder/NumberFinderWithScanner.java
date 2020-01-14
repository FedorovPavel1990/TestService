package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.util.Scanner;

@Service("Scanner")
public class NumberFinderWithScanner extends AbstractNumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws IOException {
        byte[] byteArray = fileWrapper.readAllBytes(file);
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(byteArray, 0, byteArray.length))) {
            scanner.useDelimiter(",");
            while (scanner.hasNextInt()) {
                if (requestNumber == scanner.nextInt()) {
                    return true;
                }
            }
        }
        return false;
    }

}
