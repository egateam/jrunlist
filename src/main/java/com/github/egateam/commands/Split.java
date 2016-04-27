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
import com.github.egateam.util.ReadWrite;
import org.apache.commons.io.FilenameUtils;

import java.io.File;
import java.util.List;
import java.util.Map;

@SuppressWarnings({"CanBeFinal"})
@Parameters(commandDescription = "Split a runlist yaml file")
public class Split {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outdir", "-o"}, description = "Output location. [stdout] for screen.")
    private String outdir = ".";

    @Parameter(names = {"--suffix", "-s"}, description = "Extension of output files.")
    private String suffix = ".yml";

    private void validateArgs() {
        if ( files.size() != 1 ) {
            throw new ParameterException("This command need one input file.");
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        Map<String, ?> master = ReadWrite.readYaml(files.get(0));

        for ( Map.Entry<String, ?> entry : master.entrySet() ) {
            String key   = entry.getKey();
            Object value = entry.getValue();

            if ( !(value instanceof Map<?, ?>) ) {
                throw new Exception("Not a valid multi-key runlist yaml file");
            }

            Map<String, ?> map = (Map<String, ?>) value;

            //----------------------------
            // Output
            //----------------------------
            if ( outdir.equals("stdout") ) {
                ReadWrite.writeYaml("stdout", map);
            } else {
                String filename = key + suffix;
                String fullPath = FilenameUtils.concat(outdir, filename);
                ReadWrite.writeYaml(fullPath, map);
            }
        }
    }
}
