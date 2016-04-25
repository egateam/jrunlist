/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.Parameters;
import com.github.egateam.util.FileConverterIn;
import com.github.egateam.util.ReadYAML;
import com.github.egateam.util.WriteYAML;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@SuppressWarnings("CanBeFinal")
@Parameters(commandDescription = "Merge runlist yaml files")
public class Merge {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infiles>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = "--help", help = true, hidden = true)
    private boolean help;

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
        Map<String, Map> master = new HashMap<>();
        for ( File inFile : files ) {
            Map<String, ?> map = new ReadYAML(inFile).read();

            String basename = FilenameUtils.getBaseName(inFile.toString());
            master.put(basename, map);
        }

        //----------------------------
        // Output
        //----------------------------
        new WriteYAML(outfile, master).write();
    }
}
