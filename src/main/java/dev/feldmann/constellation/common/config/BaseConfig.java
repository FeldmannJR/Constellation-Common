package dev.feldmann.constellation.common.config;

public abstract class BaseConfig {

    protected abstract void load();

    String env(String key, String defaultValue) {
        String value = System.getenv(key);
        if (value != null && value.isEmpty()) {
            value = null;
        }
        if (value == null) {
            value = defaultValue;
        }
        return value;
    }

    boolean envBoolean(String key, boolean defaultValue) {
        String value = env(key, "" + defaultValue);
        if (value.equalsIgnoreCase("true") || value.equalsIgnoreCase("1")) {
            return true;
        }
        if (value.equalsIgnoreCase("false") || value.equalsIgnoreCase("0")) {
            return false;
        }
        return defaultValue;
    }
}
