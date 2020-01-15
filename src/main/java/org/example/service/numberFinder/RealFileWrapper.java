package org.example.service.numberFinder;

import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.stream.Stream;

@Service("RealFileWrapper")
public class RealFileWrapper extends AbstractFileWrapper {

    @Override
    public byte[] readAllBytes(File file) throws IOException {
        return Files.readAllBytes(Paths.get(file.getPath()));
    }

    @Override
    public long length(File file) {
        return file.length();
    }

    @Override
    public boolean hasRemaining(ByteBuffer bf) {
        return bf.hasRemaining();
    }

    @Override
    public byte get(ByteBuffer bf) {
        return bf.get();
    }

    @Override
    public long position(ByteBuffer bf) {
        return bf.position();
    }

    @Override
    public Stream<Integer> getStream(MappedByteBuffer mbf) {
        return Stream.generate(() -> NumberFinderUtil.getNextIntFromMBFSync(this, mbf, ','));
    }

    @Override
    public String getStringFromMappedByteBuffer(MappedByteBuffer mbf) {
        return StandardCharsets.UTF_8.decode(mbf).toString();
    }
}
