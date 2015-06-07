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

public class CalGray extends IColorSpace {

    private static final long serialVersionUID = -8342841348127096411L;
    double[] whitePoint;
    double Xw, Yw, Zw;
    double[] blackPoint;
    double gamma;

    public CalGray(double[] whitePoint, double[] blackPoint, double gamma) {
	super(TYPE_GRAY, 1);
	this.whitePoint = whitePoint;

	Xw = whitePoint[0];
	Yw = whitePoint[1];
	Zw = whitePoint[2];

	if (blackPoint == null || blackPoint.length == 0) {
	    blackPoint = new double[] { 0.0, 0.0, 0.0 };
	}
	this.blackPoint = blackPoint;
	this.gamma = gamma;
    }

    public Color getColor(float[] d) {
	return new Color(this, d, 1.0f);
//	double a = d[0];
//	double aG = Math.pow(a, gamma);
//
//	double X = Xw * aG;
//	double Y = Yw * aG;
//	double Z = Zw * aG;
//
//	float[] xyz = { (float) X, (float) Y, (float) Z };
//
//	float[] rgb = srgb.fromCIEXYZ(xyz);
//
//	int r = (int) (rgb[0] * 255);
//	int g = (int) (rgb[1] * 255);
//	int b = (int) (rgb[2] * 255);
//
//	return new Color(r, g, b);
    }

    public float[] toRGB(float[] colorvalue) {
	float a = colorvalue[0];
	return new float[] {a, a, a};
//	double aG = Math.pow(a, gamma);
//
//	double X = Xw * aG;
//	double Y = Yw * aG;
//	double Z = Zw * aG;
//
//	float[] xyz = { (float) X, (float) Y, (float) Z };
//
//	return srgb.fromCIEXYZ(xyz);
    }

    public float[] toCIEXYZ(float[] colorvalue) {
	double a = colorvalue[0];
	double aG = Math.pow(a, gamma);

	double X = Xw * aG;
	double Y = Yw * aG;
	double Z = Zw * aG;

	float[] xyz = { (float) X, (float) Y, (float) Z };

	return xyz;
    }

    public int getComponentCount() {
	return 1;
    }
}