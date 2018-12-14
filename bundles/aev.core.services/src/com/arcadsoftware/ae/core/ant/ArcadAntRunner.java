/*
 * Créé le 20 nov. 08
 *
 * TODO Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre - Préférences - Java - Style de code - Modèles de code
 */
package com.arcadsoftware.ae.core.ant;

import java.io.File;
import java.util.Iterator;
import java.util.Map;
import java.util.Properties;
import java.util.Set;

import org.apache.tools.ant.AntClassLoader;
import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DirectoryScanner;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;
import org.apache.tools.ant.types.Path;


public class ArcadAntRunner {

	private Project project;
	
	public ArcadAntRunner(){
		super();
		 project = new Project(){
		    public AntClassLoader createClassLoader(Path path) {
		    	if (path==null) {
		    		if(getCoreLoader()!=null) {
		    			return (AntClassLoader)getCoreLoader();
		    		} else {
		    			return super.createClassLoader(path);
		    		}
		    	} else
		    		return super.createClassLoader(path);
		    }	
		    
		    public Project createSubProject() {
		        Project subProject = null;
		        try {
		            subProject = (Project) (getClass().newInstance());
		        } catch (Exception e) {
		            subProject = new Project() {
					    public AntClassLoader createClassLoader(Path path) {
					    	if (path==null) {
					    		if(getCoreLoader()!=null) {
					    			return (AntClassLoader)getCoreLoader();
					    		} else {
					    			return super.createClassLoader(path);
					    		}
					    	} else
					    		return super.createClassLoader(path);
					    }	
		            };		            
		        }
		        initSubProject(subProject);
		        return subProject;
		    }

		    
		    
		    /**
		     * Initialize a subproject.
		     * @param subProject the subproject to initialize.
		     */
		    public void initSubProject(Project subProject) {
		    	super.initSubProject(subProject);
		    	
		    	subProject.setCoreLoader(ArcadAntRunner.this.getProject().getCoreLoader());
		    }		    
		    
		    
		 };		 
	}
	
	/**
	 * M‚thode permettant de sp‚cifier un rep‚rtoire de recherche des
	 * bibliothŠque (*.jar) … charger dans le projet.<br>
	 * Le r‚pertoire est scann‚ r‚cursivement pour trouver tous les
	 * fichiers jar. Ils sont ensuite ajout‚s aux r‚pertoires de recherche.
	 * @param libFolder File : R‚pertoire de recherche
	 * @throws ExecException - Projet est null
	 */
	public void setLibraryFolder(File libFolder) {		
		// Test if the project exists
		if (project == null) 
			return;		
		AntClassLoader loader = 
			new AntClassLoader(project.getCoreLoader(),false);
		if (libFolder.isDirectory()){
			DirectoryScanner ds = new DirectoryScanner();
			ds.setBasedir(libFolder);
			//Parcours recursif pour trouver tous les jars
			ds.setIncludes(new String[]{"**\\*.jar"}); //$NON-NLS-1$
			ds.setCaseSensitive(false);
			ds.setFollowSymlinks(true);
			ds.scan();
			String[] files = ds.getIncludedFiles();
			for (int i= 0;i<files.length;i++){
				loader.addPathElement(libFolder+File.separator+files[i]);
			}
		}	
		//Réaafectation au projet
		project.setCoreLoader(loader);		
	}
	
	/**
	 * Méthode permettant de sp‚cifier un rep‚rtoire de recherche des
	 * bibliothèque (*.jar) … charger dans le projet.<br>
	 * Le répertoire est scanné récursivement pour trouver tous les
	 * fichiers jar. Ils sont ensuite ajoutés aux répertoires de recherche.
	 * @param libFolder String : Répertoire de recherche
	 */	
	public void setLibraryFolder(String libFolder) {
		if ((libFolder!=null) && (!libFolder.equals(""))) {
			setLibraryFolder(new File(libFolder));		
		}
	}
	
	
     /**
      * Initialisation d'un projet ant
      * @param buildFile Le fichier projet à utiliser. SI il est passé à null, il sera remplacé
      *        par "build.xml"
      * @param baseDir Le répertoire de base du projet. S'il est passé à null, il sera remplacé
      *        par le répertoire "."
      * @throws ExecException : - Aucune tache par d‚faut
      *                         - Repertoire de base non existant ou n'est pas un r‚pertoire
      *                         - Erreur de parsining du projet
      */
	public void init(String buildFile, String baseDir) throws ArcadAntException {
		// Create a new project, and perform some default initialization
        //project = new Project();
        try { 
        	project.init(); 
        	
        } catch (BuildException e) { 
        	throw new ArcadAntException(e); 
        }

        // Set the base directory. If none is given, "." is used.
        if (baseDir == null)
        	baseDir=new String("."); //$NON-NLS-1$
        try { 
        	project.setBasedir(baseDir); 
        } catch (BuildException e) {
        	throw new ArcadAntException(e);
        }
        // Parse the given buildfile. If none is given, "build.xml" is used.
        if (buildFile == null) 
        	buildFile=new String("build.xml"); //$NON-NLS-1$
        try {
        	project.setProperty("ant.file",buildFile); //$NON-NLS-1$
        	File buildf = new File(buildFile);
        	ProjectHelper helper = ProjectHelper.getProjectHelper();
        	helper.parse(project, buildf);         	
        }  catch (BuildException e) {
        	throw new ArcadAntException(e); 
        }  
	}


	 /**
	  * Méthode permettant de fixer les propriétés du projet
	  * May be called to set project-wide properties, or just before a target call to \
	            set target-related properties only.
	  * @param properties A map containing the properties' name/value couples
	  * @param overridable If set, the provided properties values may be overriden \
	            by the config file's values
	  * @throws Exception Exceptions are self-explanatory (read their Message)
	  */
	public void setProperties(Map properties, boolean overridable) throws ArcadAntException  {
		// Test if the project exists
		if (project == null) 
			return;
	
	    // Property hashmap is null
	    if (properties == null) 
	    	return;
	
	     // Loop through the property map
	     Set propertyNames = properties.keySet();
	     Iterator iter = propertyNames.iterator();
	     while (iter.hasNext()) {
	         // Get the property's name and value
	         String propertyName =  (String) iter.next();
	         String propertyValue = (String) properties.get(propertyName);
	         if (propertyValue == null) continue;
	
	         // Set the properties
	         if (overridable) 
	        	 project.setProperty(propertyName, propertyValue);
	         else 
	        	 project.setUserProperty(propertyName, propertyValue);
	     }
	 }



	/**
	* Runs the given Target.
	* @param _target The name of the target to run. If null, the project's default \
	          target will be used.
	* @throws Exception Exceptions are self-explanatory (read their Message)
	*/
	public void runTarget(String target) throws ArcadAntException {
	     // Test if the project exists
		if (project == null) 
			return;
		// If no target is specified, run the default one.
		if (target == null) 
			target = project.getDefaultTarget();
	
		// Run the target
	    try { 
	    	if ((target==null) || target.equals("")) //$NON-NLS-1$
	    		project.executeTarget(project.getDefaultTarget());
	    	else
	    		project.executeTarget(target);  
	    	
	    } catch (BuildException e) {
	    	throw new ArcadAntException(e); }
	    }


	public Project getProject() {
		return project;
	}
	
	public void getReturnProperties(Properties props) {
	    // Loop through the property map
	    Set systemPropertyNames = props.keySet();
	    Iterator iter = systemPropertyNames.iterator();
	    while (iter.hasNext()) {
	        String propertyName =  (String) iter.next();
	        Object o = project.getProperties().get(propertyName); 
	        if (o !=null) {
	        	props.setProperty(propertyName,(String)o);
	        }
	    }				
	}
}
