package org.example.service.numberFinder;

import java.io.File;
import java.io.RandomAccessFile;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;

public class NumberFinderUtil {

    public static int getNextIntFromMBF(AbstractFileWrapper fileWrapper, MappedByteBuffer mbf, char delimiter) {
        StringBuilder builder = new StringBuilder();
        while (fileWrapper.hasRemaining(mbf)) {
            char ch = (char) fileWrapper.get(mbf);
            if (ch == delimiter) {
                break;
            }
            builder.append(ch);
        }
        return Integer.parseInt(builder.toString());
    }

    public static synchronized Integer getNextIntFromMBFSync(AbstractFileWrapper fileWrapper, MappedByteBuffer mbf, char delimiter) {
        if (!fileWrapper.hasRemaining(mbf)) {
            return null;
        }
        return getNextIntFromMBF(fileWrapper, mbf, delimiter);
    }

    public static long getNextDelimiterPosition(AbstractFileWrapper fileWrapper, File file, long position, char delimiter) throws Exception {
        long nextDelimiterPosition = 0;

        try (FileChannel fileChannel = new RandomAccessFile(file, "r").getChannel()) {
            MappedByteBuffer mappedByteBuffer = fileChannel.map(FileChannel.MapMode.READ_ONLY, position, 12);
            try {
                while (fileWrapper.hasRemaining(mappedByteBuffer)) {
                    char ch = (char) fileWrapper.get(mappedByteBuffer);
                    if (ch == delimiter) {
                        nextDelimiterPosition = fileWrapper.position(mappedByteBuffer);
                        break;
                    }
                }
            } finally {
                closeMappedByteBuffer(mappedByteBuffer);
            }
        }
        return nextDelimiterPosition;
    }

    public static void closeMappedByteBuffer(ByteBuffer buffer) throws Exception {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Object unsafe = unsafeField.get(null);
        Method invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
        invokeCleaner.invoke(unsafe, buffer);
    }

}
