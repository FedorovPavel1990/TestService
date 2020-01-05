package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.ByteArrayInputStream;
import java.io.File;
import java.nio.ByteBuffer;

@Service("MockFileWrapper")
public class MockFileWrapper extends AbstractFileWrapper {

    private String mockFile = "68484,46864,84866,48686,33537,786456,456486,484,54254,-6541,516854,2165,-654,9871,1549,6354,548,846383,5482";

    @Override
    public byte[] readAllBytes(File file) {
        return mockFile.getBytes();
    }

    @Override
    public long length(File file) {
        return mockFile.getBytes().length;
    }

    @Override
    public boolean hasRemaining(ByteBuffer bf) {
        return bf.position() < mockFile.length();
    }

    @Override
    public byte get(ByteBuffer bf) {
        int position = bf.position();
        ByteBuffer byteBuffer = ByteBuffer.wrap(mockFile.getBytes());
        byteBuffer.position(position);
        if (bf.hasRemaining()) {
            bf.get();
        }
        return byteBuffer.get();
    }
}
