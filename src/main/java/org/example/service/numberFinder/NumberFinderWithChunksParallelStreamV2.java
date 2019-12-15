package org.example.service.numberFinder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.stream.Stream;

@Service("ChunksParallelStreamV2")
public class NumberFinderWithChunksParallelStreamV2 implements NumberFinder {

    @Value("${testservice.chunks_count}")
    private int countChunks;

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws IOException {
        boolean result = false;

        long length = file.length();
        long chunk = length / countChunks;
        long chunkPosition = 0;

        for (int i = 0; i < countChunks; i++) {

            chunk = chunkPosition + chunk >= length
                    ? length - chunkPosition
                    : chunk + NumberFinderUtil.getNextDelimiterPosition(file, chunkPosition + chunk, ',');

            try (RandomAccessFile randomAccessFile = new RandomAccessFile(file, "r")) {

                MappedByteBuffer mappedByteBuffer = randomAccessFile
                        .getChannel()
                        .map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);

                result = Stream.generate(() -> NumberFinderUtil.getNextIntFromMBFSync(mappedByteBuffer, ','))
                        .takeWhile(Objects::nonNull)
                        .parallel()
                        .anyMatch(n -> requestNumber == n);

                if (result) {
                    break;
                }
            }

            chunkPosition += chunk;
        }

        return result;
    }
}
