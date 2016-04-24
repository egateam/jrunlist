/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.ObjectWriter;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.fasterxml.jackson.dataformat.yaml.YAMLFactory;
import com.github.egateam.IntSpan;
import com.github.egateam.util.FileConverterIn;
import com.github.egateam.util.ReadSizes;

import java.io.*;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Parameters(commandDescription = "Convert chr.size to runlists")
public class Genome {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    @Parameter(names = "--help", help = true)
    private boolean help;

    private void validateArgs() {
        if ( files.size() != 1 ) {
            throw new ParameterException("This command need one input file.");
        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".yml";
        }
    }

    private Writer openForFile(String fileName) {
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
        return (Writer) writer;
    }

    public void execute() {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        File inFile = files.get(0);
        Map<String, Integer> lengthOf = new ReadSizes(inFile, remove).read();

        //----------------------------
        // Loading
        //----------------------------
        Map<String, String> runlistOf = new HashMap<>();
        for ( Map.Entry<String, Integer> entry : lengthOf.entrySet() ) {
            IntSpan set = new IntSpan();
            set.addPair(1, entry.getValue());
            runlistOf.put(entry.getKey(), set.toString());
        }

        //----------------------------
        // Output
        //----------------------------
        try {
            // http://www.mkyong.com/java/how-to-convert-java-map-to-from-json-jackson/
            // http://stackoverflow.com/questions/4405078/how-to-write-to-standard-output-using-bufferedwriter
            ObjectWriter omw = new ObjectMapper(new YAMLFactory()).writer();
            BufferedWriter writer = new BufferedWriter(openForFile(outfile));

            // write YAML to a file or stdout
            omw.with(SerializationFeature.ORDER_MAP_ENTRIES_BY_KEYS).writeValue(writer, runlistOf);
        } catch ( Exception err ) {
            err.printStackTrace();
        }
    }
}
