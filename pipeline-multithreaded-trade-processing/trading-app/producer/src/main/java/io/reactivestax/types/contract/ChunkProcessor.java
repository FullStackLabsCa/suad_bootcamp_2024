package io.reactivestax.types.contract;


public interface ChunkProcessor {
    void processChunks(String filePath) throws Exception;
}
