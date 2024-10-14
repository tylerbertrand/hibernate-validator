/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.test.internal.engine.groups.defaultgroupwithinheritance;

import jakarta.validation.GroupSequence;
import jakarta.validation.constraints.NotNull;

/**
 * @author Gunnar Morling
 */
@GroupSequence({ Max.class, A.class })
public class A {

	@NotNull(groups = Max.class)
	public String foo;

	@NotNull(groups = Min.class)
	public String bar;
}
