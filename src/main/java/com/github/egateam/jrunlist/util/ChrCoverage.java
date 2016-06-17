/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.jrunlist.util;

import java.util.List;

public class ChrCoverage {
    private final String name;
    private final int    length;
    private final int    size;
    private final double coverage;

    public ChrCoverage(String name, int length, int size) {
        this.name = name;
        this.length = length;
        this.size = size;
        this.coverage = (double) size / (double) length;
    }

    public String csvLine() {
        return String.format("%s,%d,%d,%.4f", name, length, size, coverage);
    }

    public static String allLine(List<ChrCoverage> list) {
        int allLength = 0;
        int allSize   = 0;
        for ( ChrCoverage one : list ) {
            allLength += one.length;
            allSize += one.size;
        }
        double allCoverage = (double) allSize / (double) allLength;

        return String.format("%s,%d,%d,%.4f", "all", allLength, allSize, allCoverage);
    }
}
