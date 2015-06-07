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

/**
 * @author Andrey Kuznetsov
 */
public class YCC_Color {

    public static  final float C_RED = 0.2989f;
    public static  final float C_GREEN = 0.5866f;
    public static final float C_BLUE = 0.1145f;

    private static YCC_Constants instance;

    public static class YCC_Constants {
        private final float[] C_RED_M = new float[256];
        private final float[] C_GREEN_M = new float[256];
        private final float[] C_BLUE_M = new float[256];

        private float V_RED;
        private float V_BLUE;

        private YCC_Constants() {
            this(C_RED, C_GREEN, C_BLUE);
        }

        public YCC_Constants(float cRed, float cGreen, float cBlue) {
            for (int i = 0; i < C_RED_M.length; i++) {
                C_RED_M[i] = (i * cRed);
                C_GREEN_M[i] = (i * cGreen);
                C_BLUE_M[i] = (i * cBlue);
            }

            V_RED = 1.0f / (2 - 2 * cRed);
            V_BLUE = 1.0f / (2 - 2 * cBlue);
        }

        public YCC_Color getColor() {
            return new YCC_Color(this);
        }

        public YCC_Color getColor(Color c) {
            return new YCC_Color(this, c);
        }

        public YCC_Color getColor(int r, int g, int b) {
            return new YCC_Color(this, r, g, b);
        }

        public YCC_Color getColor(float y, float cr, float cb) {
            return new YCC_Color(this, y, cr, cb);
        }

        public static YCC_Constants getYCC_Constants() {
            if(instance == null) {
                instance = new YCC_Constants();
            }
            return instance;
        }
    }

    private float[] C_RED_M = new float[256];
    private float[] C_GREEN_M = new float[256];
    private float[] C_BLUE_M = new float[256];

    private float V_RED;
    private float V_BLUE;

    private float Y, Cr, Cb;

    private int rgb;
    private int ycc;

    private int r;
    private int g;
    private int b;
    private int a;

    private boolean setFromRGB;


    private YCC_Color(YCC_Constants ycc_constants) {
        setConstants(ycc_constants);
    }

    private YCC_Color(YCC_Constants ycc_constants, Color c) {
        setConstants(ycc_constants);
        setColor(c);
    }

    private YCC_Color(YCC_Constants ycc_constants, int r, int g, int b) {
        setConstants(ycc_constants);
        setRGB(r, g, b);
    }

    private YCC_Color(YCC_Constants ycc_constants, float y, float cr, float cb) {
        setConstants(ycc_constants);
        setYCC(y, cr, cb);
    }

    public void setYCC(float y, float cr, float cb) {
        Y = y;
        Cr = cr;
        Cb = cb;
        ycc = ((int) Y << 16) | ((int) Cb << 8) | (int) Cr;
        ycc2rgb();
    }

    public void setColor(Color c) {
        setRGB(c.getRGB());
    }

    public void setRGB(int rgb) {
        this.rgb = rgb;
        setFromRGB = true;
        setRGB((rgb >> 16) & 0xFF, (rgb >> 8) & 0xFF, rgb & 0xFF);
        setFromRGB = false;
    }

    public void setRGB(int r, int g, int b) {
        this.r = r & 0xFF;
        this.g = g & 0xFF;
        this.b = b & 0xFF;
        if (!setFromRGB) {
            a = 0xFF;
            rgb = (a << 24) | (r << 16) | (g << 8) | b;
        }
        else {
            a = (rgb >> 24) & 0xFF;
        }
        rgb2ycc();
    }

    private void ycc2rgb() {
        float cr = Cr;
        float cb = Cb;

        r = (int) (Y + 1.4022f * cr);
        g = (int) (Y - 0.34414 * cb - 0.71414 * cr);
        b = (int) (Y + 1.772f * cb);

        a = 0xFF;
        if (r > 255 || r < 0 || g > 255 || g < 0 || b > 255 || b < 0) {
            a = 0;
        }

        r = (r > 255) ? 255 : ((r < 0) ? 0 : r);
        g = (g > 255) ? 255 : ((g < 0) ? 0 : g);
        b = (b > 255) ? 255 : ((b < 0) ? 0 : b);

        rgb = (a << 24) | (r << 16) | (g << 8) | b;
    }

    private void rgb2ycc() {
        Y = C_RED_M[r] + C_GREEN_M[g] + C_BLUE_M[b];
        Cr = ((r - Y) * V_RED);
        Cb = ((b - Y) * V_BLUE);

        ycc = ((int) Y << 16) | ((int) Cb << 8) | (int) Cr;
    }

    public int getRed() {
        return r;
    }

    public int getGreen() {
        return g;
    }

    public int getBlue() {
        return b;
    }

    public int getRGB() {
        return rgb;
    }

    public int getRGB(int alpha) {
        return ((alpha & 0xFF) << 24) | (r << 16) | (g << 8) | b;
    }

    public float getY() {
        return Y;
    }

    public float getCr() {
        return Cr;
    }

    public float getCb() {
        return Cb;
    }

    public int getYCbCr() {
        return ycc;
    }
    
    public int getAlpha() {
	return a;
    }

    private void setConstants(YCC_Constants ycc_constants) {
        this.C_RED_M = ycc_constants.C_RED_M;
        this.C_GREEN_M = ycc_constants.C_GREEN_M;
        this.C_BLUE_M = ycc_constants.C_BLUE_M;
        V_RED = ycc_constants.V_RED;
        V_BLUE = ycc_constants.V_BLUE;
    }
}
