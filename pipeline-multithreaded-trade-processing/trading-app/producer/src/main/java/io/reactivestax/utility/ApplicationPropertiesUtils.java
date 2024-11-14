package io.reactivestax.utility;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.Optional;
import java.util.Properties;

import io.reactivestax.factory.BeanFactory;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;


@Slf4j
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
            log.error(e.getMessage());
        }
        return "";
    }

    public static Integer readFromApplicationPropertiesIntegerFormat(String propertyName) throws IOException {
        Properties properties = new Properties();

        // Use class loader to load the file from the resources folder
        try (InputStream inputStream = ApplicationPropertiesUtils.class.getClassLoader().getResourceAsStream(applicationResource)) {
            if (inputStream == null) {
                throw new FileNotFoundException("Property file " + applicationResource + "not found in the classpath");
            }
            properties.load(inputStream);
            return Integer.parseInt(properties.getProperty(propertyName));
        }
    }
}
