package com.epam.clientinterface.configuration;

import com.epam.clientinterface.configuration.property.DatasourceConnectionProperties;
import com.zaxxer.hikari.HikariDataSource;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.orm.jpa.JpaTransactionManager;
import org.springframework.orm.jpa.LocalContainerEntityManagerFactoryBean;
import org.springframework.orm.jpa.vendor.HibernateJpaDialect;
import org.springframework.orm.jpa.vendor.HibernateJpaVendorAdapter;
import javax.persistence.EntityManagerFactory;
import javax.sql.DataSource;
import java.util.Properties;

@Configuration
public class DatasourceConfiguration {

    @Bean
    public DataSource dataSource(DatasourceConnectionProperties datasourceConnectionProperties) {
        HikariDataSource dataSource = new HikariDataSource();
        dataSource.setJdbcUrl(datasourceConnectionProperties.getUrl());
        dataSource.setUsername(datasourceConnectionProperties.getUsername());
        dataSource.setPassword(datasourceConnectionProperties.getPassword());
        dataSource.setDriverClassName("org.postgresql.Driver");
        return dataSource;
    }

    @Bean
    public LocalContainerEntityManagerFactoryBean entityManagerFactory(DataSource dataSource) {
        LocalContainerEntityManagerFactoryBean em = new LocalContainerEntityManagerFactoryBean();
        HibernateJpaVendorAdapter vendorAdapter = new HibernateJpaVendorAdapter();
        vendorAdapter.setGenerateDdl(false);
        vendorAdapter.setShowSql(true);
        Properties properties = new Properties();
        properties.setProperty("hibernate.dialect", "org.hibernate.dialect.PostgreSQL10Dialect");

        em.setPersistenceUnitName("persistence-unit");
        em.setPackagesToScan("com.epam.clientinterface");
        em.setDataSource(dataSource);
        em.setJpaVendorAdapter(vendorAdapter);
        em.setJpaDialect(new HibernateJpaDialect());
        em.setJpaProperties(properties);
        return em;

    }

    @Bean
    public JpaTransactionManager jpaTransactionManager(EntityManagerFactory entityManagerFactory) {
        JpaTransactionManager transactionManager = new JpaTransactionManager();
        transactionManager.setEntityManagerFactory(entityManagerFactory);
        return transactionManager;
    }
}
