package io.reactivestax.types.contract;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.Map;

public interface ChunkGenerator {
    Integer generateAndSubmitChunks(String filePath, Integer numberOfChunks) throws IOException;
}
