/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.util.actions;

import java.lang.invoke.MethodHandles;

import org.hibernate.validator.HibernateValidator;
import org.hibernate.validator.internal.util.logging.Log;
import org.hibernate.validator.internal.util.logging.LoggerFactory;

/**
 * Loads a class specified by name.
 * <p>
 * If no class loader is provided, first the thread context class loader is tried, and finally Hibernate Validator's own
 * class loader.
 * <p>
 * <b>Note</b>: When loading classes provided by the user (such as XML-configured beans or constraint types), the user
 * class loader passed to the configuration must be passed.
 *
 * @author Emmanuel Bernard
 * @author Hardy Ferentschik
 * @author Kevin Pollet &lt;kevin.pollet@serli.com&gt; (C) 2011 SERLI
 * @author Gunnar Morling
 * @author Guillaume Smet
 */
public final class LoadClass {

	private static final Log LOG = LoggerFactory.make( MethodHandles.lookup() );

	private static final String HIBERNATE_VALIDATOR_CLASS_NAME = "org.hibernate.validator";

	private LoadClass() {
	}

	public static Class<?> action(String className, ClassLoader classLoader) {
		return action( className, classLoader, true );
	}

	public static Class<?> action(String className, ClassLoader classLoader, boolean fallbackOnTCCL) {
		return action( className, classLoader, null, fallbackOnTCCL );
	}

	/**
	 * in some cases, the TCCL has been overridden so we need to pass it explicitly.
	 */
	public static Class<?> action(String className, ClassLoader classLoader, ClassLoader initialThreadContextClassLoader) {
		return action( className, classLoader, initialThreadContextClassLoader, true );
	}

	private static Class<?> action(String className, ClassLoader classLoader, ClassLoader initialThreadContextClassLoader, boolean fallbackOnTCCL) {
		if ( className.startsWith( HIBERNATE_VALIDATOR_CLASS_NAME ) ) {
			return loadClassInValidatorNameSpace( className, classLoader, initialThreadContextClassLoader, fallbackOnTCCL );
		}
		else {
			return loadNonValidatorClass( className, classLoader, initialThreadContextClassLoader, fallbackOnTCCL );
		}
	}

	// HV-363 - library internal classes are loaded via Class.forName first
	private static Class<?> loadClassInValidatorNameSpace(String className, ClassLoader classLoader, ClassLoader initialThreadContextClassLoader, boolean fallbackOnTCCL) {
		final ClassLoader loader = HibernateValidator.class.getClassLoader();
		Exception exception;
		try {
			return Class.forName( className, true, HibernateValidator.class.getClassLoader() );
		}
		catch (ClassNotFoundException e) {
			exception = e;
		}
		catch (RuntimeException e) {
			exception = e;
		}
		if ( fallbackOnTCCL ) {
			ClassLoader contextClassLoader = initialThreadContextClassLoader != null
					? initialThreadContextClassLoader
					: Thread.currentThread().getContextClassLoader();
			if ( contextClassLoader != null ) {
				try {
					return Class.forName( className, false, contextClassLoader );
				}
				catch (ClassNotFoundException e) {
					throw LOG.getUnableToLoadClassException( className, contextClassLoader, e );
				}
			}
			else {
				throw LOG.getUnableToLoadClassException( className, loader, exception );
			}
		}
		else {
			throw LOG.getUnableToLoadClassException( className, loader, exception );
		}
	}

	private static Class<?> loadNonValidatorClass(String className, ClassLoader classLoader, ClassLoader initialThreadContextClassLoader, boolean fallbackOnTCCL) {
		Exception exception = null;
		if ( classLoader != null ) {
			try {
				return Class.forName( className, false, classLoader );
			}
			catch (ClassNotFoundException e) {
				exception = e;
			}
			catch (RuntimeException e) {
				exception = e;
			}
		}
		if ( fallbackOnTCCL ) {
			try {
				ClassLoader contextClassLoader = initialThreadContextClassLoader != null
						? initialThreadContextClassLoader
						: Thread.currentThread().getContextClassLoader();
				if ( contextClassLoader != null ) {
					return Class.forName( className, false, contextClassLoader );
				}
			}
			catch (ClassNotFoundException e) {
				// ignore - try using the classloader of the caller first
				// TODO: might be wise to somehow log this
			}
			catch (RuntimeException e) {
				// ignore
			}
			final ClassLoader loader = LoadClass.class.getClassLoader();
			try {
				return Class.forName( className, true, loader );
			}
			catch (ClassNotFoundException e) {
				throw LOG.getUnableToLoadClassException( className, loader, e );
			}
		}
		else {
			throw LOG.getUnableToLoadClassException( className, classLoader, exception );
		}
	}
}
