package com.beautystock.crm;

import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles("test")
@SpringBootTest(properties = {
        "spring.jpa.hibernate.ddl-auto=validate",
        "spring.flyway.enabled=true"
})
class BackendApiApplicationTests {

    @Container
    @ServiceConnection
    static final PostgreSQLContainer postgres =
            new PostgreSQLContainer("postgres:17-alpine")
                    .withDatabaseName("beautystock_test")
                    .withUsername("test")
                    .withPassword("test");

    @Autowired
    private Flyway flyway;

    @Autowired
    private EntityManagerFactory entityManagerFactory;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    @Test
    void shouldApplyFlywayMigrationsAndBootstrapJpaOnEmptyPostgres() {
        assertThat(postgres.isRunning()).isTrue();

        assertThat(flyway.info().applied())
                .isNotEmpty();

        assertThat(entityManagerFactory.isOpen())
                .isTrue();

        String revisionTable = jdbcTemplate.queryForObject(
                "SELECT to_regclass('public.revinfo')",
                String.class
        );

        assertThat(revisionTable).isEqualTo("revinfo");
    }
}
