/**
 * THE SOFTWARE IS PROVIDED "AS IS" WITHOUT A WARRANTY OF ANY KIND. ALL EXPRESS OR IMPLIED
 * REPRESENTATIONS AND WARRANTIES, INCLUDING ANY IMPLIED WARRANTY OF MERCHANTABILITY, FITNESS FOR A
 * PARTICULAR PURPOSE OR NON-INFRINGEMENT, ARE HEREBY DISCLAIMED.
 */

package com.github.egateam.commands;

import com.beust.jcommander.Parameter;
import com.beust.jcommander.ParameterException;
import com.beust.jcommander.Parameters;
import com.github.egateam.IntSpan;
import com.github.egateam.util.*;

import java.io.File;
import java.util.*;

@SuppressWarnings({"CanBeFinal", "unused"})
@Parameters(commandDescription = "Combine multiple sets of runlists in a yaml file.\n"
    + "\t\tIt's expected that the YAML file is --mk.\n"
    + "\t\tOtherwise this command will make no effects.")
public class Combine {

    @SuppressWarnings("MismatchedQueryAndUpdateOfCollection")
    @Parameter(description = "<infile>", converter = FileConverterIn.class, required = true)
    private List<File> files;

    @Parameter(names = {"--outfile", "-o"}, description = "Output filename. [stdout] for screen.")
    private String outfile;

    @Parameter(names = {"--remove", "-r"}, description = "Remove 'chr0' from chromosome names.")
    private Boolean remove = false;

    private void validateArgs() {
        if ( files.size() != 1 ) {
            throw new ParameterException("This command need one input file.");
        }

        if ( outfile == null ) {
            outfile = files.get(0) + ".combine.yml";
        }
    }

    public void execute() throws Exception {
        validateArgs();

        //----------------------------
        // Loading
        //----------------------------
        YAMLInfo yaml = new YAMLInfo();
        Map<String, Map<String, IntSpan>> setOf = yaml.invoke(files.get(0), remove);

        //----------------------------
        // Operating
        //----------------------------
        Map<String, IntSpan> opResultOf = new HashMap<>();
        for ( String chr : yaml.getSortedChrs() ) {
            opResultOf.put(chr, new IntSpan());
        }

        for ( String name : yaml.getSortedNames() ) {
            Map<String, IntSpan> curSet = setOf.get(name);
            for ( String chr : curSet.keySet() ) {
                IntSpan curResult = opResultOf.get(chr);
                IntSpan curIntSpan = curSet.get(chr);
                opResultOf.put(chr, curResult.add(curIntSpan));
            }
        }

        //----------------------------
        // Output
        //----------------------------
        new WriteYAML(outfile, new Transform(opResultOf, remove).toRunlist()).invoke();
    }
}
