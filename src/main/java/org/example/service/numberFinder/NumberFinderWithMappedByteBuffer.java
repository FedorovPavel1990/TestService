package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Service("MappedByteBuffer")
public class NumberFinderWithMappedByteBuffer extends AbstractNumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws Exception {
        long length = fileWrapper.length(file);
        try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);
            try {
                while (fileWrapper.hasRemaining(mappedByteBuffer)) {
                    if (requestNumber == NumberFinderUtil.getNextIntFromMBF(fileWrapper, mappedByteBuffer, ',')) {
                        return true;
                    }
                }
            } finally {
                NumberFinderUtil.closeMappedByteBuffer(mappedByteBuffer);
            }
        }
        return false;
    }

}
