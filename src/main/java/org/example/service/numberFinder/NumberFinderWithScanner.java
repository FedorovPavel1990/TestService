package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Scanner;

@Service("Scanner")
public class NumberFinderWithScanner implements NumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws IOException {
        byte[] byteArray = Files.readAllBytes(Paths.get(file.getPath()));
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
