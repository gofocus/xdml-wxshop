package com.gofocus.wxshop.order.orderservice;

import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.configuration.ClassicConfiguration;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Value;

/**
 * @Author: GoFocus
 * @Date: 2020-06-28 11:34
 * @Description:
 */


public abstract class AbstractIntegrationTest {

    @Value("${spring.datasource.url}")
    private String dbUrl;
    @Value("${spring.datasource.username}")
    private String dbUsername;
    @Value("${spring.datasource.password}")
    private String dbPassword;

    @BeforeEach
    void setUp() {
        ClassicConfiguration conf = new ClassicConfiguration();
        conf.setDataSource(dbUrl, dbUsername, dbPassword);
        Flyway flyway = new Flyway(conf);
        flyway.clean();
        flyway.migrate();
    }
}
