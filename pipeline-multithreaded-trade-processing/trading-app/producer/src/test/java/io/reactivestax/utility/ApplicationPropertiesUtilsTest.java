package io.reactivestax.utility;

import org.junit.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.Assert.*;

public class ApplicationPropertiesUtilsTest {

    @Test
    public void shouldReturnApplicationPropertiesInStringFormat() {
        String messagingTechnology = "rabbitmq";
        String propertyName = readFromApplicationPropertiesStringFormat("messaging.technology");
        assertNotNull(propertyName);
        assertEquals(messagingTechnology, propertyName);
    }

    @Test
    public void shouldReturnApplicationPropertiesInIntegerFormat() throws IOException {
        Integer poolSize = 10;
        ApplicationPropertiesUtils.setApplicationResource("application.properties");
        Integer propertyInIntegerFormat = readFromApplicationPropertiesIntegerFormat("chunk.processor.thread.count");
        assertNotNull(propertyInIntegerFormat);
        assertEquals(poolSize, propertyInIntegerFormat);
    }

    @Test
    public void shouldThrowIOException() {
        String mockFileName = "mockApplicationProperties";
        ApplicationPropertiesUtils.setApplicationResource(mockFileName);
        IOException fileNotFoundException = assertThrows(FileNotFoundException.class, () ->
                readFromApplicationPropertiesIntegerFormat("chunks.count"));
        String message = fileNotFoundException.getMessage();
        assertEquals(message, "Property file " + mockFileName + "not found in the classpath");
    }

    @Test
    public void shouldReturnNullWithoutPropertiesInFile()  {
        ApplicationPropertiesUtils.setApplicationResource("application.properties");
        assertNull(readFromApplicationPropertiesStringFormat("check"));
    }

    @Test
    public void shouldThrowNumberFormatExceptionWithoutPropertiesInFileForIntegerFormat() {
        assertThrows(NumberFormatException.class, () ->
                readFromApplicationPropertiesIntegerFormat("test"));
    }
}
