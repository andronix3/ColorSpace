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

public abstract class Composer {

    String type;

    final float denom = 1f / 255f;

    public abstract void compose(int[] src, int[] dst, float[] result);

    //
    // private static float[] create() {
    // float[] res = new float[256];
    // for (int i = 0; i < res.length; i++) {
    // res[i] = i / 255.0f;
    // }
    // return res;
    // }

    final float multiply(float cb, float cs) {
	return cb * cs;
    }

    final float hardlight(float cb, float cs) {
	if (cs <= 0.5f) {
	    return multiply(cb, cs + cs);
	}
	return screen(cb, cs + cs - 1);
    }

    final float screen(float cb, float cs) {
	return cb + cs - (cb * cs);
    }

    static class Subtract extends Composer {

	@Override
	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		result[i] = Math.max(0, cb - cs);
	    }
	}
    }

    static class Add extends Composer {

	@Override
	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		result[i] = cb + cs;
	    }
	}
    }

    static class Distance extends Composer {

	@Override
	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cb = src[i] * denom;
		float cs = dst[i] * denom;
		result[i] = (float) Math.sqrt(cb * cb + cs * cs);
	    }
	}
    }

    static class Normal extends Composer {

	Normal() {
	    type = "Normal";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		result[i] = src[i] * denom;
	    }
	}
    }

    final float lum(float[] color) {
	float r = color[0];
	float g = color[1];
	float b = color[2];
	return 0.3f * r + 0.59f * g + 0.11f * b;
    }

    final float sat(float[] color) {
	float r = color[0];
	float g = color[1];
	float b = color[2];
	return max(r, g, b) - min(r, g, b);
    }

    final void setSat(float[] color, float S) {
	int max = getMaxIndex(color);
	int min = getMinIndex(color);
	int mid = getMidIndex(min, max);
	if (color[max] > color[min]) {
	    color[mid] = (((color[mid] - color[min]) * S) / (color[max] - color[min]));
	    color[max] = S;
	} else {
	    color[mid] = 0;
	    color[max] = 0;
	}
	color[min] = 0;
    }

    final void setLum(float[] color, float L) {
	float d = L - lum(color);

	color[0] += d;
	color[1] += d;
	color[2] += d;

	clipColor(color);
    }

    final void clipColor(float[] color) {
	float r = color[0];
	float g = color[1];
	float b = color[2];

	float L = lum(color);
	float n = min(r, g, b);
	float x = max(r, g, b);
	if (n < 0.0) {
	    r = L + (((r - L) * L) / (L - n));
	    g = L + (((g - L) * L) / (L - n));
	    b = L + (((b - L) * L) / (L - n));
	}
	if (x > 1.0) {
	    r = L + (((r - L) * (1 - L)) / (x - L));
	    g = L + (((g - L) * (1 - L)) / (x - L));
	    b = L + (((b - L) * (1 - L)) / (x - L));
	}
	color[0] = r;
	color[1] = g;
	color[2] = b;
    }

    final float min(float r, float g, float b) {
	float min = r;
	if (g < r) {
	    min = g;
	}
	if (b < min) {
	    return b;
	}
	return min;
    }

    final float max(float r, float g, float b) {
	float max = r;
	if (g > max) {
	    max = g;
	}
	if (b > max) {
	    return b;
	}
	return max;
    }

    final int getMaxIndex(float[] color) {
	int maxIndex = 0;
	if (color[1] > color[0]) {
	    maxIndex = 1;
	}
	if (color[2] > color[maxIndex]) {
	    return 2;
	}
	return maxIndex;
    }

    final int getMinIndex(float[] color) {
	int minIndex = 0;
	if (color[1] < color[0]) {
	    minIndex = 1;
	}
	if (color[2] < color[minIndex]) {
	    return 2;
	}
	return minIndex;
    }

    final int getMidIndex(int minIndex, int maxIndex) {
	if (minIndex == 0 && maxIndex == 1) {
	    return 2;
	} else if (minIndex == 0 && maxIndex == 2) {
	    return 1;
	} else if (minIndex == 1 && maxIndex == 0) {
	    return 2;
	} else if (minIndex == 1 && maxIndex == 2) {
	    return 0;
	} else if (minIndex == 2 && maxIndex == 0) {
	    return 1;
	} else /* if(minIndex == 2 && maxIndex == 1) */{
	    return 0;
	}
    }

    static class Multiply extends Composer {

	Multiply() {
	    type = "Multiply";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		result[i] = cs * cb;
	    }
	}
    }

    static class Screen extends Composer {

	Screen() {
	    type = "Screen";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		result[i] = (cb + cs - (cb * cs));
	    }
	}
    }

    static class Overlay extends Composer {

	Overlay() {
	    type = "Overlay";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		result[i] = hardlight(cs, cb);
	    }
	}
    }

    static class Darken extends Composer {

	Darken() {
	    type = "Darken";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		result[i] = Math.min(src[i], dst[i]) * denom;
	    }
	}
    }

    static class Lighten extends Composer {

	Lighten() {
	    type = "Lighten";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		result[i] = Math.max(src[i], dst[i]) * denom;
	    }
	}
    }

    static class ColorDodge extends Composer {

	ColorDodge() {
	    type = "ColorDodge";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		if (cs < 1.0) {
		    result[i] = Math.min(1, cb / (1 - cs));
		} else {
		    result[i] = 1;
		}
	    }
	}
    }

    static class ColorBurn extends Composer {

	ColorBurn() {
	    type = "ColorBurn";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		if (cs > 0) {
		    result[i] = (1 - Math.min(1, (1 - cb) / cs));
		} else {
		    result[i] = 0;
		}
	    }
	}
    }

    static class HardLight extends Composer {

	HardLight() {
	    type = "HardLight";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		result[i] = hardlight(cb, cs);
	    }
	}
    }

    static class SoftLight extends Composer {

	SoftLight() {
	    type = "SoftLight";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;
		if (cs <= 0.5f) {
		    result[i] = (cb - (1 - (cs + cs)) * (cb - cb * cb));
		} else {
		    float dx;
		    if (cb > 0.25) {
			dx = (float) Math.sqrt(cb);
		    } else {
			dx = ((16 * cb - 12) * cb + 4) * cb;
		    }
		    result[i] = (cb + (cs + cs - 1) * (dx - cb));
		}
	    }
	}
    }

    static class Difference extends Composer {

	Difference() {
	    type = "Difference";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		result[i] = Math.abs(src[i] - dst[i]) * denom;
		// result[i] = (src[i] - dst[i]) * denom;
	    }
	}
    }

    static class Exclusion extends Composer {

	Exclusion() {
	    type = "Exclusion";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    for (int i = 0; i < src.length; i++) {
		float cs = src[i] * denom;
		float cb = dst[i] * denom;

		float m = cs * cb;
		result[i] = (cb + cs - (m + m));
	    }
	}
    }

    static class Hue extends Composer {

	Hue() {
	    type = "Hue";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    float[] cs = result;
	    float[] cb = new float[3];
	    for (int i = 0; i < 3; i++) {
		cs[i] = dst[i] * denom;
		cb[i] = src[i] * denom;
	    }
	    float b_sat = sat(cb);
	    float b_lum = lum(cb);
	    setSat(cs, b_sat);
	    setLum(cs, b_lum);
	}
    }

    static class Saturation extends Composer {

	Saturation() {
	    type = "Saturation";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    float[] cs = new float[3];
	    float[] cb = result;
	    for (int i = 0; i < 3; i++) {
		cs[i] = src[i] * denom;
		cb[i] = dst[i] * denom;
	    }

	    float s_sat = sat(cs);
	    float b_lum = lum(cb);
	    setSat(cb, s_sat);
	    setLum(cb, b_lum);
	}
    }

    static class Color extends Composer {

	Color() {
	    type = "Color";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    float[] cs = result;
	    float[] cb = new float[3];
	    for (int i = 0; i < 3; i++) {
		cs[i] = dst[i] * denom;
		cb[i] = src[i] * denom;
	    }
	    float b_lum = lum(cb);
	    setLum(cs, b_lum);
	}
    }

    static class Luminosity extends Composer {

	Luminosity() {
	    type = "Luminosity";
	}

	public void compose(int[] src, int[] dst, float[] result) {
	    float[] cs = new float[3];
	    float[] cb = result;
	    for (int i = 0; i < 3; i++) {
		cs[i] = src[i] * denom;
		cb[i] = dst[i] * denom;
	    }
	    float s_lum = lum(cs);
	    setLum(cb, s_lum);
	}
    }
}
