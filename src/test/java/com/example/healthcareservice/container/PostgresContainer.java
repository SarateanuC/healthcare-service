package com.example.healthcareservice.container;

import org.junit.jupiter.api.BeforeAll;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.springframework.test.context.jdbc.Sql;
import org.testcontainers.containers.PostgreSQLContainer;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.utility.DockerImageName;

@AutoConfigureMockMvc
@SpringBootTest
@Testcontainers
@Sql("/init.sql")
public class PostgresContainer {
    @Container
    private static final PostgreSQLContainer POSTGRES_SQL_CONTAINER =
            new PostgreSQLContainer<>(DockerImageName.parse("postgres:latest"))
                    .withUsername("postgres")
                    .withDatabaseName("hc-test")
                    .withPassword("postgres");

    @DynamicPropertySource
    static void overrideTestProperties(DynamicPropertyRegistry registry) {
        registry.add("spring.datasource.url", POSTGRES_SQL_CONTAINER::getJdbcUrl);
        registry.add("spring.datasource.username", POSTGRES_SQL_CONTAINER::getUsername);
        registry.add("spring.datasource.password", POSTGRES_SQL_CONTAINER::getPassword);
        registry.add("spring.datasource.driver-class-name", POSTGRES_SQL_CONTAINER::getDriverClassName);
        registry.add("spring.jpa.hibernate.ddl-auto", () -> "validate");
    }

    @BeforeAll
    public static void setUp() {
        POSTGRES_SQL_CONTAINER.start();
    }
}
