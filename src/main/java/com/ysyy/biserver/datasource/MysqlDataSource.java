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
/**配置数据源**/
@Configuration
@MapperScan(basePackages = {"com.ysyy.biserver.mysqlserver","com.ysyy.biserver.mapper","com.ysyy.biserver.mysqlclass"}, sqlSessionFactoryRef = "mysqlSqlSessionFactory")
public class MysqlDataSource {
    @Bean(name="mysqlDatasource")
    @ConfigurationProperties(prefix = "spring.datasource.mysql")
    public DataSource sqlDatasource(){

        return DataSourceBuilder.create().build();
    }
    @Bean(name="mysqlSqlSessionFactory")
    public SqlSessionFactory sqlSqlSessionFactory(@Qualifier("mysqlDatasource") DataSource dataSource) throws Exception{
        SqlSessionFactoryBean bean=new SqlSessionFactoryBean();
        bean.setDataSource(dataSource);
        return  bean.getObject();
    }
    @Bean(name="mysqlTransactionManager")
    public DataSourceTransactionManager sqlTransactionManager(@Qualifier("mysqlDatasource") DataSource dataSource){
        return new DataSourceTransactionManager(dataSource);
    }
    @Bean(name="mysqlSqlSessionTemplate")
    public SqlSessionTemplate sqlSqlSessionTemplate(@Qualifier("mysqlSqlSessionFactory") SqlSessionFactory sqlSessionFactory){
        return new SqlSessionTemplate(sqlSessionFactory);
    }

}
