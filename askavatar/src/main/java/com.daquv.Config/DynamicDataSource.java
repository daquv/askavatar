package com.daquv.Config;

import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.util.Assert;
import com.daquv.Config.*;

import java.util.HashMap;
import java.util.Map;

public class DynamicDataSource extends AbstractRoutingDataSource {
	private Object writeDataSource;
	private Object readDataSource;

	@Override
	public void afterPropertiesSet() {
		Assert.notNull(this.writeDataSource, "Property 'writeDataSource' is required");
		this.setDefaultTargetDataSource(this.writeDataSource);
		final Map<Object, Object> targetDataSources = new HashMap<>();
		targetDataSources.put(DataSourceFrom.WRITE.name(), this.writeDataSource);
		if (this.readDataSource != null) {
			targetDataSources.put(DataSourceFrom.READ.name(), this.readDataSource);
		}
		this.setTargetDataSources(targetDataSources);
		super.afterPropertiesSet();
	}

	@Override
	protected Object determineCurrentLookupKey() {
		final DataSourceFrom dataSourceFrom = DynamicDataSourceHolder.getDataSource();
		if (dataSourceFrom != null && dataSourceFrom == DataSourceFrom.READ) {
			return DataSourceFrom.READ.name();
		}
		return DataSourceFrom.WRITE.name();
	}

	public Object getWriteDataSource() {
		return this.writeDataSource;
	}

	public void setWriteDataSource(final Object writeDataSource) {
		this.writeDataSource = writeDataSource;
	}

	public Object getReadDataSource() {
		return this.readDataSource;
	}

	public void setReadDataSource(final Object readDataSource) {
		this.readDataSource = readDataSource;
	}
}
