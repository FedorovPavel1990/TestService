
package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Service("ChunksOfMappedByteBuffer")
public class NumberFinderWithChunksOfMappedByteBuffer extends AbstractNumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws Exception {
        long length = fileWrapper.length(file);
        long chunkSize = length / countChunks;
        long chunkPosition = 0;

        for (int i = 0; i < countChunks; i++) {

            chunkSize = chunkPosition + chunkSize >= length
                    ? length - chunkPosition
                    : chunkSize + NumberFinderUtil.getNextDelimiterPosition(fileWrapper, file, chunkPosition + chunkSize, ',');

            try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
                MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunkSize);
                try {
                    while (fileWrapper.hasRemaining(mappedByteBuffer)) {
                        if (requestNumber == NumberFinderUtil.getNextIntFromMBF(fileWrapper, mappedByteBuffer, ',')) {
                            System.out.println("Что-то найдено!");
                            return true;
                        }
                    }
                } finally {
                    NumberFinderUtil.closeMappedByteBuffer(mappedByteBuffer);
                }
            }

            chunkPosition += chunkSize;
        }

        System.out.println("Ничего не найдено!");
        return false;
    }

}