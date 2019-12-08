package dev.feldmann.constellation.common.config;

import lombok.Getter;

public class Config {
    @Getter
    private DatabaseConfig database = new DatabaseConfig();

}
