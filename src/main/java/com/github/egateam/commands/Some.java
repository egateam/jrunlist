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
import com.github.egateam.util.StaticUtils;

import java.io.File;
import java.util.*;

@SuppressWarnings({"CanBeFinal"})
@Parameters(commandDescription = "Extract some records from a runlist yaml file")
public class Some {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile> <list.file>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

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
        Map<String, ?> master = StaticUtils.readRl(files.get(0));

        Set<String> allNames = new HashSet<>();
        for ( String str : StaticUtils.readLines(files.get(1)) ) {
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
        StaticUtils.writeRl(outfile, outMap);
    }
}
