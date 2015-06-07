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

import java.awt.Composite;
import java.awt.CompositeContext;
import java.awt.RenderingHints;
import java.awt.image.ColorModel;
import java.util.logging.Level;
import java.util.logging.Logger;

import com.imagero.reader.pdf.CompositeType;

public class IComposite implements Composite {

    private float alpha;
    private CompositeType type;

    protected Composer composer;

    public IComposite(CompositeType type, float alpha) {
	setType(type);
	setAlpha(alpha);
    }

    public void setAlpha(float alpha) {
	this.alpha = alpha;
    }

    public float getAlpha() {
	return alpha;
    }

    public CompositeType getType() {
	return type;
    }

    public Composer getComposer() {
	return composer;
    }

    public void setType(CompositeType type) {
	this.type = type;
	switch (type) {
	case NORMAL:
	    composer = new Composer.Normal();
	    break;
	case MULTIPLY:
	    composer = new Composer.Multiply();
	    break;
	case SCREEN:
	    composer = new Composer.Screen();
	    break;
	case OVERLAY:
	    composer = new Composer.Overlay();
	    break;
	case DARKEN:
	    composer = new Composer.Darken();
	    break;
	case LIGHTEN:
	    composer = new Composer.Lighten();
	    break;
	case COLORDODGE:
	    composer = new Composer.ColorDodge();
	    break;
	case COLORBURN:
	    composer = new Composer.ColorBurn();
	    break;
	case HARDLIGHT:
	    composer = new Composer.HardLight();
	    break;
	case SOFTLIGHT:
	    composer = new Composer.SoftLight();
	    break;
	case DIFFERENCE:
	    composer = new Composer.Difference();
	    break;
	case EXCLUSION:
	    composer = new Composer.Exclusion();
	    break;
	case HUE:
	    composer = new Composer.Hue();
	    break;
	case SATURATION:
	    composer = new Composer.Saturation();
	    break;
	case COLOR:
	    composer = new Composer.Color();
	    break;
	case LUMINOSITY:
	    composer = new Composer.Luminosity();
	    break;
	case SUB:
	    composer = new Composer.Subtract();
	    break;
	case ADD:
	    composer = new Composer.Add();
	    break;
	case DISTANCE:
	    composer = new Composer.Distance();
	    break;
	default:
	    Logger.getGlobal().log(Level.WARNING, "Unknown composite type:" + type + ". Using NORMAL instead.");
	    composer = new Composer.Normal();
	    break;
	}
    }

    public CompositeContext createContext(ColorModel srcColorModel, ColorModel dstColorModel, RenderingHints hints) {
	return new PDF_CompositeContext(this);
    }
}
