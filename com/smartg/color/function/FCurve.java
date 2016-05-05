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
 * Simplified sampled function with one input and one output. Input and output
 * values are both in range from 0.0 to 1.0
 * 
 * @author andrey
 * 
 */
public class FCurve extends com.smartg.icc.Curve implements IFunction {

	protected Range[] domain = new Range[] { new Range(0.0f, 1.0f) };
	protected Range[] range = new Range[] { new Range(0.0f, 1.0f) };

	/**
	 * Curve constructor
	 * 
	 * @param values
	 *            If values is null or values.length is 0 than it is identity
	 *            function. If values.length is 1 than it is a gamma function.
	 *            Otherwise it is a curve function.
	 * 
	 */
	public FCurve(float[] values) {
		super(values);
	}

	@Override
	public void setValues(float[] values) {
		super.setValues(values);
	}

	public void compute(float[] output, float ... input) {
		output[0] = get(input[0]);
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
