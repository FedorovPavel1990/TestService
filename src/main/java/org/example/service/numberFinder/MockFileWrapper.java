package org.example.service.numberFinder;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Stream;

@Service("MockFileWrapper")
public class MockFileWrapper extends AbstractFileWrapper {

    @Value("${testservice.chunks_count}")
    protected int countChunks;

    private String mockFile = "68484,46864,84866,48686,33537,786456,456486,484,216546,-6541,516854,2165,-654,9871,54254";

    @Override
    public byte[] readAllBytes(File file) {
        return mockFile.getBytes();
    }

    @Override
    public long length(File file) {
        return mockFile.length() * countChunks;
    }

    @Override
    public boolean hasRemaining(ByteBuffer bf) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(mockFile.getBytes());
        byteBuffer.position(bf.position());
        return byteBuffer.hasRemaining() && bf.hasRemaining();
    }

    @Override
    public byte get(ByteBuffer bf) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(mockFile.getBytes());
        byteBuffer.position(bf.position());
        if (byteBuffer.hasRemaining()) {
            bf.get();
        }
        return byteBuffer.get();
    }

    @Override
    public long position(ByteBuffer bf) {
        ByteBuffer byteBuffer = ByteBuffer.wrap(mockFile.getBytes());
        return Math.min(bf.position(), byteBuffer.limit());
    }

    @Override
    public Stream<Integer> getStream(MappedByteBuffer bf) {
        List<String> strings = Arrays.asList(mockFile.split(","));
        List<Integer> ints = new ArrayList<>();
        for (String s : strings) {
            ints.add(Integer.parseInt(s));
        }
        return ints.parallelStream();
    }

    @Override
    public String getStringFromMappedByteBuffer(MappedByteBuffer mbf) {
        return mockFile;
    }
}
