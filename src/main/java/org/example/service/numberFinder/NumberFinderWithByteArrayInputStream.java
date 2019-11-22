package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;

@Service("ByteArrayInputStream")
public class NumberFinderWithByteArrayInputStream implements NumberFinder {



    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws IOException {
        byte[] byteArray = Files.readAllBytes(Paths.get(file.getPath()));
        try (ByteArrayInputStream fileReader = new ByteArrayInputStream(byteArray, 0, byteArray.length)) {

            int i = 0;
            while (true) {
                int number;
                StringBuilder builder = new StringBuilder();
                if (i == -1) break;
                while (true) {
                    i = fileReader.read();
                    if (i == (int) ',' || i == -1) break;
                    builder.append((char) i);
                }

                number = Integer.parseInt(builder.toString());

                if (requestNumber == number) {
                    return true;
                }
            }
        }
        return false;
    }
}
