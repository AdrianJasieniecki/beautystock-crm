package com.beautystock.crm;

import jakarta.persistence.EntityManagerFactory;
import org.flywaydb.core.Flyway;
import org.flywaydb.core.api.MigrationInfo;
import org.flywaydb.core.api.MigrationVersion;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.testcontainers.service.connection.ServiceConnection;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.test.context.ActiveProfiles;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.postgresql.PostgreSQLContainer;

import javax.sql.DataSource;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.SQLException;

import static org.assertj.core.api.Assertions.assertThat;

@Testcontainers
@ActiveProfiles
@SpringBootTest
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
    private DataSource dataSource;

    @Test
    void shouldApplyFlywayMigrationsAndBootstrapJpaOnEmptyPostgres() {
        assertThat(postgres.isRunning()).isTrue();
        assertThat(flyway.info().current().getVersion()).isEqualTo(MigrationVersion.fromVersion("1"));
        assertThat(flyway.info().current().getDescription()).isEqualTo("baseline");
        assertThat(flyway.info().current().isApplied()).isTrue();
        assertThat(entityManagerFactory.isOpen()).isTrue();
        try (Connection connection = dataSource.getConnection()) {
            DatabaseMetaData metadata = connection.getMetaData();
            assertThat(metadata.getDatabaseProductName()).isEqualTo("PostgreSQL");
            assertThat(metadata.getURL()).startsWith("jdbc:postgresql:");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
