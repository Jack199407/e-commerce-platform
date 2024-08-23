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
import org.springframework.util.StringUtils;

import javax.sql.DataSource;
import java.sql.Connection;
import java.sql.SQLException;

@Configuration
@EnableTransactionManagement(proxyTargetClass = true)
@PropertySource(ignoreResourceNotFound = true, value = {"classpath:jdbc-mysql.properties"})
@ComponentScan("com.ecommerce.infrastructure.repository")
@MapperScan(basePackages = {"com.ecommerce.infrastructure.repository.mapper.order"},
        sqlSessionFactoryRef = "orderSqlSessionFactory")
public class OrderDataSourceConfiguration {

    @Bean(name = "orderDataSource")
    public DataSource gaotuDataSource(
            @Value("${jdbc.driverClassName:com.mysql.jdbc.Driver}") final String driverClassName,
            @Value("${jdbc.order.url:jdbc:mysql://localhost:3306/order?zeroDateTimeBehavior=convertToNull&characterEncoding=utf8&allowMultiQueries=true&useSSL=false}") final String polarJdbcUrl,
            @Value("${jdbc.order.username:root}") final String polarUsername,
            @Value("${jdbc.order.password:Zk@041916}") final String polarPassword,
            @Value("${jdbc.order.testQuery:#{'select 1 from dual'}}") final String testQuery,
            @Value("${jdbc.order.connectionTimeout:30000}") final int connectionTimeout,
            @Value("${jdbc.order.idleTimeout:600000}") final int idleTimeout,
            @Value("${jdbc.order.maxPoolSize:10}") final int maxPoolSize,
            @Value("${jdbc.order.minimumIdle:1}") final int minimumIdle) throws SQLException {
        final HikariConfig config = new HikariConfig();
        config.setDriverClassName(driverClassName);
        config.setJdbcUrl(polarJdbcUrl);
        config.setUsername(polarUsername);
        config.setPassword(polarPassword);
        config.setConnectionTimeout(connectionTimeout);
        config.setConnectionTestQuery(StringUtils.isEmpty(testQuery) ? "select 1 from dual" : testQuery);
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

    @Bean(name = "orderSqlSessionFactory")
    public SqlSessionFactory sqlSessionFactoryBean(
            @Autowired @Qualifier(value = "orderDataSource") final DataSource dataSource)
            throws Exception {
        SqlSessionFactoryBean sqlSessionFactoryBean = new SqlSessionFactoryBean();
        PathMatchingResourcePatternResolver resolver = new PathMatchingResourcePatternResolver();
        sqlSessionFactoryBean.setMapperLocations(resolver.getResources("classpath*:mapper/gaotu/*.xml"));
        sqlSessionFactoryBean.setConfigLocation(new ClassPathResource("mybatis-config.xml"));
        sqlSessionFactoryBean.setDataSource(dataSource);
        return sqlSessionFactoryBean.getObject();
    }

    @Bean(name = "orderTransactionManager")
    public PlatformTransactionManager orderTransactionManager(
            @Autowired @Qualifier(value = "orderDataSource") final DataSource dataSource) {
        return new DataSourceTransactionManager(dataSource);
    }

    @Bean(name = "orderTransactionTemplate")
    public TransactionTemplate orderTransactionTemplate(
            @Autowired @Qualifier(value = "orderTransactionManager")
            PlatformTransactionManager gaotuTransactionManager) {
        return new TransactionTemplate(gaotuTransactionManager);
    }

    /**
     * 定义一个编程式事务，其传播行为为REQUIRED
     */
    @Bean(name = "orderTransactionPropagationTemplate")
    public TransactionTemplate orderTransactionPropagationTemplate(
            @Autowired @Qualifier(value = "orderTransactionManager")
            PlatformTransactionManager gaotuTransactionManager) {
        TransactionTemplate template = new TransactionTemplate(gaotuTransactionManager);
        template.setPropagationBehavior(TransactionDefinition.PROPAGATION_REQUIRED);
        return template;
    }

}
