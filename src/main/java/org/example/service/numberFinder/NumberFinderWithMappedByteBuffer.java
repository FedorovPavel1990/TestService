package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.RandomAccessFile;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Service("MappedByteBuffer")
public class NumberFinderWithMappedByteBuffer implements NumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws Exception {
        long length = file.length();
        try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, 0, length);
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
        return false;
    }

}
