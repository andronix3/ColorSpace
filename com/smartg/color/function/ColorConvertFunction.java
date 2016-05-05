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
import com.smartg.function.IFunction;
import com.smartg.function.misc.Range;

public abstract class ColorConvertFunction implements IFunction {

    RGB_ColorSpace color;
    Range[] domain;
    Range[] range;

    protected ColorConvertFunction(RGB_ColorSpace color, Range[] domain, Range[] range) {
	this.domain = domain.clone();
	this.range = range.clone();
	this.color = color;
    }

    public Range[] getInputDomain() {
	return domain.clone();
    }

    public Range[] getOutputRange() {
	return domain.clone();
    }

    public static class LCHab2XYZ extends ColorConvertFunction {

	public LCHab2XYZ(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 1), new Range(-1, 1), new Range(-1, 1) }, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.lchab2xyz(input[0], input[1], input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class Lab2XYZ extends ColorConvertFunction {

	public Lab2XYZ(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 1), new Range(-1, 1), new Range(-1, 1) }, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.lab2xyz(input[0], input[1], input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class XYZ2RGB extends ColorConvertFunction {

	public XYZ2RGB(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) }, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.xyz2rgb(input[0], input[1], input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class Lab2RGB extends ColorConvertFunction {

	public Lab2RGB(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 1), new Range(-1, 1), new Range(-1, 1) }, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.lab2rgb(input[0], input[1], input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class RGB2XYZ extends ColorConvertFunction {

	public RGB2XYZ(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 255), new Range(0, 255), new Range(0, 255) }, new Range[] { new Range(0, 1), new Range(0, 1),
		    new Range(0, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.rgb2xyz((int) input[0], (int) input[1], (int) input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class RGB2Lab extends ColorConvertFunction {

	public RGB2Lab(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 255), new Range(0, 255), new Range(0, 255) }, new Range[] { new Range(0, 1), new Range(-1, 1),
		    new Range(-1, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.rgb2lab((int) input[0], (int) input[1], (int) input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class XYZ2Lab extends ColorConvertFunction {

	public XYZ2Lab(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) }, new Range[] { new Range(0, 1), new Range(-1, 1), new Range(-1, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.xyz2lab(input[0], input[1], input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class XYZ2Yxy extends ColorConvertFunction {

	public XYZ2Yxy(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) }, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.xyz2Yxy(input[0], input[1], input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }

    public static class Yxy2XYZ extends ColorConvertFunction {

	public Yxy2XYZ(RGB_ColorSpace color) {
	    super(color, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) }, new Range[] { new Range(0, 1), new Range(0, 1), new Range(0, 1) });
	}

	public void compute(float[] output, float... input) {
	    color.Yxy2xyz(input[0], input[1], input[2], output);
	}

	public int getNumInputs() {
	    return 3;
	}

	public int getNumOutputs() {
	    return 3;
	}
    }
}
