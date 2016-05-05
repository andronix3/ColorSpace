/*
 * Copyright (c) Andrey Kuznetsov. All Rights Reserved.
 *
 * Redistribution and use in source and binary forms, with or without
 * modification, are permitted provided that the following conditions are met:
 *
 *  o Redistributions of source code must retain the above copyright notice,
 *    this list of conditions and the following disclaimer.
 *
 *  o Redistributions in binary form must reproduce the above copyright notice,
 *    this list of conditions and the following disclaimer in the documentation
 *    and/or other materials provided with the distribution.
 *
 *  o Neither the name of imagero Andrey Kuznetsov nor the names of
 *    its contributors may be used to endorse or promote products derived
 *    from this software without specific prior written permission.
 *
 * THIS SOFTWARE IS PROVIDED BY THE COPYRIGHT HOLDERS AND CONTRIBUTORS "AS IS"
 * AND ANY EXPRESS OR IMPLIED WARRANTIES, INCLUDING, BUT NOT LIMITED TO,
 * THE IMPLIED WARRANTIES OF MERCHANTABILITY AND FITNESS FOR A PARTICULAR
 * PURPOSE ARE DISCLAIMED. IN NO EVENT SHALL THE COPYRIGHT OWNER OR
 * CONTRIBUTORS BE LIABLE FOR ANY DIRECT, INDIRECT, INCIDENTAL, SPECIAL,
 * EXEMPLARY, OR CONSEQUENTIAL DAMAGES (INCLUDING, BUT NOT LIMITED TO,
 * PROCUREMENT OF SUBSTITUTE GOODS OR SERVICES; LOSS OF USE, DATA, OR PROFITS;
 * OR BUSINESS INTERRUPTION) HOWEVER CAUSED AND ON ANY THEORY OF LIABILITY,
 * WHETHER IN CONTRACT, STRICT LIABILITY, OR TORT (INCLUDING NEGLIGENCE
 * OR OTHERWISE) ARISING IN ANY WAY OUT OF THE USE OF THIS SOFTWARE,
 * EVEN IF ADVISED OF THE POSSIBILITY OF SUCH DAMAGE.
 */
package com.smartg.color.function;

import com.smartg.function.IFunction;
import com.smartg.function.misc.Range;

/**
 * Parametric curve function. Valid parameter counts are: 1, 3, 4, 5 and 7.
 * Function type depends on parameter count:
 * 
 * <pre>
 *   count         parameter               function
 *   1             k                       Y = X^k
 *   3	        k, a, b                 Y = (aX + B)^k for (X >= -b/a), Y = 0 for (X < -b/a)
 *   4	        k, a, b, c              Y = (aX + B)^k + c for (X >= -b/a), Y = c for (X < -b/a)
 *   5	        k, a, b, c, d           Y = (aX + B)^k for (X >= d), Y = cX for (X < d)
 *   7	        k, a, b, c, d, e, f     Y = (aX + B)^k + e for (X >= d), Y = cX + f for (X < d)
 * </pre>
 * 
 * This function has one input and one output. Input and output values are both
 * in range from 0.0 to 1.0
 * 
 * @author andrey
 * 
 */
public class ParametricCurve extends com.smartg.icc.ParametricCurve implements
		IFunction {

	protected Range[] domain = new Range[] { new Range(0.0f, 1.0f) };
	protected Range[] range = new Range[] { new Range(0.0f, 1.0f) };

	/**
	 * 
	 * @param params
	 *            valid parameter counts are: 1, 3, 4, 5 and 7
	 */
	public ParametricCurve(float[] params) {
		super(getFunctionType(params), params);
	}

	/**
	 * @param output
	 *            output values (single value in range from 0.0 to 1.0)
	 * @param input
	 *            input values (single value in range from 0.0 to 1.0)
	 */
	public void compute(float[] output, float ... input) {
		output[0] = get(input[0]);
	}

	private static int getFunctionType(float[] params) {
		switch (params.length) {
		case 1:
			return 0;
		case 3:
			return 1;
		case 4:
			return 2;
		case 5:
			return 3;
		case 7:
			return 4;
		default:
			throw new IllegalArgumentException();
		}
	}

	public int getNumInputs() {
		return 1;
	}

	public int getNumOutputs() {
		return 1;
	}

	public Range[] getInputDomain() {
		return domain;
	}

	public Range[] getOutputRange() {
		return range;
	}
}
