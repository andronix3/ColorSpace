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
 * Some commonly used functions for color processing. All calls are redirected
 * to ColorFunctions, so it is probably better to use ColorFunctions direct.
 * 
 * @author andrey
 * 
 */
public enum EColorFunction implements IFunction {
	ColorBurn(new ColorFunction.ColorBurn()), //
	ColorDodge(new ColorFunction.ColorDodge()), //
	Darken(new ColorFunction.Darken()), //
	Difference(new ColorFunction.Difference()), //
	Exclusion(new ColorFunction.Exclusion()), //
	Hardlight(new ColorFunction.Hardlight()), //
	Lighten(new ColorFunction.Lighten()), //
	Multiply(new ColorFunction.Multiply()), //
	Overlay(new ColorFunction.Overlay()), //
	Screen(new ColorFunction.Screen()), //
	Softlight(new ColorFunction.Softlight()), //
	Distance(new ColorFunction.Distance()), //
	Add(new ColorFunction.Add()), //
	Sub(new ColorFunction.Sub())//
	;

	private final ColorFunction sf;

	private Range[] domain = new Range[] { new Range(0f, 1f) };
	private Range[] range = new Range[] { new Range(0f, 1f) };

	private EColorFunction(ColorFunction sf) {
		this.sf = sf;
	}

	public final void compute(float[] output, float... input) {
		sf.compute(output, input);
	}

	public Range[] getInputDomain() {
		return domain;
	}

	public int getNumInputs() {
		return 2;
	}

	public int getNumOutputs() {
		return 1;
	}

	public Range[] getOutputRange() {
		return range;
	}
}
