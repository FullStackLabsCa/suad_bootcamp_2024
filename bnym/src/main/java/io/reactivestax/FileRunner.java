package io.reactivestax;

import io.reactivestax.service.TreeProcessor;
import io.reactivestax.service.FileReader;

import java.io.IOException;
import java.util.List;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;

public class FileRunner {
    public static void main(String[] args) throws IOException {
        String filePath = readFromApplicationPropertiesStringFormat("file.path");
        FileReader fileReader = new FileReader();
        List<String> dataSet = fileReader.readFile(filePath);
        new TreeProcessor().applyRuleSet(dataSet);
    }
}