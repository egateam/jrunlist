/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.github.egateam.util.FileConverterIn;
import com.github.egateam.util.ReadLines;
import com.github.egateam.util.ReadYAML;
import com.github.egateam.util.WriteYAML;

import java.io.File;
import java.util.*;

@SuppressWarnings({"CanBeFinal", "unused"})
@Parameters(commandDescription = "Extract some records from a runlist yaml file")
public class Some {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile> <list.file>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = "--help", help = true, hidden = true)
    private boolean help;

    private void validateArgs() {
        if ( files.size() != 2 ) {
            throw new ParameterException("This command need two input files.");
        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".list.yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        Map<String, ?> master = new ReadYAML(files.get(0)).invoke();

        Set<String> allNames = new HashSet<>();
        for ( String str : new ReadLines(files.get(1)).invoke() ) {
            allNames.add(str);
        }

        Map<String, Object> outMap = new HashMap<>();
        for ( Map.Entry<String, ?> entry : master.entrySet() ) {
            String key = entry.getKey();

            if ( allNames.contains(key) ) {
                Object value = entry.getValue();
                outMap.put(key, value);
            }
        }

        //----------------------------
        // Output
        //----------------------------
        new WriteYAML(outfile, outMap).invoke();
    }
}
