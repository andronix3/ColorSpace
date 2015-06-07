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
 * Date: 24.11.2008
 * Some predefined RGB color spaces.
 *
 * @author Andrey Kuznetsov
 */
class Colors {

     static final ColorMatrix adobe_rgb_toXYZ = new ColorMatrix(0.576700, 0.185556, 0.188212, 0.297361, 0.627355, 0.0752847, 0.0270328, 0.0706879, 0.991248);
     static final ColorMatrix apple_rgb_toXYZ = new ColorMatrix(0.449695, 0.316251, 0.18452, 0.244634, 0.672034, 0.0833318, 0.0251829, 0.141184, 0.922602);
     static final ColorMatrix best_rgb_toXYZ = new ColorMatrix(0.632670, 0.204556, 0.126995, 0.228457, 0.737352, 0.0341908, 0.000000, 0.00951424, 0.815696);
     static final ColorMatrix beta_rgb_toXYZ = new ColorMatrix(0.671254, 0.174583, 0.118383, 0.303273, 0.663786, 0.0329413, 0.000000, 0.040701, 0.784509);
     static final ColorMatrix bruce_rgb_toXYZ = new ColorMatrix(0.467384, 0.294454, 0.188629, 0.240995, 0.683554, 0.0754517, 0.0219086, 0.0736135, 0.993447);
     static final ColorMatrix cie_rgb_toXYZ = new ColorMatrix(0.488718, 0.310680, 0.200602, 0.176204, 0.812985, 0.0108109, 0.000000, 0.0102048, 0.989795);
     static final ColorMatrix color_match_toXYZ = new ColorMatrix(0.509344, 0.320907, 0.133969, 0.274884, 0.658132, 0.0669845, 0.0242545, 0.108782, 0.692174);
     static final ColorMatrix don_rgb4_toXYZ = new ColorMatrix(0.645771, 0.193351, 0.125098, 0.278350, 0.687970, 0.0336802, 0.00371134, 0.0179862, 0.803513);
     static final ColorMatrix eci_toXYZ = new ColorMatrix(0.650204, 0.178077, 0.135938, 0.320250, 0.602071, 0.0776791, 0.000000, 0.0678390, 0.757371);
     static final ColorMatrix ekta_space_ps5_toXYZ = new ColorMatrix(0.593891, 0.272980, 0.0973486, 0.260629, 0.734946, 0.00442493, 0.000000, 0.0419970, 0.783213);
     static final ColorMatrix ntsc_toXYZ = new ColorMatrix(0.606734, 0.173564, 0.200112, 0.298839, 0.586811, 0.114350, 0.000000, 0.0661196, 1.11491);
     static final ColorMatrix pal_secam_toXYZ = new ColorMatrix(0.430587, 0.341545, 0.178336, 0.222021, 0.706645, 0.0713342, 0.0201837, 0.129551, 0.939234);
     static final ColorMatrix pro_photo_toXYZ = new ColorMatrix(0.7978, 0.1352, 0.0313, 0.2881, 0.7118, 0.0001, 0.0000, 0.0000, 0.8251);
     static final ColorMatrix smpte_c_toXYZ = new ColorMatrix(0.393555, 0.365253, 0.191659, 0.212395, 0.701049, 0.0865558, 0.0187407, 0.111932, 0.958297);
     static final ColorMatrix srgb_toXYZ = new ColorMatrix(0.412424, 0.357579, 0.180464, 0.212656, 0.715158, 0.0721856, 0.0193324, 0.119193, 0.950444);
     static final ColorMatrix opti_rgb_toXYZ = new ColorMatrix(0.6173, 0.1576, 0.1755, 0.3097, 0.6386, 0.0517, 0.0002, 0.0208, 1.0680);
     static final ColorMatrix wide_gamut_toXYZ = new ColorMatrix(0.716105, 0.100930, 0.147186, 0.258187, 0.724938, 0.0168748, 0.000000, 0.0517813, 0.773429);

     static final RGB_ColorSpace ADOBE_RGB = new RGB_ColorSpace("Adobe RGB (1998)", adobe_rgb_toXYZ, new ReferenceWhite(WhitePoint.D65), 2.2f);
     static final RGB_ColorSpace APPLE_RGB = new RGB_ColorSpace("Apple RGB", apple_rgb_toXYZ, new ReferenceWhite(WhitePoint.D65), 1.8f);
     static final RGB_ColorSpace BEST_RGB = new RGB_ColorSpace("Best RGB", best_rgb_toXYZ, new ReferenceWhite(WhitePoint.D50), 2.2f);
     static final RGB_ColorSpace BETA_RGB = new RGB_ColorSpace("Beta RGB", beta_rgb_toXYZ, new ReferenceWhite(WhitePoint.D50), 2.2f);
     static final RGB_ColorSpace BRUCE_RGB = new RGB_ColorSpace("Bruce RGB", bruce_rgb_toXYZ, new ReferenceWhite(WhitePoint.D65), 2.2f);
     static final RGB_ColorSpace CIE_RGB = new RGB_ColorSpace("CIE", cie_rgb_toXYZ, new ReferenceWhite(WhitePoint.E), 2.2f);
     static final RGB_ColorSpace COLOR_MATCH_RGB = new RGB_ColorSpace("ColorMatch RGB", color_match_toXYZ, new ReferenceWhite(WhitePoint.D50), 1.8f);
     static final RGB_ColorSpace DON_RGB4 = new RGB_ColorSpace("Don RGB 4", don_rgb4_toXYZ, new ReferenceWhite(WhitePoint.D50), 2.2f);
     static final RGB_ColorSpace ECI_RGB = new RGB_ColorSpace("ECI RGB", eci_toXYZ, new ReferenceWhite(WhitePoint.D50), 1.8f);
     static final RGB_ColorSpace EKTA_SPACE_PS5 = new RGB_ColorSpace("Ekta Space PS5", ekta_space_ps5_toXYZ, new ReferenceWhite(WhitePoint.D50), 2.2f);
     static final RGB_ColorSpace NTSC_RGB = new RGB_ColorSpace("NTSC", ntsc_toXYZ, new ReferenceWhite(WhitePoint.C), 2.2f);
     static final RGB_ColorSpace PAL_SECAM_RGB = new RGB_ColorSpace("PAL/SECAM", pal_secam_toXYZ, new ReferenceWhite(WhitePoint.D65), 2.2f);
     static final RGB_ColorSpace PRO_PHOTO = new RGB_ColorSpace("ProPhoto", pro_photo_toXYZ, new ReferenceWhite(WhitePoint.D50), 1.8f);
     static final RGB_ColorSpace SMPTE_C_RGB = new RGB_ColorSpace("SMPTE-C RGB", smpte_c_toXYZ, new ReferenceWhite(WhitePoint.D65), 2.2f);
     static final RGB_ColorSpace S_RGB = RGB_ColorSpace.createSRGB(8);
     static final RGB_ColorSpace WIDE_GAMUT = new RGB_ColorSpace("Wide Gamut", wide_gamut_toXYZ, new ReferenceWhite(WhitePoint.D50), 2.2f);
     static final RGB_ColorSpace OPTI_RGB = new RGB_ColorSpace("OptiRGB", opti_rgb_toXYZ, new ReferenceWhite(WhitePoint.D65), 2.2f);
}
