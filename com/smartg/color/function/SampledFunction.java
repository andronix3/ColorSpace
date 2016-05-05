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

import java.io.IOException;
import java.io.InputStream;
import java.util.Enumeration;
import java.util.logging.Logger;

import com.imagero.reader.pdf.PDF_File;
import com.imagero.reader.pdf.PdfObject.StreamObject;
import com.imagero.uio.io.BitInputStream;
import com.smartg.function.Function;
import com.smartg.function.misc.Range;
import com.smartg.java.util.IEnumerator;

/**
 * Sampled functions use a sequence of sample values to provide an approximation
 * for functions whose domains and ranges are bounded. The samples are organized
 * as an m-dimensional table in which each entry has ncomponents.
 * 
 * 
 * @author andrey
 * 
 */
public class SampledFunction extends Function {

    int[][] samples;

    // An array of m positive integers specifying the number of samples in
    // each input dimension of the sample table.
    int[] size;

    Range[] encode;
    Range[] decode;

    int bitsPerSample;
    float twoHochBPS;
    int order;

    /**
     * 
     * @param domain
     *            An array of 2 × m numbers, where m is the number of input
     *            values. For each i from 0 to m - 1, Domain[2i] must be less
     *            than or equal to Domain[2i+1] , and the ith input value, xi ,
     *            must lie in the interval Domain[2i] = xi = Domain[2i+1]. Input
     *            values outside the declared domain are clipped to the nearest
     *            boundary value.
     * @param range
     *            An array of 2 × n numbers, where n is the number of output
     *            values. For each j from 0 to n - 1, Range[2j] must be less
     *            than or equal to Range[2j+1] , and the jth output value, yj ,
     *            must lie in the interval Range[2j] = yj = Range[2j+1] . Output
     *            values outside the declared range are clipped to the nearest
     *            boundary value.
     * @param bitsPerSample
     *            The number of bits used to represent each sample. (If the
     *            function has multiple output values, each one occupies
     *            BitsPerSample bits.) Valid values are 1, 2, 4, 8, 12, 16, 24,
     *            and 32.
     * @param size
     *            An array of m positive integers specifying the number of
     *            samples in each input dimension of the sample table
     * @param sampleData
     *            InputStream with sample data. Each sample value is represented
     *            as a sequence of BitsPerSample bits. There is no padding at
     *            byte boundaries. For a function with multidimensional input
     *            (more than one input variable), the sample values in the first
     *            dimension vary fastest, and the values in the last dimension
     *            vary slowest. For a function with multidimensional output, the
     *            values are stored in the same order as Range.
     */
    public SampledFunction(Range[] domain, Range[] range, int bitsPerSample, int[] size, InputStream sampleData) {
	this(domain, range, bitsPerSample, 1, size, null, null, sampleData);
    }

    /**
     * 
     * @param domain
     *            An array of 2 × m numbers, where m is the number of input
     *            values. For each i from 0 to m - 1, Domain[2i] must be less
     *            than or equal to Domain[2i+1] , and the ith input value, xi ,
     *            must lie in the interval Domain[2i] = xi = Domain[2i+1]. Input
     *            values outside the declared domain are clipped to the nearest
     *            boundary value.
     * @param range
     *            An array of 2 × n numbers, where n is the number of output
     *            values. For each j from 0 to n - 1, Range[2j] must be less
     *            than or equal to Range[2j+1] , and the jth output value, yj ,
     *            must lie in the interval Range[2j] = yj = Range[2j+1] . Output
     *            values outside the declared range are clipped to the nearest
     *            boundary value.
     * @param bitsPerSample
     *            The number of bits used to represent each sample. (If the
     *            function has multiple output values, each one occupies
     *            BitsPerSample bits.) Valid values are 1, 2, 4, 8, 12, 16, 24,
     *            and 32.
     * @param order
     *            The order of interpolation between samples. Valid values are 1
     *            and 3, specifying linear and cubic spline interpolation,
     *            respectively. Currently only linear interpolation is
     *            supported.
     * @param size
     *            An array of m positive integers specifying the number of
     *            samples in each input dimension of the sample table
     * @param encode
     *            An array of 2 × m numbers specifying the linear mapping of
     *            input values into the domain of the function’s sample table.
     *            Default value: [ 0 (Size0 - 1) 0 (Size1 - 1)].
     * @param decode
     *            An array of 2 × n numbers specifying the linear mapping of
     *            sample values into the range appropriate for the function’s
     *            output values. Default value: same as the value of Range
     * @param sampleData
     *            InputStream with sample data. Each sample value is represented
     *            as a sequence of BitsPerSample bits. There is no padding at
     *            byte boundaries. For a function with multidimensional input
     *            (more than one input variable), the sample values in the first
     *            dimension vary fastest, and the values in the last dimension
     *            vary slowest. For a function with multidimensional output, the
     *            values are stored in the same order as Range.
     */
    public SampledFunction(Range[] domain, Range[] range, int bitsPerSample, int order, int[] size, Range[] encode, Range[] decode, InputStream sampleData) {
	super(domain, range);
	setBitsPerSample(bitsPerSample);
	setOrder(order);
	setSize(size);
	setEncode(encode);
	setDecode(decode);
	setData(sampleData);
    }

    void setOrder(int m) {
	order = m;
    }

    void setSize(int[] sizeValues) {
	size = sizeValues;
    }

    void setBitsPerSample(int value) {
	bitsPerSample = value;
	twoHochBPS = (float) Math.pow(2, bitsPerSample);
    }

    void setData(InputStream in) {
	samples = createSamples();
	try {
	    readSamples(in);
	} catch (IOException ex) {
	    Logger.getGlobal().throwing("SampledFunction", "setData", ex);
	}
    }

    void setDecode(Range[] decodeValues) {
	decode = decodeValues;
	if (decode == null || decode.length == 0) {
	    decode = new Range[range.length];
	    for (int i = 0; i < range.length; i++) {
		decode[i] = range[i];
	    }
	}
    }

    void setEncode(Range[] values) {
	encode = values;

	if (encode == null || encode.length == 0) {
	    encode = new Range[size.length];
	    for (int i = 0; i < size.length; i++) {
		encode[i] = new Range(0, size[i] - 1);
	    }
	}
    }

    int[][] createSamples() {
	int length = 1;
	for (int i = 0; i < size.length; i++) {
	    length *= size[i];
	}
	int[][] res = new int[length][decode.length / 2];
	return res;
    }

    public void compute(float[] output, float... input) {
	try {
	    encode(input);
	    int index0 = 0;
	    int index1 = 0;
	    for (int i = 0; i < input.length; i++) {
		float r = input[i];
		int k0 = (int) Math.floor(r);
		int k1 = (int) Math.ceil(r);
		index0 += k0;
		index1 += k1;
	    }
	    int[] out0 = samples[index0];
	    int[] out1 = samples[index1];

	    for (int i = 0; i < output.length; i++) {
		output[i] = (out0[i] + out1[i]) / 2;
		output[i] = interpolate(output[i], 0, twoHochBPS, decode[i + i].min, decode[i + i].max);
	    }
	    clipOutputValues(output);
	} catch (Throwable t) {
	    Logger.getGlobal().throwing("SampledFunction", "compute", t);
	}
    }

    private void encode(float[] input) {
	clipInputValues_1(input);

	for (int i = 0; i < input.length; i++) {
	    float x = input[i];
	    input[i] = interpolate(x, domain[i + i].min, domain[i + i].max, encode[i + i].min, encode[i + i].max);
	}
	clipInputValues_2(input);
    }

    void clipOutputValues(float[] values) {
	for (int i = 0; i < values.length; i++) {
	    float x = values[i];
	    x = Math.min(Math.max(x, range[i + i].min), range[i + i].max);
	    values[i] = x;
	}
    }

    void clipInputValues_1(float[] values) {
	for (int i = 0; i < values.length; i++) {
	    float x = values[i];
	    float max = Math.max(x, domain[i + i].min);
	    x = Math.min(max, domain[i + i].max);
	    values[i] = x;
	}
    }

    void clipInputValues_2(float[] values) {
	for (int i = 0; i < values.length; i++) {
	    float e = values[i];
	    e = Math.min(Math.max(e, 0), size[i] - 1);
	    values[i] = e;
	}
    }

    void readSamples(StreamObject stream, PDF_File pdf) throws IOException {
	InputStream in = pdf.getInputStream(stream);
	readSamples(in);
    }

    @SuppressWarnings("unchecked")
    void readSamples(InputStream in) throws IOException {
	if (in == null) {
	    return;
	}
	@SuppressWarnings("resource")
	BitInputStream bin = new BitInputStream(in);
	int[] res = new int[1024];
	int p = 0;
	while (bin.available() > 0) {
	    int a = bin.read(bitsPerSample);
	    if (a == -1) {
		break;
	    }
	    res[p++] = a;
	    if (p >= res.length) {
		int[] tmp = new int[res.length + 1024];
		System.arraycopy(res, 0, tmp, 0, res.length);
		res = tmp;
	    }
	}
	if (res.length > p) {
	    int[] tmp = new int[p];
	    System.arraycopy(res, 0, tmp, 0, p);
	    res = tmp;
	}

	IArraysEnumerator en = new IArraysEnumerator(samples);
	@SuppressWarnings("rawtypes")
	IEnumerator ienum = new IEnumerator(en);
	for (int i = 0; i < res.length; i++) {
	    if (!ienum.hasMoreElements()) {
		break;
	    }
	    IAccess next = (IAccess) ienum.nextElement();
	    next.set(res[i]);
	}
    }

    static class IArrayEnumerator implements Enumeration<IAccess> {
	int[] data;
	int x;

	IArrayEnumerator(int[] data) {
	    this.data = data;
	}

	public boolean hasMoreElements() {
	    return x < data.length;
	}

	public IAccess nextElement() {
	    return new IAccess(x++, data);
	}
    }

    static class IAccess {
	int[] data;
	int x;

	public IAccess(int x, int[] data) {
	    this.data = data;
	    this.x = x;
	}

	public int get() {
	    return data[x];
	}

	public void set(int a) {
	    data[x] = a;
	}
    }

    static class IArraysEnumerator implements Enumeration<IArrayEnumerator> {

	int[][] data;
	int y;

	IArraysEnumerator(int[][] data) {
	    this.data = data;
	}

	public boolean hasMoreElements() {
	    return y < data.length;
	}

	public IArrayEnumerator nextElement() {
	    return new IArrayEnumerator(data[y++]);
	}
    }

}