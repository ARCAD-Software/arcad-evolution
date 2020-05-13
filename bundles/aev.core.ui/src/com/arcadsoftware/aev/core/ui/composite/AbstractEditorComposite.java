package com.arcadsoftware.aev.core.ui.composite;

import java.util.ArrayList;

import org.eclipse.swt.SWT;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;

import com.arcadsoftware.aev.core.model.ArcadListenerList;
import com.arcadsoftware.aev.core.ui.editors.AbstractArcadEditorPart;
import com.arcadsoftware.aev.core.ui.listeners.IDirtyListener;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;


public abstract class AbstractEditorComposite extends AbstractArcadComposite 
implements ModifyListener,SelectionListener {

	
	Object edited;
	boolean dirty = false;
	private AbstractArcadEditorPart parentEditorPart;


	private class DirtyListener  {
		private ArcadListenerList contentChangedListeners = new ArcadListenerList(3);
		/**
		 * 
		 */
		public DirtyListener() {
			super();
		}
		
		public void fireContentChanged() {		
			Object[] listeners = contentChangedListeners.getListeners();
			for (int i = 0; i < listeners.length; ++i) {
				final IDirtyListener l = (IDirtyListener)listeners[i];
				try {
					l.dirtyEvent(dirty);
				} catch (RuntimeException e1) {
					removeDirtyListener(l);
				}				
			}	
		}		
		
		public void addDirtyListener(IDirtyListener listener) {
			contentChangedListeners.add(listener);
		}
		
		/* (non-Javadoc)
		 * Method declared on ISelectionProvider.
		 */
		public void removeDirtyListener(IDirtyListener listener) {
			contentChangedListeners.remove(listener);
		}			
	}
	DirtyListener l = new DirtyListener(); 

	
	ArrayList<AbstractEditorComposite> childs = 
		new ArrayList<AbstractEditorComposite>();
	
	
	public AbstractEditorComposite(Composite parent, int style,Object edited,boolean withinit,
			AbstractArcadEditorPart editor) {
		super(parent, style);
		this.edited = edited;
		this.parentEditorPart = editor;
		format();
		if (withinit)
			createContent();		
	}	
	
	public AbstractArcadEditorPart getParentEditorPart(){
		return parentEditorPart;
	}
	
	public AbstractEditorComposite(Composite parent, int style,Object edited,AbstractArcadEditorPart editor) {
		this(parent, style,edited,true,editor);
	}

	public void format(){
		GridLayout gridLayout =new GridLayout(1,false); 
		gridLayout.marginWidth=0;		
		gridLayout.marginHeight=0;
		gridLayout.verticalSpacing=0;
		this.setLayout(gridLayout);
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace=true;
		gridData.grabExcessVerticalSpace=true;
		this.setLayoutData(gridData);		
	}		
	
	
	
	public Object getEdited() {
		return edited;
	}
	public ArrayList<AbstractEditorComposite> getChilds() {
		return childs;
	}

	
	public void widgetDefaultSelected(SelectionEvent arg0) {
	}	
	
	private AbstractEditorComposite getAncestor(AbstractEditorComposite c){
		AbstractEditorComposite result = c;
		Composite composite = (Composite)c;
		while(composite.getParent()!=null) {
			if (composite.getParent() instanceof AbstractEditorComposite) {
				result = (AbstractEditorComposite)composite.getParent(); 
			}
			composite=composite.getParent();
		}
		return result; 
	}
	
	
	public void registerControl(Control c) {		
		AbstractEditorComposite registry = getAncestor(this);
		if (registry!=null) {
			if (c instanceof Text)
				((Text)c).addModifyListener(registry);
			if (c instanceof Button) {
				((Button )c).addSelectionListener(registry);
			}
			if (c instanceof Combo) {
				((Combo )c).addSelectionListener(registry);
				((Combo )c).addModifyListener(registry);
			}
			if (c instanceof DoubleSpinner) {
				((DoubleSpinner )c).addSelectionListener(registry);
			}			
		}

	}

	public void addChangeListener(IDirtyListener listener) {
		l.addDirtyListener(listener);		
	}
	
	public void removeChangeListener(IDirtyListener listener) {
		l.removeDirtyListener(listener);		
	}
	
	public void modifyText(ModifyEvent arg0) {
		dirty = true;
		l.fireContentChanged();
	}
	public void widgetSelected(SelectionEvent arg0) {
		dirty = true;
		l.fireContentChanged();
	}	
	public boolean isDirty() {
		return dirty;
	}

	public void fireDirty(){
		dirty = true;
		l.fireContentChanged();
	}
	
	public void setDirty(boolean dirty) {
		this.dirty = dirty;
	}	
	
	protected void toScreen(Object edited){
		
	}
	protected void fromScreen(Object edited){
		
	}
	protected boolean checkData(Object edited){
		return true;
	}	
	
	private void recursiveToScreen(AbstractEditorComposite c,Object edited){
		ArrayList<AbstractEditorComposite> kids = c.getChilds();
		for (int i=0;i<kids.size();i++){
			kids.get(i).toScreen(edited);
			recursiveToScreen(kids.get(i),edited);
		}			
	}
	private void recursiveFromScreen(AbstractEditorComposite c,Object edited){
		ArrayList<AbstractEditorComposite> kids = c.getChilds();
		for (int i=0;i<kids.size();i++){
			kids.get(i).fromScreen(edited);
			recursiveFromScreen(kids.get(i),edited);
		}			
	}
	private boolean recursiveCheckData(AbstractEditorComposite c,Object edited){
		ArrayList<AbstractEditorComposite> kids = c.getChilds();
		for (int i=0;i<kids.size();i++){
			if (kids.get(i).checkData(edited)) 
				recursiveFromScreen(kids.get(i),edited);
			else
				return false;
		}			
		return c.checkData(edited);
	}	
	
	public void editedToScreen(Object edited){
		recursiveToScreen(this,edited);		
		this.toScreen(edited);
	}
	public void editedFromScreen(Object edited){
		recursiveFromScreen(this,edited);
		this.fromScreen(edited);		
	}
	
	public boolean editedCheckData(Object edited){
		return recursiveCheckData(this,edited);
	}		

	public abstract void createContent();
	
	
	protected Label createInformationLabel(Composite parent) {
		Composite userInfoBar = new Composite(parent,SWT.BORDER);
		GridLayout l = new GridLayout(1,true); 
		l.marginHeight = l.marginWidth = l.marginBottom = l.marginTop= 1;
		l.marginLeft = 5;
		userInfoBar.setLayout(l);
		GridData gridData = new GridData(GridData.FILL_HORIZONTAL);
		gridData.grabExcessHorizontalSpace = true;
		gridData.heightHint = 18;
		userInfoBar.setLayoutData(gridData);
		Label infoLabel = GuiFormatTools.createLabel(userInfoBar,""); //$NON-NLS-1$
		return infoLabel;
	}
	

	

	
}
