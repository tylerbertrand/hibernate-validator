/*
 * SPDX-License-Identifier: Apache-2.0
 * Copyright Red Hat Inc. and Hibernate Authors
 */
package org.hibernate.validator.internal.constraintvalidators.bv.number.bound;

import java.math.BigInteger;

/**
 * Check that the number being validated is greater than or equal to the minimum
 * value specified.
 *
 * @author Marko Bekhta
 */
public class MinValidatorForBigInteger extends AbstractMinValidator<BigInteger> {

	@Override
	protected int compare(BigInteger number) {
		return NumberComparatorHelper.compare( number, minValue );
	}
}
