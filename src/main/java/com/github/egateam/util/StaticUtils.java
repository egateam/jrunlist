/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.egateam.IntSpan;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class StaticUtils {

    public static Map<String, ?> readRl(File file) throws Exception {
        if ( !file.isFile() ) {
            throw new IOException(String.format("YAML file [%s] doesn't exist", file));
        }

        // load YAML from a file
        ObjectMapper om = new ObjectMapper(new YAMLFactory());

        return om.<HashMap<String, Object>>readValue(
            file,
            new TypeReference<Map<String, Object>>() {
            });
    }

    public static void writeRl(String fileName, Map<String, ?> map) throws Exception {
        // http://www.mkyong.com/java/how-to-convert-java-map-to-from-json-jackson/
        // http://stackoverflow.com/questions/4405078/how-to-write-to-standard-output-using-bufferedwriter
        ObjectWriter omw = new ObjectMapper(new YAMLFactory()).writer();
        String yamlString = omw
            .with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .writeValueAsString(map);

        // write YAML to a file or stdout
        if ( fileName.equals("stdout") )
            System.out.print(yamlString);
        else {
            FileUtils.writeStringToFile(new File(fileName), yamlString, "UTF-8");
        }
    }

    public static Map<String, Integer> readSizes(File file, boolean remove) throws Exception {
        HashMap<String, Integer> lengthOf = new HashMap<>();

        try ( BufferedReader reader = new BufferedReader(new FileReader(file)) ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                line = line.trim();
                String[] fields = line.split("\\t");
                if ( fields.length == 2 ) {
                    if ( remove ) fields[0] = fields[0].replaceFirst("chr0?", "");

                    lengthOf.put(fields[0], Integer.parseInt(fields[1]));
                }
            }
        }

        return lengthOf;
    }

    public static List<String> readLines(File file) throws Exception {
        List<String> lines = new ArrayList<>();

        try ( BufferedReader reader = new BufferedReader(new FileReader(file)) ) {
            String line;
            while ( (line = reader.readLine()) != null ) {
                line = line.trim();
                lines.add(line);
            }
        }

        return lines;
    }

    public static void writeLines(String fileName, List<String> lines) throws Exception {
        if ( fileName.equals("stdout") )
            for ( String line : lines ) {
                System.out.println(line);
            }
        else {
            FileUtils.writeLines(new File(fileName), "UTF-8", lines, "\n");
        }
    }

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

    public static String validateOpCompare(String op) throws RuntimeException {
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

    public static String validateOpSpan(String op) throws RuntimeException {
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
}
