/*
 * Créé le 20 nov. 08
 *
 * TODO Pour changer le modèle de ce fichier généré, allez à :
 * Fenêtre - Préférences - Java - Style de code - Modèles de code
 */
package com.arcadsoftware.ae.core.ant;

import com.arcadsoftware.ae.core.exceptions.ArcadException;

public class ArcadAntException extends ArcadException {
		/**
		 * 
		 */
		private static final long serialVersionUID = 1L;
		private Exception cause;
	     
	     /**
	      * Creates a RexecJException object.
	      *
	      * @param message a String containing the message for this exception.
	      */
	     
	     public ArcadAntException (String message) {
	          super (message, new Exception (message));
	     }
	     

	     
	     /**
	      * Creates a RexecJException object.
	      *
	      * @param message a String containing the message for this exception.
	      * @param cause an Exception object that is the root cause of this
	      *        exception.
	      */
	     
	     public ArcadAntException (Exception cause) {
	          super (cause.getMessage());	          
	          this.cause = cause;
	     }	     
	     

}
