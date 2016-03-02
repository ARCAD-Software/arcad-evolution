/*
 * Cr‚‚ le 12 oct. 07
 *
 * TODO Pour changer le modŠle de ce fichier g‚n‚r‚, allez … :
 * Fenˆtre - Pr‚f‚rences - Java - Style de code - ModŠles de code
 */
package com.arcadsoftware.ae.core.hsqldb;

import java.text.SimpleDateFormat;
import java.util.Date;



public abstract class AbstractConnector {
	
	protected HSQLDBConnector dbconnector;
	protected SimpleDateFormat df =null; 	
	
	public AbstractConnector(){
		super();
		dbconnector = createDbConnector();
		df = new SimpleDateFormat(dbconnector.getDbConfig().getDbFilterDateFormat());
	}
	
	public void close() {
		dbconnector.close();		
	}
	
	protected String convertDate(Date d) { 
		return convertDate(d,df);
	}
	protected String convertDate(Date d, SimpleDateFormat format) { 
		return (d==null)?"null":format.format(d);
	}		
	
	protected String nullString(String s) {
		return s==null?"":s;
	}
	
	public abstract HSQLDBConnector createDbConnector();
}
