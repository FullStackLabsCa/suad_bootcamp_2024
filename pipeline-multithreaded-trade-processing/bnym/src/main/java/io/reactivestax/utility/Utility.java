package io.reactivestax.utility;

import java.io.InputStream;

public class Utility {

    public static InputStream getResourceAsStream(Class<?> className, String filePath) {
        return className.getClassLoader().getResourceAsStream(filePath);
    }
}
