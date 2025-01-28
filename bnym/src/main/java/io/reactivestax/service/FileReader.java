package io.reactivestax.service;

import io.reactivestax.utility.ApplicationPropertiesUtils;
import io.reactivestax.utility.Utility;
import lombok.Getter;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.atomic.AtomicInteger;

@Getter
public class FileReader {

    final ConcurrentHashMap<String, String> ruleValueSet = new ConcurrentHashMap<>();
    final AtomicInteger counter = new AtomicInteger();
    final List<String> lines = new ArrayList<>();

    public List<String> readFile(String filePath) throws IOException {
        try (InputStream inputStream = Utility.getResourceAsStream(ApplicationPropertiesUtils.class, filePath);
             BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream))) {
            String line;
            while ((line = bufferedReader.readLine()) != null) {
                lines.add(line.trim());
            }
        }
        return lines;
    }
}
