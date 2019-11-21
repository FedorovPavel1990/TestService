package utils;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.MappedByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Scanner;
import java.util.stream.IntStream;


public class Test {
    private static final int COUNT_CHUNKS = 2;

    public static void main(String[] args) throws IOException {
        Test test = new Test();
        test.testServiceTest();
//        test.test();
    }

    private void test() {
        List<Integer> ints = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            ints.add(i);
        }

        boolean isNumberFound = ints.stream().anyMatch(n -> n == 1);
        System.out.println(isNumberFound);
    }

    private void testServiceTest() throws IOException {
        int n = -968120087; //1568642635, -2030360796
        File file = new File(
                "tmpTestFiles/testFile1"
//                "C:/1/testFile1"
        );
        long startTime = new Date().getTime();
        boolean result = findNumberInFile(file, n);
        long endTime = new Date().getTime();
        System.out.println(result);
        System.out.println("Ответ получен за " + (endTime - startTime) + " мс");

    }

    private boolean findNumberInFile(File file, int requestNumber) throws IOException {
        byte[] byteArray = Files.readAllBytes(Paths.get(file.getPath()));
        boolean result;
        try (Scanner scanner = new Scanner(new ByteArrayInputStream(byteArray, 0, byteArray.length))) {
            scanner.useDelimiter(",");
            IntStream intStream = IntStream.generate(scanner::nextInt).limit(Integer.MAX_VALUE);
            result = intStream.anyMatch(n -> n == requestNumber);
        }
        return result;
//---------------------------------------------------------------------------------------------------
//        long length = file.length();
//        long chunk = length / COUNT_CHUNKS;
//        long chunkPosition = 0;
//
//        for (int i = 0; i < COUNT_CHUNKS; i++) {
//
//            chunk = chunkPosition + chunk >= length
//                    ? length - chunkPosition
//                    : chunk + getNextDelimiterPosition(file, chunkPosition + chunk, ',');
//
//            try (FileInputStream fileInputStream = new FileInputStream(file)) {
//                MappedByteBuffer mappedByteBuffer = fileInputStream
//                        .getChannel()
//                        .map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);
//
//
//                IntStream intStream = IntStream.generate(mappedByteBuffer::getInt).limit(20); //mappedByteBuffer.remaining()
////                StringBuilder builder = new StringBuilder();
////                intStream.forEach(ch -> builder.append((char) ch));
////                String string = builder.toString();
////                List<String> ints = Arrays.asList(string.split(","));
//
//                intStream.forEach(System.out::print);
////                intStream.forEach(ch -> System.out.print((char) ch));
//                System.out.println();
//
////                boolean isNumberFound = intStream.parallel().anyMatch(n -> n == requestNumber);
////                if (isNumberFound) {
////                    return true;
////                }
//
//            }
//
//            chunkPosition += chunk;
//        }
//
//        return false;
//---------------------------------------------------------------------------------------------------
//        long length = file.length();
//        long chunk = length / COUNT_CHUNKS;
//        long chunkPosition = 0;
//
//        for (int i = 0; i < COUNT_CHUNKS; i++) {
//
//            chunk = chunkPosition + chunk >= length
//                    ? length - chunkPosition
//                    : chunk + getNextDelimiterPosition(file, chunkPosition + chunk, ',');
//
//            try (FileInputStream fileInputStream = new FileInputStream(file)) {
//                MappedByteBuffer mappedByteBuffer = fileInputStream
//                        .getChannel()
//                        .map(FileChannel.MapMode.READ_ONLY, chunkPosition, chunk);
//                while (mappedByteBuffer.hasRemaining()) {
//                    if (requestNumber == getNextIntFromMBF(mappedByteBuffer, ',')) {
//                        return true;
//                    }
//                }
//            }
//
//            chunkPosition += chunk;
//        }
//
//        return false;
//---------------------------------------------------------------------------------------------------
//        long length = file.length();
//        try (FileInputStream fileInputStream = new FileInputStream(file)) {
//            MappedByteBuffer mappedByteBuffer = fileInputStream.getChannel().map(FileChannel.MapMode.READ_ONLY, 0, length);
//
//            while (mappedByteBuffer.hasRemaining()) {
//                if (requestNumber == getNextIntFromMBF(mappedByteBuffer, ',')) {
//                    return true;
//                }
//            }
//        }
//        return false;
//---------------------------------------------------------------------------------------------------
//        byte[] byteArray = Files.readAllBytes(Paths.get(file.getPath()));
//        try (Scanner scanner = new Scanner(new ByteArrayInputStream(byteArray, 0, byteArray.length))) {
//            scanner.useDelimiter(",");
//            while (scanner.hasNextInt()) {
//                if (requestNumber == scanner.nextInt()) {
//                    return true;
//                }
//            }
//        }
//        return false;
//---------------------------------------------------------------------------------------------------
//        byte[] byteArray = Files.readAllBytes(Paths.get(file.getPath()));
//        try (ByteArrayInputStream fileReader = new ByteArrayInputStream(byteArray, 0, byteArray.length)) {
//
//            int i = 0;
//            while (true) {
//                int number;
//                StringBuilder builder = new StringBuilder();
//                if (i == -1) break;
//                while (true) {
//                    i = fileReader.read();
//                    if (i == (int) ',' || i == -1) break;
//                    builder.append((char) i);
//                }
//
//                number = Integer.parseInt(builder.toString());
//
//                if (requestNumber == number) {
//                    return true;
//                }
//            }
//        }
//        return false;
    }

    private int getNextIntFromMBF(MappedByteBuffer mbf, char delimiter) {
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

    private long getNextDelimiterPosition(File file, long position, char delimiter) throws IOException {
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