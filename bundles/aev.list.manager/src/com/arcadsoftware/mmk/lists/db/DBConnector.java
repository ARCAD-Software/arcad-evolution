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
		private static DBConnector instance = new DBConnector();
		private Connection connection = null;
				
		public DBConnector(){
			super();
		}

		public static DBConnector getInstance() {
			return instance;
		}
		
		
		private String createConnectionString() {
			StringBuffer sb = new StringBuffer("jdbc:hsqldb:mem:arcad");				
			return sb.toString();
		}
		
		public Properties getConnectionProperties() {		
	    	Properties props = new Properties();    	
	    	props.setProperty("user","sa");
	    	props.setProperty("password","");	    	
	    	props.setProperty("shutdown","true");
	    	return props;	
		}
		
		private Properties prepareConnection() throws ArcadException  {
		    try {	        	    	
		    	Properties props = getConnectionProperties();
		        Class.forName("org.hsqldb.jdbcDriver" );
		        return props;        
		    } catch (ClassNotFoundException e) {
		    	throw new ArcadException(Translator.resString("error.db.jdbcDriverNotFound"),e);
			}
		}	
		
		/**
		 * Méthode d'initalisation et de fourniture d'une connection jdbc
		 * @return Connection connexion jdbc ou null si une erreur est survenue
		 * @throws ExecException - si le driver n'est pas trouvé<br>
		 *                       - si une erreur s'est produite lors de la création de la connexion
		 */
		public Connection getConnection()  throws ArcadException {
			if (connection==null){
				try {
					Properties props = prepareConnection();
					connection = 
			        	DriverManager.getConnection(createConnectionString(),props);	
				} catch (SQLException e) {
					throw new ArcadException(Translator.resString("error.db.connectionFailed"),e);
				}			
			}
			return connection;
		}
			
	    public synchronized int execute(String expression) throws ArcadException {
	        Statement st = null; 	
	        int i = -1;
	        try {
				st = getConnection().createStatement();
				i = st.executeUpdate(expression);    // run the query
		        st.close();			
			} catch (SQLException e) {
				throw new ArcadException(
						Translator.resString("error.db.executionFailed",new String[]{expression}),e);
			}   
			return i;
	    }
		
	    public synchronized ResultSet executeQuery(String sql) throws ArcadException{
	        Statement st = null;
	        ResultSet result = null;
	        try {
				st = getConnection().createStatement();
				result = st.executeQuery(sql);    // run the query
		        st.close();			
			} catch (SQLException e) {
				throw new ArcadException(
						Translator.resString("error.db.executionFailed",new String[]{sql}),e);
			}   		
			return result;
	    }
	    
	    public synchronized  int executePrepareStatement(String sql,String[] data) 
	    throws ArcadException{	    	 
	    	PreparedStatement st = null;
	    	 int count = -1;
	        try {
				st = getConnection().prepareStatement(sql);
				for (int i=0;i<data.length;i++) {
					st.setString(i+1, data[i]); // 1 => premier ? dans la requète
				}
				count = st.executeUpdate();
		        st.close();			
			} catch (SQLException e) {
				throw new ArcadException(
						Translator.resString("error.db.executionFailed",new String[]{sql}),e);
			}   		
			return count;
	    }	    
	    
	    public synchronized ResultSet executePrepareQuery(String sql,String[] data) 
	    throws ArcadException{
	    	PreparedStatement st = null;
	        ResultSet result = null;
	        try {
	        	st = getConnection().prepareStatement(sql);
				for (int i=0;i<data.length;i++) {
					st.setString(i+1, data[i]); // 1 => premier ? dans la requète
				}	        	
				result = st.executeQuery();    // run the query
		        st.close();			
			} catch (SQLException e) {
				throw new ArcadException(
						Translator.resString("error.db.executionFailed",new String[]{sql}),e);
			}   		
			return result;
	    }	    
	    
	    
	    public synchronized void close() throws SQLException {
	    	connection.close();
	    }

}
