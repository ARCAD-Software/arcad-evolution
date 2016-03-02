package com.arcadsoftware.ae.core.utils;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.URL;
import java.net.URLClassLoader;

public class ClassUtils {
	
	
	
	public static Object createObject(String jarFile,String classname) 
	throws ClassNotFoundException, 
		InstantiationException, IllegalAccessException, IOException{	    
		//Get the jar file
    	File f = new File(jarFile);
    	if (f.exists()) { //if exists
    		//Create an array of URL based on the jarfile
			URL[] urls = new URL[]{f.toURL()}; 
			//Create a new classloader using these URLs
		    URLClassLoader classLoader = new URLClassLoader(urls);
		    //If the classname is correctly declared
		    if ((classname!=null) && classname.length()!=0) {
		    	//create the transformer
		        Class transformerClass = Class.forName(classname,true,classLoader);
		        Object transformer = transformerClass.newInstance();
		        return transformer;
		    } else {
		    	classLoader.close();
		    	throw new ClassNotFoundException(classname);
		    }		    
    	} else {
    		throw new FileNotFoundException(jarFile);
    	}
	}	
	
	public static Object createObject(String classname) 
	throws ClassNotFoundException, InstantiationException, IllegalAccessException {	    
	    if ((classname!=null) && classname.length()!=0) {
	    	//create the transformer
	        Class transformerClass = Class.forName(classname);
	        Object transformer = transformerClass.newInstance();
	        return transformer;
	    } else {
	    	throw new ClassNotFoundException(classname);
	    }		    
	}	
	

	
}
