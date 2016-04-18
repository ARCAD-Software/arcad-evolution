/*
 * Créé le 25 mai 2004
 * Projet : ARCAD Plugin Core UI
 * <i> Copyright 2004, Arcad-Software.</i>
 *  
 */
package com.arcadsoftware.aev.core.ui.composite;

import java.math.BigDecimal;

import org.eclipse.swt.SWT;
import org.eclipse.swt.SWTError;
import org.eclipse.swt.events.DisposeEvent;
import org.eclipse.swt.events.DisposeListener;
import org.eclipse.swt.events.SelectionListener;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.Font;
import org.eclipse.swt.graphics.GC;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Event;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Listener;
import org.eclipse.swt.widgets.TypedListener;

import com.arcadsoftware.aev.core.tools.StringTools;

/**
 * @author mlafon
 * @version 1.0.0
 * 
 *          Spinner de double (avec précision définie et saisie interdite).
 * 
 */
public class DoubleSpinner extends Composite {

	static final int BUTTON_WIDTH = 16;
	private Label text;
	private Button up, down, doubleUp, doubleDown;
	private int minimum, maximum;
	private int decimal;
	Font font;
	private boolean roll;
	private String postFix;

	/**
	 * @param parent
	 * @param style
	 */
	public DoubleSpinner(Composite parent, int style) {
		super(parent, style);
		text = new Label(this, style | SWT.BORDER);
		up = new Button(this, style | SWT.ARROW | SWT.UP);
		down = new Button(this, style | SWT.ARROW | SWT.DOWN);
		doubleUp = new Button(this, style | SWT.ARROW | SWT.UP);
		doubleDown = new Button(this, style | SWT.ARROW | SWT.DOWN);

		up.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				up();
			}
		});

		doubleUp.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				doubleUp();
			}
		});

		down.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				down();
			}
		});

		doubleDown.addListener(SWT.Selection, new Listener() {
			public void handleEvent(Event e) {
				doubleDown();
			}
		});

		addListener(SWT.Resize, new Listener() {
			public void handleEvent(Event e) {
				resize();
			}
		});

		Color oldBackground = getDisplay().getSystemColor(SWT.COLOR_WHITE);
		font = new Font(getDisplay(), text.getFont().getFontData()[0].getName(), text.getFont().getFontData()[0]
				.getHeight() + 2, text.getFont().getFontData()[0].getStyle());
		text.setFont(font);
		text.setBackground(oldBackground);

		minimum = 0;
		maximum = 1000;
		decimal = 1;
		postFix = StringTools.EMPTY;
		roll = false;
		setSelection(minimum);

		addDisposeListener(new DisposeListener() {
			public void widgetDisposed(DisposeEvent event) {
				font.dispose();
			}
		});
	}

	void up() {
		// FM 2006/226 FA 2006/258
		BigDecimal bd = new BigDecimal(getSelection() + Math.pow(10, -decimal));
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		setSelection(bd.doubleValue());
		notifyListeners(SWT.Selection, new Event());
	}

	void down() {
		// FM 2006/226 FA 2006/258
		BigDecimal bd = new BigDecimal(getSelection() - Math.pow(10, -decimal));
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		setSelection(bd.doubleValue());
		notifyListeners(SWT.Selection, new Event());
	}

	void doubleUp() {
		// FM 2006/226 FA 2006/258
		BigDecimal bd = new BigDecimal(getSelection() + 1);
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		setSelection(bd.doubleValue());
		notifyListeners(SWT.Selection, new Event());
	}

	void doubleDown() {
		// FM 2006/226 FA 2006/258
		BigDecimal bd = new BigDecimal(getSelection() - 1);
		bd = bd.setScale(decimal, BigDecimal.ROUND_HALF_UP);
		setSelection(bd.doubleValue());
		notifyListeners(SWT.Selection, new Event());
	}

	void resize() {
		Point pt = computeSize(SWT.DEFAULT, SWT.DEFAULT);
		int textWidth = pt.x - (BUTTON_WIDTH * 2);
		int buttonHeight = pt.y / 2;
		text.setBounds(0, 0, textWidth, pt.y);
		doubleUp.setBounds(textWidth, 0, BUTTON_WIDTH - 1, buttonHeight);
		doubleDown.setBounds(textWidth, pt.y - buttonHeight, BUTTON_WIDTH - 1, buttonHeight);
		up.setBounds(textWidth + BUTTON_WIDTH - 1, 0, BUTTON_WIDTH + 1, buttonHeight);
		down.setBounds(textWidth + BUTTON_WIDTH - 1, pt.y - buttonHeight, BUTTON_WIDTH + 1, buttonHeight);
	}

	@Override
	public Point computeSize(int wHint, int hHint, boolean changed) {
		GC gc = new GC(text);
		Point textExtent = gc.textExtent(String.valueOf(maximum + decimal).concat(postFix));
		gc.dispose();
		Point pt = text.computeSize(textExtent.x, textExtent.y);
		int width = pt.x + (BUTTON_WIDTH * 2) + 10;
		int height = pt.y;
		if (wHint != SWT.DEFAULT)
			width = wHint;
		if (hHint != SWT.DEFAULT)
			height = hHint;
		return new Point(width, height);
	}

	public void addSelectionListener(SelectionListener listener) {
		if (listener == null)
			throw new SWTError(SWT.ERROR_NULL_ARGUMENT);
		addListener(SWT.Selection, new TypedListener(listener));
	}

	/**
	 * @return boolean
	 */
	public boolean isRoll() {
		return roll;
	}

	/**
	 * @param b
	 */
	public void setRoll(boolean b) {
		roll = b;
	}

	public void setSelection(double selection) {
		double sel = selection;
		if (sel < minimum) {
			if (roll) {
				sel = maximum;
			} else {
				sel = minimum;
			}
		} else if (sel > maximum) {
			if (roll) {
				sel = minimum;
			} else {
				sel = maximum;
			}
		}
		text.setText(Double.toString(sel) + postFix);
	}

	public double getSelection() {
		int i = text.getText().indexOf(postFix);
		String s;
		if (i > -1) {
			s = text.getText().substring(0, i);
		} else {
			s = text.getText();
		}
		return Double.parseDouble(s);
	}

	public void setMaximum(int maximum) {
		checkWidget();
		this.maximum = maximum;
		resize();
	}

	public int getMaximum() {
		return maximum;
	}

	public void setMinimum(int minimum) {
		this.minimum = minimum;
	}

	public int getMinimum() {
		return minimum;
	}

	/**
	 * @return int
	 */
	public int getDecimal() {
		return decimal;
	}

	/**
	 * @param i
	 */
	public void setDecimal(int i) {
		if (i > 0)
			decimal = i;
	}

	/**
	 * @return String
	 */
	public String getPostFix() {
		return postFix;
	}

	/**
	 * @param string
	 */
	public void setPostFix(String string) {
		checkWidget();
		int i = text.getText().indexOf(postFix);
		String s;
		if (i > -1) {
			s = text.getText().substring(0, i);
		} else {
			s = text.getText();
		}
		postFix = string == null ? StringTools.EMPTY : string;
		text.setText(s + postFix);
		resize();
	}

}
