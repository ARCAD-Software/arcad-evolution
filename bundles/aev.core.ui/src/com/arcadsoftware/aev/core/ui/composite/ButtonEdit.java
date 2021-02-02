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
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.TypedListener;

import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.tools.CoreUILabels;
import com.arcadsoftware.aev.core.ui.tools.GuiFormatTools;

/**
 * @author mlafon
 * @version 1.0.0 Classe abstraite permetant de définir des éditeurs associées à une dialog box.
 */
public abstract class ButtonEdit extends Composite {

	private Button button;
	private Label label;
	private Text text;

	public ButtonEdit(final Composite parent, final boolean editable, final boolean password) {
		this(parent, SWT.NONE);
		label = new Label(this, SWT.LEFT);
		label.setText(StringTools.EMPTY);
		label.setLayoutData(new GridData(GridData.VERTICAL_ALIGN_CENTER | GridData.HORIZONTAL_ALIGN_FILL));
		if (editable) {
			text = new Text(this, SWT.SINGLE | SWT.BORDER);
			text.addModifyListener(e -> sendModifyEvent(e));
		} else {
			text = new Text(this, SWT.SINGLE | SWT.BORDER | SWT.READ_ONLY);
		}
		text.setLayoutData(new GridData(GridData.FILL_HORIZONTAL | GridData.VERTICAL_ALIGN_CENTER));
		if (password) {
			text.setEchoChar(GuiFormatTools.PWDChar);
		}
		button = new Button(this, SWT.PUSH);
		button.setText(getButtonLabel());
		button.setLayoutData(new GridData());
		button.addListener(SWT.Selection, event -> {
			final String newText = openDialog(getText());
			if (newText != null && !newText.equals(getText())) {
				setText(newText);
			}
		});
	}

	/**
	 * @param parent
	 * @param style
	 */
	private ButtonEdit(final Composite parent, final int style) {
		super(parent, style);
		setLayout(new GridLayout(3, false));
		setLayoutData(new GridData(GridData.FILL_BOTH));
	}

	/**
	 * Adds the listener to the collection of listeners who will be notified when the receiver's text is modified, by
	 * sending it one of the messages defined in the <code>ModifyListener</code> interface.
	 *
	 * @param listener
	 *            the listener which should be notified
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * @see ModifyListener
	 * @see #removeModifyListener
	 */
	public void addModifyListener(final ModifyListener listener) {
		checkWidget();
		if (listener == null) {
			SWT.error(SWT.ERROR_NULL_ARGUMENT);
		}
		final TypedListener typedListener = new TypedListener(listener);
		addListener(SWT.Modify, typedListener);
	}

	/**
	 * @return Button
	 */
	public Button getButton() {
		return button;
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

	public String getText() {
		return text.getText();
	}

	public void initText(final String newText) {
		text.setText(newText);
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
	 * Removes the listener from the collection of listeners who will be notified when the receiver's text is modified.
	 *
	 * @param listener
	 *            the listener which should no longer be notified
	 * @exception IllegalArgumentException
	 *                <ul>
	 *                <li>ERROR_NULL_ARGUMENT - if the listener is null</li>
	 *                </ul>
	 * @exception SWTException
	 *                <ul>
	 *                <li>ERROR_WIDGET_DISPOSED - if the receiver has been disposed</li>
	 *                <li>ERROR_THREAD_INVALID_ACCESS - if not called from the thread that created the receiver</li>
	 *                </ul>
	 * @see ModifyListener
	 * @see #addModifyListener
	 */
	public void removeModifyListener(final ModifyListener listener) {
		// checkWidget();
		// if (listener == null)
		// SWT.error(SWT.ERROR_NULL_ARGUMENT);
		// removeListener(SWT.Modify, listener);
	}

	public void resizeText(final String model) {
		final GC gc = new GC(text);
		final Point textExtent = gc.textExtent(model);
		gc.dispose();
		final Point pt = text.computeSize(textExtent.x, textExtent.y);
		text.setSize(pt.x + 10, pt.y);
		text.redraw();
	}

	protected final void sendModifyEvent(final ModifyEvent e) {
		// if (isListening(SWT.Modify)) {
		// Event event = new Event();
		// event.type = SWT.Modify;
		// event.display = getDisplay();
		// event.widget = this;
		// if (e != null)
		// event.time = e.time;
		// notifyListeners(SWT.Modify, event);
		// }
	}

	public void setLabel(final String caption) {
		label.setText(caption);
	}

	/**
	 * Modifie le texte en générant un évènement de modification.
	 *
	 * @param newText
	 */
	public void setText(final String newText) {
		text.setText(newText);
		sendModifyEvent(null);
	}

}
