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

import com.smartg.color.RGB_ColorSpace;
import com.smartg.function.Function;
import com.smartg.function.IFunction;
import com.smartg.function.misc.Range;

/**
 * LabFunction allows to apply arbitrary function to image pixels in Lab color
 * space. The outcome of function for each channel may be controlled by
 * <code>amount</code>. The function must have 3 inputs and 3 outputs.
 * 
 * @author andrey
 * 
 */
public class LabFunction extends Function {
	float[] xyz = new float[3];
	float[] lab = new float[3];

	RGB_ColorSpace color = RGB_ColorSpace.createSRGB(8);
	IFunction function;

	float[] amount = { 1f, 1f, 1f };

	public LabFunction(IFunction func) {
		this(RGB_ColorSpace.createSRGB(8), func);
	}

	public LabFunction(RGB_ColorSpace color, IFunction func) {
		this.color = color;
		this.function = func;
		setDomain(createDomain());
		setRange(createRange());
	}

	public LabFunction(RGB_ColorSpace color, IFunction func, float amount) {
		this.color = color;
		this.function = func;
		setAmount(amount);
		setDomain(createDomain());
		setRange(createRange());
	}

	public LabFunction(RGB_ColorSpace color, IFunction func, float[] amount) {
		this.color = color;
		this.function = func;
		setAmount(amount);
		setDomain(createDomain());
		setRange(createRange());
	}

	public LabFunction(IFunction func, float[] amount) {
		this.function = func;
		setAmount(amount);
		setDomain(createDomain());
		setRange(createRange());
	}

	public LabFunction(IFunction func, float amount) {
		this.function = func;
		setAmount(amount);
		setDomain(createDomain());
		setRange(createRange());
	}

	public void compute(float[] output, float ... input) {
		int r0 = (int) input[0];
		int g0 = (int) input[1];
		int b0 = (int) input[2];

		color.rgb2xyz(r0, g0, b0, xyz);
		color.xyz2lab(xyz[0], xyz[1], xyz[2], lab);

		float labL = lab[0] / 100f;
		float labA = lab[1] / 127f;
		float labB = lab[2] / 127f;

		function.compute(output, labL, labA, labB);

		labL = input[0] + (output[0] - input[0]) * amount[0];
		labA = input[1] + (output[1] - input[1]) * amount[1];
		labB = input[2] + (output[2] - input[2]) * amount[2];

		labL *= 100;
		labA *= 127;
		labB *= 127;

		color.lab2xyz(labL, labA, labB, xyz);
		color.xyz2rgb(xyz[0], xyz[1], xyz[2], output);
	}

	public float getAmountAt(int index) {
		return amount[index];
	}

	public void setAmount(float[] amount) {
		if (amount.length == 1) {
			this.amount[0] = amount[0];
			this.amount[1] = amount[0];
			this.amount[2] = amount[0];
		} else {
			this.amount[0] = amount[0];
			this.amount[1] = amount[1];
			this.amount[2] = amount[2];
		}
	}

	public void setAmount(float amount) {
		this.amount[0] = amount;
		this.amount[1] = amount;
		this.amount[2] = amount;
	}

	public IFunction getFunction() {
		return function;
	}

	public void setFunction(IFunction function) {
		this.function = function;
	}

	public void setAmountAt(int index, float amount) {
		this.amount[index] = amount;
	}

	private Range[] createDomain() {
		return new Range[] { new Range(0f, 255f), new Range(0f, 255f),
				new Range(0f, 255f) };
	}

	private Range[] createRange() {
		return new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) };
	}
}