package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

@Service("MappedByteBuffer")
public class NumberFinderWithMappedByteBuffer implements NumberFinder {

    @Override
    public boolean findNumberInFile(File file, int requestNumber) throws IOException {
        long length = file.length();
        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MappedByteBuffer mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);

            while (mappedByteBuffer.hasRemaining()) {
                if (requestNumber == NumberFinderUtil.getNextIntFromMBF(mappedByteBuffer, ',')) {
                    return true;
                }
            }
        }
        return false;
    }

}
