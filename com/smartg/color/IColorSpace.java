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
import java.awt.color.ColorSpace;

import com.smartg.icc.ICCProfile;
import com.smartg.icc.ICC_BasedCS;

/**
 * Implementation of color spaces used in PDF. Includes folgende color spaces:
 * CalGray, CalRGB, Decode, DeviceCMYK, DeviceGray, DeviceRGB, ICC_Based,
 * Indexed, Lab and Separation
 * 
 */
public abstract class IColorSpace extends ColorSpace {

    private static final long serialVersionUID = -6387040202299079905L;

    ICCProfile profile = ICCProfile.createSRGB();
    ICC_BasedCS srgb = new ICC_BasedCS(profile);

    protected IColorSpace(int type, int numcomponents) {
	super(type, numcomponents);
    }

    public abstract Color getColor(float[] d);

    public final int getRGB(float[] d) {
	float[] frgb = toRGB(d);
	int r, g, b;
	if (frgb.length == 1) {
	    r = (int) (Math.min(1, frgb[0]) * 255);
	    g = r;
	    b = r;
	} else {
	    r = (int) (Math.min(1, frgb[0]) * 255);
	    g = (int) (Math.min(1, frgb[1]) * 255);
	    b = (int) (Math.min(1, frgb[2]) * 255);
	}
	return (0xFF << 24) | (r << 16) | (g << 8) | b;
    }

    public float[] fromCIEXYZ(float[] colorvalue) {
	return new float[getNumComponents()];
    }

    public float[] fromRGB(float[] rgbvalue) {
	return new float[getNumComponents()];
    }

    public float[] toCIEXYZ(float[] colorvalue) {
	return new float[getNumComponents()];
    }

    public abstract int getComponentCount();
}
