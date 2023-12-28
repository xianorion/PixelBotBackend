package org.app.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.orm.hibernate5.HibernateTransactionManager;
import org.springframework.orm.hibernate5.LocalSessionFactoryBean;


import javax.sql.DataSource;
import java.util.Properties;

/*

You dont need to configure hibernate on your own as spring jpa cna do this for you
but if you want more control over you transactions/session, you can have this set up.

I had to add:
@SpringBootApplication(exclude = HibernateJpaAutoConfiguration.class)
to Application class
The exclude helps with a conflicting dependency in hibernate with springs jpa as they both try to use some SessionHolder class.

in application.properties
I added
spring.jpa.hibernate.ddl-auto=validate
so we wouldnt see a dropping/creating of DB tables when we start up the application

 */
@Configuration
public class HibernateConfig {

    @Autowired
    private Environment environment;

    @Value("${spring.datasource.url}")
    private String datasoureUrl;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;
    @Value("${spring.datasource.driver-class-name}")
    private String driver;

    @Bean
    public DataSource datasource() {
        return DataSourceBuilder
                .create()
                .url(datasoureUrl)
                .driverClassName(driver)
                .username(username)
                .password(password)
                .build();
    }

    @Bean
    public LocalSessionFactoryBean  entityManagerFactory() {
        LocalSessionFactoryBean sessionFactory = new LocalSessionFactoryBean();
        sessionFactory.setDataSource(datasource());
        sessionFactory.setPackagesToScan(
                "org.app");

        return sessionFactory;
    } private Properties hibernateProperties() {
        Properties properties = new Properties();
        properties.put("hibernate.dialect", environment.getRequiredProperty("hibernate.dialect"));
        properties.put("hibernate.show_sql", environment.getRequiredProperty("hibernate.show_sql"));
        properties.put("hibernate.format_sql", environment.getRequiredProperty("hibernate.format_sql"));
        properties.put("hibernate.hbm2ddl.auto", environment.getRequiredProperty("hibernate.hbm2ddl.auto"));
        return properties;
    }

    @Bean(name = "transactionManager")
    public HibernateTransactionManager getTransactionManager() {
        HibernateTransactionManager transactionManager = new HibernateTransactionManager();
        transactionManager.setSessionFactory(entityManagerFactory().getObject());
        return transactionManager;
    }


}
