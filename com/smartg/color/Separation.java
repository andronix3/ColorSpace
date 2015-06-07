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
package com.smartg.color;

import java.awt.Color;

import com.imagero.function.Function;
import com.imagero.reader.pdf.PdfObject.NameObject;

public class Separation extends IColorSpace {

    private static final long serialVersionUID = 384616434624598282L;
    Function function;
    IColorSpace alternateCS;
    NameObject name;

    public Separation(IColorSpace alternateCS, NameObject name, Function function) {
	super(TYPE_2CLR, function.getNumInputs());
	this.name = name;
	this.alternateCS = alternateCS;
	this.function = function;
    }

    public Color getColor(float[] d) {
	float[] res = new float[function.getNumOutputs()];
	function.compute(res, d);
	return alternateCS.getColor(res);
    }

    public float[] toRGB(float[] colorvalue) {
	float[] res = new float[function.getNumOutputs()];
	function.compute(res, colorvalue);
	return alternateCS.toRGB(res);
    }

    public int getComponentCount() {
	return alternateCS.getComponentCount();
    }
}