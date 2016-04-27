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
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class ReadWrite {

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
}
