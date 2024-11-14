package io.reactivestax.utility;

import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.stream.Collectors;

public class FileLoaderUtil {

    private static FileLoaderUtil instance ;

    public static synchronized FileLoaderUtil  getInstance(){
        if(instance == null) {
            instance = new FileLoaderUtil();
        }
        return instance;
    }

    public String loadFileFromResources(String fileName) {
        // Load the file using the class loader
        InputStream inputStream = getClass().getClassLoader().getResourceAsStream(fileName);

        if (inputStream == null) {
            throw new IllegalArgumentException("File not found in resources: " + fileName);
        }

        // Read and return the content as a string
        try (BufferedReader reader = new BufferedReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
            return reader.lines().collect(Collectors.joining("\n"));
        } catch (Exception e) {
            throw new RuntimeException("Failed to read file: " + fileName, e);
        }
    }
}
