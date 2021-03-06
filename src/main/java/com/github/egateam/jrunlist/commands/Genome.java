/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.jrunlist.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;

import com.github.egateam.IntSpan;
import com.github.egateam.commons.Utils;
import com.github.egateam.jrunlist.util.StaticUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal"})
@Parameters(commandDescription = "Convert chr.size to runlists")
public class Genome {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile>", required = true)
    private List<String> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    private void validateArgs() throws ParameterException {
        if ( files.size() != 1 ) {
            throw new ParameterException("This command need one input file.");
        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        Map<String, Integer> lengthOf = Utils.readSizes(files.get(0), remove);

        //----------------------------
        // Operating
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
        StaticUtils.writeRl(outfile, runlistOf);
    }
}
