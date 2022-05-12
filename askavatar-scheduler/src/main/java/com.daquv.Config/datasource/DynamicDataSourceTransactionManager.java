package com.daquv.Config.datasource;

import org.springframework.jdbc.datasource.DataSourceTransactionManager;
import org.springframework.transaction.TransactionDefinition;

import javax.sql.DataSource;

public class DynamicDataSourceTransactionManager extends DataSourceTransactionManager {

	public DynamicDataSourceTransactionManager() {
		super();
	}

	public DynamicDataSourceTransactionManager(final DataSource dataSource) {
		super(dataSource);
	}

	@Override
	protected void doBegin(final Object transaction, final TransactionDefinition definition) {
		// 设置数据源
		final boolean readOnly = definition.isReadOnly();
		if (readOnly) {
			DynamicDataSourceHolder.putDataSource(DataSourceFrom.READ);
		} else {
			DynamicDataSourceHolder.putDataSource(DataSourceFrom.WRITE);
		}
		super.doBegin(transaction, definition);
	}

	@Override
	protected void doCleanupAfterCompletion(final Object transaction) {
		super.doCleanupAfterCompletion(transaction);
		DynamicDataSourceHolder.clearDataSource();
	}
}
