/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import com.github.egateam.IntSpan;

import java.io.File;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;

public class YAMLInfo {
    private final Set<String> allChrs;
    private final Set<String> allNames;
    private       boolean     isMultiKey;
    private static final String SINGLE_KEY = "__single";

    public static String getSingleKey() {
        return SINGLE_KEY;
    }

    public YAMLInfo() {
        allChrs = new HashSet<>();
        allNames = new HashSet<>();
        isMultiKey = false;
    }

    private Set<String> getAllChrs() {
        return allChrs;
    }

    private Set<String> getAllNames() {
        return allNames;
    }

    public boolean isMultiKey() {
        return isMultiKey;
    }

    public List<String> getSortedChrs() {
        ArrayList<String> sorted = new ArrayList<>(getAllChrs());
        Collections.sort(sorted);

        return sorted;
    }

    public List<String> getSortedNames() {
        ArrayList<String> sorted = new ArrayList<>(getAllNames());
        Collections.sort(sorted);

        return sorted;
    }

    public Map<String, Map<String, IntSpan>> load(File file, boolean remove) throws Exception {
        Map<String, ?> runlistOf = new ReadYAML(file).invoke();

        // check depth of YAML
        // get one (maybe not first) value from Map
        isMultiKey = !(runlistOf.entrySet().iterator().next().getValue() instanceof String);

        Map<String, Map<String, IntSpan>> setOf = new HashMap<>();
        if ( isMultiKey ) {
            for ( Map.Entry<String, ?> entry : runlistOf.entrySet() ) {
                String key = entry.getKey();
                //noinspection unchecked
                HashMap<String, String> value = (HashMap<String, String>) entry.getValue();

                setOf.put(key, new Transform(value, remove).toIntSpan());
                allChrs.addAll(value.keySet());

                allNames.add(key);
            }
        } else {
            allChrs.addAll(runlistOf.keySet());
            allNames.add(getSingleKey());
            setOf.put(getSingleKey(), new Transform(runlistOf, remove).toIntSpan());
        }

        return setOf;
    }

    public Map<String, IntSpan> loadSingle(File file, boolean remove) throws Exception {
        Map<String, ?> runlistSingle = new ReadYAML(file).invoke();

        // check depth of YAML
        // get one (maybe not first) value from Map
        if ( !(runlistSingle.entrySet().iterator().next().getValue() instanceof String) ) {
            throw new RuntimeException(
                String.format("File [%s] shouldn't be a multikey YAML.", file.toString())
            );
        }

        allChrs.addAll(runlistSingle.keySet());
        return new Transform(runlistSingle, remove).toIntSpan();
    }

    public static String validateCompare(String op) throws RuntimeException {
        String result;

        if ( op.startsWith("dif") ) {
            result = "diff";
        } else if ( op.startsWith("uni") ) {
            result = "union";
        } else if ( op.startsWith("int") ) {
            result = "intersect";
        } else if ( op.startsWith("xor") ) {
            result = "xor";
        } else {
            throw new RuntimeException(String.format("op [%s] is invalid", op));
        }

        return result;
    }

    public static String validateSpan(String op) throws RuntimeException {
        String result;

        if ( op.startsWith("cover") ) {
            result = "cover";
        } else if ( op.startsWith("hole") ) {
            result = "holes";
        } else if ( op.startsWith("trim") ) {
            result = "trim";
        } else if ( op.startsWith("pad") ) {
            result = "pad";
        } else if ( op.startsWith("excise") ) {
            result = "excise";
        } else if ( op.startsWith("fill") ) {
            result = "fill";
        } else {
            throw new RuntimeException(String.format("op [%s] is invalid", op));
        }

        return result;
    }

    // Create empty IntSpan for each name:chr
    public void fillUp(Map<String, Map<String, IntSpan>> setOf) {
        for ( String name : getSortedNames() ) {
            Map<String, IntSpan> setOne = setOf.get(name);

            for ( String chr : getSortedChrs() ) {
                if ( !setOne.containsKey(chr) ) {
                    setOne.put(chr, new IntSpan());
                }
            }
        }
    }

    // Create empty IntSpan for each chr
    public void fillUpSingle(Map<String, IntSpan> setSingle) {
        for ( String chr : getSortedChrs() ) {
            if ( !setSingle.containsKey(chr) ) {
                setSingle.put(chr, new IntSpan());
            }
        }
    }

    public Map<String, Map<String, IntSpan>> opResult(String op, Map<String, Map<String, IntSpan>> setOf, Map<String, IntSpan> setSingle) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        fillUp(setOf);
        fillUpSingle(setSingle);

        Map<String, Map<String, IntSpan>> opResultOf = new HashMap<>();
        for ( String name : getSortedNames() ) {
            Map<String, IntSpan> setOne = setOf.get(name);

            Map<String, IntSpan> setOP = new HashMap<>();
            for ( String chr : getSortedChrs() ) {
                Method  methodOP  = IntSpan.class.getMethod(op, IntSpan.class);
                IntSpan opIntSpan = (IntSpan) methodOP.invoke(setOne.get(chr), setSingle.get(chr));
                setOP.put(chr, opIntSpan);
            }

            opResultOf.put(name, setOP);
        }
        return opResultOf;
    }

}
