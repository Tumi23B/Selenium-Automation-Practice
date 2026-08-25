package utils;

import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

public class ConfigReader {

    private static final Properties properties = new Properties();

    static {
        loadProperties();
    }

    /**
     * Loads the config.properties file from
     * src/test/resources.
     */
    private static void loadProperties() {

        try (InputStream input = ConfigReader.class
                .getClassLoader()
                .getResourceAsStream("config.properties")) {

            if (input == null) {
                throw new RuntimeException(
                        "config.properties file not found. " +
                        "Make sure it is located in src/test/resources."
                );
            }

            properties.load(input);

            System.out.println(
                    "Configuration loaded successfully."
            );

        } catch (IOException e) {

            throw new RuntimeException(
                    "Failed to load config.properties",
                    e
            );
        }
    }

    /**
     * Gets a String value from config.properties.
     *
     * @param key property key
     * @return property value
     */
    public static String get(String key) {

        String value = properties.getProperty(key);

        if (value == null) {

            throw new RuntimeException(
                    "Configuration property not found: " + key
            );
        }

        return value.trim();
    }

    /**
     * Gets an integer value from config.properties.
     *
     * @param key property key
     * @return integer property value
     */
    public static int getInt(String key) {

        try {

            return Integer.parseInt(get(key));

        } catch (NumberFormatException e) {

            throw new RuntimeException(
                    "Configuration property '" + key +
                    "' must be a valid integer.",
                    e
            );
        }
    }
}