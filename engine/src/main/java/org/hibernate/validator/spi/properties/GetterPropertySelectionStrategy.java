/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.spi.properties;

import java.util.List;
import java.util.Optional;
import java.util.Set;

import org.hibernate.validator.Incubating;

/**
 * Used to define the strategy to detect the getters of a bean.
 * <p>
 * A getter is considered as being a property of the bean and thus validated when validating the bean.
 *
 * @author Marko Bekhta
 * @since 6.1.0
 */
@Incubating
public interface GetterPropertySelectionStrategy {

	/**
	 * Returns the property corresponding to the getter if the method is considered a getter.
	 *
	 * @param executable a {@link ConstrainableExecutable}
	 *
	 * @return an optional containing the property corresponding to the given executable if it is considered a getter,
	 * or an empty optional otherwise
	 *
	 * @throws IllegalArgumentException if a property name cannot be constructed
	 */
	Optional<String> getProperty(ConstrainableExecutable executable);

	/**
	 * Gives a set of possible method names based on a property name. Usually, it means
	 * a property name prefixed with something like "get", "is", "has" etc.
	 *
	 * @param propertyName a property name
	 *
	 * @return the {@link Set} of possible getter names
	 */
	List<String> getGetterMethodNameCandidates(String propertyName);

}
