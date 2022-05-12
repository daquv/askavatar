package com.daquv.Config.datasource;


import lombok.extern.slf4j.Slf4j;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;
import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.support.TransactionTemplate;

import javax.sql.DataSource;

@Slf4j
@Configuration
@MapperScan(basePackages = {DataSourceConfig.PACKAGE,}, sqlSessionFactoryRef = "SqlSessionFactory")
public class DataSourceConfig extends AbstractDataSourceConfig {
	static final String PACKAGE = "com.daquv.Repository";

	private static final String[] MAPPER_LOCATIONS = {"classpath*:mybatis/mapper/*.xml"};

	@Value("${daquv.user.master.datasource.type}")
	private Class<? extends DataSource> masterDataSourceType;

	@Value("${daquv.user.slave.datasource.type}")
	private Class<? extends DataSource> slaveDataSourceType;

	@Primary
	@ConfigurationProperties(prefix = "daquv.user.master.datasource")
	@Bean(name = "MasterDataSource")
	public DataSource masterDataSource() {
		return DataSourceBuilder.create().type(this.masterDataSourceType).build();
	}

	@ConfigurationProperties(prefix = "daquv.user.slave.datasource")
	@Bean(name = "SlaveDataSource")
	public DataSource slaveDataSource() {
		return DataSourceBuilder.create().type(this.slaveDataSourceType).build();
	}

	@Bean(name = "DataSource")
	public DataSource dataSource(@Qualifier("MasterDataSource") final DataSource masterDataSource,
								 @Qualifier("SlaveDataSource") final DataSource slaveDataSource) {
		final DynamicDataSource dynamicDataSource = new DynamicDataSource();
		dynamicDataSource.setWriteDataSource(masterDataSource);
		dynamicDataSource.setReadDataSource(slaveDataSource);
		return dynamicDataSource;
	}

	@Primary
	@Bean(name = "TransactionManager")
	public DataSourceTransactionManager transactionManager(@Qualifier("DataSource") final DataSource dataSource) {
		return new DynamicDataSourceTransactionManager(dataSource);
	}

	@Primary
	@Bean(name = "SqlSessionFactory")
	public SqlSessionFactory sqlSessionFactory(@Qualifier("DataSource") final DataSource dataSource)
			throws Exception {
		final SqlSessionFactoryBean sessionFactory = this.createSqlSessionFactoryBean(dataSource, MAPPER_LOCATIONS);
		return sessionFactory.getObject();
	}

	@Primary
	@Bean(name = "SqlSessionTemplate")
	public SqlSessionTemplate sqlSessionTemplate(
			@Qualifier("SqlSessionFactory") final SqlSessionFactory sqlSessionFactory) throws Exception {
		return this.createSqlSessionTemplate(sqlSessionFactory);
	}

	@Primary
	@Bean(name = "TransactionTemplate")
	public TransactionTemplate transactionTemplate(
			@Qualifier("TransactionManager") final DataSourceTransactionManager transactionManager)
			throws Exception {
		return this.createTransactionTemplate(transactionManager);
	}
}
