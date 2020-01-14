package org.example.service.numberFinder;

import java.io.File;
import java.io.IOException;
import java.nio.ByteBuffer;

public abstract class AbstractFileWrapper {

    public abstract byte[] readAllBytes(File file) throws IOException;

    public abstract long length(File file);

    public abstract boolean hasRemaining(ByteBuffer bf);

    public abstract byte get(ByteBuffer bf);

    public abstract long position(ByteBuffer bf);

}
