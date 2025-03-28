/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.constraintvalidators.bv.number.bound;

/**
 * Check that the number being validated is less than or equal to the maximum
 * value specified.
 *
 * @author Marko Bekhta
 */
public class MaxValidatorForShort extends AbstractMaxValidator<Short> {

	@Override
	protected int compare(Short number) {
		return NumberComparatorHelper.compare( number.longValue(), maxValue );
	}
}
