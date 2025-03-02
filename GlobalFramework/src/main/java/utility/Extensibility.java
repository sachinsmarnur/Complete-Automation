package utility;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import java.io.FileInputStream;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

public class Extensibility {

    Logger log = LogManager.getLogger(Extensibility.class);
    Properties properties;

    public Extensibility(){}

    public Extensibility(String file) {
        properties  = new Properties();
        try (InputStream fis = Files.newInputStream(Paths.get(file))) {
            properties.load(fis);
        } catch (Exception e) {
            log.info("Unable to find the specific properties file");
            log.trace(e);
        }
    }

    public String getProperty(String key, String fileName) {
        properties = new Properties();
        try (InputStream fis = Files.newInputStream(Paths.get(fileName))) {
            properties.load(fis);
        } catch (Exception e) {
            log.info("Unable to find the specific properties file");
            log.trace(e);
        }
        return properties.getProperty(key);
    }

    public String getProperty(String key) {
        return properties.getProperty(key);
    }

    public String getProperty(String key, String fileName, String... replaceable) {
        String value = getProperty(key, fileName);
        return String.format(value, replaceable);
    }
}
