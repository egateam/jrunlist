/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import org.apache.commons.io.FileUtils;

import java.io.*;
import java.util.Map;

public class WriteYAML {
    private final String fileName;
    private final Map<?, ?> map;

    /**
     * Constructor for map
     *
     * @param fileName Output
     * @param map      Map<String, ?>
     */
    public WriteYAML(String fileName, Map<?, ?> map) {
        this.fileName = fileName;
        this.map = map;
    }

    public void write() throws Exception {
        // http://www.mkyong.com/java/how-to-convert-java-map-to-from-json-jackson/
        // http://stackoverflow.com/questions/4405078/how-to-write-to-standard-output-using-bufferedwriter
        ObjectWriter omw = new ObjectMapper(new YAMLFactory()).writer();
        String yamlString = omw.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS).writeValueAsString(map);

        // write YAML to a file or stdout
        if ( fileName.equals("stdout") )
            System.out.print(yamlString);
        else {
            FileUtils.writeStringToFile(new File(fileName), yamlString, "UTF-8");
        }
    }
}
