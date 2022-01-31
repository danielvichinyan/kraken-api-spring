package com.nexo.marketdata;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = { DataSourceAutoConfiguration.class })
public class MarketDataApplication {

    public static void main(String[] args) {
        SpringApplication.run(MarketDataApplication.class, args);
    }

}
