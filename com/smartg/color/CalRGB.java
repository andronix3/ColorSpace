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

public class CalRGB extends IColorSpace {

    // DictionaryObject cdict;

    private static final long serialVersionUID = -1461750799997504247L;

    @SuppressWarnings("unused")
    private double[] whitePoint;
    @SuppressWarnings("unused")
    private double XW, YW, ZW;
    @SuppressWarnings("unused")
    private double[] blackPoint;
    private double[] matrix;
    private double[] gamma;
    private double GR, GG, GB;
    private double XA, YA, ZA, XB, YB, ZB, XC, YC, ZC;
    private boolean makeNormalization;

    public CalRGB(WhitePoint wp) {
	this(wp.getValues());
    }

    public CalRGB(double[] whitePoint) {
	this(whitePoint, null, null, null);
    }

    public CalRGB(double[] whitePoint, double[] gamma) {
	this(whitePoint, gamma, null, null);
    }

    public CalRGB(double[] whitePoint, double[] gamma, double[] matrix) {
	this(whitePoint, gamma, null, matrix);
    }

    public CalRGB(double[] whitePoint, double[] blackPoint, double[] gamma, double[] matrix) {
	super(TYPE_RGB, 3);

	this.whitePoint = whitePoint;
	this.gamma = gamma;
	this.matrix = matrix;
	this.blackPoint = blackPoint;

	XW = whitePoint[0];
	YW = whitePoint[1];
	ZW = whitePoint[2];

	init();
    }

    public Color getColor(float[] d) {
	// int r = (int) (d[0] * 255);
	// int g = (int) (d[1] * 255);
	// int b = (int) (d[2] * 255);
	return new Color(this, d, 1.0f);
	// double A = d[0];
	// double B = d[1];
	// double C = d[2];
	//
	// double ag = Math.pow(A, GR);
	// double bg = Math.pow(B, GG);
	// double cg = Math.pow(C, GB);
	//
	// double X = XA * ag + XB * bg + XC * cg;
	// double Y = YA * ag + YB * bg + YC * cg;
	// double Z = ZA * ag + ZB * bg + ZC * cg;
	//
	// float[] xyz = { (float) X, (float) Y, (float) Z };
	//
	// float[] rgb = srgb.fromCIEXYZ(xyz);
	//
	// int r = (int) (rgb[0] * 255);
	// int g = (int) (rgb[1] * 255);
	// int b = (int) (rgb[2] * 255);
	//
	// return new Color(r, g, b);
    }

    protected void init() {
	if (gamma == null || gamma.length == 0) {
	    gamma = new double[] { 1.0, 1.0, 1.0 };
	}
	GR = gamma[0];
	GG = gamma[1];
	GB = gamma[2];

	if (matrix == null || matrix.length == 0) {
	    matrix = new double[] { 1.0, 0.0, 0.0, 0.0, 1.0, 0.0, 0.0, 0.0, 1.0 };
	}
	XA = matrix[0];
	YA = matrix[1];
	ZA = matrix[2];
	XB = matrix[3];
	YB = matrix[4];
	ZB = matrix[5];
	XC = matrix[6];
	YC = matrix[7];
	ZC = matrix[8];
    }


    public boolean isMakeNormalization() {
	return makeNormalization;
    }

    public void setMakeNormalization(boolean makeNormalization) {
	this.makeNormalization = makeNormalization;
    }

    public float[] toRGB(float[] colorvalue) {
	if(makeNormalization) {
	    for(int i = 0; i < colorvalue.length; i++) {
		colorvalue[i] /= 255.0;
	    }
	}
	return colorvalue;
	// double A = colorvalue[0];
	// double B = colorvalue[1];
	// double C = colorvalue[2];
	//
	// double ag = Math.pow(A, GR);
	// double bg = Math.pow(B, GG);
	// double cg = Math.pow(C, GB);
	//
	// double X = XA * ag + XB * bg + XC * cg;
	// double Y = YA * ag + YB * bg + YC * cg;
	// double Z = ZA * ag + ZB * bg + ZC * cg;
	//
	// float[] xyz = { (float) X, (float) Y, (float) Z };
	//
	// return srgb.fromCIEXYZ(xyz);
    }

    public float[] toCIEXYZ(float[] colorvalue) {
	double A = colorvalue[0];
	double B = colorvalue[1];
	double C = colorvalue[2];

	double ag = Math.pow(A, GR);
	double bg = Math.pow(B, GG);
	double cg = Math.pow(C, GB);

	double X = XA * ag + XB * bg + XC * cg;
	double Y = YA * ag + YB * bg + YC * cg;
	double Z = ZA * ag + ZB * bg + ZC * cg;

	float[] xyz = { (float) X, (float) Y, (float) Z };
	return xyz;
    }

    public int getComponentCount() {
	return 3;
    }
}