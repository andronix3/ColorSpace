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

import com.smartg.color.YCC_Color.YCC_Constants;

public class YCbCrColorSpace extends IColorSpace {

	private static final long serialVersionUID = 2470271774988383967L;
	private YCC_Color yccColor;

	public YCbCrColorSpace() {
		this(YCC_Constants.getYCC_Constants());
	}

	public YCbCrColorSpace(YCC_Constants yccConstants) {
		super(TYPE_YCbCr, 3);
		yccColor = yccConstants.getColor();
	}

	@Override
	public Color getColor(float[] d) {
		yccColor.setYCC(d[0], d[1], d[2]);
		return new Color(yccColor.getRGB(255));
	}

	@Override
	public int getComponentCount() {
		return 3;
	}

	@Override
	public float[] toRGB(float[] d) {
		yccColor.setYCC(d[0], d[1], d[2]);
		return new float[] { yccColor.getRed(), yccColor.getGreen(),
				yccColor.getBlue() };
	}
}
