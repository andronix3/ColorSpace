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
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Date: 16.11.2008
 * 
 * @author Andrey Kuznetsov
 */
public class ChromaticAdaptation {

    public static enum CA_Type {
	BRADFORD, VON_KRIES, XYZ_SCALING
    }

    ReferenceWhite source;
    ReferenceWhite destination;

    ColorMatrix ma;
    ColorMatrix maInv;

    public final ColorMatrix m;

    static final ColorMatrix xyz_scaling = new ColorMatrix(//
	    1.0, 0.0, 0.0,//
	    0.0, 1.0, 0.0,//
	    0.0, 0.0, 1.0//
    );

    static final ColorMatrix bradford = new ColorMatrix(//
	    0.8951, -0.7502, 0.0389,//
	    0.2664, 1.7135, -0.0685,//
	    -0.1614, 0.0367, 1.0296//
    );

    static final ColorMatrix vonkries = new ColorMatrix(//
	    0.40024, -0.22630, 0.00000,//
	    0.70760, 1.16532, 0.00000,//
	    -0.08081, 0.04570, 0.91822//
    );

    public ChromaticAdaptation(CA_Type type, ReferenceWhite source, ReferenceWhite destination) {
	this.source = source;
	this.destination = destination;
	switch (type) {
	case BRADFORD:
	    ma = bradford;
	    maInv = bradford.inverse();
	    break;
	case VON_KRIES:
	    ma = vonkries;
	    maInv = vonkries.inverse();
	    break;
	case XYZ_SCALING:
	    ma = xyz_scaling;
	    maInv = xyz_scaling.inverse();
	    break;
	default:
	    throw new IllegalArgumentException("Unknown method:" + type);
	}

	double rs = source.Xn * ma.Rx + source.Xn * ma.Gx + source.Xn * ma.Bx;
	double gs = source.Yn * ma.Ry + source.Yn * ma.Gy + source.Yn * ma.By;
	double bs = source.Zn * ma.Rz + source.Zn * ma.Gz + source.Zn * ma.Bz;

	double rd = destination.Xn * ma.Rx + destination.Xn * ma.Gx + destination.Xn * ma.Bx;
	double gd = destination.Yn * ma.Ry + destination.Yn * ma.Gy + destination.Yn * ma.By;
	double bd = destination.Zn * ma.Rz + destination.Zn * ma.Gz + destination.Zn * ma.Bz;

	ColorMatrix r = new ColorMatrix(rd / rs, 0, 0, 0, gd / gs, 0, 0, 0, bd / bs);
	m = ma.multiply(r).multiply(maInv);
    }

    public void convert(float X, float Y, float Z, float[] dest) {
	dest[0] = (float) (X * m.Rx + X * m.Gx + X * m.Bx);
	dest[1] = (float) (Y * m.Ry + Y * m.Gy + Y * m.By);
	dest[2] = (float) (Z * m.Rz + Z * m.Gz + Z * m.Bz);
    }

    public static void main(String[] args) {
	ChromaticAdaptation cadapt = new ChromaticAdaptation(ChromaticAdaptation.CA_Type.BRADFORD, new ReferenceWhite(E_WhitePoint.D50.getWhitePoint()),
		new ReferenceWhite(E_WhitePoint.D65.getWhitePoint()));
	NumberFormat nf = NumberFormat.getNumberInstance();
	cadapt.ma.print(nf, "MA");
	cadapt.m.print(nf, "M");

	cadapt.source.print(nf);
	cadapt.destination.print(nf);

	GammaTables gh = GammaTables.createSRGB(8);
	Logger.getGlobal().log(Level.INFO, String.valueOf(gh));
    }
}
