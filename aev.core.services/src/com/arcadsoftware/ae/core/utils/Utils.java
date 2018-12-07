package com.arcadsoftware.ae.core.utils;


import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.IOException;
import java.io.InputStream;
import java.lang.reflect.Method;
import java.net.JarURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Properties;

/**
 * @author MD
 *
 */
public class Utils {
	protected static final String ARCAD_HOME = "ARCAD_HOME";
    private static final Class[] parameters = new Class[]{URL.class};
    
    public static void addFile(String s) throws IOException {
        File f = new File(s);
        addFile(f);
    }
    
    public static void addFile(File f) throws IOException {
        addURL(f.toURL());
    }
        
    public static void addURL(URL u) throws IOException {   	
        URLClassLoader sysloader = (URLClassLoader)ClassLoader.getSystemClassLoader();
        Class sysclass = URLClassLoader.class;    
        try {
            Method method = sysclass.getDeclaredMethod("addURL",parameters);
            method.setAccessible(true);
            method.invoke(sysloader,new Object[]{ u });
        } catch (Throwable t) {
            t.printStackTrace();
            throw new IOException("Error, could not add URL to system classloader");
        }
   }    
    
	/**
	 * M‚thode permettant de cr‚er un tableau d'URL associ‚ … chaque fichier contenu
	 * dans un r‚pertoire sp‚cifique pass‚ en paramŠtre.
	 * @param libFolder : File : R‚pertoire de base
	 * @param recursive : Analyse r‚cursive
	 * @return Tableau des URL
	 */
	public static URL[] getURLs(File libFolder,boolean recursive)  {				
		File[] files = getFiles(libFolder,recursive);
		if (files!=null) {
			ArrayList<URL> l = new ArrayList<URL>(files.length);		
			for (int i=0;i<files.length;i++){
				try {
					URL u = files[i].toURL(); 
					l.add(u);
				} catch (MalformedURLException e) {
					
				}
			}
			URL[] result = new URL[l.size()];
			for (int i=0;i<l.size();i++){
				result[i]=(URL)l.get(i);
			}				
			return result;
		} 
		return new URL[0];
	}    
    
	public static void setUrls(String folder)  {
		//R‚cup‚ration des URL repr‚sentant les fichiers stock‚s dans
		//le r‚pertoire des URL
		URL[] urls = Utils.getURLs(new File(folder),true);
		for (int i=0;i<urls.length;i++){
		    try {
                Utils.addURL(urls[i]);
            } catch (IOException e) {
                e.printStackTrace();
            }
		}			    
	}	
	
	/**
	 * M‚thode permettant de cr‚er un tableau de File associ‚ … chaque fichier contenu
	 * dans un r‚pertoire sp‚cifique pass‚ en paramŠtre.
	 * @param libFolder : File : R‚pertoire de base
	 * @param recursive : Analyse r‚cursive
	 * @return Tableau des URL
	 */
	public static File[] getFiles(File libFolder,boolean recursive)  {
		if (libFolder.exists()) {
			File[] list = libFolder.listFiles();
			ArrayList<File> l = new ArrayList<File>();
			for (int i= 0;i<list.length;i++){
				if (list[i].isDirectory() && recursive){
					File[] files = getFiles(list[i],recursive);
					for (int j=0;j<files.length;j++){
						l.add(files[j]);
					}
				}else {			
					l.add(list[i]);
				}
			}		
			File[] result = new File[l.size()];
			for (int i=0;i<l.size();i++){
				result[i]=(File)l.get(i);
			}		
			return result;
		}
		return null;
	}		
	
    public static String stackTrace(Throwable e) {
    	StringBuffer sb = new StringBuffer();
    	sb.append(e.getMessage()).append('\n');
    	StackTraceElement[] ste =  e.getStackTrace();
    	for (int i=0;i<ste.length;i++) {
    		StackTraceElement st = ste[i];
    		sb.append(st.toString()).append('\n');
    	}
    	return sb.toString();
    }	
	
	public static synchronized String computeId(){
		SimpleDateFormat fd = new SimpleDateFormat("yyyyMMddhhmmssSSS");
		try {
			Thread.sleep(2);
		} catch (InterruptedException e) {}		
		return fd.format(new Date());		
	}    
    
	//--------------------------------------------------------------------------
	public static String substitute(String s,
									String oldPattern,
									String newPattern,
									int nbocc){
		String s1 = new String(s);
		int i = 0;
		int st=0;		
		int start = 0;
		int pos = s1.indexOf(oldPattern,start);
		boolean follow = true;
		while ((pos!=-1) && follow ){
			i++;
			start=pos+newPattern.length();
			StringBuffer sb = new StringBuffer(s1.substring(0,pos));		
			sb.append(newPattern);
			st = pos+oldPattern.length();
			sb.append(s1.substring(st,s1.length()));
			s1=sb.toString();				
			pos = s1.indexOf(oldPattern,start); // Ajout...
			if (nbocc !=-1)
				follow = (i<nbocc);			
		}
		return s1;
	}	
	
	//--------------------------------------------------------------------------
	public static String substitute(String s,
									String oldPattern,
									String newPattern){
		return substitute(s,oldPattern,newPattern,1);
	}		
	
	public static String substituteProperty(String value,String propertyName){
		if (System.getProperty(propertyName)!=null){
			StringBuffer toReplace = new StringBuffer();
			toReplace.append("${").append(propertyName).append("}");			
			value = Utils.substitute(value,toReplace.toString(),System.getProperty(propertyName),-1);
		}
		return value;
	}
	
	
	public static StringBuffer replicateToStringBuffer(char c,int size) {
		StringBuffer s = new StringBuffer("");//$NON-NLS-1$ 
		for(int i=1;i<=size;i++){
			s.append(c);
		}
		return s;
	}
	public static String replicate(char c,int size) {		
		StringBuffer s = replicateToStringBuffer(c,size);
		return s.toString();
	}   	
	

	public static String[] toSubstitute(String value){
		ArrayList l = new ArrayList();
		//Recherche d'une propri‚t‚		
		String s = new String(value);
		int start = s.indexOf("${",0);
		if (start>-1) {
			int end = s.indexOf("}",start+2);
			if (end>-1) {
				String sub = s.substring(start+2,end);
				l.add(sub);
			}
		}		
		String[] result = new String[l.size()];
		for (int i=0;i<l.size();i++){
			result[i] = (String)l.get(i);
		}
		return result;	
	}	
	public static String quotedString(String s) {
		if (s==null)
			return "";
		StringBuffer sb = new StringBuffer(s);
		StringBuffer result = new StringBuffer();		
		int size = s.length();
		for (int i=0;i<size;i++) {
			if (sb.charAt(i)=='\'') {
				result.append("''");//$NON-NLS-1$ 
			} else {
				result.append(sb.charAt(i));			
			}
		}		
		return result.toString();
	}	
	
	public static void sleepForAWhile(long millisecond){
		try {Thread.sleep(millisecond);}
		catch (InterruptedException e) {}		
	}		
	
	//--------------------------------------------------------------------------
	public static final String lpad(String s,int length) {
		return lpad(s,length,' ');
	}
	
	//--------------------------------------------------------------------------
	public static final String rpad(String s,int length) {
		return rpad(s,length,' ');
	}	
	//--------------------------------------------------------------------------
	public static final String pad(String s,int length) {
		return rpad(s,length,' ');
	}		
	//--------------------------------------------------------------------------
	public static final String lpad(String s,int length, char paddedChar) {
		StringBuffer sb = new StringBuffer(s);
		if (s.length() < length) {
			while(sb.length()<length)
				sb.insert(0, paddedChar);			
			return sb.toString();
		}
		return sb.toString().substring(0, length);
	}
	//--------------------------------------------------------------------------
	public static final String rpad(String s,int length, char paddedChar) {
		StringBuffer sb = new StringBuffer(s);
		if (s.length() < length) {
			while(sb.length()<length)
				sb.append(paddedChar);			
			return sb.toString();
		}
		return sb.toString().substring(0, length);
	}	
	
	/**
	 * Check that the given String is neither <code>null nor of length 0.
	 * Note: Will return <code>true for a String that purely consists of whitespace.
	 * <p>

	 * StringUtils.hasLength(null) = false
	 * StringUtils.hasLength("") = false
	 * StringUtils.hasLength(" ") = true
	 * StringUtils.hasLength("Hello") = true
	 * </pre>
	 * @param str the String to check (may be <code>null)
	 * @return <code>true if the String is not null and has length
	 * @see #hasText(String)
	 */
	public static boolean hasLength(String str) {
		return (str != null && str.length() > 0);
	}
	
	/**
	 * Trim all occurences of the supplied leading character from the given String.
	 * @param str the String to check
	 * @param leadingCharacter the leading character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimLeadingCharacter(String str, char leadingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(0) == leadingCharacter) {
			buf.deleteCharAt(0);
		}
		return buf.toString();
	}

	/**
	 * Trim all occurences of the supplied trailing character from the given String.
	 * @param str the String to check
	 * @param trailingCharacter the trailing character to be trimmed
	 * @return the trimmed String
	 */
	public static String trimTrailingCharacter(String str, char trailingCharacter) {
		if (!hasLength(str)) {
			return str;
		}
		StringBuffer buf = new StringBuffer(str);
		while (buf.length() > 0 && buf.charAt(buf.length() - 1) == trailingCharacter) {
			buf.deleteCharAt(buf.length() - 1);
		}
		return buf.toString();
	}


	public static String getTemporaryFolder(){
		String folder = System.getProperty("java.io.tmpdir","");
		if (folder.equals("")) {
			String userhome= System.getProperty("user.home","");
			if (!userhome.equals("")) {
				return userhome+File.pathSeparator+"tmp";
			} else
				return null;
		} else
			return folder;
	}
    public static String getHomeDirectory(){
    	String result = System.getProperty(ARCAD_HOME);		
		
    	if (isEmpty(result)){
    		result = System.getenv(ARCAD_HOME);	
		}
		
		if (!isEmpty(result)) {
			System.setProperty(ARCAD_HOME,result);
			System.out.println("ARCAD_HOME is " + result);
		}
		else{
			System.err.println("Error: ARCAD_HOME was not found, neither as an environment variable nor as a property.");
		}
		
		return result;
    }
    
	public static String readTextFile(String fileName) {
		BufferedReader in = null;
		StringBuffer sb = new StringBuffer();
        try {        	
        	in = new BufferedReader(new FileReader(fileName));
            
        	String str;
            while ((str = in.readLine()) != null) {
            	sb.append(str).append('\n');
            }        	        	    	    	    
        } catch (FileNotFoundException e) {
        } catch (IOException e) {
        } finally{
        	if (in != null)
        		try {in.close();} catch (IOException e) {}
        }
        return sb.toString();
	}    
    
	public static void writeToFile(String filename, String data, boolean append) throws FileNotFoundException, IOException{				
		File f = new File(filename);
		if (!f.getParentFile().exists()) {
			f.getParentFile().mkdirs();
		}		
		FileOutputStream file;
		file = new FileOutputStream(filename,append);
		file.write(data.getBytes());
		file.close();					
	}
    
	public static boolean isEmpty(String string){
		return string == null || string.equals("");
	}
	
	public static String getProperty(Class<?> sourceClass, String jarFile, String fileInJar, String propertyName){
		String value = "N/A";
		InputStream inputStream = null;
		try {
			//Load from an external JAR
			if(!isEmpty(jarFile)){
				String jarFileURL = String.format("jar:file:/%1$s!/%2$s", jarFile, fileInJar);
				URL inputURL = new URL(jarFileURL);
		    	JarURLConnection conn = (JarURLConnection)inputURL.openConnection();
				inputStream = conn.getInputStream();
			}
			//Load from current JAR
			else{
				inputStream = sourceClass.getClass().getResourceAsStream(fileInJar);
			}
			
			//Load from file
		    if(inputStream == null){
				inputStream = new FileInputStream(new File(fileInJar));	
		    }
		}
    	catch (Exception e) {
    		System.err.println(String.format("Cannot find %1$s (%2$s) file: %3$s", fileInJar, jarFile, e.getLocalizedMessage()));
		}
	    
	    if(inputStream != null){
	    	try {
	    		Properties versionProps = new Properties();			
				versionProps.load(inputStream);
				value = (String) versionProps.get(propertyName);
			}
	    	catch (IOException e) {
	    		System.err.println(String.format("Cannot load %1$s from %2$s (%3$s): %4$s", propertyName, fileInJar, jarFile, e.getLocalizedMessage()));
			}
	    }
		
	    return value;
	}
}
