package com.zemiak.movies;

import com.zemiak.movies.service.ConfigurationProvider;
import java.util.HashMap;
import java.util.Map;

/**
 * Needed ENV keys are listed below.
 *
 * BIN_PATH
 * MEDIA_PATH
 * EXTERNAL_URL
 * SYSTEM_NAME
 * MAIL_TO
 */
final public class TestConfigurationProvider {
    public static void init() {
        Map<String, String> config = new HashMap<>();

        config.put("BIN_PATH", "/usr/bin");
        config.put("MEDIA_PATH", "/pictures");
        config.put("EXTERNAL_URL", "http://127.0.0.1:8080/nasphotos/");
        config.put("SYSTEM_NAME", "test");
        config.put("MAIL_TO", "test@non-existing.local");

        ConfigurationProvider.setProvidedConfig(config);
    }
}
