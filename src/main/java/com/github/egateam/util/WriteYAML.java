package com.github.egateam.util;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;

import java.io.*;
import java.util.Map;

public class WriteYAML {
    private String fileName;
    private Map<String, ?> map;

    public WriteYAML(String fileName, Map<String, ?> map) {
        this.fileName = fileName;
        this.map = map;
    }

    public void write() {
        Object writer = null;
        if ( !fileName.equals("stdout") )
            try {
                writer = new PrintWriter(fileName);
            } catch ( FileNotFoundException e ) {
                e.printStackTrace();
            }
        else {
            writer = new OutputStreamWriter(System.out);
        }

        try {
            // http://www.mkyong.com/java/how-to-convert-java-map-to-from-json-jackson/
            // http://stackoverflow.com/questions/4405078/how-to-write-to-standard-output-using-bufferedwriter
            ObjectWriter omw = new ObjectMapper(new YAMLFactory()).writer();
            BufferedWriter bufferedWriter = new BufferedWriter((Writer) writer);

            // write YAML to a file or stdout
            omw.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS).writeValue(bufferedWriter, map);
        } catch ( Exception err ) {
            err.printStackTrace();
        }
    }
}
