package com.ehrms.tmis.database.postgreSql;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.data.jpa.repository.config.EnableJpaRepositories;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.annotation.EnableTransactionManagement;

import javax.sql.DataSource;
import java.util.HashMap;
import java.util.Map;

@Configuration
@EnableTransactionManagement
@EnableJpaRepositories(basePackages = "com.ehrms.tmis.database.postgreSql.postgreSqlRepository", // Replace with your
                                                                                                 // PostgreSQL
                                                                                                 // repository
                                                                                                 // package
        entityManagerFactoryRef = "postgreEntityManagerFactory", transactionManagerRef = "postgreTransactionManager")
public class PostgreSQLConfig {
    @Primary
    @Bean(name = "postgreDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("org.postgresql.Driver")
                .url("jdbc:postgresql://localhost:5432/TmisEhrms")
                .username("postgres")
                .password("postgres")
                .build();
    }

    @Primary
    @Bean(name = "postgreEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("postgreDataSource") DataSource dataSource) {

        // Apply Hibernate properties to the EntityManagerFactory
        Map<String, Object> hibernateProperties = hibernateProperties("update");

        return builder
                .dataSource(dataSource)
                .packages("com.ehrms.tmis.database.postgreSql.postgreSqlEntity") // Replace with your PostgreSQL entity
                                                                                 // package
                .persistenceUnit("postgresql")
                .properties(hibernateProperties) // Pass the Hibernate properties here
                .build();
    }

    private Map<String, Object> hibernateProperties(String ddlAuto) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", ddlAuto); // Schema validation
        properties.put("hibernate.dialect", "org.hibernate.dialect.PostgreSQLDialect"); // PostgreSQL dialect
        properties.put("hibernate.default_schema", "tmis"); // Specify the schema here
        properties.put("hibernate.naming.physical-strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"); // Naming strategy for PostgreSQL
        // Uncomment this line if you want formatted SQL logs for readability
        // properties.put("hibernate.format_sql", "true");
        return properties;
    }

    @Primary
    @Bean(name = "postgreTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("postgreEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
