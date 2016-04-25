/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import com.github.egateam.IntSpan;

import java.util.HashMap;
import java.util.Map;

public class Transform {
    private final Map<?, ?> map;
    private final boolean remove;

    public Transform(Map<?, ?> map, boolean remove) {
        this.map = map;
        this.remove = remove;
    }

    public Map<String, IntSpan> toIntSpan() throws Exception, AssertionError {
        Map<String, IntSpan> setOf = new HashMap<>();

        for ( Map.Entry<?, ?> entry : map.entrySet() ) {
            String chr = entry.getKey().toString();
            if ( remove ) {
                chr = chr.replaceFirst("chr0?", "");
            }
            IntSpan set = new IntSpan(entry.getValue().toString());
            setOf.put(chr, set);
        }

        return setOf;
    }

    public Map<String, String> toRunlist() throws Exception, AssertionError {
        Map<String, String> runlistOf = new HashMap<>();

        for ( Map.Entry<?, ?> entry : map.entrySet() ) {
            String chr = entry.getKey().toString();
            if ( remove ) {
                chr = chr.replaceFirst("chr0?", "");
            }
            String runlist = entry.getValue().toString();
            runlistOf.put(chr, runlist);
        }

        return runlistOf;
    }
}
