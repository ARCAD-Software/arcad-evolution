/*
 * Cr�� le 17 mars 05
 *
 * Pour changer le mod�le de ce fichier g�n�r�, allez � :
 * Fen�tre&gt;Pr�f�rences&gt;Java&gt;G�n�ration de code&gt;Code et commentaires
 */
package com.arcadsoftware.aev.core.ui.tools;

import java.net.MalformedURLException;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

import org.eclipse.jface.action.Action;
import org.eclipse.jface.action.ContributionItem;
import org.eclipse.jface.action.IAction;
import org.eclipse.jface.action.MenuManager;
import org.eclipse.jface.action.ToolBarManager;
import org.eclipse.jface.dialogs.MessageDialog;
import org.eclipse.jface.resource.ImageDescriptor;
import org.eclipse.jface.window.Window;
import org.eclipse.swt.SWT;
import org.eclipse.swt.custom.ScrolledComposite;
import org.eclipse.swt.events.ControlEvent;
import org.eclipse.swt.events.ControlListener;
import org.eclipse.swt.events.FocusEvent;
import org.eclipse.swt.events.FocusListener;
import org.eclipse.swt.events.ModifyEvent;
import org.eclipse.swt.events.ModifyListener;
import org.eclipse.swt.events.SelectionAdapter;
import org.eclipse.swt.events.SelectionEvent;
import org.eclipse.swt.graphics.Color;
import org.eclipse.swt.graphics.FontData;
import org.eclipse.swt.graphics.Image;
import org.eclipse.swt.graphics.ImageData;
import org.eclipse.swt.graphics.PaletteData;
import org.eclipse.swt.graphics.Point;
import org.eclipse.swt.graphics.RGB;
import org.eclipse.swt.graphics.Rectangle;
import org.eclipse.swt.layout.GridData;
import org.eclipse.swt.layout.GridLayout;
import org.eclipse.swt.widgets.Button;
import org.eclipse.swt.widgets.ColorDialog;
import org.eclipse.swt.widgets.Combo;
import org.eclipse.swt.widgets.Composite;
import org.eclipse.swt.widgets.Control;
import org.eclipse.swt.widgets.DateTime;
import org.eclipse.swt.widgets.Display;
import org.eclipse.swt.widgets.ExpandBar;
import org.eclipse.swt.widgets.ExpandItem;
import org.eclipse.swt.widgets.FontDialog;
import org.eclipse.swt.widgets.Group;
import org.eclipse.swt.widgets.Label;
import org.eclipse.swt.widgets.Layout;
import org.eclipse.swt.widgets.Link;
import org.eclipse.swt.widgets.List;
import org.eclipse.swt.widgets.Menu;
import org.eclipse.swt.widgets.MenuItem;
import org.eclipse.swt.widgets.Shell;
import org.eclipse.swt.widgets.Spinner;
import org.eclipse.swt.widgets.Table;
import org.eclipse.swt.widgets.Text;
import org.eclipse.swt.widgets.ToolBar;
import org.eclipse.swt.widgets.ToolItem;
import org.eclipse.ui.PartInitException;
import org.eclipse.ui.PlatformUI;
import org.eclipse.ui.forms.widgets.ExpandableComposite;
import org.eclipse.ui.forms.widgets.Hyperlink;
import org.eclipse.ui.forms.widgets.Section;

import com.arcadsoftware.aev.core.messages.MessageManager;
import com.arcadsoftware.aev.core.tools.StringTools;
import com.arcadsoftware.aev.core.ui.EvolutionCoreUIPlugin;
import com.arcadsoftware.aev.core.ui.actions.MenuAction;
import com.arcadsoftware.aev.core.ui.composite.DoubleSpinner;
import com.arcadsoftware.aev.core.ui.composite.Period;
import com.arcadsoftware.aev.core.ui.dialogs.gui.CalendarDialog;
import com.arcadsoftware.aev.core.ui.dialogs.gui.MultiValueDialog;
import com.arcadsoftware.aev.core.ui.listeners.SimpleDateVerifierListener;
import com.arcadsoftware.aev.core.ui.widgets.ArcadColorWidget;
import com.arcadsoftware.documentation.icons.Icon;

/**
 * @author MD
 */
public class GuiFormatTools {
	private static class AutoSearchComboListener implements ModifyListener, FocusListener {
		private final Combo combo;
		private String lastValue = StringTools.EMPTY;

		public AutoSearchComboListener(final Combo combo) {
			super();
			this.combo = combo;
		}

		@Override
		public void focusGained(final FocusEvent arg0) {
			// TODO Raccord de m�thode auto-g�n�r�

		}

		@Override
		public void focusLost(final FocusEvent arg0) {
			final String value = combo.getText();

			boolean valueOk = false;
			if (combo.getItemCount() > 1) {
				for (int i = 0; i < combo.getItemCount(); i++) {
					if (combo.getItem(i).equals(value)) {
						valueOk = true;
						break;
					}
				}
			}
			if (!valueOk) {
				combo.select(-1);
				combo.setText(StringTools.EMPTY);
			}
		}

		@Override
		public void modifyText(final ModifyEvent arg0) {
			final String value = combo.getText();

			if (!value.equalsIgnoreCase(StringTools.EMPTY) && combo.getItemCount() > 1) {
				for (int i = 0; i < combo.getItemCount(); i++) {
					if (combo.getItem(i).toUpperCase().indexOf(value.toUpperCase()) == 0) {
						if (lastValue.length() < value.length()) {
							combo.select(i);
							combo.setSelection(new Point(value.length(), combo.getItem(i).length()));
						}
						break;
					}
				}
			}
			lastValue = value;
		}
	}

	public class CalendarManager {
		private CalendarDialog dialog;
		private String format = null;
		private final SimpleDateFormat formatter;
		int style;
		private final Text text;

		public CalendarManager(final Text t, final int style) {
			text = t;
			this.style = style;
			switch (style) {
			case 0:
				dialog = new CalendarDialog(text.getShell(), true, false);
				format = CoreUILabels.resString("simpleDateFomat.Text");//$NON-NLS-1$
				break;
			case 1:
				dialog = new CalendarDialog(text.getShell(), false, true);
				format = CoreUILabels.resString("label.timeformat");//$NON-NLS-1$
				break;
			default:
				dialog = new CalendarDialog(text.getShell(), true, true);
				format = CoreUILabels.resString("CalendarEdit.Format_Date");//$NON-NLS-1$
				break;
			}
			formatter = new SimpleDateFormat(format);
		}

		/*
		 * (non-Javadoc)
		 * @see com.arcadsoftware.aev.core.ui.widgets.ButtonEdit#openDialog(java. lang.String)
		 */
		protected String choose(final String defaultText) {
			if (!defaultText.equals(StringTools.EMPTY)) {
				final SimpleDateFormat df = new SimpleDateFormat(format);
				try {
					final Date d = df.parse(defaultText);
					setDate(d);
				} catch (final ParseException e) {
					// Do nothing
				}
			} else {
				setDate(new Date());
			}
			if (dialog.open() == 0) {
				return formatter.format(dialog.getCalendar().getTime());
			}

			return null;
		}

		public Date getDate() {
			if (text.getText().equals(StringTools.EMPTY)) {
				return null;
			}
			try {
				return formatter.parse(text.getText());
			} catch (final ParseException e) {
				MessageManager.addException(e, MessageManager.LEVEL_DEVELOPMENT);
				return null;
			}
		}

		public String getFormat() {
			return format;
		}

		public void setDate(final Date date) {
			if (date != null) {
				dialog.getCalendar().setTime(date);
				// text.setText(formatter.format(date));
			}
		}

		public void setDateFromString(final String format, final String date) {
			if (date != null) {
				final Date dt = GuiFormatTools.getDateFromString(format, date);
				dialog.getCalendar().setTime(dt);
				// text.setText(formatter.format(dt));
			}
		}

	}

	public class DropdownMenuSelectionListener extends SelectionAdapter {
		private final ToolItem dropdown;
		private final Menu menu;

		public DropdownMenuSelectionListener(final ToolItem dropdown) {
			this.dropdown = dropdown;
			menu = new Menu(dropdown.getParent().getShell());
		}

		public void add(final Action action) {
			final MenuItem menuItem = new MenuItem(menu, SWT.CHECK);
			menuItem.setText(action.getText());
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent event) {
					action.setChecked(menuItem.getSelection());
					action.run();
				}
			});
			menuItem.setSelection(action.isChecked());
		}

		@Override
		public void widgetSelected(final SelectionEvent event) {
			if (event.detail == SWT.ARROW) {
				final ToolItem item = (ToolItem) event.widget;
				final Rectangle rect = item.getBounds();
				final Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
				menu.setLocation(pt.x, pt.y + rect.height);
				menu.setVisible(true);
			} else {
				System.out.println(dropdown.getText() + " Pressed");
			}
		}
	}

	public class MenuItemSelectionListener extends SelectionAdapter {
		private final Menu menu;

		public MenuItemSelectionListener(final Menu menu) {
			this.menu = menu;
		}

		public void add(final Action action) {
			final MenuItem menuItem = new MenuItem(menu, SWT.CHECK);
			menuItem.setText(action.getText());
			menuItem.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent event) {
					action.setChecked(menuItem.getSelection());
					action.run();
				}
			});
			menuItem.setSelection(action.isChecked());
		}

		@Override
		public void widgetSelected(final SelectionEvent event) {
			if (event.detail == SWT.ARROW) {
				final ToolItem item = (ToolItem) event.widget;
				final Rectangle rect = item.getBounds();
				final Point pt = item.getParent().toDisplay(new Point(rect.x, rect.y));
				menu.setLocation(pt.x, pt.y + rect.height);
				menu.setVisible(true);
			}
		}
	}

	private class MultiValueSelectionAdapter extends SelectionAdapter {
		private final String dialogLabel;
		private final Text text;

		public MultiValueSelectionAdapter(final Text text, final String dialogLabel) {
			super();
			this.text = text;
			this.dialogLabel = dialogLabel;
		}

		@Override
		public void widgetSelected(final SelectionEvent e) {
			// Selection du type d'action
			final MultiValueDialog dialog = new MultiValueDialog(EvolutionCoreUIPlugin.getShell(), dialogLabel);
			dialog.setValue(text.getText());
			if (dialog.open() == Window.OK) {
				final String val = dialog.getValue();
				text.setText(val);
			}
		}
	}

	private static String ASTERISK_MARK = EvolutionCoreUIPlugin.getResourceString("asterisk.mark.label");
	public static int BOTH = 2;

	private static String COLON_MARK = EvolutionCoreUIPlugin.getResourceString("colon.mark.label");
	private static Color COLOR_RED = new Color(Display.getCurrent(), 255, 0, 0);
	public static int DATE_ONLY = 0;

	private static GuiFormatTools instance = new GuiFormatTools();

	static String NUMCHAR = "0123456789-"; //$NON-NLS-1$

	static String NUMCHARPOSITIVE = "0123456789"; //$NON-NLS-1$

	/**
	 * Caract�re utilis� pour les champs de saisie prot�g� (saisie des mots de passes).
	 */
	public static final char PWDChar = '*';

	public static int TIME_ONLY = 1;

	public static String TIME_PART_HOUR = "hour";

	public static String TIME_PART_MINUTE = "minute";

	public static String TIME_PART_SECOND = "second";

	public static void addNumericLimiter(final Text text, final Integer min, final Integer max) {
		text.addListener(SWT.Modify, e -> {
			boolean valid = true;
			final String value = ((Text) e.widget).getText();
			if (value.equalsIgnoreCase("-")) {
				return;
			}

			try {
				final int v = Integer.valueOf(value);
				if (min == null && max != null) {
					valid = v <= max;
				} else if (min != null && max == null) {
					valid = v >= min;
				} else if (min != null && max != null) {
					valid = min.intValue() <= v && v <= max.intValue();
				}
				if (!valid) {
					e.doit = false;
					final String lgv = (String) text.getData("last_good_value");
					text.setText(lgv == null ? "" : lgv);
					return;
				} else {
					text.setData("last_good_value", value);
				}

			} catch (final NumberFormatException e1) {
				e.doit = false;
				return;
			}
		});
	}

	public static void addNumericVerifier(final Text text) {
		text.addListener(SWT.Verify, e -> {
			if (e.keyCode == 8 || e.keyCode == 127) {
				return;
			}
			final String string = e.text;

			final char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (int i = 0; i < chars.length; i++) {
				if (!('0' <= chars[i] && chars[i] <= '9')) {
					if (chars[i] != '-') {
						e.doit = false;
						return;
					}
				}
			}
		});
	}

	public static void allLayout(final Composite c) {
		if (c != null) {
			c.layout();
			allLayout(c.getParent());
		}
	}

	public static String choosefile(final String title, final String[] fileExtensions) {
		final String s = getFileManagerProvider().selectFile(
				EvolutionCoreUIPlugin.getShell(), 0, title, fileExtensions);
		//
		// FileDialog chooser = new FileDialog(EvolutionCoreUIPlugin.getShell());
		// chooser.setFilterExtensions(fileExtensions);
		// chooser.setText(title);
		// String s = chooser.open();
		if (s != null && !s.equals(StringTools.EMPTY)) {
			return s;
		}
		return StringTools.EMPTY;
	}

	public static Button createButton(final Composite parent, final String buttonLabel, final int style) {
		return createButton(parent, buttonLabel, style, 3);
	}

	public static Button createButton(final Composite parent, final String buttonLabel, final int style,
			final int horizontalSpan) {
		return createButton(parent, buttonLabel, style, horizontalSpan, -1);
	}

	public static Button createButton(final Composite parent, final String buttonLabel, final int style,
			final int horizontalSpan, final int size) {
		final Button b = new Button(parent, SWT.PUSH);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(style);
			gridData.horizontalSpan = horizontalSpan;
			if (size != -1) {
				gridData.widthHint = size;
			}
			b.setLayoutData(gridData);
		}
		b.setText(buttonLabel);
		return b;
	}

	public static Button createCheckButton(final Composite parent, final String buttonLabel, final int style) {
		return createCheckButton(parent, buttonLabel, style, 3);
	}

	public static Button createCheckButton(final Composite parent, final String buttonLabel, final int style,
			final int horizontalSpan) {
		final Button b = new Button(parent, SWT.CHECK);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(style);
			gridData.horizontalSpan = horizontalSpan;
			b.setLayoutData(gridData);
		}
		b.setText(buttonLabel);
		return b;
	}

	public static Composite createComposite(final Composite parent) {
		return createComposite(parent, 3, false, 1);
	}

	public static Composite createComposite(final Composite parent, final int numberOfColumn,
			final boolean equalWidth) {
		return createComposite(parent, numberOfColumn, equalWidth, 1);
	}

	public static Composite createComposite(final Composite parent, final int numberOfColumn, final boolean equalWidth,
			final int horizontalSpan) {

		return createComposite(parent, numberOfColumn, equalWidth, horizontalSpan, SWT.NONE);
	}

	public static Composite createComposite(final Composite parent, final int numberOfColumn, final boolean equalWidth,
			final int style,
			final boolean grabExcess) {
		int columnNumber = numberOfColumn;
		if (numberOfColumn < 1) {
			columnNumber = 1;
		}
		final Composite composite = new Composite(parent, style);
		composite.setLayout(new GridLayout(columnNumber, equalWidth));
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL_BOTH);
			if (grabExcess) {
				gridData.grabExcessHorizontalSpace = true;
				gridData.grabExcessVerticalSpace = true;
			}
			composite.setLayoutData(gridData);
		}
		return composite;
	}

	public static Composite createComposite(final Composite parent, final int numberOfColumn, final boolean equalWidth,
			final int horizontalSpan, final int style) {
		int newHorizontalSpan = horizontalSpan;
		int columnNumber = numberOfColumn;
		if (numberOfColumn < 1) {
			columnNumber = 1;
		}
		if (horizontalSpan < 1) {
			newHorizontalSpan = 1;
		}
		final Composite composite = new Composite(parent, style);
		composite.setLayout(new GridLayout(columnNumber, equalWidth));
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalSpan = newHorizontalSpan;
			composite.setLayoutData(gridData);
		}
		return composite;
	}

	public static Label createDescription(final Composite parent, final String description) {
		new Label(parent, SWT.NONE);
		new Label(parent, SWT.NONE);
		final Label lbl = new Label(parent, SWT.NONE | SWT.WRAP);
		lbl.setText(description);
		return lbl;
	}

	public static ExpandBar createExpandBar(final Composite parent, final int style) {
		final ExpandBar bar = new ExpandBar(parent, style);
		bar.setLayout(new GridLayout());
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL_BOTH);
			gridData.grabExcessHorizontalSpace = true;
			gridData.grabExcessVerticalSpace = true;
			gridData.horizontalSpan = 3;
			bar.setLayoutData(gridData);
		}
		return bar;
	}

	public static ExpandItem createExpandBarItem(final ExpandBar bar, final Composite composite, final String label,
			final int position,
			final boolean setheight) {
		final ExpandItem item = new ExpandItem(bar, SWT.NONE, position);
		item.setText(label);
		if (setheight) {
			item.setHeight(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT).y);
		}
		item.setControl(composite);
		return item;
	}

	/**
	 * M�thode permettant l'association d'un gridData au control pass� en param�tre.
	 *
	 * @param c
	 *            Control : Control � affecter.
	 */
	public static void createGridData(final Control c) {
		createGridData(c, -1, -1);
	}

	/**
	 * M�thode permettant l'association d'un gridData au control pass� en param�tre.
	 *
	 * @param c
	 *            Control : Control � affecter.
	 * @param height
	 *            int : hauteur
	 * @param width
	 *            int : largeur
	 */
	public static void createGridData(final Control c, final int height, final int width) {
		if (c.getParent().getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData();
			if (height != -1) {
				gridData.heightHint = height;
			} else {
				gridData.verticalAlignment = GridData.FILL;
				gridData.grabExcessVerticalSpace = true;
			}
			if (width != -1) {
				gridData.widthHint = width;
			} else {
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessHorizontalSpace = true;
			}
			c.setLayoutData(gridData);
		}
	}

	/**
	 * M�thode permettant l'association d'un layout au layout pass� en param�tre.
	 *
	 * @param c
	 *            Composite : composite;
	 * @param nbCol
	 *            int : Nombre de colonne.
	 */
	public static void createGridLayout(final Composite c, final int nbCol) {
		createGridLayout(c, nbCol, false);
	}

	/**
	 * M�thode permettant l'association d'un layout au layout pass� en param�tre.
	 *
	 * @param c
	 *            Composite : composite;
	 * @param nbCol
	 *            int : Nombre de colonne.
	 */
	public static void createGridLayout(final Composite c, final int nbCol, final boolean equalWidth) {
		final GridLayout gridLayout = new GridLayout(nbCol, equalWidth);
		gridLayout.marginHeight = 2;
		gridLayout.marginWidth = 2;
		c.setLayout(gridLayout);
	}

	public static Group createGroup(final Composite parent, final String label) {
		return createGroup(parent, label, 3);
	}

	public static Group createGroup(final Composite parent, final String label, final int nbrColumns) {
		final Group g = new Group(parent, SWT.NONE);
		g.setLayout(new GridLayout(nbrColumns, false));
		g.setText(label);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = 3;
			g.setLayoutData(gridData);
		}
		return g;
	}

	public static Hyperlink createHyperlinkWithLabel(final Composite parent, final String label,
			final Color foregroundColor,
			final String text, final int style) {
		final Hyperlink link = new Hyperlink(parent, style);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			link.setLayoutData(gridData);
		}
		link.setText(text);
		link.setForeground(foregroundColor);

		final Label twopoints = new Label(parent, SWT.NONE);
		twopoints.setText(":"); //$NON-NLS-1$
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			twopoints.setLayoutData(gridData);
		}
		final Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			textLabel.setLayoutData(gridData);
		}
		textLabel.setText(label);

		return link;
	}

	/**
	 * Create a Form Entry component
	 * 
	 * @param parent
	 * @param prop
	 * @return
	 */
	public static Text createItemFormComposite(final Composite parent, final String supTitle, final String itemLabel,
			final String errorMessage, final String key, final String value, final String defaultValue,
			final String helper) {
		final Composite cmp = parent;

		// Layout
		Layout parentLayout = parent.getLayout();
		if (!(parentLayout instanceof GridLayout)) {
			parentLayout = new GridLayout(4, false);
			parent.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 1));
		}
		if (((GridLayout) parentLayout).numColumns != 4) {
			((GridLayout) parentLayout).numColumns = 4;
			((GridLayout) parentLayout).makeColumnsEqualWidth = false;
			parent.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, true, 3, 1));

		}
		parent.setLayout(parentLayout);

		// Content
		Label label = null;

		// Title
		if (supTitle != null) {
			label = new Label(cmp, SWT.NONE);
			label.setText(supTitle);
			label.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, false, 4, 1));
		}

		// Variable Name
		label = new Label(cmp, SWT.NONE);
		label.setText(itemLabel);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, true));

		label = new Label(cmp, SWT.NONE);
		label.setText(COLON_MARK);
		label.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, true));

		final Label warn = new Label(cmp, SWT.NONE);
		warn.setText(ASTERISK_MARK);
		warn.setForeground(COLOR_RED);
		warn.setLayoutData(new GridData(SWT.BEGINNING, SWT.CENTER, false, true));
		if (errorMessage != null) {
			warn.setToolTipText(errorMessage);
		} else {
			warn.setVisible(false);
		}

		// Complete line and start new line
		if (errorMessage != null && errorMessage.length() > 0) {
			final Label msg = new Label(cmp, SWT.NONE);
			msg.setText(errorMessage);
			msg.setForeground(new Color(Display.getCurrent(), 255, 0, 0));

			new Label(cmp, SWT.NONE);
			new Label(cmp, SWT.NONE);
			new Label(cmp, SWT.NONE);
		}

		// Variable Value
		final Text textValue = new Text(cmp, SWT.BORDER);
		textValue.setText(value);
		textValue.setLayoutData(new GridData(SWT.FILL, SWT.FILL, true, true));
		if (key != null) {
			textValue.setToolTipText(key);
		}
		if (value != null) {
			textValue.setText(value);
			/*
			 * textValue.setData(prop); textValue.addVerifyListener(this); editors.add(textValue);
			 */
		}

		/* NEW LINE */
		// Variable Default Value
		if (defaultValue != null && !defaultValue.isEmpty()) {
			new Label(cmp, SWT.NONE);
			new Label(cmp, SWT.NONE);
			new Label(cmp, SWT.NONE);

			final Button checkBox = new Button(cmp, SWT.CHECK);
			checkBox.setText(EvolutionCoreUIPlugin.getResourceString("button.defaultvalue.label"));
			checkBox.setData(textValue);
			checkBox.setToolTipText(defaultValue);
			checkBox.addSelectionListener(new SelectionAdapter() {
				@Override
				public void widgetSelected(final SelectionEvent e) {
					final Text textWidget = (Text) checkBox.getData();
					if (checkBox.getSelection()) {
						textWidget.setText(defaultValue);
						textWidget.setEnabled(false);
					} else {
						textWidget.setEnabled(true);
					}
				}
			});
		}

		/* NEW LINE */
		if (helper != null && !helper.isEmpty()) {
			new Label(cmp, SWT.NONE);
			new Label(cmp, SWT.NONE);
			new Label(cmp, SWT.NONE);

			final Text help = new Text(cmp, /* SWT.H_SCROLL | SWT.V_SCROLL | */ SWT.WRAP);
			help.setBackground(cmp.getBackground());
			final GridData gridData = new GridData(SWT.FILL, SWT.BEGINNING, true, false);
			// gridData.heightHint = 40;
			help.setLayoutData(gridData);
			help.setText(helper);
			help.setEditable(false);
			help.setEnabled(false);
		}

		/* NEW EMPTY LINE */
		new Label(cmp, SWT.NONE);
		new Label(cmp, SWT.NONE);
		new Label(cmp, SWT.NONE);
		new Label(cmp, SWT.NONE);

		return textValue;
	}

	public static Label createLabel(final Composite parent, final String label) {
		return createLabel(parent, label, false);
	}

	public static Label createLabel(final Composite parent, final String label, final boolean verticalFill) {
		return createLabel(parent, label, verticalFill, 3);
	}

	public static Label createLabel(final Composite parent, final String label, final boolean verticalFill,
			final int horizontalSpan) {
		return createLabel(parent, label, verticalFill, horizontalSpan, true);
	}

	public static Label createLabel(final Composite parent, final String label, final boolean verticalFill,
			final int horizontalSpan,
			final boolean grabExcessHorizontalSpace) {
		final Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
			if (verticalFill) {
				gridData.verticalAlignment = GridData.FILL;
				gridData.grabExcessVerticalSpace = true;
			}

			gridData.horizontalSpan = horizontalSpan;
			textLabel.setLayoutData(gridData);
		}
		textLabel.setText(label);
		return textLabel;
	}

	public static Combo createLabelledAutoSearchCombo(final Composite parent, final String label) {
		return createLabelledAutoSearchCombo(parent, label, false, null, 0);
	}

	public static Combo createLabelledAutoSearchCombo(final Composite parent, final String label,
			final boolean readonly) {
		return createLabelledAutoSearchCombo(parent, label, readonly, null, 0);
	}

	/**
	 * Combo permettant de faire une recherche automatique sur le nom saisi parmis la liste propos�e.
	 *
	 * @author MH
	 */
	public static Combo createLabelledAutoSearchCombo(final Composite parent, final String label,
			final boolean readOnly, final String[] items,
			final int selectedIndex) {

		final Combo combo = createLabelledCombo(parent, label, false, items, selectedIndex);
		final AutoSearchComboListener cbListener = new AutoSearchComboListener(combo);
		combo.addModifyListener(cbListener);
		if (readOnly) {
			combo.addFocusListener(cbListener);
		}
		return combo;
	}

	public static Combo createLabelledAutoSearchCombo(final Composite parent, final String label,
			final String[] items) {
		return createLabelledAutoSearchCombo(parent, label, false, items, -1);
	}

	public static Combo createLabelledAutoSearchCombo(final Composite parent, final String label, final String[] items,
			final int selectedIndex) {
		return createLabelledAutoSearchCombo(parent, label, false, items, selectedIndex);
	}

	public static Button createLabelledCheckbox(final Composite parent, final String label, final boolean checked) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		final Button checkbox = new Button(parent, SWT.CHECK);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			checkbox.setLayoutData(gridData);
		}
		checkbox.setSelection(checked);
		return checkbox;
	}

	public static ArcadColorWidget createLabelledColorWidget(final Composite parent, final String label) {
		final ArcadColorWidget widget = new ArcadColorWidget();
		/*
		 * Label textLabel = new Label(parent, SWT.NONE); textLabel.setText(label); new Label(parent,
		 * SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		final Composite compo = GuiFormatTools.createComposite(parent, 2, false);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.grabExcessHorizontalSpace = true;
			compo.setLayoutData(gridData);
		}
		final Button color = new Button(compo, SWT.PUSH);
		GridData gridData = new GridData(GridData.CENTER);
		gridData.grabExcessHorizontalSpace = true;
		gridData.widthHint = 50;
		color.setLayoutData(gridData);
		color.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				final ArcadColorUI newColor = GuiFormatTools.getColor(EvolutionCoreUIPlugin.getShell(), CoreUILabels
						.resString("color.widget.title"), widget.getColorUI());//$NON-NLS-1$
				if (newColor != null && !newColor.equals(widget.getColorUI())) {
					widget.setColorUI(newColor);
				}
			}
		});
		widget.addModifyListener(() -> setButtonImageColor(widget, color));
		final Button erase = new Button(compo, SWT.PUSH);
		gridData = new GridData(GridData.CENTER);
		gridData.grabExcessHorizontalSpace = true;
		gridData.widthHint = 25;
		gridData.heightHint = 25;
		erase.setLayoutData(gridData);
		erase.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent event) {
				widget.setColorUI(null);
			}
		});
		erase.setImage(Icon.CLEANUP.image());
		erase.setToolTipText(CoreUILabels.resString("CalendarEdit.ClearButtton.Tooltips"));//$NON-NLS-1$
		return widget;
	}

	public static Combo createLabelledCombo(final Composite parent, final String label) {
		return createLabelledCombo(parent, label, false, null, 0);
	}

	public static Combo createLabelledCombo(final Composite parent, final String label, final boolean readonly) {
		return createLabelledCombo(parent, label, readonly, null, 0);
	}

	public static Combo createLabelledCombo(final Composite parent, final String label, final boolean readOnly,
			final String[] items,
			final int selectedIndex) {
		if (label != null) {
			/*
			 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
			 */
			createTextLabel(parent, label);
		}
		Combo combo;
		if (readOnly) {
			combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		} else {
			combo = new Combo(parent, SWT.BORDER);
		}
		if (items != null) {
			combo.setItems(items);
		}
		if (selectedIndex < combo.getItemCount()) {
			combo.select(selectedIndex);
		}
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(SWT.BEGINNING);
			gridData.horizontalAlignment = SWT.FILL;
			gridData.grabExcessHorizontalSpace = true;
			if (label == null) {
				gridData.horizontalSpan = 3;
			}
			combo.setLayoutData(gridData);
		}
		return combo;
	}

	public static Combo createLabelledCombo(final Composite parent, final String label, final String[] items) {
		return createLabelledCombo(parent, label, false, items, -1);
	}

	public static Combo createLabelledCombo(final Composite parent, final String label, final String[] items,
			final int selectedIndex) {
		return createLabelledCombo(parent, label, false, items, selectedIndex);
	}

	public static Composite createLabelledComposite(final Composite parent, final String label,
			final int numberOfColumn,
			final boolean equalWidth) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		return createComposite(parent, numberOfColumn, equalWidth);
	}

	public static Text createLabelledDate(final Composite parent, final String label, final String defaultText) {
		/*
		 * Label textLabel = new Label(parent, SWT.NONE); textLabel.setText(label); new Label(parent,
		 * SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		final Label textLabel = createTextLabel(parent, label);
		final Text text = new Text(parent, SWT.BORDER);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			text.setLayoutData(gridData);
		}
		text.setText(defaultText);
		text.setData(textLabel);

		final String format = CoreUILabels.resString("simpleDateFomat.Text"); //$NON-NLS-1$
		text.setText(format);
		text.addListener(SWT.Verify, new SimpleDateVerifierListener(text, format));

		return text;
	}

	/**
	 * Create DateTime
	 * 
	 * @param parent
	 * @param label
	 * @param style
	 *            SWT.TIME || SWT.DATE
	 * @return
	 */
	public static DateTime createLabelledDateTime(final Composite parent, final String label, final int style) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);

		final DateTime time = new DateTime(parent, style);
		time.setLayoutData(new GridData(SWT.FILL, SWT.BEGINNING, true, false));

		return time;
	}

	public static Text createLabelledDateWithDateSelector(final Composite parent, final String label, final int style) {
		return createLabelledDateWithDateSelector(parent, label, style, StringTools.EMPTY);
	}

	public static Text createLabelledDateWithDateSelector(final Composite parent, final String label, final int style,
			final String defaultDateText) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		// Cr�ation du composite de r�ception
		final Composite p = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(3, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		final Text valueText = new Text(p, SWT.BORDER | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(gridData);

		final GuiFormatTools.CalendarManager manager = GuiFormatTools.instance.new CalendarManager(valueText, style);

		final Button browseButton = new Button(p, SWT.PUSH);
		browseButton.setImage(Icon.CALENDAR_SELECT_DAY.image());
		gridData = new GridData();
		gridData.heightHint = 21;
		browseButton.setLayoutData(gridData);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final String newText = manager.choose(valueText.getText());
				if (newText != null && !newText.equals(valueText.getText())) {
					valueText.setText(newText);
				}
			}
		});

		final Button clearButton = new Button(p, SWT.PUSH);
		clearButton.setImage(Icon.CLEANUP.image());
		clearButton.setToolTipText(CoreUILabels.resString("CalendarEdit.ClearButtton.Tooltips"));//$NON-NLS-1$
		gridData = new GridData();
		gridData.heightHint = 21;
		clearButton.setLayoutData(gridData);
		clearButton.addListener(SWT.Selection, event -> valueText.setText(defaultDateText));
		valueText.setData(manager.getFormat());
		valueText.setData("browsebutton", browseButton);
		valueText.setData("clearbutton", clearButton);
		valueText.setData("main", p);
		return valueText;
	}

	public static Text createLabelledDateWithDateSelectorNotNull(final Composite parent, final String label,
			final int style) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		// Cr�ation du composite de r�ception
		final Composite p = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		final Text valueText = new Text(p, SWT.BORDER | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(gridData);

		final GuiFormatTools.CalendarManager manager = GuiFormatTools.instance.new CalendarManager(valueText, style);

		final Button browseButton = new Button(p, SWT.PUSH);
		browseButton.setImage(Icon.CALENDAR_SELECT_DAY.image());
		gridData = new GridData();
		gridData.heightHint = 21;
		browseButton.setLayoutData(gridData);
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final String newText = manager.choose(valueText.getText());
				if (newText != null && !newText.equals(valueText.getText())) {
					valueText.setText(newText);
				}
			}
		});

		valueText.setData(manager.getFormat());

		return valueText;
	}

	public static DoubleSpinner createLabelledDoubleSpinner(final Composite parent, final String label) {
		return createLabelledDoubleSpinner(parent, label, StringTools.EMPTY);
	}

	public static DoubleSpinner createLabelledDoubleSpinner(final Composite parent, final String label,
			final String units) {
		return createLabelledDoubleSpinner(parent, label, units, 9999);
	}

	public static DoubleSpinner createLabelledDoubleSpinner(final Composite parent, final String label,
			final String units, final int maximum) {
		/*
		 * Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP); textLabel.setText(label); Label twopoints = new
		 * Label(parent, SWT.NONE); twopoints.setText(":"); //$NON-NLS-1$
		 */
		final Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP);

		final DoubleSpinner spinner = new DoubleSpinner(parent, SWT.NONE);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			spinner.setLayoutData(gridData);
		}
		spinner.setData(textLabel);
		spinner.setMaximum(maximum);
		spinner.setPostFix(" " + units); //$NON-NLS-1$
		return spinner;
	}

	public static Hyperlink createLabelledHyperlink(final Composite parent, final String label,
			final Color foregroundColor, final String text,
			final int style) {
		final Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			gridData.horizontalSpan = 2;
			textLabel.setLayoutData(gridData);
			textLabel.setText(label);
		}
		/*
		 * Label twopoints = new Label(parent, SWT.NONE); twopoints.setText(":"); //$NON-NLS-1$ if (parent.getLayout()
		 * instanceof GridLayout) { GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		 * twopoints.setLayoutData(gridData); }
		 */
		final Hyperlink link = new Hyperlink(parent, style);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			link.setLayoutData(gridData);
		}
		link.setText(text);
		if (foregroundColor != null) {
			link.setForeground(foregroundColor);
		}
		return link;
	}

	public static Text createLabelledIntegerText(final Composite parent, final String label, final int intialValue) {
		return createLabelledIntegerText(parent, label, intialValue, -1, null, false);
	}

	public static Text createLabelledIntegerText(final Composite parent, final String label, final int intialValue,
			final int maxValue) {
		return createLabelledIntegerText(parent, label, intialValue, maxValue, null, false);
	}

	public static Text createLabelledIntegerText(final Composite parent, final String label, final int intialValue,
			final int maxValue, final String help, final boolean isPositive) {
		int maximumValue = maxValue;
		if (maximumValue != -1) {
			final String stMaxValue = new Integer(maximumValue).toString();
			maximumValue = stMaxValue.length();
		}
		final Text t = createLabelledText(parent, label, String.valueOf(intialValue), maximumValue, help);
		t.addListener(SWT.Verify, e -> {
			final String string = e.text;
			final char[] chars = new char[string.length()];
			string.getChars(0, chars.length, chars, 0);
			for (final char c : chars) {
				final String testString = isPositive ? NUMCHARPOSITIVE : NUMCHAR;
				if (testString.indexOf(c) == -1) {
					e.doit = false;
					return;
				}
			}
		});
		return t;
	}

	public static Text createLabelledIntegerText(final Composite parent, final String label, final int intialValue,
			final String help) {
		return createLabelledIntegerText(parent, label, intialValue, -1, help, false);
	}

	public static Label createLabelledLabel(final Composite parent, final String label) {
		return createLabelledLabel(parent, label, SWT.NONE);
	}

	public static Label createLabelledLabel(final Composite parent, final String label, final int style) {
		final Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			textLabel.setLayoutData(gridData);
			gridData.horizontalSpan = 2;
		}
		textLabel.setText(label + ':');
		/*
		 * Label twopoints = new Label(parent, SWT.NONE); twopoints.setText(":"); //$NON-NLS-1$ if (parent.getLayout()
		 * instanceof GridLayout) { GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		 * twopoints.setLayoutData(gridData); }
		 */
		final Label lbl = new Label(parent, style);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			lbl.setLayoutData(gridData);
		}
		lbl.setData(textLabel);
		return lbl;
	}

	public static Link createLabelledLink(final Composite parent, final String label, final String text,
			final int style) {
		final Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP);
		GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		gridData.horizontalSpan = 2;
		textLabel.setLayoutData(gridData);

		textLabel.setText(label + ':');
		/*
		 * Label twopoints = new Label(parent, SWT.NONE); twopoints.setText(":"); //$NON-NLS-1$ gridData = new
		 * GridData(GridData.VERTICAL_ALIGN_BEGINNING); twopoints.setLayoutData(gridData);
		 */

		final Link link = new Link(parent, style);
		gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		link.setLayoutData(gridData);
		link.setData(textLabel);
		link.setText(text);
		link.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				try {
					// Open default external browser
					PlatformUI.getWorkbench().getBrowserSupport().createBrowser("browserId").openURL(new URL(e.text)); //$NON-NLS-1$
				} catch (final PartInitException ex) {
					MessageManager.addException(ex, MessageManager.LEVEL_PRODUCTION);
				} catch (final MalformedURLException ex) {
					MessageManager.addException(ex, MessageManager.LEVEL_PRODUCTION);
				}
			}
		});
		return link;
	}

	public static List createLabelledList(final Composite parent, final String label, final boolean verticalFill,
			final String[] items,
			final int selectedIndex) {
		if (label != null) {
			createLabel(parent, label);
		}
		final List list = new List(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL_BOTH);
			if (verticalFill) {
				gridData.horizontalAlignment = GridData.FILL;
				gridData.verticalAlignment = GridData.FILL;
				gridData.grabExcessVerticalSpace = true;
			} else {
				gridData.horizontalAlignment = GridData.FILL;
			}
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = 3;
			list.setLayoutData(gridData);
		}
		if (items != null) {
			list.setItems(items);
		}
		if (selectedIndex > -1) {
			list.select(selectedIndex);
		}
		return list;
	}

	/**
	 * cr�ation d'un labelledText avec barre de d�filement horizontal et vertical
	 *
	 * @param parent
	 * @param label
	 * @param defaultText
	 * @param limit
	 * @return Text
	 */
	public static Text createLabelledLongText(final Composite parent, final String label) {
		/*
		 * Label textLabel = new Label(parent, SWT.NONE); textLabel.setText(label); new Label(parent,
		 * SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		final Label textLabel = createTextLabel(parent, label);

		final Text text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.heightHint = 100;
			text.setLayoutData(gridData);
		}
		text.setData(textLabel);
		return text;
	}

	public static Text createLabelledMultiValues(final Composite parent, final String label, final String dialogLabel) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		// Cr�ation du composite de r�ception
		final Composite p = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		// Formatage de la liste sur 3 cellules
		final Text valueText = new Text(p, SWT.BORDER | SWT.READ_ONLY);
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(gridData);

		final Button bAdd = new Button(p, SWT.PUSH);
		bAdd.setText("..."); //$NON-NLS-1$
		gridData = new GridData();
		gridData.heightHint = 21;
		gridData.widthHint = 21;
		bAdd.setLayoutData(gridData);
		bAdd.addSelectionListener(GuiFormatTools.getInstance().new MultiValueSelectionAdapter(valueText, dialogLabel));
		valueText.setData(bAdd);
		return valueText;
	}

	public static Period createLabelledPeriod(final Composite parent, final String label, final String beginDate,
			final String endDate,
			final int style) {
		final Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
			gridData.horizontalSpan = 2;
			textLabel.setLayoutData(gridData);
		}
		textLabel.setText(label + ':');
		/*
		 * Label twopoints = new Label(parent, SWT.NONE); twopoints.setText(":"); //$NON-NLS-1$ if (parent.getLayout()
		 * instanceof GridLayout) { GridData gridData = new GridData(GridData.VERTICAL_ALIGN_BEGINNING);
		 * twopoints.setLayoutData(gridData); }
		 */
		final Period period = new Period(parent, style);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			period.setLayoutData(gridData);
		}
		period.setBeginDate(beginDate == null ? StringTools.EMPTY : beginDate);
		period.setEndDate(endDate == null ? StringTools.EMPTY : endDate);
		return period;
	}

	public static Spinner createLabelledSpinner(final Composite parent, final String label) {
		return createLabelledSpinner(parent, label, 0);
	}

	public static Spinner createLabelledSpinner(final Composite parent, final String label, final int digits) {
		return createLabelledSpinner(parent, label, digits, 9999);
	}

	public static Spinner createLabelledSpinner(final Composite parent, final String label, final int digits,
			final int maximum) {
		/*
		 * Label textLabel = new Label(parent, SWT.NONE | SWT.WRAP); textLabel.setText(label); Label twopoints = new
		 * Label(parent, SWT.NONE); twopoints.setText(":"); //$NON-NLS-1$
		 */
		final Label textLabel = createTextLabel(parent, label);

		final Spinner spinner = new Spinner(parent, SWT.BORDER);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			spinner.setLayoutData(gridData);
		}
		spinner.setData(textLabel);
		spinner.setDigits(digits);
		spinner.setMaximum(maximum);
		return spinner;
	}

	public static Text createLabelledText(final Composite parent, final String label) {
		return createLabelledText(parent, label, StringTools.EMPTY, -1);
	}

	public static Text createLabelledText(final Composite parent, final String label, final int limit) {
		return createLabelledText(parent, label, StringTools.EMPTY, limit);
	}

	public static Text createLabelledText(final Composite parent, final String label, final String defaultText) {
		return createLabelledText(parent, label, defaultText, -1);
	}

	public static Text createLabelledText(final Composite parent, final String label, final String defaultText,
			final int limit) {
		return createLabelledText(parent, label, defaultText, limit, SWT.BORDER, null);
	}

	public static Text createLabelledText(final Composite parent, final String label, final String defaultText,
			final int limit, final int style, final String help) {
		/*
		 * Label textLabel = new Label(parent, SWT.NONE); textLabel.setText(label); new Label(parent,
		 * SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		final Label textLabel = createTextLabel(parent, label);

		final Text text = new Text(parent, style);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			text.setLayoutData(gridData);
		}
		if (defaultText != null) {
			text.setText(defaultText);
		}
		text.setData(textLabel);
		if (limit > 0) {
			text.setTextLimit(limit);
		}

		if (help != null) {
			new Label(parent, SWT.NONE).setText(""); //$NON-NLS-1$
			new Label(parent, SWT.NONE).setText(""); //$NON-NLS-1$
			final Label helpLabel = new Label(parent, SWT.NONE);
			helpLabel.setText(help);
		}
		return text;
	}

	public static Text createLabelledText(final Composite parent, final String label, final String defaultText,
			final int limit, final String help) {
		return createLabelledText(parent, label, defaultText, limit, SWT.BORDER, help);
	}

	public static Text createLabelledTextWithButton(final Composite parent, final String label,
			final String defaultText,
			final boolean readonly, final String buttonText) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		// Cr�ation du composite de r�ception
		final Composite p = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		Text valueText;
		if (readonly) {
			valueText = new Text(p, SWT.BORDER | SWT.READ_ONLY);
		} else {
			valueText = new Text(p, SWT.BORDER);
		}
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(gridData);

		final Button bAdd = new Button(p, SWT.PUSH);
		bAdd.setText(buttonText);
		gridData = new GridData();
		gridData.heightHint = 21;
		bAdd.setLayoutData(gridData);

		valueText.setData(bAdd);
		valueText.setText(defaultText);
		return valueText;
	}

	/**
	 * Create labelled text input with multiple buttons
	 * 
	 * @param parent
	 * @param label
	 * @param defaultText
	 * @param readonly
	 * @param buttonTexts
	 * @return
	 */
	public static Text createLabelledTextWithButtons(final Composite parent, final String label,
			final String defaultText,
			final boolean readonly, final String[] buttonTexts) {
		if (buttonTexts == null) {
			return createLabelledTextWithButton(parent, label, defaultText, readonly, "");
		}
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);

		// Reception component creation
		final int count = buttonTexts.length;
		final Composite p = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(count + 1, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		Text valueText;
		if (readonly) {
			valueText = new Text(p, SWT.BORDER | SWT.READ_ONLY);
		} else {
			valueText = new Text(p, SWT.BORDER);
		}
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(gridData);

		final java.util.List<Button> buttons = new ArrayList<>(count);
		Button bAdd = null;
		for (final String buttonText : buttonTexts) {
			bAdd = new Button(p, SWT.PUSH);
			bAdd.setText(buttonText);
			gridData = new GridData();
			gridData.heightHint = 21;
			bAdd.setLayoutData(gridData);
			buttons.add(bAdd);
		}

		valueText.setData(buttons);
		valueText.setText(defaultText);
		return valueText;
	}

	public static Text createLabelledTextWithDirectorySelector(final Composite parent, final String label,
			final boolean readonly,
			final String title) {
		return createLabelledTextWithDirectorySelector(parent, label, readonly, title, SWT.OPEN);
	}

	public static Text createLabelledTextWithDirectorySelector(final Composite parent, final String label,
			final boolean readonly,
			final String title, final int actionStyle) {
		final Text text = createLabelledTextWithButton(parent, label, StringTools.EMPTY, readonly, "..."); //$NON-NLS-1$
		final Button browseButton = (Button) text.getData();
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				// DirectoryDialog chooser = new DirectoryDialog(EvolutionCoreUIPlugin.getShell(), actionStyle);
				// chooser.setText(title);
				// String s = chooser.open();
				final String s = getFileManagerProvider().selectDirectory(EvolutionCoreUIPlugin.getShell(), actionStyle,
						title);
				if (s != null && !s.equals(StringTools.EMPTY)) {
					text.setText(s);
				}
			}
		});
		return text;
	}

	public static Text createLabelledTextWithEllipsis(final Composite parent, final String label,
			final boolean readonly) {
		return createLabelledTextWithButton(parent, label, StringTools.EMPTY, readonly, "..."); //$NON-NLS-1$
	}

	public static Text createLabelledTextWithFileSelector(final Composite parent, final String label,
			final boolean readonly,
			final String title, final String[] fileExtensions) {
		return createLabelledTextWithFileSelector(parent, label, readonly, title, fileExtensions, SWT.OPEN);
	}

	public static Text createLabelledTextWithFileSelector(final Composite parent, final String label,
			final boolean readonly,
			final String title, final String[] fileExtensions, final int actionStyle) {
		final Text text = createLabelledTextWithButton(parent, label, StringTools.EMPTY, readonly, "..."); //$NON-NLS-1$
		final Button browseButton = (Button) text.getData();
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final String s = getFileManagerProvider().selectFile(
						EvolutionCoreUIPlugin.getShell(), actionStyle, title, fileExtensions);
				if (s != null && !s.equals(StringTools.EMPTY)) {
					text.setText(s);
				}
			}
		});
		return text;
	}

	public static Text createLabelledTextWithMenuAction(final Composite parent, final String label,
			final boolean readonly,
			final ImageDescriptor image) {
		/*
		 * Label textLabel = new Label(parent, SWT.NONE); textLabel.setText(label); new Label(parent,
		 * SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		final Label textLabel = createTextLabel(parent, label);
		final Composite container = GuiFormatTools.createComposite(parent, 2, false);
		if (parent.getLayout() instanceof GridLayout) {
			container.setLayoutData(new GridData(GridData.BEGINNING | GridData.FILL_HORIZONTAL));
		}
		Text text = null;
		if (readonly) {
			text = new Text(container, SWT.BORDER | SWT.READ_ONLY);
		} else {
			text = new Text(container, SWT.BORDER);
		}
		final GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		text.setLayoutData(gridData);
		textLabel.setData(text);

		final ToolBar toolbar = new ToolBar(container, SWT.FLAT);
		final ToolBarManager toolbarManager = new ToolBarManager(toolbar);
		final MenuManager menuManager = new MenuManager();
		final MenuAction menuAction = new MenuAction(StringTools.EMPTY, IAction.AS_DROP_DOWN_MENU, menuManager);

		menuAction.setImageDescriptor(image);
		toolbarManager.add(menuAction);
		toolbarManager.update(true);
		text.setData(menuAction);

		return text;
	}

	public static Text createLabelledTextWithMultiFileSelector(final Composite parent, final String label,
			final boolean readonly,
			final String title, final String[] fileExtensions) {
		return createLabelledTextWithMultiFileSelector(parent, label, readonly, title, fileExtensions,
				SWT.OPEN | SWT.MULTI);
	}

	public static Text createLabelledTextWithMultiFileSelector(final Composite parent, final String label,
			final boolean readonly,
			final String title, final String[] fileExtensions, final int actionStyle) {
		final Text text = createLabelledTextWithButton(parent, label, StringTools.EMPTY, readonly, "..."); //$NON-NLS-1$
		final Button browseButton = (Button) text.getData();
		browseButton.addSelectionListener(new SelectionAdapter() {
			@Override
			public void widgetSelected(final SelectionEvent e) {
				final java.util.List<String> selection = getFileManagerProvider().selectFiles(
						EvolutionCoreUIPlugin.getShell(), actionStyle, title, fileExtensions);
				if (selection != null && selection.size() > 0) {
					final StringBuilder fileList = new StringBuilder();
					for (final String file : selection) {
						if (fileList.length() > 0) {
							fileList.append(';');
						}
						fileList.append(file);
					}
					text.setText(fileList.toString());
				}
			}
		});
		return text;
	}

	public static Text createLabelledTextWithTwoButtons(final Composite parent, final String label,
			final String defaultText,
			final boolean readonly, final String button1Text, final Image img1, final String button2Text,
			final Image img2) {
		return createTextWithTwoOrThreeButtons(parent, label, defaultText, readonly, button1Text, img1, button2Text,
				img2, null, null);
	}

	public static Composite createLabelledTime(final Composite parent, final String label, final int style) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		final Composite p = new Composite(parent, SWT.BORDER);
		final GridLayout layout = new GridLayout(style == 0 ? 3 : 2, true);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			p.setLayoutData(gridData);
		}
		final Spinner hourChooser = new Spinner(p, SWT.NONE);
		hourChooser.setMaximum(23);
		//
		final Spinner minutChooser = new Spinner(p, SWT.NONE);
		minutChooser.setMaximum(59);
		if (style == 0) {
			final Spinner secondChooser = new Spinner(p, SWT.NONE);
			secondChooser.setMaximum(59);
			p.setData(TIME_PART_SECOND, secondChooser);
		}

		p.setData(TIME_PART_MINUTE, minutChooser);
		p.setData(TIME_PART_HOUR, hourChooser);
		return p;
	}

	public static Button createLeftButton(final Composite parent, final String buttonLabel) {
		return createButton(parent, buttonLabel, SWT.BEGINNING);
	}

	public static Combo createLinkedCombo(final Composite parent, final String label, final boolean readOnly) {
		final Hyperlink link = new Hyperlink(parent, SWT.NONE);
		link.setText(label);
		link.setUnderlined(true);
		new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		Combo combo;
		if (readOnly) {
			combo = new Combo(parent, SWT.BORDER | SWT.READ_ONLY);
		} else {
			combo = new Combo(parent, SWT.BORDER);
		}
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			if (label == null) {
				gridData.horizontalSpan = 3;
			}
			combo.setLayoutData(gridData);
		}
		combo.setData(link);
		return combo;
	}

	public static Hyperlink createLinkedComboWithButton(final Composite parent, final String label,
			final boolean readOnly,
			final String buttonText) {
		final Hyperlink link = new Hyperlink(parent, SWT.NONE);
		link.setText(label);
		link.setUnderlined(true);
		new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$

		final Composite p = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		Combo combo;
		if (readOnly) {
			combo = new Combo(p, SWT.BORDER | SWT.READ_ONLY);
		} else {
			combo = new Combo(p, SWT.BORDER);
		}
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;

		final Button bAdd = new Button(p, SWT.PUSH);
		bAdd.setText(buttonText);
		gridData = new GridData();
		gridData.heightHint = 21;
		bAdd.setLayoutData(gridData);

		combo.setData(bAdd);
		link.setData(combo);
		return link;
	}

	public static Text createLinkedText(final Composite parent, final String label) {
		final Hyperlink textLabel = new Hyperlink(parent, SWT.NONE);
		textLabel.setText(label);
		textLabel.setUnderlined(true);
		new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		final Text text = new Text(parent, SWT.NONE);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			text.setLayoutData(gridData);
		}
		text.setData(textLabel);
		return text;
	}

	public static Hyperlink createLinkedTextWithButton(final Composite parent, final String label,
			final String defaultText,
			final boolean readonly, final String buttonText) {
		final Hyperlink valueLink = new Hyperlink(parent, SWT.NONE);
		valueLink.setText(label);
		valueLink.setUnderlined(true);
		new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		// Cr�ation du composite de r�ception
		final Composite p = new Composite(parent, SWT.NONE);
		final GridLayout layout = new GridLayout(2, false);
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		Text valueText;
		if (readonly) {
			valueText = new Text(p, SWT.BORDER | SWT.READ_ONLY);
		} else {
			valueText = new Text(p, SWT.BORDER);
		}
		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(gridData);

		final Button bAdd = new Button(p, SWT.PUSH);
		bAdd.setText(buttonText);
		gridData = new GridData();
		gridData.heightHint = 21;
		bAdd.setLayoutData(gridData);

		valueText.setData(bAdd);
		valueText.setText(defaultText);
		valueLink.setData(valueText);
		return valueLink;
	}

	public static Text createLongText(final Composite parent) {
		return createLongText(parent, 100);
	}

	public static Text createLongText(final Composite parent, final int heightInt) {
		final Text text = createLongText(parent, heightInt, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.WRAP);
		return text;
	}

	public static Text createLongText(final Composite parent, final int heightInt, final int style) {
		final Text text = new Text(parent, style);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING | GridData.FILL_BOTH);
			// gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			if (heightInt != -1) {
				gridData.heightHint = heightInt;
			}
			gridData.horizontalSpan = 3;
			text.setLayoutData(gridData);
		}
		return text;
	}

	public static ContributionItem createMenu(final java.util.List<Action> actions) {
		final ContributionItem contributionItem = new ContributionItem("") {
			@Override
			public void fill(final Menu menu, final int index) {
				final MenuItemSelectionListener l = GuiFormatTools.instance.new MenuItemSelectionListener(menu);
				for (final Action action : actions) {
					l.add(action);
				}
				// ti.addSelectionListener(l);
			}
		};
		return contributionItem;
	}

	public static Button createMiddleButton(final Composite parent, final String buttonLabel) {
		return createButton(parent, buttonLabel, SWT.CENTER);
	}

	public static Button createRadioButton(final Composite parent, final String label) {
		final Button button = new Button(parent, SWT.RADIO);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = 3;
			button.setLayoutData(gridData);
		}
		button.setText(label);
		return button;
	}

	public static Button createRadioButton(final Composite parent, final String label, final int horizontalSpan) {
		final Button button = new Button(parent, SWT.RADIO);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = horizontalSpan;
			button.setLayoutData(gridData);
		}
		button.setText(label);
		return button;
	}

	public static Button createRadioButton(final Composite parent, final String label, final int horizontalSpan,
			final boolean grabExcessHorizontalSpace) {
		final Button button = new Button(parent, SWT.RADIO);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = grabExcessHorizontalSpace;
			gridData.horizontalSpan = horizontalSpan;
			button.setLayoutData(gridData);
		}
		button.setText(label);
		return button;
	}

	public static Button[] createRadioButtonGroup(final Composite parent, final String groupLabel,
			final String[] buttonLabels,
			final int selected) {
		final Group g = new Group(parent, SWT.NONE);
		g.setLayout(new GridLayout(buttonLabels.length, true));
		g.setText(groupLabel);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = 3;
			g.setLayoutData(gridData);
		}
		final Button[] result = new Button[buttonLabels.length];
		for (int i = 0; i < buttonLabels.length; i++) {
			final Button b = new Button(g, SWT.RADIO);
			b.setText(buttonLabels[i]);
			result[i] = b;
			if (i == selected) {
				b.setSelection(true);
			}
		}
		return result;
	}

	public static Button createRightButton(final Composite parent, final String buttonLabel) {
		return createButton(parent, buttonLabel, SWT.END);
	}

	public static Composite createScrolledCompositeWithOneChildAndReturnChild(final Composite parent,
			final int numberOfColumn,
			final boolean equalWidth) {

		final ScrolledComposite scrolledComposite = new ScrolledComposite(parent, SWT.V_SCROLL
				| SWT.H_SCROLL);
		scrolledComposite.setExpandHorizontal(true);
		scrolledComposite.setExpandVertical(true);
		scrolledComposite.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 1, 1));

		final Composite composite = createComposite(scrolledComposite, numberOfColumn, equalWidth);

		scrolledComposite.setContent(composite);
		scrolledComposite.addControlListener(new ControlListener() {
			@Override
			public void controlMoved(final ControlEvent e) {
				// Do nothing
			}

			@Override
			public void controlResized(final ControlEvent e) {
				scrolledComposite.setMinSize(composite.computeSize(SWT.DEFAULT, SWT.DEFAULT));
			}
		});
		return composite;
	}

	public static Section createSection(final Composite parent, final String text) {
		final Section section = new Section(parent, ExpandableComposite.TREE_NODE | ExpandableComposite.TWISTIE
				| ExpandableComposite.SHORT_TITLE_BAR | ExpandableComposite.EXPANDED);
		if (parent.getLayout() instanceof GridLayout) {
			section.setLayoutData(new GridData(GridData.FILL, GridData.FILL, true, true, 3, 1));
		}
		section.setText(text);
		section.setClient(createComposite(section, 3, false));
		return section;
	}

	public static Spinner createSpinner(final Composite parent, final int digits, final int maximum,
			final int horizontalSpan) {
		final Spinner spinner = new Spinner(parent, SWT.BORDER);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = horizontalSpan;
			spinner.setLayoutData(gridData);
		}
		spinner.setDigits(digits);
		spinner.setMaximum(maximum);
		return spinner;
	}

	public static Text createText(final Composite parent) {
		return createText(parent, StringTools.EMPTY, true);
	}

	public static Text createText(final Composite parent, final String defaultValue, final boolean verticalFill) {
		return createText(parent, defaultValue, verticalFill, 3);
	}

	public static Text createText(final Composite parent, final String defaultValue, final boolean verticalFill,
			final int horizontalSpan) {
		final Text text = new Text(parent, SWT.BORDER | SWT.H_SCROLL | SWT.V_SCROLL | SWT.MULTI | SWT.WRAP);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL_BOTH);
			if (verticalFill) {
				gridData.horizontalAlignment = GridData.FILL;
				gridData.verticalAlignment = GridData.FILL;
				gridData.grabExcessVerticalSpace = true;
			} else {
				gridData.horizontalAlignment = GridData.FILL;
				gridData.grabExcessVerticalSpace = false;
			}
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = horizontalSpan;
			text.setLayoutData(gridData);
		}
		text.setText(defaultValue);
		return text;
	}

	public static Text createText(final Composite parent, final String defaultValue, final boolean verticalFill,
			final int horizontalSpan,
			final int style) {
		final Text text = new Text(parent, style);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.FILL_BOTH);
			if (verticalFill) {
				gridData.horizontalAlignment = GridData.FILL;
				gridData.verticalAlignment = GridData.FILL;
				gridData.grabExcessVerticalSpace = true;
			} else {
				gridData.horizontalAlignment = GridData.FILL;
			}
			gridData.grabExcessHorizontalSpace = true;
			gridData.horizontalSpan = horizontalSpan;
			text.setLayoutData(gridData);
		}
		text.setText(defaultValue);
		return text;
	}

	// <MR number="2019/00145" version="11.00.04" date="Apr 5, 2019" type="Enh" user="ACL">
	/**
	 * Create (ARIA-compatible) text label
	 */
	public static Label createTextLabel(final Composite parent, final String label) {
		final Label textLabel = new Label(parent, SWT.NONE);
		textLabel.setText(label + ':');
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData();
			gridData.horizontalSpan = 2;
			textLabel.setLayoutData(gridData);
		}
		return textLabel;
	}
	// </MR>

	public static Text createTextWithTwoOrThreeButtons(final Composite parent, final String label,
			final String defaultText,
			final boolean readonly, final String button1Text, final Image img1, final String button2Text,
			final Image img2, final String button3Text,
			final Image img3) {
		/*
		 * new Label(parent, SWT.NONE).setText(label); new Label(parent, SWT.NONE).setText(":"); //$NON-NLS-1$
		 */
		createTextLabel(parent, label);
		// Cr�ation du composite de r�ception
		final Composite p = new Composite(parent, SWT.NONE);
		GridLayout layout = null;
		if (button3Text == null && img3 == null) {
			layout = new GridLayout(3, false);
		} else {
			layout = new GridLayout(4, false);
		}
		layout.marginWidth = 0;
		layout.marginHeight = 0;
		layout.horizontalSpacing = 0;
		p.setLayout(layout);
		if (parent.getLayout() instanceof GridLayout) {
			final GridData gridData = new GridData(GridData.BEGINNING);
			gridData.horizontalAlignment = GridData.FILL;
			gridData.grabExcessHorizontalSpace = true;
			p.setLayoutData(gridData);
		}
		Text valueText;
		if (readonly) {
			valueText = new Text(p, SWT.BORDER | SWT.READ_ONLY);
		} else {
			valueText = new Text(p, SWT.BORDER);
		}

		GridData gridData = new GridData(GridData.BEGINNING);
		gridData.horizontalAlignment = GridData.FILL;
		gridData.grabExcessHorizontalSpace = true;
		valueText.setLayoutData(gridData);

		final Button bAdd = new Button(p, SWT.PUSH);
		if (img1 != null) {
			bAdd.setImage(img1);
		} else {
			bAdd.setText(button1Text);
		}
		gridData = new GridData();
		gridData.heightHint = 21;
		bAdd.setLayoutData(gridData);

		valueText.setData(bAdd);
		valueText.setText(defaultText);

		final Button bb = new Button(p, SWT.PUSH);
		if (img2 != null) {
			bb.setImage(img2);
		} else {
			bb.setText(button2Text);
		}
		gridData = new GridData();
		gridData.heightHint = 21;
		bb.setLayoutData(gridData);
		bAdd.setData(bb);

		if (button3Text != null || img3 != null) {
			final Button b3 = new Button(p, SWT.PUSH);
			if (img3 != null) {
				b3.setImage(img2);
			} else {
				b3.setText(button3Text);
			}
			gridData = new GridData();
			gridData.heightHint = 21;
			b3.setLayoutData(gridData);
			bb.setData(b3);
		}
		return valueText;
	}

	/**
	 * M�thode de cr�ation d'un panneaux avec un titre.
	 *
	 * @param parent
	 * @param title
	 * @return Composite
	 */
	public static Composite createTitlePanel(final Composite parent, final String title) {
		final Group p1 = new Group(parent, SWT.NONE);
		createGridLayout(p1, 1);
		createGridData(p1);
		p1.setText(title);
		return p1;
	}

	public static ContributionItem createToolbarDropDownMenu(final java.util.List<Action> actions) {
		final ContributionItem contributionItem = new ContributionItem("") {
			@Override
			public void fill(final ToolBar parent, final int index) {
				final ToolItem ti = new ToolItem(parent, SWT.DROP_DOWN);
				ti.setText("Section");

				final DropdownMenuSelectionListener l = GuiFormatTools.instance.new DropdownMenuSelectionListener(ti);
				for (final Action action : actions) {
					l.add(action);
				}
				ti.addSelectionListener(l);

			}
		};
		return contributionItem;
	}

	public static void disableComposite(final Composite c) {
		final Control[] controls = c.getChildren();
		for (final Control control : controls) {
			if (control instanceof Table) {
				((Table) control).setEnabled(false);
			} else if (control instanceof Composite) {
				disableComposite((Composite) control);
			} else {
				control.setEnabled(false);
			}
		}
	}

	public static ArcadColorUI getColor(final Shell shell, final String text, final ArcadColorUI color) {
		final ColorDialog colorDialog = new ColorDialog(shell);
		colorDialog.setText(text);
		if (color != null && color.getColor() != null) {
			colorDialog.setRGB(color.getColor());
		}
		final RGB newColor = colorDialog.open();
		if (newColor != null) {
			return new ArcadColorUI(newColor);
		}
		return color;
	}

	public static Date getDateFromSimpleFormattedString(final String dateToParse) {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(CoreUILabels.resString("simpleDateFomat.Text")); //$NON-NLS-1$
		try {
			return simpleDateFormat.parse(dateToParse);
		} catch (final ParseException e) {
			return null;
		}
	}

	public static Date getDateFromString(final String format, final String dateToParse) {
		final SimpleDateFormat simpleDateFormat = new SimpleDateFormat(format);
		try {
			return simpleDateFormat.parse(dateToParse);
		} catch (final ParseException e) {
			return null;
		}
	}

	public static String getDateToSimpleFormattedString(final Date date) {
		final SimpleDateFormat dateFormat = new SimpleDateFormat(CoreUILabels.resString("simpleDateFomat.Text"));//$NON-NLS-1$
		return dateFormat.format(Long.valueOf(date.getTime()));
	}

	public static IFileManagerProvider getFileManagerProvider() {
		return EvolutionCoreUIPlugin.getDefault().getFileManagerProvider();
	}

	public static ArcadFontUI getFont(final Shell shell, final String text, final ArcadFontUI font) {
		final FontDialog fontDialog = new FontDialog(shell);
		fontDialog.setText(text);
		if (font != null) {
			fontDialog.setFontList(new FontData[] { font.getFontData() });
			if (font.getColor() != null) {
				fontDialog.setRGB(font.getColor());
			}
		}
		final FontData fontData = fontDialog.open();
		if (fontData != null) {
			final ArcadFontUI newFont = new ArcadFontUI();
			newFont.setFontData(fontData);
			newFont.setColor(fontDialog.getRGB());
			return newFont;
		}
		return font;
	}

	public static String getFormattedCurrentDate() {
		final Calendar c = new GregorianCalendar();
		final SimpleDateFormat dateFormat = new SimpleDateFormat(CoreUILabels.resString("simpleDateFomat.Text"));//$NON-NLS-1$
		return dateFormat.format(c.getTime());
	}

	public static String getFormattedDate(final String dateToFormat) {
		String entryFormat;
		if (dateToFormat.length() == 8) {
			entryFormat = "yyyyMMdd"; //$NON-NLS-1$
		} else {
			entryFormat = "yyMMdd"; //$NON-NLS-1$
		}

		final SimpleDateFormat dfreader = new SimpleDateFormat(entryFormat);
		final SimpleDateFormat dfwriter = new SimpleDateFormat(CoreUILabels.resString("simpleDateFomat.Text")); //$NON-NLS-1$
		try {
			final Date d = dfreader.parse(dateToFormat);
			return dfwriter.format(d);
		} catch (final ParseException e) {
			return StringTools.EMPTY;
		}
	}

	public static String getFormattedTime(final String timeToFormat) {
		final SimpleDateFormat dfreader = new SimpleDateFormat("hhmmss");//$NON-NLS-1$
		final SimpleDateFormat dfwriter = new SimpleDateFormat("hh:mm:ss");//$NON-NLS-1$
		try {
			final Date d = dfreader.parse(timeToFormat);
			return dfwriter.format(d);
		} catch (final ParseException e) {
			return StringTools.EMPTY;
		}
	}

	/**
	 * @return Returns the instance.
	 */
	public static GuiFormatTools getInstance() {
		return instance;
	}

	public static int getTimeValue(final Composite c, final String key) {
		final Object o = c.getData(key);
		if (o instanceof Spinner) {
			return ((Spinner) o).getSelection();
		} else {
			return -1;
		}
	}

	public static void openError(final String message) {
		MessageDialog.openError(EvolutionCoreUIPlugin.getShell(), CoreUILabels.resString("msg.commonTitle"), //$NON-NLS-1$
				message);
	}

	static void setButtonImageColor(final ArcadColorWidget widget, final Button color) {
		final Display display = EvolutionCoreUIPlugin.getShell().getDisplay();
		Image image = null;
		if (widget.getColorUI() != null) {
			final Color c = widget.getColorUI().getSWTColor(display);
			final PaletteData pd = new PaletteData(new RGB[] { c.getRGB() });
			final ImageData id = new ImageData(40, 10, 1, pd);
			image = new Image(display, id);
		}
		color.setImage(image);
	}

	public static void setHelp(final Control control, final String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(control, contextId);
	}

	public static void setHelp(final IAction action, final String contextId) {
		PlatformUI.getWorkbench().getHelpSystem().setHelp(action, contextId);
	}

	public static void setTimeValues(final Composite c, final int value, final String key) {
		final Object o = c.getData(key);
		if (o instanceof Spinner) {
			final Spinner s = (Spinner) o;
			s.setSelection(value);
		}
	}

	public static void setVisible(final Combo c, final boolean visible) {
		c.setVisible(visible);
		if (c.getData() instanceof Control) {
			((Control) c.getData()).setVisible(visible);
		}

	}

	private GuiFormatTools() {
		super();
	}

}
