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
import org.osgi.service.component.ComponentConstants;
import org.osgi.service.component.annotations.Activate;
import org.osgi.service.component.annotations.Component;
import org.osgi.service.component.annotations.Deactivate;

/**
 * Prefer dependency injection (SCR...) to register/lookup mechanisms.
 */
@Component(immediate = true)
public final class ServiceRegistry {
	private static Optional<ServiceRegistry> instance;

	private static BundleContext getBundleContext() {
		return getInstance().map(sr -> sr.bundleContext).orElse(null);
	}

	private static Optional<ServiceRegistry> getInstance() {
		return instance;
	}

	public static Map<Class<?>, ServiceRegistration<?>> getRegistrations() {
		return getInstance().map(sr -> sr.registrations).orElse(null);
	}

	public static <T> Optional<T> lookupNamed(final Class<T> clazz, final String name) {
		return lookup(clazz, String.format("(%s=%s)", ComponentConstants.COMPONENT_NAME, name));
	}
	
	public static <T> Optional<T> lookup(final Class<T> clazz) {
		final BundleContext context = getBundleContext();
		if (context != null) {
			final ServiceReference<T> reference = context.getServiceReference(clazz);
			if (reference != null) {
				return Optional.ofNullable(context.getService(reference));
			}
		}
		return Optional.empty();
	}

	public static <T> Optional<T> lookup(final Class<T> clazz, final String filter) {
		try {
			final BundleContext context = getBundleContext();
			if (context != null) {
				final Collection<ServiceReference<T>> references = context.getServiceReferences(clazz, filter);
				return Optional.ofNullable(references.stream().map(context::getService).findFirst().orElse(null));
			}
		} catch (final InvalidSyntaxException e) {
			return Optional.empty();
		}
		return Optional.empty();
	}

	public static <T> List<T> lookupAll(final Class<T> clazz) {
		final BundleContext context = getBundleContext();
		if (context != null) {
			try {
				return context.getServiceReferences(clazz, null)
						.stream()
						.map(context::getService).collect(Collectors.toList());
			} catch (final InvalidSyntaxException e) {
				return Collections.emptyList();
			}
		}
		return Collections.emptyList();
	}

	public static <T> T lookupNamedOrDie(final Class<T> clazz, final String name) {
		return lookupNamed(clazz, name).orElseThrow(() -> new ServiceNotFoundException(clazz));
	}
	
	public static <T> T lookupOrDie(final Class<T> clazz) {
		return lookup(clazz).orElseThrow(() -> new ServiceNotFoundException(clazz));
	}

	public static <T> T lookupOrDie(final Class<T> clazz, final String filter) {
		return lookup(clazz, filter).orElseThrow(() -> new ServiceNotFoundException(clazz));
	}

	public static synchronized <T> void register(final Class<T> clazz, final T service) {
		final Map<Class<?>, ServiceRegistration<?>> regs = getRegistrations();
		final BundleContext context = getBundleContext();
		if (regs != null && context != null) {
			final ServiceRegistration<?> oldRegistration = regs.remove(clazz);
			if (oldRegistration != null) {
				oldRegistration.unregister();
			}

			final ServiceRegistration<T> newRegistration = context.registerService(clazz, service, null);
			regs.put(clazz, newRegistration);
		}
	}

	private BundleContext bundleContext;

	private final Map<Class<?>, ServiceRegistration<?>> registrations = new HashMap<>();

	@Activate
	public void activate(final BundleContext bundleContext) {
		this.bundleContext = bundleContext;
		instance = Optional.of(this);
	}

	@Deactivate
	public void deactivate() {
		bundleContext = null;
		registrations.clear();
		instance = Optional.empty();
	}
}