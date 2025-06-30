package com.ehrms.tmis.database.msSql;

import jakarta.persistence.EntityManagerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.boot.orm.jpa.EntityManagerFactoryBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
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
@EnableJpaRepositories(basePackages = "com.ehrms.tmis.database.msSql.sqlRepository", entityManagerFactoryRef = "sqlServerEntityManagerFactory", transactionManagerRef = "sqlServerTransactionManager")
public class SqlServerConfig {

    // @Primary
    @Bean(name = "sqlServerDataSource")
    public DataSource dataSource() {
        return DataSourceBuilder.create()
                .driverClassName("com.microsoft.sqlserver.jdbc.SQLServerDriver")
                .url("jdbc:sqlserver://localhost:1433;encrypt=true;trustServerCertificate=true;databaseName=ehrms")
                .username("sa")
                .password("H12aT34e")
                .build();
    }

    @Bean(name = "sqlServerEntityManagerFactory")
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(
            EntityManagerFactoryBuilder builder,
            @Qualifier("sqlServerDataSource") DataSource dataSource) {

        // Apply Hibernate properties to the EntityManagerFactory
        Map<String, Object> hibernateProperties = hibernateProperties("none");

        return builder
                .dataSource(dataSource)
                .packages("com.ehrms.tmis.database.msSql.sqlEntity")
                .persistenceUnit("sqlServer")
                .properties(hibernateProperties) // Pass the Hibernate properties here
                .build();
    }

    private Map<String, Object> hibernateProperties(String ddlAuto) {
        Map<String, Object> properties = new HashMap<>();
        properties.put("hibernate.hbm2ddl.auto", ddlAuto); // Schema validation
        properties.put("hibernate.dialect", "org.hibernate.dialect.SQLServer2012Dialect"); // SQL Server dialect
        properties.put("hibernate.naming.physical-strategy",
                "org.hibernate.boot.model.naming.PhysicalNamingStrategyStandardImpl"); // Naming strategy for SQL Server
        // Uncomment this line if you want formatted SQL logs for readability
        // properties.put("hibernate.format_sql", "true");
        return properties;
    }

    // @Primary
    @Bean(name = "sqlServerTransactionManager")
    public PlatformTransactionManager transactionManager(
            @Qualifier("sqlServerEntityManagerFactory") EntityManagerFactory entityManagerFactory) {
        return new JpaTransactionManager(entityManagerFactory);
    }
}
