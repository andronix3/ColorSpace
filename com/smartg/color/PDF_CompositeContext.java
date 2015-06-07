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

import java.awt.CompositeContext;
import java.awt.image.Raster;
import java.awt.image.WritableRaster;

class PDF_CompositeContext implements CompositeContext {

    IComposite composite;

    PDF_CompositeContext(IComposite composite) {
	this.composite = composite;
    }

    public void compose(Raster src, Raster dstIn, WritableRaster dstOut) {
	compose2(src, dstIn, dstOut);
    }

    @SuppressWarnings("unused")
    private void composeN(Raster src, Raster dstIn, WritableRaster dstOut) {
	int width = Math.min(src.getWidth(), dstIn.getWidth());
	int height = Math.min(src.getHeight(), dstIn.getHeight());

	boolean srcHasAlpha = src.getNumBands() == 4;
	boolean dstHasAlpha = dstIn.getNumBands() == 4;

	float[] result = new float[4];

	// there are no alpha processing by Composer
	int[] srcPixel = new int[3];
	int[] dstPixel = new int[3];

	int[] srcPixels = new int[width];
	int[] dstPixels = new int[width];

	final Composer composer = composite.composer;
	final float alpha = composite.getAlpha();

	int[] dest = new int[3];

	final float denom = 1f / 255f;

	for (int y = 0; y < height; y++) {
	    src.getDataElements(0, y, width, 1, srcPixels);
	    dstIn.getDataElements(0, y, width, 1, dstPixels);
	    for (int x = 0; x < width; x++) {

		int s_pixel = srcPixels[x];
		srcPixel[0] = (s_pixel >> 16) & 0xFF;
		srcPixel[1] = (s_pixel >> 8) & 0xFF;
		srcPixel[2] = s_pixel & 0xFF;

		int d_pixel = dstPixels[x];
		dstPixel[0] = (d_pixel >> 16) & 0xFF;
		dstPixel[1] = (d_pixel >> 8) & 0xFF;
		dstPixel[2] = d_pixel & 0xFF;

		float as = alpha;
		float ab = 1;
		if (srcHasAlpha) {
		    as = ((s_pixel >> 24) & 0xFF) * alpha * denom;
		}
		if (dstHasAlpha) {
		    ab = ((d_pixel >> 24) & 0xFF) * denom;
		}

		float ar = ab + as - (ab * as);

		composer.compose(srcPixel, dstPixel, result);

		for (int i = 0; i < 3; i++) {
		    int d = dstPixel[i];
		    int s = srcPixel[i];
		    float r = result[i];

		    float cb = d * denom;
		    float cs = s * denom;

		    float f = as / ar;
		    float cr = (1 - f) * cb + f * ((1 - ab) * cs + ab * r);

		    dest[i] = (int) (cr * 255);
		}

		dstPixels[x] = ((int) (ar * 255) & 0xFF) << 24 | (dest[0] & 0xFF) << 16 | (dest[1] & 0xFF) << 8 | (dest[2] & 0xFF);
	    }
	    dstOut.setDataElements(0, y, width, 1, dstPixels);
	}
    }

    private void compose2(Raster src, Raster dstIn, WritableRaster dstOut) {

	final float denom = 1f / 255f;

	int width = Math.min(src.getWidth(), dstIn.getWidth());
	int height = Math.min(src.getHeight(), dstIn.getHeight());

	boolean srcHasAlpha = src.getNumBands() == 4;
	boolean dstHasAlpha = dstIn.getNumBands() == 4;
	boolean outHasAlpha = dstOut.getNumBands() == 4;

	boolean srcIsGray = src.getNumBands() == 1;
	boolean dstIsGray = dstIn.getNumBands() == 1;
	boolean outIsGray = dstOut.getNumBands() == 1;

	float[] result = new float[4];

	// there are no alpha processing by Composer
	int[] srcPixel = new int[3];
	int[] dstPixel = new int[3];

	// int[] srcPixels = new int[width];
	// int[] dstPixels = new int[width];

	float[] srcSamples0 = new float[width];
	float[] srcSamples1 = new float[width];
	float[] srcSamples2 = new float[width];
	float[] srcSamples3 = new float[width];

	float[] inSamples0 = new float[width];
	float[] inSamples1 = new float[width];
	float[] inSamples2 = new float[width];
	float[] inSamples3 = new float[width];

	float[] outSamples0 = new float[width];
	float[] outSamples1 = new float[width];
	float[] outSamples2 = new float[width];
	float[] outSamples3 = new float[width];

	final Composer composer = composite.composer;
	final float alpha = composite.getAlpha();

	int[] dest = new int[3];

	for (int y = 0; y < height; y++) {
	    // src.getDataElements(0, y, width, 1, srcPixels);
	    src.getSamples(0, y, width, 1, 0, srcSamples0);
	    if (!srcIsGray) {
		src.getSamples(0, y, width, 1, 1, srcSamples1);
		src.getSamples(0, y, width, 1, 2, srcSamples2);
		if (srcHasAlpha) {
		    src.getSamples(0, y, width, 1, 3, srcSamples3);
		}
	    } else {
		srcSamples1 = srcSamples0;
		srcSamples2 = srcSamples0;
	    }

	    // dstIn.getDataElements(0, y, width, 1, dstPixels);
	    dstIn.getSamples(0, y, width, 1, 0, inSamples0);
	    if (!dstIsGray) {
		dstIn.getSamples(0, y, width, 1, 1, inSamples1);
		dstIn.getSamples(0, y, width, 1, 2, inSamples2);
		if (dstHasAlpha) {
		    dstIn.getSamples(0, y, width, 1, 3, inSamples3);
		}
	    } else {
		inSamples1 = inSamples0;
		inSamples2 = inSamples0;
	    }

	    for (int x = 0; x < width; x++) {

		// int s_pixel = srcPixels[x];
		srcPixel[0] = (int) srcSamples0[x];
		srcPixel[1] = (int) srcSamples1[x];
		srcPixel[2] = (int) srcSamples2[x];

		// int d_pixel = dstPixels[x];
		dstPixel[0] = (int) inSamples0[x];
		dstPixel[1] = (int) inSamples1[x];

		dstPixel[2] = (int) inSamples2[x];

		float as = alpha;
		float ab = 1;
		if (srcHasAlpha) {
		    as = srcSamples3[x] * alpha * denom;
		}
		if (dstHasAlpha) {
		    ab = inSamples3[x] * denom;
		}

		float ar = ab + as - (ab * as);

		composer.compose(srcPixel, dstPixel, result);

		for (int i = 0; i < 3; i++) {
		    int d = dstPixel[i];
		    int s = srcPixel[i];
		    float r = result[i];

		    if (d < 0) {
			d = 0;
		    }
		    if (s < 0) {
			s = 0;
		    }
		    if (d > 255) {
			d = 255;
		    }
		    if (s > 255) {
			s = 255;
		    }
		    float cb = d * denom;
		    float cs = s * denom;

		    float f = as / ar;
		    float cr = (1 - f) * cb + f * ((1 - ab) * cs + ab * r);

		    dest[i] = (int) (cr * 255);
		}

		outSamples0[x] = dest[0];
		outSamples1[x] = dest[1];
		outSamples2[x] = dest[2];
		if (outHasAlpha) {
		    outSamples3[x] = ar * 255f;
		}

		// dstPixels[x] = ((int) (ar * 255) & 0xFF) << 24
		// | (dest[0] & 0xFF) << 16 | (dest[1] & 0xFF) << 8
		// | (dest[2] & 0xFF);
	    }
	    // dstOut.setDataElements(0, y, width, 1, dstPixels);

	    dstOut.setSamples(0, y, width, 1, 0, outSamples0);
	    if (!outIsGray) {
		dstOut.setSamples(0, y, width, 1, 1, outSamples1);
		dstOut.setSamples(0, y, width, 1, 2, outSamples2);
		if (outHasAlpha) {
		    dstOut.setSamples(0, y, width, 1, 3, outSamples3);
		}
	    }
	}
    }

    public void dispose() {

    }
}
