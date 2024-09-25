package de.objektkontor.config.tomcatjdbc;

import java.time.Duration;

import com.zaxxer.hikari.HikariDataSource;

import de.objektkontor.config.ObservableConfig;
import de.objektkontor.config.annotation.ConfigParameter;
import de.objektkontor.config.common.DBConfig;

public class HikariPoolConfig extends ObservableConfig {

	@ConfigParameter(description = ""
			+ "This property controls the default auto-commit behavior of connections returned from the pool. It is a boolean value.") private boolean autoCommit = true;

	@ConfigParameter(description = ""
			+ "This property controls the maximum number of milliseconds that a client (that's you) will wait for a connection from "
			+ "the pool. If this time is exceeded without a connection becoming available, a SQLException will be thrown. "
			+ "Lowest acceptable connection timeout is 250 ms.") private Duration connectionTimeout = Duration.ofSeconds(30);

	@ConfigParameter(description = ""
			+ "This property controls the maximum amount of time that a connection is allowed to sit idle in the pool. This setting "
			+ "only applies when minimumIdle is defined to be less than maximumPoolSize. Idle connections will not be retired once "
			+ "the pool reaches minimumIdle connections. Whether a connection is retired as idle or not is subject to a maximum "
			+ "variation of +30 seconds, and average variation of +15 seconds. A connection will never be retired as idle before this "
			+ "timeout. A value of 0 means that idle connections are never removed from the pool. The minimum allowed value is "
			+ "10000ms (10 seconds).") private Duration idleTimeout = Duration.ofMinutes(10);

	@ConfigParameter(description = ""
			+ "This property controls how frequently HikariCP will attempt to keep a connection alive, in order to prevent it from "
			+ "being timed out by the database or network infrastructure. This value must be less than the maxLifetime value. "
			+ "A \"keepalive\" will only occur on an idle connection. When the time arrives for a \"keepalive\" against a given "
			+ "connection, that connection will be removed from the pool, \"pinged\", and then returned to the pool. The 'ping' "
			+ "is one of either: invocation of the JDBC4 isValid() method, or execution of the connectionTestQuery. Typically, "
			+ "the duration out-of-the-pool should be measured in single digit milliseconds or even sub-millisecond, and therefore "
			+ "should have little or no noticeable performance impact. The minimum allowed value is 30000ms (30 seconds), but a "
			+ "value in the range of minutes is most desirable.") private Duration keepaliveTime = Duration.ofSeconds(0);

	@ConfigParameter(description = ""
			+ "This property controls the maximum lifetime of a connection in the pool. An in-use connection will never be retired, "
			+ "only when it is closed will it then be removed. On a connection-by-connection basis, minor negative attenuation is "
			+ "applied to avoid mass-extinction in the pool. We strongly recommend setting this value, and it should be several seconds "
			+ "shorter than any database or infrastructure imposed connection time limit. A value of 0 indicates no maximum lifetime "
			+ "(infinite lifetime), subject of course to the idleTimeout setting. The minimum allowed value is 30000ms (30 seconds).") private Duration maxLifetime = Duration.ofSeconds(30);

	@ConfigParameter(description = ""
			+ "This property controls the minimum number of idle connections that HikariCP tries to maintain in the pool. If the idle "
			+ "connections dip below this value and total connections in the pool are less than maximumPoolSize, HikariCP will make a "
			+ "best effort to add additional connections quickly and efficiently. However, for maximum performance and responsiveness "
			+ "to spike demands, we recommend not setting this value and instead allowing HikariCP to act as a fixed size connection pool."
			+ "Default: same as maximumPoolSize") private Integer minimumIdle; // not set

	@ConfigParameter(description = ""
			+ "This property controls the maximum size that the pool is allowed to reach, including both idle and in-use connections. "
			+ "Basically this value will determine the maximum number of actual connections to the database backend. A reasonable value "
			+ "for this is best determined by your execution environment. When the pool reaches this size, and no idle connections are "
			+ "available, calls to getConnection() will block for up to connectionTimeout milliseconds before timing out. Please read "
			+ "about pool sizing.") private int maximumPoolSize = 10;

	@ConfigParameter(description = ""
			+ "This property represents a user-defined name for the connection pool and appears mainly in logging and JMX management "
			+ "consoles to identify pools and pool configurations.") private String poolName; // not set


	/**
	 * Configures specified data source instance
	 *
	 * @param dataSource
	 */
	public void applyTo(HikariDataSource dataSource) {
		// dimensions
		dataSource.setAutoCommit(autoCommit);
		dataSource.setConnectionTimeout(connectionTimeout.toMillis());
		dataSource.setIdleTimeout(idleTimeout.toMillis());
		dataSource.setKeepaliveTime(keepaliveTime.toMillis());
		dataSource.setMaxLifetime(maxLifetime.toMillis());
		if (minimumIdle != null) {
			dataSource.setMinimumIdle(minimumIdle);
		}
		dataSource.setMaximumPoolSize(maximumPoolSize);
		if (poolName != null) {
			dataSource.setPoolName(poolName);
		}
	}

	/**
	 * Untility function to apply external db config to data source
	 *
	 * @param dbConfig
	 * @param dataSource
	 */
	public static void applyJdbcConfig(DBConfig dbConfig, HikariDataSource dataSource) {
		dataSource.setDriverClassName(dbConfig.getDriver());
		dataSource.setJdbcUrl(dbConfig.getUrl());
		dataSource.setUsername(dbConfig.getUser());
		dataSource.setPassword(dbConfig.getPassword());
	}
}
