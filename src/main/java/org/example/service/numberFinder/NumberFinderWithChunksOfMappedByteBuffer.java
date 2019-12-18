package org.example.service.numberFinder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Service("ChunksOfMappedByteBuffer")
public class NumberFinderWithChunksOfMappedByteBuffer implements NumberFinder {

    @Value("${testservice.chunks_count}")
    private int countChunks;

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws Exception {
        long length = file.length();
        long chunk = length / countChunks;
        long chunkPosition = 0;

        for (int i = 0; i < countChunks; i++) {

            chunk = chunkPosition + chunk >= length
                    ? length - chunkPosition
                    : chunk + NumberFinderUtil.getNextDelimiterPosition(file, chunkPosition + chunk, ',');

            try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
                MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);
                try {
                    while (mappedByteBuffer.hasRemaining()) {
                        if (requestNumber == NumberFinderUtil.getNextIntFromMBF(mappedByteBuffer, ',')) {
                            return true;
                        }
                    }
                } finally {
                    NumberFinderUtil.closeMappedByteBuffer(mappedByteBuffer);
                }
            }

            chunkPosition += chunk;
        }

        return false;
    }

    public void setCountChunks(int countChunks) {
        this.countChunks = countChunks;
    }
}
