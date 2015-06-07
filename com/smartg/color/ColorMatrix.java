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

import java.text.NumberFormat;
import java.util.logging.Logger;

/**
 * @author Andrey Kuznetsov
 */
public class ColorMatrix {
    public final double Rx, Gx, Bx;// 3x3 matrix
    public final double Ry, Gy, By;
    public final double Rz, Gz, Bz;

    public final double Rs, Gs, Bs;// summe columns
    public final double Xs, Ys, Zs;// summe rows

    public final double determinante;

    public ColorMatrix(float[][] m) {
	this(m[0][0], m[0][1], m[0][2], m[1][0], m[1][1], m[1][2], m[2][0], m[2][1], m[2][2]);
    }

    public ColorMatrix(double[] v) {
	this(v[0], v[1], v[2], v[3], v[4], v[5], v[6], v[7], v[8]);
    }

    public ColorMatrix multiply(ColorMatrix m2) {
	double rx = Rx * m2.Rx + Gx * m2.Ry + Bx * m2.Rz;
	double gx = Rx * m2.Gx + Gx * m2.Gy + Bx * m2.Gz;
	double bx = Rx * m2.Bx + Gx * m2.By + Bx * m2.Bz;

	double ry = Ry * m2.Rx + Gy * m2.Ry + By * m2.Rz;
	double gy = Ry * m2.Gx + Gy * m2.Gy + By * m2.Gz;
	double by = Ry * m2.Bx + Gy * m2.By + By * m2.Bz;

	double rz = Rz * m2.Rx + Gz * m2.Ry + Bz * m2.Rz;
	double gz = Rz * m2.Gx + Gz * m2.Gy + Bz * m2.Gz;
	double bz = Rz * m2.Bx + Gz * m2.By + Bz * m2.Bz;

	return new ColorMatrix(rx, gx, bx, ry, gy, by, rz, gz, bz);
    }

    public ColorMatrix inverse() {
	double a = Rx;
	double b = Gx;
	double c = Bx;
	double d = Ry;
	double e = Gy;
	double f = By;
	double g = Rz;
	double h = Gz;
	double i = Bz;

	double det = 1 / determinante;

	ColorMatrix m = new ColorMatrix(e * i - f * h, c * h - b * i, b * f - c * e, f * g - d * i, a * i - c * g, c * d - a * f, d * h - e * g, b * g - a * h,
		a * e - b * d);
	return m.multiply(det);
    }

    public ColorMatrix multiply(double d) {
	return new ColorMatrix(d * Rx, d * Gx, d * Bx, d * Ry, d * Gy, d * By, d * Rz, d * Gz, d * Bz);
    }

    public ColorMatrix(float[] redColumn, float[] greenColumn, float[] blueColumn) {
	Rx = redColumn[0];
	Gx = greenColumn[0];
	Bx = blueColumn[0];

	Ry = redColumn[1];
	Gy = greenColumn[1];
	By = blueColumn[1];

	Rz = redColumn[2];
	Gz = greenColumn[2];
	Bz = blueColumn[2];

	Rs = Rx + Ry + Rz;
	Gs = Gx + Gy + Gz;
	Bs = Bx + By + Bz;

	Xs = Rx + Gx + Bx;
	Ys = Ry + Gy + By;
	Zs = Rz + Gz + Bz;

	determinante = Rx * Gy * Bz + Gx * By * Rz + Bx * Ry * Gz - Bx * Gy * Rz - Gx * Ry * Bz - Rx * By * Gz;
    }

    public double[] getMatrix() {
	return new double[] { Rx, Gx, Bx, Ry, Gy, By, Rz, Gz, Bz };
    }

    public ColorMatrix(double rx, double gx, double bx, double ry, double gy, double by, double rz, double gz, double bz) {
	Rx = Math.abs(rx);
	Gx = gx;
	Bx = bx;
	Ry = ry;
	Gy = gy;
	By = by;
	Rz = rz;
	Gz = gz;
	Bz = bz;

	Rs = Rx + Ry + Rz;
	Gs = Gx + Gy + Gz;
	Bs = Bx + By + Bz;

	Xs = Rx + Gx + Bx;
	Ys = Ry + Gy + By;
	Zs = Rz + Gz + Bz;

	determinante = Rx * Gy * Bz + Gx * By * Rz + Bx * Ry * Gz - Bx * Gy * Rz - Gx * Ry * Bz - Rx * By * Gz;
    }

    public void print(NumberFormat format, String name) {
	Logger l = Logger.getLogger("com.imagero.color");
	l.info(name + ":\n");
	l.info(format.format(Rx));
	l.info(" ");
	l.info(format.format(Gx));
	l.info(" ");
	l.info(format.format(Bx));
	l.info(" ");
	l.info(format.format(Ry));
	l.info(" ");
	l.info(format.format(Gy));
	l.info(" ");
	l.info(format.format(By));
	l.info(" ");
	l.info(format.format(Rz));
	l.info(" ");
	l.info(format.format(Gz));
	l.info(" ");
	l.info(format.format(Bz));
	l.info(" ");

	l.info("\nDeterminante:" + format.format(determinante));
    }

    // public static void main(String[] args) {
    // ColorMatrix m = Colors.srgb_toXYZ;
    // NumberFormat nf = NumberFormat.getNumberInstance();
    // nf.setMaximumFractionDigits(3);
    // m.print(nf, "toXYZ");
    // ColorMatrix mi = m.inverse();
    // mi.print(nf, "AI");
    // ColorMatrix mii = mi.inverse();
    // mii.print(nf, "AII");
    //
    // ColorMatrix m2 = Colors.srgb_toXYZ.inverse();
    // m2.print(nf, "fromXYZ");
    // }
}
