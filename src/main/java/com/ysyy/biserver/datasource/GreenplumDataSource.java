package com.ysyy.biserver.datasource;

import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;

import javax.sql.DataSource;

@Configuration
@MapperScan(basePackages = "com.ysyy.biserver.gpserver", sqlSessionFactoryRef = "gpSqlSessionFactory")
public class GreenplumDataSource {
    @Bean(name="gpDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.gp")
    public DataSource sqlDatasource(){

        return DataSourceBuilder.create().build();
    }
    @Bean(name="gpSqlSessionFactory")
    public SqlSessionFactory sqlSqlSessionFactory(@Qualifier("gpDatasource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return  bean.getObject();
    }
    @Bean(name="gpTransactionManager")
    public DataSourceTransactionManager sqlTransactionManager(@Qualifier("gpDatasource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name="gpSqlSessionTemplate")
    public SqlSessionTemplate sqlSqlSessionTemplate(@Qualifier("gpSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }
}
