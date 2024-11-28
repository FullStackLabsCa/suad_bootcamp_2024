package io.reactivestax.service;

import io.reactivestax.types.contract.ChunkGenerator;
import io.reactivestax.factory.BeanFactory;
import io.reactivestax.utility.ApplicationPropertiesUtils;
import lombok.extern.slf4j.Slf4j;

import java.io.*;
import java.net.URISyntaxException;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.stream.IntStream;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;


@Slf4j
public class ChunkGeneratorService implements ChunkGenerator {

    private static ChunkGeneratorService instance;

    private ChunkGeneratorService() {
    }

    public static ChunkGeneratorService getInstance() {
        if (instance == null) {
            instance = new ChunkGeneratorService();
        }
        return instance;
    }

    protected BufferedReader createBufferReader(InputStream inputStream) throws IOException {
        return new BufferedReader(new InputStreamReader(inputStream));
    }

    @Override
    public Integer generateAndSubmitChunks(String filePath, Integer numberOfChunks) throws IOException {
        List<String> lines = new ArrayList<>();
        try (InputStream inputStream = ApplicationPropertiesUtils.class.getClassLoader().getResourceAsStream(filePath);
             BufferedReader reader = createBufferReader(inputStream)) {

            String line;
            while ((line = reader.readLine()) != null) {
                lines.add(line);
            }
        } catch (IOException e) {
            log.info("File not found.. {}", e.getMessage());
            throw new FileNotFoundException(e.getMessage());
        }
        int totalLines = lines.size();
        int linesPerChunk = (totalLines - 1) / numberOfChunks; //excluding the header here so doing -1

        String header = lines.get(0);
        generateChunks(numberOfChunks, header, linesPerChunk, totalLines, lines);
        return numberOfChunks;
    }


    private static void generateChunks(Integer numberOfChunks, String header, int linesPerChunk, int totalLines, List<String> lines) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        //creating the chunks and submitting to the executorService
        AtomicInteger startLine = new AtomicInteger(1);

        String resourceDirectory;
        try {
            resourceDirectory = Paths.get(ClassLoader.getSystemResource("").toURI()).toString();
        } catch (URISyntaxException e) {
            log.error("Failed to locate resource directory", e);
            throw new RuntimeException(e);
        }

        IntStream.range(0, numberOfChunks).forEach(i -> {
            String relativePath = readFromApplicationPropertiesStringFormat("chunks.file.path") + "trades_chunk_" + (i + 1) + ".csv";
            String outputFile = Paths.get(resourceDirectory, relativePath).toString(); //Paths.get(resource) retrieves the baseFilePath tills the resources and concatenate with the chunks file path

            executorService.submit(() -> {

                try (BufferedWriter writer = new BufferedWriter(new FileWriter(outputFile))) {
                    writer.write(header);
                    writer.newLine();
                    int endLine = Math.min(startLine.get() + linesPerChunk, totalLines); // this ensures that line doesn't read beyond the last-line.
                    for (int j = startLine.get(); j < endLine; j++) {
                        writer.write(lines.get(j));
                        writer.newLine();
                    }
                    startLine.set(endLine);
                    //adding to queue for making the chunk generator and chunk processor decoupled
                    BeanFactory.setChunksFileMappingQueue(outputFile);
                    log.info("Created  {}", outputFile);
                } catch (IOException e) {
                    log.info("Error in chunks generation {}", e.getMessage());
                }
            });
        });
        executorService.shutdown();
    }
}
