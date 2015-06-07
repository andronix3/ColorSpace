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
public class WhitePoint {

    static final WhitePoint D65 = new WhitePoint("D65", .3127, .3290, .3582);
    static final WhitePoint D50 = new WhitePoint("D50", .3457, .3585, .2958);
    static final WhitePoint E = new WhitePoint("E", .3333, .3333, .3334);
    static final WhitePoint C = new WhitePoint("C", .3100, .3160, .3740);

    public final String name;

    public final double wx;
    public final double wy;
    public final double wz;

    public WhitePoint(float[] v) {
	this(v[0], v[1], v[2]);
    }

    public WhitePoint(double[] v) {
	this(v[0], v[1], v[2]);
    }

    public WhitePoint(double wx, double wy, double wz) {
	this("", wx, wy, wz);
    }

    public WhitePoint(String name, double wx, double wy, double wz) {
	this.name = name;
	this.wx = wx;
	this.wy = wy;
	this.wz = wz;
    }

    public boolean equals(Object obj) {
	if (obj == null || !(obj instanceof WhitePoint)) {
	    return false;
	}
	WhitePoint wp = (WhitePoint) obj;
	final int m = 100000;
	if (Math.round(wx * m) != Math.round(wp.wx * m)) {
	    return false;
	}
	if (Math.round(wy * m) != Math.round(wp.wy * m)) {
	    return false;
	}
	if (Math.round(wz * m) != Math.round(wp.wz * m)) {
	    return false;
	}
	return true;
    }

    public double[] getValues() {
	return new double[] { wx, wy, wz };
    }

    public int hashCode() {
	return (int) (Math.round(1 / wx) * 13 * Math.round(1 / wy) * 17 * Math.round(1 / wz) * 31);
    }

    void print(NumberFormat format) {
	Logger l = Logger.getLogger("com.imagero.color");

	l.info("White point: [" + name + "] ");
	l.info(format.format(wx));
	l.info(" ");
	l.info(format.format(wy));
	l.info(" \n");
	l.info(format.format(wz));
    }
}
