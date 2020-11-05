package com.arcadsoftware.aev.core.ui.actions;

import java.util.ArrayList;
import java.util.stream.Collectors;

import org.eclipse.jface.action.IMenuManager;
import org.eclipse.jface.action.IToolBarManager;
import org.eclipse.jface.action.Separator;

import com.arcadsoftware.aev.core.ui.container.Container;
import com.arcadsoftware.aev.core.ui.container.ContainerProvider;

/**
 * @author MD
 */
public abstract class ArcadActions {

	private final ArrayList<ArcadAction> actions = new ArrayList<>();
	private Container container;
	private ContainerProvider containerProvider;

	public ArcadActions() {

	}

	public ArcadActions(final Container container) {
		this.container = container;
	}

	public void addAction(final ArcadAction a) {
		actions.add(a);
	}

	public void addSeparator() {
		actions.add(new ArcadSeparator());
	}

	public void fillMenuAction(final IMenuManager manager) {
		for (final ArcadAction action : actions) {
			if (action.isNotSeparator()) {
				if (match(action)) {
					manager.add(action);
				}
			} else {
				manager.add(new Separator());
			}
		}
		manager.add(new Separator());
	}

	public void fillToolbarAction(final IToolBarManager manager) {
		for (final ArcadAction action : actions) {
			if (action.isNotSeparator()) {
				manager.add(action);
			} else {
				manager.add(new Separator());
			}
		}
		manager.add(new Separator());
	}

	public ArcadAction[] getActions() {
		return actions //
				.stream() //
				.filter(ArcadAction::isNotSeparator) //
				.collect(Collectors.toList()) //
				.toArray(new ArcadAction[0]);
	}

	public Container getContainer() {
		return container;
	}

	public ContainerProvider getContainerProvider() {
		return containerProvider;
	}

	public abstract void makeAction();

	protected boolean match(final ArcadAction a) {
		return true;
	}

	public void mergeAction(final ArcadActions actionToMerge) {
		final ArcadAction[] a = actionToMerge.getActions();
		if (a.length > 0) {
			addSeparator();
		}
		for (final ArcadAction element : a) {
			addAction(element);
		}
	}

	public void setContainer(final Container container) {
		this.container = container;
	}

	public void setContainerProvider(final ContainerProvider provider) {
		containerProvider = provider;
	}
}
