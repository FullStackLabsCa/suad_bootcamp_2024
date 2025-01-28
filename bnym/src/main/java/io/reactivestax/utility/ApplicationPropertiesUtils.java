package io.reactivestax.utility;

import lombok.Setter;
import lombok.extern.slf4j.Slf4j;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ApplicationPropertiesUtils {

    private static final String DEFAULT_APPLICATION_PROPERTIES = "application.properties";
    @Setter
    private static String applicationResource = DEFAULT_APPLICATION_PROPERTIES;

    public static String readFromApplicationPropertiesStringFormat(String propertyName) {
        Properties properties = new Properties();

        try (InputStream inputStream = ApplicationPropertiesUtils.class.getClassLoader().getResourceAsStream(applicationResource)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Property file " + applicationResource + "not found in the classpath");
            }
            properties.load(inputStream);
            return properties.getProperty(propertyName);
        } catch (IOException e) {
            throw new RuntimeException(e.getMessage());
        }
    }

}
