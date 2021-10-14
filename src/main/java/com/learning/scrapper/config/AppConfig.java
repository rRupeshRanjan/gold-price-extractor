package com.learning.scrapper.config;

import lombok.Data;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Data
@Configuration
public class AppConfig {

    @Value("${date.pattern}")
    private String datePattern;

    @Value("${price.url}")
    private String priceUrl;
}
