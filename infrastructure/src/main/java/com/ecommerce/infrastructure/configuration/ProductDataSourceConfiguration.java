package com.ecommerce.infrastructure.configuration;

import com.zaxxer.hikari.HikariConfig;
import com.zaxxer.hikari.HikariDataSource;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.PlatformTransactionManager;
import org.springframework.transaction.TransactionDefinition;
import org.springframework.transaction.annotation.EnableTransactionManagement;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(ignoreResourceNotFound = true, value = {"classpath:jdbc-mysql.properties"})
@ComponentScan("com.ecommerce.infrastructure.repository")
@MapperScan(basePackages = {"com.ecommerce.infrastructure.repository.mapper.product"},
        sqlSessionFactoryRef = "productSqlSessionFactory")
public class ProductDataSourceConfiguration {

    @Bean(name = "productDataSource")
    public DataSource getDataSource(
            @Value("${jdbc.driverClassName:com.mysql.jdbc.Driver}") final String driverClassName,
            @Value("${jdbc.product.url:jdbc:mysql://localhost:3306/product?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8&allowMultiQueries=true&useSSL=false}") final String polarJdbcUrl,
            @Value("${jdbc.product.username:root}") final String polarUsername,
            @Value("${jdbc.product.password:Zk@041916}") final String polarPassword,
            @Value("${jdbc.product.testQuery:#{'select 1 from dual'}}") final String testQuery,
            @Value("${jdbc.product.connectionTimeout:30000}") final int connectionTimeout,
            @Value("${jdbc.product.idleTimeout:600000}") final int idleTimeout,
            @Value("${jdbc.product.maxPoolSize:10}") final int maxPoolSize,
            @Value("${jdbc.product.minimumIdle:1}") final int minimumIdle) throws SQLException {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(polarJdbcUrl);
        config.setUsername(polarUsername);
        config.setPassword(polarPassword);
        config.setConnectionTimeout(connectionTimeout);
        config.setConnectionTestQuery("select 1 from dual");
        config.setMaximumPoolSize(maxPoolSize);
        config.setMinimumIdle(minimumIdle);
        config.setConnectionTimeout(connectionTimeout);
        config.setIdleTimeout(idleTimeout);
        final HikariDataSource dataSource = new HikariDataSource(config);
        //启动时候就获取连接
        for (int i = 0; i < minimumIdle / 3; i++) {
            Connection connection = dataSource.getConnection();
            connection.close();
        }
        return dataSource;
    }

    @Bean(name = "productSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(
            @Autowired @Qualifier(value = "productDataSource") final DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/product/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "productTransactionManager")
    public PlatformTransactionManager productTransactionManager(
            @Autowired @Qualifier(value = "productDataSource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "productTransactionTemplate")
    public TransactionTemplate productTransactionTemplate(
            @Autowired @Qualifier(value = "productTransactionManager")
            PlatformTransactionManager productTransactionManager) {
        return new TransactionTemplate(productTransactionManager);
    }

    /**
     * 定义一个编程式事务，其传播行为为REQUIRED
     */
    @Bean(name = "productTransactionPropagationTemplate")
    public TransactionTemplate productTransactionPropagationTemplate(
            @Autowired @Qualifier(value = "productTransactionManager")
            PlatformTransactionManager productTransactionManager) {
        TransactionTemplate template = new TransactionTemplate(productTransactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return template;
    }

}
