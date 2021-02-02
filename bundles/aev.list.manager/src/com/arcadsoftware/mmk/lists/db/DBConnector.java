package com.arcadsoftware.mmk.lists.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.ae.core.translation.Translator;

public class DBConnector {
	private static final String ERROR_DB_EXECUTION_FAILED = "error.db.executionFailed";
	private static DBConnector instance = new DBConnector();

	public static DBConnector getInstance() {
		return instance;
	}

	private Connection connection = null;

	public DBConnector() {
		super();
	}

	public synchronized void close() throws SQLException {
		connection.close();
	}

	private String createConnectionString() {
		final StringBuilder sb = new StringBuilder("jdbc:hsqldb:mem:arcad");
		return sb.toString();
	}

	public synchronized int execute(final String expression) throws ArcadException {		
		int i = -1;
		try (Statement st = getConnection().createStatement()){
			i = st.executeUpdate(expression); // run the query
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString(ERROR_DB_EXECUTION_FAILED, new String[] { expression }), e);
		}
		return i;
	}

	public synchronized ResultSet executePrepareQuery(final String sql, final String[] data)
	throws ArcadException {
		ResultSet result = null;
		try (PreparedStatement st = getConnection().prepareStatement(sql)){
			for (int i = 0; i < data.length; i++) {
				st.setString(i + 1, data[i]); // 1 => premier ? dans la requète
			}
			result = st.executeQuery(); // run the query
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString(ERROR_DB_EXECUTION_FAILED, new String[] { sql }), e);
		}
		return result;
	}

	public synchronized int executePrepareStatement(final String sql, final String[] data)
			throws ArcadException {
		int count = -1;
		try (PreparedStatement st = getConnection().prepareStatement(sql)){
			for (int i = 0; i < data.length; i++) {
				st.setString(i + 1, data[i]); // 1 => premier ? dans la requète
			}
			count = st.executeUpdate();
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString(ERROR_DB_EXECUTION_FAILED, new String[] { sql }), e);
		}
		return count;
	}

	public synchronized ResultSet executeQuery(final String sql) throws ArcadException {
		ResultSet result = null;
		try (Statement st = getConnection().createStatement()){
			result = st.executeQuery(sql); // run the query
		} catch (final SQLException e) {
			throw new ArcadException(
					Translator.resString(ERROR_DB_EXECUTION_FAILED, new String[] { sql }), e);
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
	public Connection getConnection() throws ArcadException {
		if (connection == null) {
			try {
				final Properties props = prepareConnection();
				connection = DriverManager.getConnection(createConnectionString(), props);
			} catch (final SQLException e) {
				throw new ArcadException(Translator.resString("error.db.connectionFailed"), e);
			}
		}
		return connection;
	}

	public Properties getConnectionProperties() {
		final Properties props = new Properties();
		props.setProperty("user", "sa");
		props.setProperty("password", "");
		props.setProperty("shutdown", "true");
		return props;
	}

	private Properties prepareConnection() throws ArcadException {
		try {
			final Properties props = getConnectionProperties();
			Class.forName("org.hsqldb.jdbcDriver");
			return props;
		} catch (final ClassNotFoundException e) {
			throw new ArcadException(Translator.resString("error.db.jdbcDriverNotFound"), e);
		}
	}

}
