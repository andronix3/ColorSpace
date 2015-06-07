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

import com.smartg.icc.ICCProfile;
import com.smartg.icc.ICC_BasedCS;

public class ICC_Based extends IColorSpace {

    private static final long serialVersionUID = 9195429246481051709L;
    // ICC_ColorSpace iccCS;
    ICC_BasedCS iccCS;
    IColorSpace alternate;

    public ICC_Based(ICCProfile icc, IColorSpace alternate) {
	super(icc.getColorSpaceType().getJavaColorSpace(), icc.getNumComponents());
	this.iccCS = new ICC_BasedCS(icc);
	this.alternate = alternate;
    }

    public Color getColor(float[] d) {
	float[] rgb = iccCS.toRGB(d);
	int r = (int) (rgb[0] * 255);
	int g = (int) (rgb[1] * 255);
	int b = (int) (rgb[2] * 255);
	int a = 255;
	if (rgb.length > 3) {
	    a = (int) (rgb[3] * 255);
	}
	Color color = null;
	try {
	    color = new Color(r, g, b, a);
	} catch (Exception ex) {
	    ex.printStackTrace();
	}
	return color;
    }

    public float[] toRGB(float[] colorvalue) {
	return iccCS.toRGB(colorvalue);
    }

    public float[] fromCIEXYZ(float[] colorvalue) {
	return iccCS.fromCIEXYZ(colorvalue);
    }

    public float[] fromRGB(float[] rgbvalue) {
	return iccCS.fromRGB(rgbvalue);
    }

    public float[] toCIEXYZ(float[] colorvalue) {
	return iccCS.toCIEXYZ(colorvalue);
    }

    public int getComponentCount() {
	return iccCS.getNumComponents();
    }
}