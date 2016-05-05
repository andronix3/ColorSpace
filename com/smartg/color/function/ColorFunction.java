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
package com.smartg.color.function;

import com.smartg.color.CompositeType;
import com.smartg.function.Function;
import com.smartg.function.misc.Range;

/**
 * ColorFunction. Domain and range are both from 0 to 1. Input count = 2. Output
 * count = 1;
 * 
 * @author andrey
 * 
 */
public abstract class ColorFunction extends Function {

    /**
     * Create ColorFunction according to CompositeType. Non-separable function
     * are not supported (HUE, COLOR, LUMINOSITY and SATURATION).
     * 
     * @param type
     * @return
     */
    public static ColorFunction create(CompositeType type) {
	switch (type) {
	case ADD:
	    return new Add();
	case COLOR:
	    break;
	case COLORBURN:
	    return new ColorBurn();
	case COLORDODGE:
	    return new ColorDodge();
	case DARKEN:
	    return new Darken();
	case DIFFERENCE:
	    return new Difference();
	case EXCLUSION:
	    return new Exclusion();
	case HARDLIGHT:
	    return new Hardlight();
	case HUE:
	    break;
	case LIGHTEN:
	    return new Lighten();
	case LUMINOSITY:
	    break;
	case MULTIPLY:
	    return new Multiply();
	case NORMAL:
	    return new Normal();
	case OVERLAY:
	    return new Overlay();
	case SATURATION:
	    break;
	case SCREEN:
	    return new Screen();
	case SOFTLIGHT:
	    return new Softlight();
	case SUB:
	    return new Sub();
	case DISTANCE:
	    return new Distance();
	}
	return null;
    }

    protected ColorFunction() {
	super(new Range[] { new Range(0, 1), new Range(0, 1) }, new Range[] { new Range(0, 1) });
    }

    protected ColorFunction(Range[] domain, Range[] range) {
	super(domain, range);
    }

    protected final float multiply(float cb, float cs) {
	return cb * cs;
    }

    protected final float hardlight(float cb, float cs) {
	if (cs <= 0.5f) {
	    return multiply(cb, cs + cs);
	}
	return screen(cb, cs + cs - 1);
    }

    protected final float screen(float cb, float cs) {
	return cb + cs - (cb * cs);
    }

    static class ColorBurn extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    if (cs > 0) {
		output[0] = 1 - Math.min(1, (1 - cb) / cs);
	    } else {
		output[0] = 0;
	    }
	}
    }

    static class ColorDodge extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    if (cs < 1) {
		output[0] = Math.min(1, cb / (1 - cs));
	    } else {
		output[0] = 1;
	    }
	}
    }

    static class Difference extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = Math.abs(cb - cs);
	}
    }

    static class Exclusion extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    float m = cs * cb;
	    output[0] = cb + cs - (m + m);
	}
    }

    static class Hardlight extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = hardlight(cb, cs);
	}
    }

    static class Normal extends ColorFunction {

	@Override
	public void compute(float[] output, float... input) {
	    output[0] = input[0];
	}
    }

    static class Overlay extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = hardlight(cs, cb);
	}
    }

    static class Darken extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = Math.min(cs, cb);
	}
    }

    static class Lighten extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = Math.max(cs, cb);
	}
    }

    static class Multiply extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = cs * cb;
	}
    }

    static class Screen extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = cs + cb - cs * cb;
	}
    }

    static class Softlight extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    if (cs <= 0.5f) {
		output[0] = cb - (1 - (cs + cs)) * (cb - cb * cb);
	    } else {
		float dx;
		if (cb > 0.25) {
		    dx = (float) Math.sqrt(cb);
		} else {
		    dx = ((16 * cb - 12) * cb + 4) * cb;
		}
		output[0] = cb + (cs + cs - 1) * (dx - cb);
	    }
	}
    }

    static class Distance extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = (float) Math.sqrt(cb * cb + cs * cs);
	}
    }

    static class Add extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = cb + cs;
	}
    }

    static class Sub extends ColorFunction {

	@Override
	public final void compute(float[] output, float... input) {
	    float cb = input[0];
	    float cs = input[1];
	    output[0] = cb - cs;
	}
    }
}
