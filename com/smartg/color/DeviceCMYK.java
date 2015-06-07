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

public class DeviceCMYK extends IColorSpace {

    private static final long serialVersionUID = 2720598284351554678L;

    public DeviceCMYK() {
	super(TYPE_CMYK, 4);
    }

    public Color getColor(float[] d) {
	if (d.length == 4) {
	    double cyan = d[0];
	    double magenta = d[1];
	    double yellow = d[2];
	    double black = d[3];

	    double red = 1.0 - Math.min(1.0, cyan + black);
	    double green = 1.0 - Math.min(1.0, magenta + black);
	    double blue = 1.0 - Math.min(1.0, yellow + black);

	    int r = (int) (red * 255);
	    int g = (int) (green * 255);
	    int b = (int) (blue * 255);

	    return new Color(r, g, b, 255);
	}
	int k = (int) (d[0] * 255);
	return new Color(k, k, k);
    }

    public float[] toRGB(float[] colorvalue) {
	float cyan = colorvalue[0];
	float magenta = colorvalue[1];
	float yellow = colorvalue[2];
	float black = colorvalue[3];

	float red = (float) (1.0 - Math.min(1.0, cyan + black));
	float green = (float) (1.0 - Math.min(1.0, magenta + black));
	float blue = (float) (1.0 - Math.min(1.0, yellow + black));

	return new float[] { red, green, blue };
    }

    public int getComponentCount() {
	return 4;
    }
}