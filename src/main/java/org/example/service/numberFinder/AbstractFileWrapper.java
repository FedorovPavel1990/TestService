package org.example.service.numberFinder;

import java.nio.file.Path;

public abstract class AbstractFileWrapper {

    public abstract byte[] readAllBytes(Path path);

    public abstract long length();

    public abstract boolean hasRemaining();

    public abstract byte get();

}
