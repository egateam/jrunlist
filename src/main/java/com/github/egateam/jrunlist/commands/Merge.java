/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.jrunlist.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.github.egateam.jrunlist.util.StaticUtils;
import org.apache.commons.io.FilenameUtils;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal"})
@Parameters(commandDescription = "Merge runlist yaml files")
public class Merge {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infiles>", required = true)
    private List<String> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    private void validateArgs() {
//        if ( files.size() < 1 ) {
//            throw new ParameterException("This command need one or more input files.");
//        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".merge.yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        Map<String, Map<String, ?>> master = new HashMap<>();
        for ( String inFile : files ) {
            Map<String, ?> map = StaticUtils.readRl(inFile);

            String basename = FilenameUtils.getBaseName(inFile);
            master.put(basename, map);
        }

        //----------------------------
        // Output
        //----------------------------
        StaticUtils.writeRl(outfile, master);
    }
}
