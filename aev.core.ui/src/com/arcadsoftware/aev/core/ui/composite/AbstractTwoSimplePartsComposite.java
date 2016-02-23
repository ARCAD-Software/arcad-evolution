package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.SashForm;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Composite;



import com.arcadsoftware.aev.core.ui.editors.AbstractArcadEditorPart;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

public abstract class AbstractTwoSimplePartsComposite extends AbstractEditorComposite {

	
	Composite leftComposite; 
	Composite rightComposite;
	
	
	public AbstractTwoSimplePartsComposite(Composite parent, int style, Object edited,
			AbstractArcadEditorPart editor) {
		super(parent, style,edited,editor);
	}	
	

	
	public AbstractTwoSimplePartsComposite(Composite parent, int style,
			Object edited, boolean withinit, AbstractArcadEditorPart editor) {
		super(parent, style, edited, withinit, editor);
	}



	private Composite createLeftPart(Composite parent) {
		Composite composite = 
			GuiFormatTools.createComposite(parent, 3, false, SWT.BORDER, true);
		GridLayout l = (GridLayout)composite.getLayout(); 
		l.marginHeight = l.marginWidth = 2;
		l.marginHeight = l.marginWidth = l.marginBottom = l.marginTop= 2;		
		composite.layout();		
		createLeftPartComposite(composite);
		return composite;
	}
	
	private Composite createRightPart(Composite parent) {
		Composite composite = 
			GuiFormatTools.createComposite(parent, 3, false, SWT.BORDER, true);
		GridLayout l = (GridLayout)composite.getLayout(); 
		l.marginHeight = l.marginWidth = l.marginBottom = l.marginTop= 2;
		l.marginLeft = 3;
		composite.layout();		
		createRightPartComposite(composite);
		return composite;
	}	
	

	public void createContent() {
		SashForm listAndEditor = new SashForm(this, SWT.HORIZONTAL );
		
		GridData gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		listAndEditor.setLayoutData(gridData);			
		
//		
		leftComposite = createLeftPart(listAndEditor);	
		gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		leftComposite.setLayoutData(gridData);


		rightComposite = createRightPart(listAndEditor);

		gridData = new GridData(GridData.FILL_BOTH);
		gridData.grabExcessHorizontalSpace = true;
		gridData.grabExcessVerticalSpace = true;
		rightComposite.setLayoutData(gridData);

		listAndEditor.setWeights(new int[] { 40, 100 });
		
		
	}

	public abstract void createRightPartComposite(Composite parent);
	public abstract void createLeftPartComposite(Composite parent); 

		
	

}
