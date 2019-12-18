package org.example.service.numberFinder;

import java.io.File;
import java.io.FileInputStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.nio.ByteBuffer;
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

    public static synchronized Integer getNextIntFromMBFSync(MappedByteBuffer mbf, char delimiter) {
        if (!mbf.hasRemaining()) {
            return null;
        }
//        StringBuilder builder = new StringBuilder();
//        while (mbf.hasRemaining()) {
//            char ch = (char) mbf.get();
//            if (ch == delimiter) {
//                break;
//            }
//            builder.append(ch);
//        }
        return getNextIntFromMBF(mbf, delimiter);
    }

    public static long getNextDelimiterPosition(File file, long position, char delimiter) throws Exception {
        long nextDelimiterPosition = 0;

        try (FileInputStream fileInputStream = new FileInputStream(file)) {
            MappedByteBuffer mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, position, 12);
            try {
                while (mappedByteBuffer.hasRemaining()) {
                    char ch = (char) mappedByteBuffer.get();
                    if (ch == delimiter) {
                        nextDelimiterPosition = mappedByteBuffer.position();
                        break;
                    }
                }
            } finally {
                closeMappedByteBuffer(mappedByteBuffer);
            }
        }
        return nextDelimiterPosition;
    }

    public static void closeMappedByteBuffer(MappedByteBuffer buffer) throws Exception {
        Class<?> unsafeClass = Class.forName("sun.misc.Unsafe");
        Field unsafeField = unsafeClass.getDeclaredField("theUnsafe");
        unsafeField.setAccessible(true);
        Object unsafe = unsafeField.get(null);
        Method invokeCleaner = unsafeClass.getMethod("invokeCleaner", ByteBuffer.class);
        invokeCleaner.invoke(unsafe, buffer);
    }

}
