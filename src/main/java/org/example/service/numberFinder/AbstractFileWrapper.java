package org.example.service.numberFinder;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.MappedByteBuffer;
import java.util.stream.Stream;

public abstract class AbstractFileWrapper {

    public abstract byte[] readAllBytes(File file) throws IOException;

    public abstract long length(File file);

    public abstract boolean hasRemaining(ByteBuffer bf);

    public abstract byte get(ByteBuffer bf);

    public abstract long position(ByteBuffer bf);

    public abstract Stream<Integer> getStream(MappedByteBuffer mbf);

    public abstract String getStringFromMappedByteBuffer(MappedByteBuffer mbf);

}
