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

/**
 * @author Andrey Kuznetsov
 */
class GammaTables {
    public float[] forward;
    public int[] inverse;

    private GammaTables() {
    }

    static GammaTables create(float gamma, int bps) {

	final int max = (1 << bps) - 1;

	float[] forward = new float[max + 1];
	for (int i = 0; i < forward.length; i++) {
	    forward[i] = (float) (Math.pow((double) i / (double) max, gamma));
	}

	int[] inverse = new int[max + 1];
	double igamma = 1.0 / gamma;
	for (int i = 0; i < inverse.length; i++) {
	    inverse[i] = (int) Math.round(Math.pow((float) i / (float) max, igamma) * max);
	}

	GammaTables tables = new GammaTables();
	tables.forward = forward;
	tables.inverse = inverse;

	return tables;
    }

    static GammaTables createSRGB(int bps) {
	final float gamma = 2.4f;
	int max = (1 << bps) - 1;

	float[] forward = new float[max + 1];
	for (int i = 0; i < forward.length; i++) {
	    double k = (double) i / (double) max;
	    if (k > 0.04045) {
		forward[i] = (float) (Math.pow((k + 0.055) / 1.055, gamma));
	    } else {
		forward[i] = (float) (k / 12.92);
	    }
	}

	int[] inverse = new int[max + 1];
	double igamma = 1.0 / gamma;
	for (int i = 0; i < inverse.length; i++) {
	    double k = (double) i / (double) max;
	    if (k < 0.0031308) {
		inverse[i] = (int) (k * 12.92);
	    } else {
		inverse[i] = (int) Math.round((Math.pow(1.055 * k, igamma) - 0.055) * max);
	    }
	}

	GammaTables tables = new GammaTables();
	tables.forward = forward;
	tables.inverse = inverse;

	return tables;
    }

    static GammaTables createLinear() {
	int max = (1 << 16) - 1;

	GammaTables tables = new GammaTables();
	tables.forward = NormTables.getTable(16);
	tables.inverse = new int[tables.forward.length];

	for (int i = 0; i < tables.inverse.length; i++) {
	    tables.inverse[i] = Math.round(tables.forward[i] * max);
	}
	return tables;
    }
}
