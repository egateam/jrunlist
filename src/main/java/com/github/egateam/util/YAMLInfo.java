package com.github.egateam.util;

import com.github.egateam.IntSpan;

import java.io.File;
import java.util.*;

public class YAMLInfo {
    private Set<String> allChrs;
    private Set<String> allNames;
    private boolean isMultiKey;
    private static final String singleKey = "__single";

    public static String getSingleKey() {
        return singleKey;
    }

    public YAMLInfo() {
        allChrs = new HashSet<>();
        allNames = new HashSet<>();
        isMultiKey = false;
    }

    public Set<String> getAllChrs() {
        return allChrs;
    }

    public Set<String> getAllNames() {
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


    public Map<String, Map<String, IntSpan>> invoke(File file, boolean remove) throws Exception {
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

    public Map<String, IntSpan> invokeSingle(File file, boolean remove) throws Exception {
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
}
