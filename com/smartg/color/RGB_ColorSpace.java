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

import java.awt.color.ICC_ProfileRGB;
import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * @author Andrey Kuznetsov
 */
public class RGB_ColorSpace {

    public static enum RGB_CS_Type {
	ADOBE_RGB, APPLE_RGB, BEST_RGB, BETA_RGB, BRUCE_RGB, CIE_RGB, COLOR_MATCH_RGB, DON_RGB4, ECI_RGB, EKTA_SPACE_PS5, NTSC_RGB, PAL_SECAM_RGB, PRO_PHOTO, SMPTE_C_RGB, S_RGB, WIDE_GAMUT, OPTI_RGB;
    }

    String type;

    float gamma;
    float igamma;

    int[] bitsPerSample;

    int maxR, maxG, maxB;

    ColorMatrix primaries;
    ColorMatrix fromXYZ;
    ColorMatrix toXYZ;

    // WhitePoint wp;
    public final ReferenceWhite refWhite;

    GammaTables Yr;
    GammaTables Yg;
    GammaTables Yb;

    double u0, v0;

    boolean is_sRGB;

    public static RGB_ColorSpace get(RGB_CS_Type type) {
	switch (type) {
	case ADOBE_RGB:
	    return Colors.ADOBE_RGB;
	case APPLE_RGB:
	    return Colors.APPLE_RGB;
	case BEST_RGB:
	    return Colors.BEST_RGB;
	case BETA_RGB:
	    return Colors.BETA_RGB;
	case BRUCE_RGB:
	    return Colors.BRUCE_RGB;
	case CIE_RGB:
	    return Colors.CIE_RGB;
	case COLOR_MATCH_RGB:
	    return Colors.COLOR_MATCH_RGB;
	case DON_RGB4:
	    return Colors.DON_RGB4;
	case ECI_RGB:
	    return Colors.ECI_RGB;
	case EKTA_SPACE_PS5:
	    return Colors.EKTA_SPACE_PS5;
	case NTSC_RGB:
	    return Colors.NTSC_RGB;
	case OPTI_RGB:
	    return Colors.OPTI_RGB;
	case PAL_SECAM_RGB:
	    return Colors.PAL_SECAM_RGB;
	case PRO_PHOTO:
	    return Colors.PRO_PHOTO;
	case SMPTE_C_RGB:
	    return Colors.SMPTE_C_RGB;
	case S_RGB:
	    return Colors.S_RGB;
	case WIDE_GAMUT:
	    return Colors.WIDE_GAMUT;
	default:
	    return null;
	}
    }

    public RGB_ColorSpace(String type, ICC_ProfileRGB icc, ReferenceWhite rf) {
	this(type, new ColorMatrix(icc.getMatrix()), rf, getGamma(icc));
    }

    public RGB_ColorSpace(String type, ICC_ProfileRGB icc) {
	this(type, new ColorMatrix(icc.getMatrix()), new ReferenceWhite(icc.getMediaWhitePoint()), getGamma(icc));
    }

    private static float getGamma(ICC_ProfileRGB icc) {
	try {
	    float gamma = icc.getGamma(0);
	    return gamma;
	} catch (Throwable t) {
	    return 1.0f;
	}
    }

    public double[] getMatrix() {
	return primaries.getMatrix();
    }

    public double[] getGamma() {
	return new double[] { gamma, gamma, gamma };
    }

    public RGB_ColorSpace(String type, ColorMatrix toXYZ, ReferenceWhite wp, float gamma) {
	this(type, gamma, computePrimaries(toXYZ), toXYZ.inverse(), toXYZ, wp, new int[] { 8, 8, 8 });
    }

    protected RGB_ColorSpace(String type, ColorMatrix fromXYZ, ColorMatrix toXYZ, ReferenceWhite wp, float gamma) {
	this(type, gamma, computePrimaries(toXYZ), fromXYZ, toXYZ, wp, new int[] { 8, 8, 8 });
    }

    protected RGB_ColorSpace(String type, ColorMatrix toXYZ, ReferenceWhite wp, float gamma, int[] bps) {
	this(type, gamma, computePrimaries(toXYZ), toXYZ.inverse(), toXYZ, wp, bps);
    }

    protected RGB_ColorSpace(String type, ColorMatrix fromXYZ, ColorMatrix toXYZ, ReferenceWhite wp, float gamma, int[] bps) {
	this(type, gamma, computePrimaries(toXYZ), fromXYZ, toXYZ, wp, bps);
    }

    protected RGB_ColorSpace(String type, float gamma, ColorMatrix primaries, ColorMatrix fromXYZ, ColorMatrix toXYZ, ReferenceWhite refWhite, int[] bps) {
	this.type = type;
	this.gamma = gamma;
	this.igamma = 1.0f / gamma;
	this.primaries = primaries;
	this.fromXYZ = fromXYZ;
	this.toXYZ = toXYZ;
	this.bitsPerSample = bps.clone();

	maxR = (1 << bitsPerSample[0]) - 1;
	maxG = (1 << bitsPerSample[1]) - 1;
	maxB = (1 << bitsPerSample[2]) - 1;

	this.refWhite = refWhite;

	Yr = GammaTables.create(gamma, bps[0]);
	Yg = GammaTables.create(gamma, bps[1]);
	Yb = GammaTables.create(gamma, bps[2]);

	u0 = 4 * refWhite.Xn / (refWhite.Xn + 15 * refWhite.Yn + 3 * refWhite.Zn);
	v0 = 9 * refWhite.Yn / (refWhite.Xn + 15 * refWhite.Yn + 3 * refWhite.Zn);
    }

    private RGB_ColorSpace(String type, ColorMatrix primaries, ColorMatrix fromXYZ, ColorMatrix toXYZ, ReferenceWhite wp) {
	this.type = "Linear" + (type != null ? type : "");
	this.gamma = 1.0f;
	this.primaries = primaries;
	this.fromXYZ = fromXYZ;
	this.toXYZ = toXYZ;
	// this.wp = wp;
	this.bitsPerSample = new int[] { 16, 16, 16 };

	maxR = (1 << bitsPerSample[0]) - 1;
	maxG = (1 << bitsPerSample[1]) - 1;
	maxB = (1 << bitsPerSample[2]) - 1;

	refWhite = wp;

	Yr = GammaTables.createLinear();
	Yg = Yr;
	Yb = Yr;

	u0 = 4 * refWhite.Xn / (refWhite.Xn + 15 * refWhite.Yn + 3 * refWhite.Zn);
	v0 = 9 * refWhite.Yn / (refWhite.Xn + 15 * refWhite.Yn + 3 * refWhite.Zn);
    }

    protected RGB_ColorSpace(RGB_ColorSpace rgbColor, int[] bps) {
	this.type = rgbColor.type;
	this.gamma = rgbColor.gamma;
	this.fromXYZ = rgbColor.fromXYZ;
	this.toXYZ = rgbColor.toXYZ;
	this.primaries = rgbColor.primaries;
	// this.wp = rgbColor.wp;
	this.bitsPerSample = bps.clone();

	maxR = (1 << bitsPerSample[0]) - 1;
	maxG = (1 << bitsPerSample[1]) - 1;
	maxB = (1 << bitsPerSample[2]) - 1;

	refWhite = rgbColor.refWhite;

	if (type.equals("sRGB")) {
	    Yr = GammaTables.createSRGB(bps[0]);
	    Yg = GammaTables.createSRGB(bps[1]);
	    Yb = GammaTables.createSRGB(bps[2]);
	} else {
	    Yr = GammaTables.create(gamma, bps[0]);
	    Yg = GammaTables.create(gamma, bps[1]);
	    Yb = GammaTables.create(gamma, bps[2]);
	}
	u0 = rgbColor.u0;
	v0 = rgbColor.v0;
    }

    private RGB_ColorSpace(int[] bps) {
	this(bps, new ReferenceWhite(WhitePoint.D65));
    }

    private RGB_ColorSpace(int[] bps, ReferenceWhite refWhite) {
	this.type = "sRGB";
	is_sRGB = true;

	this.gamma = 2.4f;
	this.igamma = 1.0f / 2.4f;
	this.fromXYZ = Colors.srgb_toXYZ.inverse();
	this.toXYZ = Colors.srgb_toXYZ;
	this.primaries = computePrimaries(toXYZ);
	// this.wp = WhitePoint.D65;
	this.bitsPerSample = bps.clone();

	maxR = (1 << bitsPerSample[0]) - 1;
	maxG = (1 << bitsPerSample[1]) - 1;
	maxB = (1 << bitsPerSample[2]) - 1;

	// refWhite = new ReferenceWhite(WhitePoint.D65);
	this.refWhite = refWhite;

	Yr = GammaTables.createSRGB(bps[0]);
	Yg = GammaTables.createSRGB(bps[1]);
	Yb = GammaTables.createSRGB(bps[2]);

	u0 = 4 * refWhite.Xn / (refWhite.Xn + 15 * refWhite.Yn + 3 * refWhite.Zn);
	v0 = 9 * refWhite.Yn / (refWhite.Xn + 15 * refWhite.Yn + 3 * refWhite.Zn);
    }

    public RGB_ColorSpace(String type, ColorMatrix toXYZ, ColorMatrix fromXYZ, float gamma, int[] bps) {
	this(type, gamma, computePrimaries(toXYZ), fromXYZ, toXYZ, computeWhitePoint(toXYZ), bps);
    }

    public static RGB_ColorSpace createSRGB(int bps) {
	return new RGB_ColorSpace(new int[] { bps, bps, bps });
    }

    public static RGB_ColorSpace createSRGB(int bps, ReferenceWhite refWhite) {
	return new RGB_ColorSpace(new int[] { bps, bps, bps }, refWhite);
    }

    public static RGB_ColorSpace create(RGB_ColorSpace rgbColor, int[] bps) {
	return new RGB_ColorSpace(rgbColor, bps);
    }

    public static RGB_ColorSpace create(String type, ColorMatrix primaries, ReferenceWhite wp, float gamma, int[] bps) {
	ColorMatrix toXYZ = compute_toXYZ(primaries, wp);
	ColorMatrix fromXYZ = toXYZ.inverse();
	return new RGB_ColorSpace(type, gamma, primaries, fromXYZ, toXYZ, wp, bps);
    }

    public static RGB_ColorSpace createLinear(String type, ColorMatrix primaries, ReferenceWhite wp) {
	ColorMatrix toXYZ = compute_toXYZ(primaries, wp);
	ColorMatrix fromXYZ = toXYZ.inverse();
	return new RGB_ColorSpace(type, primaries, fromXYZ, toXYZ, wp);
    }

    public static RGB_ColorSpace createLinear(RGB_ColorSpace rgbColor) {
	return new RGB_ColorSpace(rgbColor.type, rgbColor.primaries, rgbColor.fromXYZ, rgbColor.toXYZ, rgbColor.refWhite);
    }

    public static RGB_ColorSpace createLinear(String type, ColorMatrix fromXYZ, ColorMatrix toXYZ, ReferenceWhite wp) {
	return new RGB_ColorSpace(type, computePrimaries(toXYZ), fromXYZ, toXYZ, wp);
    }

    public int getSamplesPerPixel() {
	return bitsPerSample.length;
    }

    static private ReferenceWhite computeWhitePoint(ColorMatrix toXYZ) {
	double wx = toXYZ.Xs / (toXYZ.Xs + toXYZ.Ys + toXYZ.Zs);
	double wy = toXYZ.Ys / (toXYZ.Xs + toXYZ.Ys + toXYZ.Zs);
	double wz = toXYZ.Zs / (toXYZ.Xs + toXYZ.Ys + toXYZ.Zs);

	return new ReferenceWhite(new WhitePoint(wx, wy, wz));
    }

    static private ColorMatrix compute_toXYZ(ColorMatrix primaries, ReferenceWhite refWhite) {
	double xr = primaries.Rx;
	double yr = primaries.Ry;
	double xg = primaries.Gx;
	double yg = primaries.Gy;
	double xb = primaries.Bx;
	double yb = primaries.By;
	return compute_toXYZ(xr, yr, xg, yg, xb, yb, refWhite);
    }

    static private ColorMatrix compute_toXYZ(double xr, double yr, double xg, double yg, double xb, double yb, ReferenceWhite refWhite) {
	double Xr = xr / yr;
	double Yr = 1;
	double Zr = (1 - xr - yr) / yr;

	double Xg = xg / yg;
	double Yg = 1;
	double Zg = (1 - xg - yg) / yg;

	double Xb = xb / yb;
	double Yb = 1;
	double Zb = (1 - xb - yb) / yb;

	double Sr = refWhite.Xn * Xr + refWhite.Xn * Yr + refWhite.Xn * Zr;
	double Sg = refWhite.Yn * Xg + refWhite.Yn * Yg + refWhite.Yn * Zg;
	double Sb = refWhite.Zn * Xb + refWhite.Zn * Yb + refWhite.Zn * Zb;

	return new ColorMatrix(Sr * Xr, Sr * Yr, Sr * Zr, Sg * Xg, Sg * Yg, Sg * Zg, Sb * Xb, Sb * Yb, Sb * Zb);
    }

    static private ColorMatrix computePrimaries(ColorMatrix toXYZ) {
	double Rx = toXYZ.Rx / toXYZ.Rs;
	double Ry = toXYZ.Ry / toXYZ.Rs;
	double Rz = toXYZ.Rz / toXYZ.Rs;

	double Gx = toXYZ.Gx / toXYZ.Gs;
	double Gy = toXYZ.Gy / toXYZ.Gs;
	double Gz = toXYZ.Gz / toXYZ.Gs;

	double Bx = toXYZ.Bx / toXYZ.Bs;
	double By = toXYZ.By / toXYZ.Bs;
	double Bz = toXYZ.Bz / toXYZ.Bs;

	return new ColorMatrix(Rx, Gx, Bx, Ry, Gy, By, Rz, Gz, Bz);
    }

    public final void print() {
	NumberFormat format = NumberFormat.getInstance();
	format.setMaximumFractionDigits(4);
	format.setMinimumFractionDigits(4);
	print(format);
    }

    public final void print(NumberFormat format) {
	Logger l = Logger.getLogger("com.imagero.color");

	l.info("----------------RGBColor----------------\n");
	l.info("Type: " + type + "\n");
	primaries.print(format, "Primaries");
	fromXYZ.print(format, "from XYZ");
	toXYZ.print(format, "to XYZ");
	refWhite.print(format);
	l.info("\nGamma: " + format.format(gamma));
    }

    /**
     * 
     * @param X
     *            from 0 to 95.047
     * @param Y
     *            from 0 to 100.000
     * @param Z
     *            from 0 to 108.883
     * @param dest
     */
    public final void xyz2Yxy(float X, float Y, float Z, float[] dest) {
	float x = X / (X + Y + Z);
	float y = Y / (X + Y + Z);
	dest[0] = Y;
	dest[1] = x;
	dest[2] = y;
    }

    /**
     * 
     * @param Y
     *            from 0 to 100
     * @param x
     *            from 0 to 1
     * @param y
     *            from 0 to 1
     * @param dest
     */
    public final void Yxy2xyz(float Y, float x, float y, float[] dest) {
	float X = x * (Y / y);
	float Z = (1 - x - y) * (Y / y);
	dest[0] = X;
	dest[1] = Y;
	dest[2] = Z;
    }

    public final void xyz2rgb(float X, float Y, float Z, float[] dest) {
	ColorMatrix fromXYZ = this.fromXYZ;
	float Yr = (float) (fromXYZ.Rx * X + fromXYZ.Gx * Y + fromXYZ.Bx * Z);
	float Yg = (float) (fromXYZ.Ry * X + fromXYZ.Gy * Y + fromXYZ.By * Z);
	float Yb = (float) (fromXYZ.Rz * X + fromXYZ.Gz * Y + fromXYZ.Bz * Z);

	if (!is_sRGB) {
	    dest[0] = (float) Math.pow(Yr, igamma);
	    dest[1] = (float) Math.pow(Yg, igamma);
	    dest[2] = (float) Math.pow(Yb, igamma);
	} else {
	    if (Yr < 0.0031308) {
		dest[0] = Yr * 12.92f;
	    } else {
		dest[0] = (float) (1.055f * Math.pow(Yr, igamma) - 0.055f);
	    }
	    if (Yg < 0.0031308) {
		dest[1] = Yg * 12.92f;
	    } else {
		dest[1] = (float) (1.055f * Math.pow(Yg, igamma) - 0.055f);
	    }
	    if (Yb < 0.0031308) {
		dest[2] = Yb * 12.92f;
	    } else {
		dest[2] = (float) (1.055f * Math.pow(Yb, igamma) - 0.055f);
	    }
	}

	for (int i = 0; i < dest.length; i++) {
	    dest[i] *= 255f;
	}
    }

    // void xyz2rgb(float X, float Y, float Z, int[] dest) {
    // Matrix fromXYZ = this.fromXYZ;
    // float Yr = (float) (fromXYZ.Rx * X + fromXYZ.Gx * Y + fromXYZ.Bx * Z);
    // float Yg = (float) (fromXYZ.Ry * X + fromXYZ.Gy * Y + fromXYZ.By * Z);
    // float Yb = (float) (fromXYZ.Rz * X + fromXYZ.Gz * Y + fromXYZ.Bz * Z);
    //
    // dest[0] = this.Yr.inverse[Math.round(Yr * maxR)];
    // dest[1] = this.Yg.inverse[Math.round(Yg * maxG)];
    // dest[2] = this.Yb.inverse[Math.round(Yb * maxB)];
    // }

    public final void rgb2xyz(int r, int g, int b, float[] dest) {
	ColorMatrix toXYZ = this.toXYZ;

	double r0 = this.Yr.forward[r];
	double g0 = this.Yg.forward[g];
	double b0 = this.Yb.forward[b];

	float X = (float) (toXYZ.Rx * r0 + toXYZ.Gx * g0 + toXYZ.Bx * b0);
	float Y = (float) (toXYZ.Ry * r0 + toXYZ.Gy * g0 + toXYZ.By * b0);
	float Z = (float) (toXYZ.Rz * r0 + toXYZ.Gz * g0 + toXYZ.Bz * b0);

	dest[0] = X;
	dest[1] = Y;
	dest[2] = Z;
    }

    public final float rgb2y(int r, int g, int b) {
	double r0 = this.Yr.forward[r];
	double g0 = this.Yg.forward[g];
	double b0 = this.Yb.forward[b];

	return (float) (toXYZ.Ry * r0 + toXYZ.Gy * g0 + toXYZ.Ry * b0);
    }

    public final void lab2xyz(float L, float a, float b, float[] dest) {
	double fy = (L + 16) / 116;
	double fx = (a / 500) + fy;
	double fz = fy - (b / 200);

	double yr = (L > 8.0 ? fy * fy * fy : L / KAPPA);

	double xr = fx * fx * fx;
	if (xr <= ETA) {
	    xr = (116 * fx - 16) / KAPPA;
	}

	double zr = fz * fz * fz;
	if (zr <= ETA) {
	    zr = (116 * fz - 16) / KAPPA;
	}
	dest[0] = (float) (xr * refWhite.Xn);
	dest[1] = (float) (yr * refWhite.Yn);
	dest[2] = (float) (zr * refWhite.Zn);
    }

    public final void lab2rgb(float L, float a, float b, float[] dest) {
	lab2xyz(L, a, b, dest);
	xyz2rgb(dest[0], dest[1], dest[2], dest);
    }

    static final double KAPPA = 24389.0 / 27.0;
    static final double k7787 = 24389.0 / 27.0 / 116.0;
    static final double ETA = 216.0 / 24389.0;
    static final double ke = KAPPA * ETA;
    static final double w = 1.0 / 3.0;

    public final void xyz2lab(float X, float Y, float Z, float[] dest) {
	double xr = X / refWhite.Xn;
	double yr = Y / refWhite.Yn;
	double zr = Z / refWhite.Zn;

	double fx, fy, fz;

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
    }

    /**
     * @param X
     * @param Z
     */
    public final void xyz2labL(float X, float Y, float Z, float[] dest) {
	double yr = Y / refWhite.Yn;

	double fy;

	if (yr > ETA) {
	    fy = Math.pow(yr, w);
	} else {
	    fy = (KAPPA * yr + 16.0) / 116.0;
	}

	float L = (float) (116 * fy - 16);

	dest[0] = L;
    }

    public final int applyGamma(int a) {
	return Yr.inverse[a];
    }

    public final void luv2xyz(float L, float u, float v, float[] dest) {
	double Y;
	if (L > ke) {
	    double y = (L + 16) / 116;
	    Y = y * y * y;
	} else {
	    Y = L / KAPPA;
	}
	final double a = (52 * L / (u + 13 * L * u0) - 1) / 3;
	final double b = -5.0 * Y;
	final double c = -1.0 / 3.0;
	final double d = Y * (39 * L / (v + 13 * L * v0) - 5);

	double X = (d - b) / (a - c);
	double Z = X * a + b;

	dest[0] = (float) X;
	dest[0] = (float) Y;
	dest[0] = (float) Z;
    }

    public final void xyz2luv(float X, float Y, float Z, float[] dest) {
	final double yr = Y / refWhite.Yn;

	double L;
	if (yr > ETA) {
	    L = 116 * Math.pow(yr, w) - 16;
	} else {
	    L = KAPPA * yr;
	}

	final float m = X + 15.0f * Y + 3.0f * Z;
	final double mr = refWhite.Xn + 15.0 * refWhite.Yn + 3.0 * refWhite.Zn;

	final double us = 4 * X / m;
	final double usr = 4 * refWhite.Xn / mr;
	final double vs = 9 * Y / m;
	final double vsr = 9 * refWhite.Yn / mr;

	double u = 13 * L * (us - usr);
	double v = 13 * L * (vs - vsr);

	dest[0] = (float) L;
	dest[0] = (float) u;
	dest[0] = (float) v;
    }

    public final void luv2rgb(float L, float u, float v, float[] dest) {
	luv2xyz(L, u, v, dest);
	xyz2rgb(dest[0], dest[1], dest[2], dest);
    }

    public final void rgb2luv(int r, int g, int b, float[] dest) {
	rgb2xyz(r, g, b, dest);
	xyz2luv(dest[0], dest[1], dest[2], dest);
    }

    public final void rgb2lab(int r, int g, int b, float[] dest) {
	rgb2xyz(r, g, b, dest);
	xyz2lab(dest[0], dest[1], dest[2], dest);
    }

    public final void lchab2lab(float L, float C, float H, float[] dest) {
	double a = C * Math.cos(H);
	double b = C * Math.sin(H);
	dest[0] = L;
	dest[1] = (float) a;
	dest[2] = (float) b;
    }

    public final void lchuv2luv(float L, float C, float H, float[] dest) {
	double u = C * Math.cos(H);
	double v = C * Math.sin(H);
	dest[0] = L;
	dest[1] = (float) u;
	dest[2] = (float) v;
    }

    public final void luv2lchuv(float L, float u, float v, float[] dest) {
	double C = Math.sqrt(u * u + v * v);
	double H = Math.atan2(v, u);
	if (H < 0) {
	    H += 360;
	} else if (H > 360) {
	    H -= 360;
	}
	dest[0] = L;
	dest[1] = (float) C;
	dest[2] = (float) H;
    }

    public final void lab2lchab(float L, float a, float b, float[] dest) {
	double C = Math.sqrt(a * a + b * b);
	double H = Math.atan2(b, a);
	if (H < 0) {
	    H += 360;
	} else if (H > 360) {
	    H -= 360;
	}
	dest[0] = L;
	dest[1] = (float) C;
	dest[2] = (float) H;
    }

    public final void lchab2xyz(float L, float C, float H, float[] dest) {
	lchab2lab(L, C, H, dest);
	lab2xyz(dest[0], dest[1], dest[2], dest);
    }

    public final void lchab2rgb(float L, float C, float H, float[] dest) {
	lchab2lab(L, C, H, dest);
	lab2rgb(dest[0], dest[1], dest[2], dest);
    }

    public final void lchuv2xyz(float L, float C, float H, float[] dest) {
	lchuv2luv(L, C, H, dest);
	luv2xyz(dest[0], dest[1], dest[2], dest);
    }

    public final void lchuv2rgb(float L, float C, float H, float[] dest) {
	lchuv2luv(L, C, H, dest);
	luv2rgb(dest[0], dest[1], dest[2], dest);
    }

    public static void main(String[] args) {
	RGB_ColorSpace rgb = RGB_ColorSpace.createSRGB(8);

	float[] dest = new float[3];
	rgb.rgb2xyz(200, 200, 200, dest);

	rgb.xyz2rgb(dest[0], dest[1], dest[2], dest);

	System.out.println(dest[0] + " " + dest[1] + " " + dest[2]);
    }
}
