package com.arcadsoftware.ae.core.hsqldb;

import java.sql.Connection;
import java.sql.ResultSet;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;

import com.arcadsoftware.ae.core.exceptions.ArcadException;
import com.arcadsoftware.ae.core.logger.MessageLogger;
import com.arcadsoftware.ae.core.utils.Utils;




/**
 * Class de gestion de la communication avec la base de donn‚es
 * @author MD
 *
 */
public abstract class HSQLDBConnector {

	private Connection connection = null;

	IHSQLDBConfiguration dbConfig = null;
	
	public HSQLDBConnector(){
		super();
		dbConfig = createDbConfig();
	}

	public abstract IHSQLDBConfiguration createDbConfig();
	
	private String createConnectionString() {
		StringBuffer sb = new StringBuffer("jdbc:hsqldb:");
		sb.append(dbConfig.getDblocation());				
		return sb.toString();
	}
	
	public Properties getConnectionProperties() throws ArcadException{				
    	Properties props = new Properties();    	
    	props.setProperty("user",dbConfig.getUserName());
    	props.setProperty("password",dbConfig.getPassword());	    	
    	props.setProperty("shutdown","true");
    	props.setProperty("dbFilterDateFormat",dbConfig.getDbFilterDateFormat());
    	props.setProperty("hsqldb.lock_file","false");
    	return props; 	
	}
	
	private Properties prepareConnection() throws ArcadException {
	    try {	        	    	
	    	Properties props = getConnectionProperties();
	        Class.forName("org.hsqldb.jdbcDriver" );
	        return props;        
	    } catch (ClassNotFoundException e) {
	    	throw new ArcadException("HSQLDB Driver not found!",e);
		} catch (ArcadException e){
			throw e;
		}
	}	
	
	/**
	 * M‚thode d'initalisation et de fourniture d'une connection jdbc
	 * @return Connection connexion jdbc ou null si une erreur est survenue
	 * @throws ExecException - si le driver n'est pas trouv‚<br>
	 *                       - si une erreur s'est produite lors de la cr‚ation de la connexion
	 */
	public Connection getConnection()  {
		if (connection==null){
			try {
				Properties props = prepareConnection();
				connection = 
		        	DriverManager.getConnection(createConnectionString(),props);	
			} catch (SQLException e) {
				MessageLogger.sendErrorMessage("",
						Utils.stackTrace(e));				
			} catch (ArcadException e) {
				MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
						Utils.stackTrace(e));				
			}			
		}
		return connection;
	}
		
    public synchronized int execute(String expression) {
        Statement st = null; 	
        int i = -1;
        try {
			st = getConnection().createStatement();
			i = st.executeUpdate(expression);    // run the query
	        st.close();	
	        commit();
		} catch (SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));				
		}   
		return i;
    }
	
    public synchronized ResultSet executeQuery(String sql){
        Statement st = null;
        ResultSet result = null;
        try {
			st = getConnection().createStatement();
			result = st.executeQuery(sql);    // run the query
	        st.close();			
		} catch (SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));	
		}   		
		return result;
    }
    
    public synchronized void close() {
    	try {
    		//execute("SHUTDOWN");
    		if (connection!=null)
    			connection.close();
		} catch (SQLException e) {
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
		}
    }
    
    public synchronized void setAutoCommit(boolean autoCommit){
    	try{
    		connection.setAutoCommit(autoCommit);
    	}catch (SQLException e){
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
    	}
    }
    	    
    public synchronized void commit(){
    	try{
    		connection.commit();
    	}catch (SQLException e){
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
    	}
    }
    
    public synchronized void rollback(){
    	try{
    		connection.rollback();
    	}catch (SQLException e){
			MessageLogger.sendErrorMessage(dbConfig.getModuleName(),
					Utils.stackTrace(e));
    	}
    }
	


	public IHSQLDBConfiguration getDbConfig() {
		return dbConfig;
	}


	public void setDbConfig(IHSQLDBConfiguration dbConfig) {
		this.dbConfig = dbConfig;
	}
    
    
	
}
