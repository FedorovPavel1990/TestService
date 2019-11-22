package org.example.service.numberFinder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.charset.StandardCharsets;
import java.util.Arrays;
import java.util.List;

@Service("ChunksParallelStream")
public class NumberFinderWithChunksParallelStream implements NumberFinder {

    @Value("${testservice.chunks_count}")
    private int countChunks;

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws IOException {
        long length = file.length();
        long chunk = length / countChunks;
        long chunkPosition = 0;

        for (int i = 0; i < countChunks; i++) {

            chunk = chunkPosition + chunk >= length
                    ? length - chunkPosition
                    : chunk + NumberFinderUtil.getNextDelimiterPosition(file, chunkPosition + chunk, ',');

            try (FileInputStream fileInputStream = new FileInputStream(file)) {
                MappedByteBuffer mappedByteBuffer = fileInputStream
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);



                String string = StandardCharsets.UTF_8.decode(mappedByteBuffer).toString();
                List<String> ints = Arrays.asList(string.split(","));

                boolean isNumberFound = ints.parallelStream().anyMatch(n -> n.equals(String.valueOf(requestNumber)));
                if (isNumberFound) {
                    return true;
                }

            }

            chunkPosition += chunk;
        }

        return false;
    }
}
