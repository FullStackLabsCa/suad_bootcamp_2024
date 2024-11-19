package io.reactivestax.utility;


import org.junit.jupiter.api.Test;

import java.io.FileNotFoundException;
import java.io.IOException;

import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesIntegerFormat;
import static io.reactivestax.utility.ApplicationPropertiesUtils.readFromApplicationPropertiesStringFormat;
import static org.junit.jupiter.api.Assertions.*;

class ApplicationPropertiesUtilsTest {

    @Test
    void shouldReturnApplicationPropertiesInStringFormat() {
        String messagingTechnology = "rabbitmq";
        String propertyName = readFromApplicationPropertiesStringFormat("messaging.technology");
        assertNotNull(propertyName);
        assertEquals(messagingTechnology, propertyName);
    }

    @Test
    void shouldReturnApplicationPropertiesInIntegerFormat() throws IOException {
        Integer queueCount = 3;
        ApplicationPropertiesUtils.setApplicationResource("application.properties");
        Integer propertyInIntegerFormat = readFromApplicationPropertiesIntegerFormat("queue.count");
        assertNotNull(propertyInIntegerFormat);
        assertEquals(queueCount, propertyInIntegerFormat);
    }

    @Test
    void shouldThrowIOExceptionInIntegerFormat() {
        String mockFileName = "mockApplicationProperties";
        ApplicationPropertiesUtils.setApplicationResource(mockFileName);
        IOException fileNotFoundException = assertThrows(FileNotFoundException.class, () ->
                readFromApplicationPropertiesIntegerFormat("chunks.count"));
        String message = fileNotFoundException.getMessage();
        assertEquals("Property file " + mockFileName + "not found in the classpath", message);
    }


    @Test
    void shouldThrowRuntimeExceptionInStringFormat() {
        String mockFileName = "mockApplicationProperties";
        ApplicationPropertiesUtils.setApplicationResource(mockFileName);
        assertThrows(RuntimeException.class, () ->
                readFromApplicationPropertiesStringFormat("chunks.count"));

    }


    @Test
    public void shouldReturnNullWithoutPropertiesInFile() {
        ApplicationPropertiesUtils.setApplicationResource("application.properties");
        assertNull(readFromApplicationPropertiesStringFormat("check"));
    }


}
