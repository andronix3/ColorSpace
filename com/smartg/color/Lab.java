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

public class Lab extends IColorSpace {

	// DictionaryObject cdict;

	private static final long serialVersionUID = 710455401502094111L;

	double[] whitePoint;
	double XW, YW, ZW;
	double[] blackPoint;
	double[] range;

	ReferenceWhite refWhite;

	public Lab(WhitePoint wp) {
		this(wp.getValues());
	}

	public Lab(double[] whitePoint) {
		this(whitePoint, null, null);
	}

	public Lab(double[] whitePoint, double[] range) {
		this(whitePoint, null, range);
	}

	public Lab(double[] whitePoint, double[] blackPoint, double[] range) {
		super(TYPE_Lab, 3);
		this.whitePoint = whitePoint;
		XW = whitePoint[0];
		YW = whitePoint[1];
		ZW = whitePoint[2];

		refWhite = new ReferenceWhite(new WhitePoint(XW, YW, ZW));

		if (blackPoint == null || blackPoint.length == 0) {
			blackPoint = new double[] { 0.0, 0.0, 0.0 };
		}
		this.blackPoint = blackPoint;

		if (range == null || range.length == 0) {
			range = new double[] { -100, 100, -100, 100 };
		}
		this.range = range;
	}

	public Color getColor(float[] d) {
		double lab_l = d[0];
		double lab_a = d[1];
		double lab_b = d[2];

		double M = (lab_l + 16) / 116.0;
		double L = M + (lab_a / 500.0);
		double N = M - (lab_b / 200.0);

		double X, Y, Z;

		double max = 6.0 / 29.0;

		if (L >= max) {
			X = XW * L * L * L;
		} else {
			X = XW * (104.0 / 841.0) * (L - 4.0 / 29.0);
		}

		if (M >= max) {
			Y = YW * M * M * M;
		} else {
			Y = YW * (104.0 / 841.0) * (M - 4.0 / 29.0);
		}

		if (N >= max) {
			Z = ZW * N * N * N;
		} else {
			Z = ZW * (104.0 / 841.0) * (N - 4.0 / 29.0);
		}

		float[] xyz = { (float) X, (float) Y, (float) Z };

		float[] rgb = srgb.fromCIEXYZ(xyz);

		int r = (int) (rgb[0] * 255);
		int g = (int) (rgb[1] * 255);
		int b = (int) (rgb[2] * 255);

		return new Color(r, g, b);
	}

	public float[] toRGB(float[] colorvalue) {
		double lab_l = colorvalue[0];
		double lab_a = colorvalue[1];
		double lab_b = colorvalue[2];

		double M = (lab_l + 16) / 116.0;
		double L = M + (lab_a / 500.0);
		double N = M - (lab_b / 200.0);

		double X, Y, Z;

		double max = 6.0 / 29.0;

		if (L >= max) {
			X = XW * L * L * L;
		} else {
			X = XW * (104.0 / 841.0) * (L - 4.0 / 29.0);
		}

		if (M >= max) {
			Y = YW * M * M * M;
		} else {
			Y = YW * (104.0 / 841.0) * (M - 4.0 / 29.0);
		}

		if (N >= max) {
			Z = ZW * N * N * N;
		} else {
			Z = ZW * (104.0 / 841.0) * (N - 4.0 / 29.0);
		}

		float[] xyz = { (float) X, (float) Y, (float) Z };

		return srgb.fromCIEXYZ(xyz);
	}

	public float[] toCIEXYZ(float[] colorvalue) {
		double lab_l = colorvalue[0];
		double lab_a = colorvalue[1];
		double lab_b = colorvalue[2];

		double M = (lab_l + 16) / 116.0;
		double L = M + (lab_a / 500.0);
		double N = M - (lab_b / 200.0);

		double X, Y, Z;

		double max = 6.0 / 29.0;

		if (L >= max) {
			X = XW * L * L * L;
		} else {
			X = XW * (104.0 / 841.0) * (L - 4.0 / 29.0);
		}

		if (M >= max) {
			Y = YW * M * M * M;
		} else {
			Y = YW * (104.0 / 841.0) * (M - 4.0 / 29.0);
		}

		if (N >= max) {
			Z = ZW * N * N * N;
		} else {
			Z = ZW * (104.0 / 841.0) * (N - 4.0 / 29.0);
		}

		float[] xyz = { (float) X, (float) Y, (float) Z };
		return xyz;
	}

	private static final double KAPPA = 24389.0 / 27.0;
	// private static final double k7787 = 24389.0 / 27.0 / 116.0;
	private static final double ETA = 216.0 / 24389.0;
	// private static final double ke = KAPPA * ETA;
	private static final double w = 1.0 / 3.0;

	@Override
	public float[] fromCIEXYZ(float[] cv) {

		double xr = cv[0] / (refWhite.Xn);
		double yr = cv[1] / (refWhite.Yn);
		double zr = cv[2] / (refWhite.Zn);
		double fx, fy, fz;

		float[] dest = new float[3];

		if (xr > ETA) {
			fx = Math.pow(xr, w);
		} else {
			fx = (KAPPA * xr + 16.0) / 116.0;
		}

		if (yr > ETA) {
			fy = Math.pow(yr, w);
		} else {
			fy = (KAPPA * yr + 16.0) / 116.0;
		}

		if (zr > ETA) {
			fz = Math.pow(zr, w);
		} else {
			fz = (KAPPA * zr + 16.0) / 116.0;
		}

		float L = (float) (116 * fy - 16);
		float a = (float) (500 * (fx - fy));
		float b = (float) (200 * (fy - fz));

		dest[0] = L;
		dest[1] = a;
		dest[2] = b;
		return dest;
	}

	public int getComponentCount() {
		return 3;
	}
}