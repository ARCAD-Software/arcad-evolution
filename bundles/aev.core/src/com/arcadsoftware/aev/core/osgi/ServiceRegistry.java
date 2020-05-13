package com.arcadsoftware.aev.core.osgi;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.osgi.framework.BundleContext;
import org.osgi.framework.InvalidSyntaxException;
import org.osgi.framework.ServiceReference;
import org.osgi.framework.ServiceRegistration;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * Prefer dependency injection (SCR...) to register/lookup mechanisms.
 */
@Component(immediate = true)
public final class ServiceRegistry {
	private static Optional<ServiceRegistry> instance;
	
	private final Map<Class<?>, ServiceRegistration<?>> registrations = new HashMap<>();
	private BundleContext bundleContext;
	
	public static Optional<ServiceRegistry> getInstance() {
		return instance;
	}
	
	@Activate
	public void activate(final BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		instance = Optional.of(this);  
	}
	
	@Deactivate
	public void deactivate() {
		this.bundleContext = null;
		this.registrations.clear();
		instance = Optional.empty();
	}
	
	public <T> List<T> lookupAll(final Class<T> clazz) {
		final BundleContext context = getBundleContext();
		try {
			return getBundleContext().getServiceReferences(clazz, null)
									 .stream()
									 .map(context::getService).collect(Collectors.toList());
		}
		catch (InvalidSyntaxException e) {
			e.printStackTrace();
			return Collections.emptyList();
		}		
	}
	
	public <T> Optional<T> lookup(final Class<T> clazz) {
		final BundleContext context = getBundleContext();
		final ServiceReference<T> reference = getBundleContext().getServiceReference(clazz);
		if (reference == null) {
			return Optional.empty();
		} else {
			return Optional.ofNullable(context.getService(reference));
		}
	}

	public <T> Optional<T> lookup(final Class<T> clazz, final String filter) {
		try {
			final BundleContext context = getBundleContext();
			final Collection<ServiceReference<T>> references = context.getServiceReferences(clazz, filter);
			return Optional.ofNullable(references.stream().map(context::getService).findFirst().orElse(null));
		} catch (final InvalidSyntaxException e) {
			e.printStackTrace();
			return Optional.empty();
		}
	}

	public synchronized <T> void register(final Class<T> clazz, final T service) {
		final ServiceRegistration<?> oldRegistration = registrations.remove(clazz);
		if (oldRegistration != null) {
			oldRegistration.unregister();
		}
		final ServiceRegistration<T> newRegistration = getBundleContext().registerService(clazz, service, null);
		registrations.put(clazz, newRegistration);
	}

	private BundleContext getBundleContext() {
		return bundleContext;
	}
}