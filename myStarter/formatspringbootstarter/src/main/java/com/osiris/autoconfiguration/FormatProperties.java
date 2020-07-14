package com.osiris.autoconfiguration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.util.Map;

/**
 * @author lijia at 2020-07-14
 */
@ConfigurationProperties(prefix = FormatProperties.FORMAT_PREFIX)
public class FormatProperties {

    public static final String FORMAT_PREFIX = "osiris.my.format";

    private Map<String,Object> properties;

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
