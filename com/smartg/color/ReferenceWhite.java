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
public class ReferenceWhite {

    private static NumberFormat numberFormat = create();

    private static NumberFormat create() {
	NumberFormat nf = NumberFormat.getNumberInstance();
	nf.setMaximumFractionDigits(4);
	return nf;
    }

    public final double Xn;
    public final double Yn;
    public final double Zn;

    public ReferenceWhite(WhitePoint wp) {
	Yn = 1.0;
	Xn = wp.wx / wp.wy;
	Zn = wp.wz / wp.wy;
    }

    public ReferenceWhite(float[] values) {
	this(values[0], values[1], values[2]);
    }

    public ReferenceWhite(double Xn, double Yn, double Zn) {
	this.Xn = Xn;
	this.Yn = Yn;
	this.Zn = Zn;
    }

    public void print(NumberFormat format) {
	Logger l = Logger.getLogger("com.imagero.color");

	l.info("Reference White: ");
	l.info(format.format(Xn));
	l.info(" ");
	l.info(format.format(Yn));
	l.info(" \n");
	l.info(format.format(Zn));
    }

    @Override
    public String toString() {
	StringBuffer sb = new StringBuffer();
	sb.append("Reference White: [Xn = ");
	sb.append(numberFormat.format(Xn));
	sb.append(", Yn = ");
	sb.append(numberFormat.format(Yn));
	sb.append(", Zn = ");
	sb.append(numberFormat.format(Zn));
	sb.append("]");
	return super.toString();
    }
}
