/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;


import com.github.egateam.IntSpan;

import java.util.List;

public class ChrCoverage {
    private final String name;
    private final int    length;
    private final int    size;
    private final float  coverage;

    public ChrCoverage(String name, int length, IntSpan intspan) {
        this.name = name;
        this.length = length;
        this.size = intspan.size();
        this.coverage = (float) intspan.size() / (float) length;
    }

    public String csvLine() {
        return String.format("%s,%d,%d,%.4f\n", name, length, size, coverage);
    }

    public static String allLine(List<ChrCoverage> list) {
        int allLength = 0;
        int allSize   = 0;
        for ( ChrCoverage one : list ) {
            allLength += one.length;
            allSize += one.size;
        }
        float allCoverage = (float) allSize / (float) allLength;

        return String.format("%s,%d,%d,%.4f\n", "all", allLength, allSize, allCoverage);
    }
}
