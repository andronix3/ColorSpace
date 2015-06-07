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

public enum E_ColorMatrix {
    adobe_rgb_toXYZ (Colors.adobe_rgb_toXYZ),
    apple_rgb_toXYZ (Colors.apple_rgb_toXYZ),
    best_rgb_toXYZ (Colors.best_rgb_toXYZ),
    beta_rgb_toXYZ (Colors.beta_rgb_toXYZ),
    bruce_rgb_toXYZ (Colors.bruce_rgb_toXYZ),
    cie_rgb_toXYZ (Colors.cie_rgb_toXYZ),
    color_match_toXYZ (Colors.color_match_toXYZ),
    don_rgb_toXYZ (Colors.don_rgb4_toXYZ),
    eci_toXYZ (Colors.eci_toXYZ),
    ekta_space_toXYZ (Colors.ekta_space_ps5_toXYZ),
    ntsc_toXYZ (Colors.ntsc_toXYZ),
    pal_secam_toXYZ (Colors.pal_secam_toXYZ),
    pro_photo_toXYZ (Colors.pro_photo_toXYZ),
    smpte_toXYZ (Colors.smpte_c_toXYZ),
    srgb_toXYZ (Colors.srgb_toXYZ),
    opti_rgb_toXYZ (Colors.opti_rgb_toXYZ),
    wide_gamut_toXYZ (Colors.wide_gamut_toXYZ)
    ;

    ColorMatrix colorMatrix;

    private E_ColorMatrix(ColorMatrix colorMatrix) {
	this.colorMatrix = colorMatrix;
    }

    public ColorMatrix getColorMatrix() {
	return colorMatrix;
    }
}
