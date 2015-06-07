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

public class Decode extends IColorSpace {

    private static final long serialVersionUID = 746833503496538278L;
    float[] decode;
    IColorSpace colorSpace;
    int bitsPerSample;

    public Decode(float[] decode, IColorSpace cs, int bitsPerSample) {
	super(cs.getType(), cs.getNumComponents());
	this.decode = decode;
	this.bitsPerSample = bitsPerSample;
	this.colorSpace = cs;

    }

    public Color getColor(float[] d) {
	float[] d2 = new float[d.length];
	decode(d, d2);
	return colorSpace.getColor(d2);
    }

    private void decode(float[] src, float[] dst) {
	for (int i = 0; i < src.length; i++) {
	    float x = src[i];
	    int index = i * 2;
	    if (index < decode.length - 1) {
		float Dmin = decode[index];
		float Dmax = decode[index + 1];

		float xmax = (1 << bitsPerSample) - 1;
		x = x * xmax;
		dst[i] = interpolate(x, 0, xmax, Dmin, Dmax) / Dmax;
	    }
	}
    }

    private static float interpolate(float x, float xmin, float xmax, float ymin, float ymax) {
	return ymin + ((x - xmin) * ((ymax - ymin) / (xmax - xmin)));
    }

    public int getComponentCount() {
	return colorSpace.getComponentCount();
    }

    public float[] toRGB(float[] colorvalue) {
	float[] d2 = new float[colorvalue.length];
	decode(colorvalue, d2);
	return colorSpace.toRGB(d2);
    }
}