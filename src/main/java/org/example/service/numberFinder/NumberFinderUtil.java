package org.example.service.numberFinder;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class NumberFinderUtil {

    public static int getNextIntFromMBF(MappedByteBuffer mbf, char delimiter) {
        StringBuilder builder = new StringBuilder();
        while (mbf.hasRemaining()) {
            char ch = (char) mbf.get();
            if (ch == delimiter) {
                break;
            }
            builder.append(ch);
        }
        return Integer.parseInt(builder.toString());
    }

    public static long getNextDelimiterPosition(File file, long position, char delimiter) throws IOException {
        long nextDelimiterPosition = 0;

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MappedByteBuffer mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, position, 12);
            while (mappedByteBuffer.hasRemaining()) {
                char ch = (char) mappedByteBuffer.get();
                if (ch == delimiter) {
                    nextDelimiterPosition = mappedByteBuffer.position();
                    break;
                }
            }
        }
        return nextDelimiterPosition;
    }

}
