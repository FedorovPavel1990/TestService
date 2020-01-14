package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Objects;
import java.util.stream.Stream;

@Service("ChunksParallelStreamV2")
public class NumberFinderWithChunksParallelStreamV2 extends AbstractNumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws Exception {
        boolean result = false;

        long length = file.length();
        long chunk = length / countChunks;
        long chunkPosition = 0;

        for (int i = 0; i < countChunks; i++) {

            chunk = chunkPosition + chunk >= length
                    ? length - chunkPosition
                    : chunk + NumberFinderUtil.getNextDelimiterPosition(fileWrapper, file, chunkPosition + chunk, ',');

            try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
                MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);
                try {
                    result = Stream.generate(() -> NumberFinderUtil.getNextIntFromMBFSync(fileWrapper, mappedByteBuffer, ','))
                            .takeWhile(Objects::nonNull)
                            .parallel()
                            .anyMatch(n -> requestNumber == n);

                    if (result) {
                        break;
                    }
                } finally {
                    NumberFinderUtil.closeMappedByteBuffer(mappedByteBuffer);
                }
            }

            chunkPosition += chunk;
        }

        return result;
    }

}
