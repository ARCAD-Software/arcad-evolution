/*
 * Créé le 14 mai 2004
 * Projet : ARCAD Plugin Core UI
 * 
 * 
 * <i> Copyright 2004, Arcad-Software.</i>
 */
package com.arcadsoftware.aev.core.ui.composite;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTException;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author mlafon
 * @version 1.0.0
 * 
 *          Classe abstraite permetant de définir des éditeurs associées à une
 *          dialog box.
 */
public abstract class ButtonEdit extends Composite {

	private Label label;
	private Text text;
	private Button button;

	/**
	 * @param parent
	 * @param style
	 */
	private ButtonEdit(Composite parent, int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	public ButtonEdit(Composite parent, boolean editable, boolean password) {
		this(parent, SWT.NONE);
		label = new Label(this, SWT.LEFT);
		label.setText(StringTools.EMPTY);
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_FILL));
		if (editable) {
			text = new Text(this, SWT.SINGLE | SWT.BORDER);
			text.addModifyListener(new ModifyListener() {
				public void modifyText(ModifyEvent e) {
					sendModifyEvent(e);
				}
			});
		} else {
			text = new Text(this, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		}
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		if (password)
			text.setEchoChar(GuiFormatTools.PWDChar);
		button = new Button(this, SWT.PUSH);
		button.setText(getButtonLabel());
		button.setLayoutData(new GridData());
		button.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event event) {
				String newText = openDialog(getText());
				if ((newText != null) && !newText.equals(getText())) {
					setText(newText);
				}
			}
		});
	}

	protected final void sendModifyEvent(ModifyEvent e) {
//		if (isListening(SWT.Modify)) {
//			Event event = new Event();
//			event.type = SWT.Modify;
//			event.display = getDisplay();
//			event.widget = this;
//			if (e != null)
//				event.time = e.time;
//			notifyListeners(SWT.Modify, event);
//		}
	}

	/**
	 * @return le label du bouton appelant la dialog.
	 */
	protected String getButtonLabel() {
		return CoreUILabels.resString("ButtonEdit.OpenDialogButtonLabel"); //$NON-NLS-1$
	}

	public String getLabel() {
		return label.getText();
	}

	public void setLabel(String caption) {
		label.setText(caption);
	}

	public String getText() {
		return text.getText();
	}

	/**
	 * Modifie le texte en générant un évènement de modification.
	 * 
	 * @param newText
	 */
	public void setText(String newText) {
		text.setText(newText);
		sendModifyEvent(null);
	}

	public void initText(String newText) {
		text.setText(newText);
	}

	public void resizeText(String model) {
		GC gc = new GC(text);
		Point textExtent = gc.textExtent(model);
		gc.dispose();
		Point pt = text.computeSize(textExtent.x, textExtent.y);
		text.setSize(pt.x + 10, pt.y);
		text.redraw();
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified
	 * when the receiver's text is modified, by sending it one of the messages
	 * defined in the <code>ModifyListener</code> interface.
	 * 
	 * @param listener
	 *            the listener which should be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #removeModifyListener
	 */
	public void addModifyListener(ModifyListener listener) {
		checkWidget();
		if (listener == null)
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	/**
	 * Removes the listener from the collection of listeners who will be
	 * notified when the receiver's text is modified.
	 * 
	 * @param listener
	 *            the listener which should no longer be notified
	 * 
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been
	 *                disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the
	 *                thread that created the receiver</li>
	 *                </ul>
	 * 
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(ModifyListener listener) {
//		checkWidget();
//		if (listener == null)
//			SWT.error(SWT.ERROR_NULL_ARGUMENT);
//		removeListener(SWT.Modify, listener);
	}

	/**
	 * Appelle la dialogBox pour modifier le texte.
	 * 
	 * @param defaultText
	 *            ancienne valeur du texte
	 * @return nouvelle valeur du texte
	 */
	protected abstract String openDialog(String defaultText);

	/**
	 * @return Button
	 */
	public Button getButton() {
		return button;
	}

}
