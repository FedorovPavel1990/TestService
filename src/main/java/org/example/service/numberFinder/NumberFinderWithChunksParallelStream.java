package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.util.Arrays;
import java.util.List;

@Service("ChunksParallelStream")
public class NumberFinderWithChunksParallelStream extends AbstractNumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws Exception {
        long length = fileWrapper.length(file);
        long chunk = length / countChunks;
        long chunkPosition = 0;

        for (int i = 0; i < countChunks; i++) {

            chunk = chunkPosition + chunk >= length
                    ? length - chunkPosition
                    : chunk + NumberFinderUtil.getNextDelimiterPosition(fileWrapper, file, chunkPosition + chunk, ',');

            try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
                MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);
                try {
                    String string = fileWrapper.getStringFromMappedByteBuffer(mappedByteBuffer);
                    List<String> ints = Arrays.asList(string.split(","));

                    boolean isNumberFound = ints.parallelStream().anyMatch(n -> n.equals(String.valueOf(requestNumber)));
                    if (isNumberFound) {
                        return true;
                    }
                } finally {
                    NumberFinderUtil.closeMappedByteBuffer(mappedByteBuffer);
                }
            }

            chunkPosition += chunk;
        }

        return false;
    }

}
