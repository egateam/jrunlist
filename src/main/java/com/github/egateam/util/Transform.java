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

    public static Map<String, IntSpan> toIntSpan(Map<String, String> map) throws AssertionError {
        Map<String, IntSpan> setOf = new HashMap<>();

        for ( Map.Entry<?, ?> entry : map.entrySet() ) {
            String chr = entry.getKey().toString();

            IntSpan intspan = new IntSpan(entry.getValue().toString());
            setOf.put(chr, intspan);
        }

        return setOf;
    }

    public static Map<String, String> toRunlist(Map<String, IntSpan> map) throws AssertionError {
        Map<String, String> runlistOf = new HashMap<>();

        for ( Map.Entry<?, ?> entry : map.entrySet() ) {
            String chr = entry.getKey().toString();

            String runlist = entry.getValue().toString();
            runlistOf.put(chr, runlist);
        }

        return runlistOf;
    }

    public static Map<String, Map<String, String>>
    toRunlistMulti(Map<String, Map<String, IntSpan>> setOf) throws AssertionError {
        Map<String, Map<String, String>> runlistOf = new HashMap<>();

        for ( Map.Entry<?, ?> entry : setOf.entrySet() ) {
            String name = entry.getKey().toString();

            Map<String, IntSpan> setOne     = (Map<String, IntSpan>) entry.getValue();
            Map<String, String>  runlistOne = new HashMap<>();
            for ( Map.Entry<?, ?> entry2 : setOne.entrySet() ) {
                String chr     = entry2.getKey().toString();
                String runlist = entry2.getValue().toString();

                runlistOne.put(chr, runlist);
            }

            runlistOf.put(name, runlistOne);
        }

        return runlistOf;
    }
}
