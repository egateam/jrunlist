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
import java.util.HashMap;
import java.util.Map;

public class ReadWrite {

    public static void writeYaml(String fileName, Map<String, ?> map) throws Exception {
        // http://www.mkyong.com/java/how-to-convert-java-map-to-from-json-jackson/
        // http://stackoverflow.com/questions/4405078/how-to-write-to-standard-output-using-bufferedwriter
        ObjectWriter omw = new ObjectMapper(new YAMLFactory()).writer();
        String yamlString = omw
            .with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS)
            .writeValueAsString(map);

        // Fixme: Fix multikey dump
        // AT5G67550: "{1=-, 2=-, 3=-, 4=-, 5=-}"
        // write YAML to a file or stdout
        if ( fileName.equals("stdout") )
            System.out.print(yamlString);
        else {
            FileUtils.writeStringToFile(new File(fileName), yamlString, "UTF-8");
        }
    }

    public static Map<String, ?> readYaml(File file) throws Exception {
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
}
