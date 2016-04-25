/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@SuppressWarnings({"unused", "CanBeFinal"})
public class ChrPosition {
    private String name;
    private String chrName;
    private String chrStrand;
    private int chrStart;
    private int chrEnd;
    private Map<String, String> others = new HashMap<>();
    private boolean isEmpty = true;

    public String getName() {
        return name;
    }

    public String getChrName() {
        return chrName;
    }

    public String getChrStrand() {
        return chrStrand;
    }

    public int getChrStart() {
        return chrStart;
    }

    public int getChrEnd() {
        return chrEnd;
    }

    public Map<String, String> getOthers() {
        return others;
    }

    public ChrPosition(String header) throws RuntimeException {
        decode(header);
    }

    public boolean isEmpty() {
        return isEmpty;
    }

    public ChrPosition(String chrName, int chrStart, int chrEnd) {
        this.chrName = chrName;
        this.chrStart = chrStart;
        this.chrEnd = chrEnd;
        isEmpty = false;
    }

    public void decode(String header) throws RuntimeException {
        Pattern p = Pattern.compile("(?xi)" +
            "(?:(?<name>[\\w_]+)\\.)?" +
            "(?<chrName>[\\w-]+)" +
            "(?:\\((?<chrStrand>.+)\\))?" +
            "[:]" +
            "(?<chrStart>\\d+)" +
            "[_\\-]" +
            "(?<chrEnd>\\d+)"
        );
        Matcher m = p.matcher(header);

        if ( m.find() ) {
            name = m.group("name");
            chrName = m.group("chrName");
            chrStrand = m.group("chrStrand");
            chrStart = Integer.parseInt(m.group("chrStart"));
            chrEnd = Integer.parseInt(m.group("chrEnd"));
            isEmpty = false;

            if ( chrStrand == null || !chrStrand.equals("-") ) {
                chrStrand = "+";
            }

            // make sure chrStart <= chrEnd
            if ( chrStart < 1 || chrEnd < 1 ) {
                throw new NumberFormatException("Positions at chromosome can't less than 1");
            } else if ( chrStart > chrEnd ) {
                int temp = chrStart;
                chrStart = chrEnd;
                chrEnd = temp;

                if ( chrStrand.equals("+") ) {
                    chrStrand = "-";
                } else {
                    chrStrand = "+";
                }
            }
        }

        if ( header.contains("|") ) {
            String nonEssential = header.replaceFirst(".+\\|", "");
            String[] parts = nonEssential.split(";");
            for ( String part : parts ) {
                String[] kv = part.split("=");
                if ( kv.length == 2 ) {
                    others.put(kv[0], kv[1]);
                }
            }
        }
    }

    public String encode(boolean onlyEssential) {
        String header = "";
        if ( chrName == null ) {
            return header;
        }

        if ( name != null ) {
            header += name + ".";
        }

        header += chrName;

        if ( chrStrand != null ) {
            header += "(" + chrStrand + ")";
        }

        header += ":" + chrStart;
        header += "-" + chrEnd;

        if ( !onlyEssential && !others.isEmpty() ) {
            List<String> parts = new ArrayList<>();
            for ( Map.Entry<String, String> entry : others.entrySet() ) {
                parts.add(entry.getKey() + "=" + entry.getValue());
            }

            String nonEssential = "";
            boolean firstFlag = true;
            for ( String str : parts ) {
                if ( firstFlag ) {
                    nonEssential += str;
                    firstFlag = false;
                } else {
                    nonEssential += ";" + str;
                }
            }

            if ( !nonEssential.isEmpty() ) {
                header += "|" + nonEssential;
            }
        }

        return header;
    }

    public String encode() {
        return encode(false);
    }

    @Override
    public String toString() {
        return encode();
    }
}
