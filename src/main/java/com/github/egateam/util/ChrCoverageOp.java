/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import java.util.List;

public class ChrCoverageOp {
    private final String name;
    private final int    length;
    private final int    size;
    private final int    length2;
    private final int    size2;
    private final double c1;
    private final double c2;
    private final double ratio;

    public ChrCoverageOp(String name, int length, int size, int length2, int size2) {
        this.name = name;
        this.length = length;
        this.size = size;
        this.length2 = length2;
        this.size2 = size2;

        this.c1 = (double) size / (double) length;
        this.c2 = length2 == 0 ? 0.0 : (double) size2 / (double) length2;
        this.ratio = c1 == 0.0 ? 0.0 : c2 / c1;
    }

    public String csvLine() {
        return String.format("%s,%d,%d,%d,%d,%.4f,%.4f,%.4f",
            name, length, size, length2, size2, c1, c2, ratio);
    }

    public static String allLine(List<ChrCoverageOp> list) {
        int allLength  = 0;
        int allSize    = 0;
        int allLength2 = 0;
        int allSize2   = 0;
        for ( ChrCoverageOp one : list ) {
            allLength += one.length;
            allSize += one.size;
            allLength2 += one.length2;
            allSize2 += one.size2;
        }
        double allC1    = (double) allSize / (double) allLength;
        double allC2    = allLength2 == 0 ? 0.0 : (double) allSize2 / (double) allLength2;
        double allRatio = allC1 == 0.0 ? 0.0 : allC2 / allC1;

        return String.format("%s,%d,%d,%d,%d,%.4f,%.4f,%.4f",
            "all", allLength, allSize, allLength2, allSize2, allC1, allC2, allRatio);
    }
}
