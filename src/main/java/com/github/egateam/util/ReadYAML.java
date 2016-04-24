/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.util;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.util.Map;

public class ReadYAML {
    private final File file;

    public ReadYAML(File fileName) {
        this.file = fileName;
    }

    public Map<String, ?> read() throws Exception {
        if ( !file.isFile() ) {
            throw new IOException(String.format("YAML file [%s] doesn't exist", file));
        }

        // read YAML from a file
        ObjectMapper mapper = new ObjectMapper(new YAMLFactory());

        return mapper.<Map<String, Object>>readValue(
            file,
            new TypeReference<Map<String, Object>>() {
            });
    }
}
