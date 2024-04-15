package ru.tipsauk.rental.config;

import lombok.Getter;

import java.io.InputStream;
import java.util.Properties;

@Getter
public class ApplicationConfig {

    private static final String CONFIG_FILE = "application.properties";

    private final Properties properties;

    private final String url;

    private final String username;

    private final String password;

    public ApplicationConfig() {
        this.properties = loadProperties();
        this.url = properties.getProperty("url");
        this.username = properties.getProperty("username");
        this.password = properties.getProperty("password");
    }

    public ApplicationConfig(String url, String username, String password) {
        this.properties = loadProperties();
        this.url = url;
        this.username = username;
        this.password = password;
    }

    private Properties loadProperties() {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream(CONFIG_FILE)) {
            if (input != null) {
                props.load(input);
            } else {
                throw new RuntimeException("Не найден " + CONFIG_FILE);
            }
        } catch (Exception e) {
            throw new RuntimeException("Ошибка чтения конфигурационного файла", e);
        }
        return props;
    }

}
