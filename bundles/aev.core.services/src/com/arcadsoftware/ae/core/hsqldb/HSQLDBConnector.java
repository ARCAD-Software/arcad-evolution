package com.arcadsoftware.ae.core.hsqldb;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.ae.core.utils.Utils;

public abstract class HSQLDBConnector {

	private Connection connection = null;

	IHSQLDBConfiguration dbConfig = null;

	public HSQLDBConnector() {
		super();
		dbConfig = createDbConfig();
	}

	public synchronized void close() {
		try {
			if (connection != null) {
				connection.close();
			}
		} catch (final SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
		}
	}

	public synchronized void commit() {
		try {
			connection.commit();
		} catch (final SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
		}
	}

	private String createConnectionString() {
		final StringBuilder sb = new StringBuilder("jdbc:hsqldb:");
		sb.append(dbConfig.getDblocation());
		return sb.toString();
	}

	public abstract IHSQLDBConfiguration createDbConfig();

	public synchronized int execute(final String expression) {
		int i = -1;
		try (Statement st = getConnection().createStatement()) {
			i = st.executeUpdate(expression); // run the query
			commit();
		} catch (final SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
		}
		return i;
	}

	public synchronized ResultSet executeQuery(final String sql) {
		ResultSet result = null;
		try (Statement st = getConnection().createStatement()) {
			result = st.executeQuery(sql); // run the query
		} catch (final SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
		}
		return result;
	}

	/**
	 * Méthode d'initalisation et de fourniture d'une connection jdbc
	 *
	 * @return Connection connexion jdbc ou null si une erreur est survenue
	 * @throws ExecException
	 *             - si le driver n'est pas trouvé<br>
	 *             - si une erreur s'est produite lors de la création de la connexion
	 */
	public Connection getConnection() {
		if (connection == null) {
			try {
				final Properties props = prepareConnection();
				connection = DriverManager.getConnection(createConnectionString(), props);
			} catch (final SQLException e) {
				MessageLogger.sendErrorMessage("",
						Utils.stackTrace(e));
			} catch (final ArcadException e) {
				MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
						Utils.stackTrace(e));
			}
		}
		return connection;
	}

	public Properties getConnectionProperties() {
		final Properties props = new Properties();
		props.setProperty("user", dbConfig.getUserName());
		props.setProperty("password", dbConfig.getPassword());
		props.setProperty("shutdown", "true");
		props.setProperty("dbFilterDateFormat", dbConfig.getDbFilterDateFormat());
		props.setProperty("hsqldb.lock_file", "false");
		return props;
	}

	public IHSQLDBConfiguration getDbConfig() {
		return dbConfig;
	}

	private Properties prepareConnection() throws ArcadException {
		try {
			final Properties props = getConnectionProperties();
			Class.forName("org.hsqldb.jdbcDriver");
			return props;
		} catch (final ClassNotFoundException e) {
			throw new ArcadException("HSQLDB Driver not found!", e);
		}
	}

	public synchronized void rollback() {
		try {
			connection.rollback();
		} catch (final SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
		}
	}

	public synchronized void setAutoCommit(final boolean autoCommit) {
		try {
			connection.setAutoCommit(autoCommit);
		} catch (final SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
		}
	}

	public void setDbConfig(final IHSQLDBConfiguration dbConfig) {
		this.dbConfig = dbConfig;
	}

}
