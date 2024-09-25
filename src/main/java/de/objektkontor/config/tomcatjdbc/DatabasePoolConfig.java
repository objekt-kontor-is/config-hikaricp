package de.objektkontor.config.tomcatjdbc;

import com.zaxxer.hikari.HikariDataSource;

import de.objektkontor.config.annotation.ConfigParameter;
import de.objektkontor.config.common.DBConfig;

public class DatabasePoolConfig extends DBConfig {

	@ConfigParameter private HikariPoolConfig hikariPool;

	public HikariPoolConfig getPool() {
		return hikariPool;
	}

	public void setPool(HikariPoolConfig pool) {
		this.hikariPool = pool;
	}

	public void applyTo(HikariDataSource dataSource) {
		HikariPoolConfig.applyJdbcConfig(this, dataSource);
		hikariPool.applyTo(dataSource);
	}
}
