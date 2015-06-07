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

public class Indexed extends IColorSpace {

    private static final long serialVersionUID = 5954764319940781709L;

    IColorSpace baseCS;
    int hival;
    byte[] lookup;
    float [] flookup;
    int componentCount;
    Color[] colors;

    public Indexed(IColorSpace baseCS, int hival, byte[] lookup) {
	super(TYPE_RGB, 3);
	this.baseCS = baseCS;
	this.hival = hival;
	this.lookup = lookup;
	

	createColors(lookup);
    }

    private void createColors(byte[] lookup) {
	this.flookup = new float[lookup.length];
	for(int i = 0; i < lookup.length; i++) {
	    flookup[i] = (lookup[i] & 0xFF) / 255f;
	}
	int max = Math.min(lookup.length, hival + 1);
	componentCount = lookup.length / max;
	if (componentCount == 0) {
	    componentCount = 1;
	}
	float[] color = new float[componentCount];
	colors = new Color[max];
	for (int i = 0; i < max; i++) {
	    int index = i * componentCount;
	    for (int j = 0; j < componentCount; j++) {
		try {
		    color[j] = flookup[index + j];
		} catch (Exception ex) {
		    // TODO Auto-generated catch block
		    ex.printStackTrace();
		}
	    }
	    colors[i] = this.baseCS.getColor(color);
	}
    }

    public Color getColor(float[] d) {
	if (colors == null) {
	    return Color.blue;
	}
	int k = (int) d[0];
	return colors[k];
    }

    public float[] toRGB(float[] colorvalue) {
	int index = Math.max(0, (int) (colorvalue[0] * 255) * componentCount);
	float[] color = new float[componentCount];
	
	for (int i = 0; i < componentCount; i++) {
	    int j = index + i;
	    if(j < lookup.length) {
		color[i] = flookup[j];
	    }
	}
	return color;
    }

    public int getComponentCount() {
	return 1;
    }
}